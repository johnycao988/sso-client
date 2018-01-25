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

package com.cly.auth.servlet.filter.keycloak;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import com.cly.auth.base.AuthServletFilter;
import com.cly.auth.base.AccessToken;
import com.cly.auth.base.AuthEnv;
import com.cly.auth.base.AuthException;
import com.cly.auth.base.AuthSecurityContext; 
import com.cly.comm.util.IDUtil;
import com.cly.comm.util.JSONUtil;

import net.sf.json.JSONObject;

public class KeycloakOIDCFilter extends AuthServletFilter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		try {

			AuthSecurityContext sc = getSecurityContent(req, res);

			if (sc == null) {
				KeycloakAuthEnv authEnv = (KeycloakAuthEnv) this.getAuthEnv();
				authEnv.forwardLoginPage(req, res);
				return;
			}
			
			
			
			testToken((KeycloakAuthEnv)this.getAuthEnv(),sc.getAccessToken().getToken());
	

			chain.doFilter(request, response);

		} catch (AuthException e) {
			e.printStackTrace();
		}

	}
	
	
	private void testToken(KeycloakAuthEnv authEnv,String accessToken) throws AuthException{
		
		//String responseJsonToken=ServerRequest.requestPAT(authEnv);
		
		//AccessToken pat=new KeycloakAccessToken (responseJsonToken); 
		
		String[] permResourceNameList=new String[2];
		permResourceNameList[0]="Default Resource";
		permResourceNameList[1]="version";		
				
		String rptToken=ServerRequest.requestRPT(authEnv, accessToken, permResourceNameList);
		
		JSONObject jo=JSONObject.fromObject(rptToken);
		
		KeycloakAccessToken.dumpToken(JSONUtil.getString(jo,"rpt"));
		
		
		rptToken=ServerRequest.requestUserInfo(authEnv, accessToken);
		
		rptToken=ServerRequest.requestAllRPT(authEnv, accessToken);
		jo=JSONObject.fromObject(rptToken);
		
		KeycloakAccessToken.dumpToken(JSONUtil.getString(jo,"rpt"));
		
		
	}
	

	private AuthSecurityContext getSecurityContent(HttpServletRequest req, HttpServletResponse res)
			throws AuthException {

		AuthSecurityContext sc = codeToToken(req, res);

		if (sc == null) {

			KeycloakAuthEnv authEnv = (KeycloakAuthEnv) this.getAuthEnv();

			sc = authEnv.getAuthSecurityContent(req);

		}

		return sc;
	}

	private AuthSecurityContext codeToToken(HttpServletRequest req, HttpServletResponse res) throws AuthException {

		String code = req.getParameter(KeycloakConst.CODE);
		String sessState = req.getParameter(KeycloakConst.SESSION_STATE);

		if (null != code && null != sessState) {

			KeycloakAuthEnv authEnv = (KeycloakAuthEnv) this.getAuthEnv();

			String redirectUrl = authEnv.getResourceRedirectRootUrl() + req.getRequestURI();

			String responseJsonToken = ServerRequest.invokeAccessCodeToToken(authEnv, code, redirectUrl);
			
			KeycloakAccessToken accessToken=new KeycloakAccessToken(responseJsonToken); 
			 
			AuthSecurityContext sc = new AuthSecurityContext(IDUtil.getRandomBase64UUID(), accessToken);

			authEnv.setAuthSecurityContent(sc, res);

			return sc;

		} else
			return null;

	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public Class<?> getAuthEnvClass() {

		return KeycloakAuthEnv.class;

	}

}
