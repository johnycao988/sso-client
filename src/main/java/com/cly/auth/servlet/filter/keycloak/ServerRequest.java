package com.cly.auth.servlet.filter.keycloak;
 

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair; 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

 
public class ServerRequest {

	 
    public static class HttpFailure extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private int status;
        private String error;

        public HttpFailure(int status, String error) {
            this.status = status;
            this.error = error;
        }

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }
    }

 

    public static AccessTokenResponse invokeAccessCodeToToken(KeycloakDeployment deployment, String code, String redirectUri, String sessionId) throws IOException, HttpFailure {
        List<NameValuePair> formparams = new ArrayList<>();
        redirectUri = stripOauthParametersFromRedirect(redirectUri);
        formparams.add(new BasicNameValuePair(OAuth2Constants.GRANT_TYPE, "authorization_code"));
        formparams.add(new BasicNameValuePair(OAuth2Constants.CODE, code));
        formparams.add(new BasicNameValuePair(OAuth2Constants.REDIRECT_URI, redirectUri));
        if (sessionId != null) {
            formparams.add(new BasicNameValuePair(AdapterConstants.CLIENT_SESSION_STATE, sessionId));
            formparams.add(new BasicNameValuePair(AdapterConstants.CLIENT_SESSION_HOST, HostUtils.getHostName()));
        }

        HttpPost post = new HttpPost(deployment.getTokenUrl());
        ClientCredentialsProviderUtils.setClientCredentials(deployment, post, formparams);

        UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
        post.setEntity(form);
        HttpResponse response = deployment.getClient().execute(post);
        int status = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        if (status != 200) {
            error(status, entity);
        }
        if (entity == null) {
            throw new HttpFailure(status, null);
        }
        InputStream is = entity.getContent();
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int c;
            while ((c = is.read()) != -1) {
                os.write(c);
            }
            byte[] bytes = os.toByteArray();
            String json = new String(bytes);
            try {
                return JsonSerialization.readValue(json, AccessTokenResponse.class);
            } catch (IOException e) {
                throw new IOException(json, e);
            }
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {

            }
        }
    }

 

 

}