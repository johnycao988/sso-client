/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cs.auth.adapter.keycloak;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.AdapterTokenStore;
import org.keycloak.adapters.AuthenticatedActionsHandler;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.NodesRegistrationManagement;
import org.keycloak.adapters.PreAuthActionsHandler;
import org.keycloak.adapters.spi.AuthChallenge;
import org.keycloak.adapters.spi.AuthOutcome;
import org.keycloak.adapters.spi.InMemorySessionIdMapper;
import org.keycloak.adapters.spi.SessionIdMapper;
import org.keycloak.adapters.spi.UserSessionManagement;
import org.keycloak.representations.AccessToken;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.keycloak.adapters.servlet.OIDCServletHttpFacade;
import org.keycloak.adapters.servlet.FilterRequestAuthenticator;
import org.keycloak.adapters.servlet.OIDCFilterSessionStore;

 
public class KeycloakOIDCFilter implements Filter {

	private final static Logger log = Logger.getLogger("" + KeycloakOIDCFilter.class);

	public static final String SKIP_PATTERN_PARAM = "keycloak.config.skipPattern";

	public static final String CONFIG_RESOLVER_PARAM = "keycloak.config.resolver";

	public static final String CONFIG_FILE_PARAM = "KEYCLOAK-CONFIG-FILE";

	public static final String CONFIG_PATH_PARAM = "keycloak.config.path";

	protected AdapterDeploymentContext deploymentContext;

	protected SessionIdMapper idMapper = new InMemorySessionIdMapper();

	protected NodesRegistrationManagement nodesRegistrationManagement;

	protected Pattern skipPattern;

	private final KeycloakConfigResolver definedconfigResolver;

	/**
	 * Constructor that can be used to define a {@code KeycloakConfigResolver}
	 * that will be used at initialization to provide the
	 * {@code KeycloakDeployment}.
	 * 
	 * @param definedconfigResolver
	 *            the resolver
	 */
	public KeycloakOIDCFilter(KeycloakConfigResolver definedconfigResolver) {
		this.definedconfigResolver = definedconfigResolver;
	}

	public KeycloakOIDCFilter() {
		this(null);
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {

		System.out.println("keycloak filter init ******************");

		String skipPatternDefinition = filterConfig.getInitParameter(SKIP_PATTERN_PARAM);
		if (skipPatternDefinition != null) {
			skipPattern = Pattern.compile(skipPatternDefinition, Pattern.DOTALL);
		}

		InputStream is = null;

		String confFile = System.getProperty(CONFIG_FILE_PARAM);
		if (confFile == null) {
			confFile = "/config/keycloak.json";
		}

		try {
			is = new FileInputStream(confFile);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		KeycloakDeployment kd = createKeycloakDeploymentFrom(is);
		deploymentContext = new AdapterDeploymentContext(kd);
		log.fine("Keycloak is using a per-deployment configuration.");

		filterConfig.getServletContext().setAttribute(AdapterDeploymentContext.class.getName(), deploymentContext);
		nodesRegistrationManagement = new NodesRegistrationManagement();
	}

	private KeycloakDeployment createKeycloakDeploymentFrom(InputStream is) {
		if (is == null) {
			log.fine("No adapter configuration. Keycloak is unconfigured and will deny all requests.");
			return new KeycloakDeployment();
		}
		return KeycloakDeploymentBuilder.build(is);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		log.fine("Keycloak OIDC Filter");

		System.out.println("keycloak filter now... ******************");

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (shouldSkip(request)) {

			chain.doFilter(req, res);
			return;
		}

		OIDCServletHttpFacade facade = new OIDCServletHttpFacade(request, response);

		KeycloakDeployment deployment = deploymentContext.resolveDeployment(facade);
		if (deployment == null || !deployment.isConfigured()) {
			response.sendError(403);
			log.fine("deployment not configured");
			return;
		}

		PreAuthActionsHandler preActions = new PreAuthActionsHandler(new UserSessionManagement() {
			@Override
			public void logoutAll() {
				if (idMapper != null) {
					idMapper.clear();
				}
			}

			@Override
			public void logoutHttpSessions(List<String> ids) {
				log.fine("**************** logoutHttpSessions");
				// System.err.println("**************** logoutHttpSessions");
				for (String id : ids) {
					log.finest("removed idMapper: " + id);
					idMapper.removeSession(id);
				}

			}
		}, deploymentContext, facade);

		if (preActions.handleRequest()) {
			// System.err.println("**************** preActions.handleRequest
			// happened!");
			return;
		}

		nodesRegistrationManagement.tryRegister(deployment);
		OIDCFilterSessionStore tokenStore = new OIDCFilterSessionStore(request, facade, 100000, deployment, idMapper);
		tokenStore.checkCurrentToken();

		FilterRequestAuthenticator authenticator = new FilterRequestAuthenticator(deployment,
				(AdapterTokenStore) tokenStore, facade, request, 8443);
		
		AuthOutcome outcome = authenticator.authenticate();

		if (outcome == AuthOutcome.AUTHENTICATED) {
			log.fine("AUTHENTICATED");
			if (facade.isEnded()) {
				return;
			}
			AuthenticatedActionsHandler actions = new AuthenticatedActionsHandler(deployment, facade);
			if (actions.handledRequest()) {
				return;
			} else {
				AccessToken at = facade.getSecurityContext().getToken();
				HttpServletRequestWrapper wrapper = tokenStore.buildWrapper();
				chain.doFilter(wrapper, res);
				return;
			}
		}
		AuthChallenge challenge = authenticator.getChallenge();
		if (challenge != null) {
			log.fine("challenge");
			challenge.challenge(facade);
			return;
		}
		response.sendError(403);

	}

	/**
	 * Decides whether this {@link Filter} should skip the given
	 * {@link HttpServletRequest} based on the configured
	 * {@link KeycloakOIDCFilter#skipPattern}. Patterns are matched against
	 * the {@link HttpServletRequest#getRequestURI() requestURI} of a request
	 * without the context-path. A request for {@code /myapp/index.html} would
	 * be tested with {@code /index.html} against the skip pattern. Skipped
	 * requests will not be processed further by {@link KeycloakOIDCFilter}
	 * and immediately delegated to the {@link FilterChain}.
	 *
	 * @param request
	 *            the request to check
	 * @return {@code true} if the request should not be handled, {@code false}
	 *         otherwise.
	 */
	private boolean shouldSkip(HttpServletRequest request) {

		if (skipPattern == null) {
			return false;
		}

		String requestPath = request.getRequestURI().substring(request.getContextPath().length());
		return skipPattern.matcher(requestPath).matches();
	}

	@Override
	public void destroy() {

	}
}
