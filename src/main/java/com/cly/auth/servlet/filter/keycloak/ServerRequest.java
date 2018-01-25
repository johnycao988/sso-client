package com.cly.auth.servlet.filter.keycloak;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet; 
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.cly.auth.base.AuthException;
import com.cly.comm.util.IDUtil;
import com.cly.comm.util.IOUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
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

	public static String invokeAccessCodeToToken(KeycloakAuthEnv authEnv, String code, String redirectUri)
			throws AuthException {
		try {
			List<NameValuePair> formparams = new ArrayList<>();
			formparams.add(new BasicNameValuePair(KeycloakConst.REQ_HEADER_GRANT_TYPE,
					KeycloakConst.REQ_HEADER_VALUE_AUTHORIZATION_CODE));
			formparams.add(new BasicNameValuePair(KeycloakConst.CODE, code));
			formparams.add(new BasicNameValuePair(KeycloakConst.REDIRECT_URL, redirectUri));

			HttpPost post = new HttpPost(authEnv.getTokenUrl());

			UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
			post.setEntity(form);
			String basicValue = authEnv.getResourceName() + ":" + authEnv.getSecret();
			basicValue = IDUtil.Base64Encode(basicValue);
			post.setHeader(KeycloakConst.REQ_HEADER_AUTHORIZATION, KeycloakConst.REQ_HEADER_VALUE_BAISC + basicValue);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();

			if (status != 200) {
				error(status, entity);
			}

			if (entity == null) {
				throw new HttpFailure(status, null);
			}

			String json = new String(IOUtil.getInputStreamBytes(entity.getContent()));

			return json;
		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

	public static String requestPAT(KeycloakAuthEnv authEnv) throws AuthException {
		try {

			List<NameValuePair> formparams = new ArrayList<>();
			formparams.add(new BasicNameValuePair(KeycloakConst.REQ_HEADER_GRANT_TYPE,
					KeycloakConst.REQ_HEADER_VALUE_CLIENT_CREDENTIALS));
			formparams.add(new BasicNameValuePair(KeycloakConst.CLIENT_ID, authEnv.getResourceName()));
			formparams.add(new BasicNameValuePair(KeycloakConst.CLIENT_SECRET, authEnv.getSecret()));

			HttpPost post = new HttpPost(authEnv.getTokenUrl());

			UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
			post.setEntity(form);

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();

			if (status != 200) {
				error(status, entity);
			}

			if (entity == null) {
				throw new HttpFailure(status, null);
			}

			String json = new String(IOUtil.getInputStreamBytes(entity.getContent()));

			return json;
		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

	public static String requestAAT(KeycloakAuthEnv authEnv, String accessToken, String permToken)
			throws AuthException {
		try {
			List<NameValuePair> formparams = new ArrayList<>();
			formparams.add(new BasicNameValuePair("ticket", permToken));
			HttpPost post = new HttpPost(authEnv.getAuthorizeUrl());

			UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
			post.setEntity(form);

			post.setHeader(KeycloakConst.REQ_HEADER_AUTHORIZATION, " Bearer " + accessToken);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();

			if (status != 200) {
				error(status, entity);
			}

			if (entity == null) {
				throw new HttpFailure(status, null);
			}

			String json = new String(IOUtil.getInputStreamBytes(entity.getContent()));

			return json;
		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

	public static String requestRPT(KeycloakAuthEnv authEnv, String accessToken, String[] permResourceNameList)
			throws AuthException {
		try {

			String url = authEnv.getEntitlementUrl() + "/" + authEnv.getResourceName();

			HttpPost post = new HttpPost(url);

			post.setHeader(KeycloakConst.REQ_HEADER_AUTHORIZATION, " Bearer " + accessToken);
			post.setHeader(KeycloakConst.REQ_HEADER_CONTENT_TYPE, "application/json");

			JSONObject jo = new JSONObject();
			JSONArray ja = new JSONArray();

			for (String pr : permResourceNameList) {

				JSONObject jp = new JSONObject();
				jp.put("resource_set_name", pr);
				ja.add(jp);
			}

			jo.put("permissions", ja);

			String jsonData = jo.toString();

			post.setEntity(new StringEntity(jsonData, Charset.forName("UTF-8")));

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();

			if (status != 200) {
				error(status, entity);
			}

			if (entity == null) {
				throw new HttpFailure(status, null);
			}

			String json = new String(IOUtil.getInputStreamBytes(entity.getContent()));

			return json;
		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

	public static String requestAllRPT(KeycloakAuthEnv authEnv, String accessToken) throws AuthException {
		try {

			String url = authEnv.getEntitlementUrl() + "/" + authEnv.getResourceName();

			HttpGet post = new HttpGet(url);

			post.setHeader(KeycloakConst.REQ_HEADER_AUTHORIZATION, " Bearer " + accessToken);
			post.setHeader(KeycloakConst.REQ_HEADER_CONTENT_TYPE, "application/json");

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();

			if (status != 200) {
				error(status, entity);
			}

			if (entity == null) {
				throw new HttpFailure(status, null);
			}

			return new String(IOUtil.getInputStreamBytes(entity.getContent()));
		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

	public static String requestUserInfo(KeycloakAuthEnv authEnv, String accessToken) throws AuthException {
		try {

			String url = authEnv.getUserInfoUrl();

			HttpPost post = new HttpPost(url);

			post.setHeader(KeycloakConst.REQ_HEADER_AUTHORIZATION, " Bearer " + accessToken);
			post.setHeader(KeycloakConst.REQ_HEADER_CONTENT_TYPE,
					KeycloakConst.REQ_HEADER_VALUE_CONTENT_TYPE_FORM_URL_ENCODEED);

			List<NameValuePair> formparams = new ArrayList<>();
			formparams.add(new BasicNameValuePair(KeycloakConst.REQ_HEADER_GRANT_TYPE,
					KeycloakConst.REQ_HEADER_VALUE_CLIENT_CREDENTIALS));
			formparams.add(new BasicNameValuePair(KeycloakConst.CLIENT_ID, authEnv.getResourceName()));
			formparams.add(new BasicNameValuePair(KeycloakConst.CLIENT_SECRET, authEnv.getSecret()));

			UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
			post.setEntity(form);

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();

			if (status != 200) {
				error(status, entity);
			}

			if (entity == null) {
				throw new HttpFailure(status, null);
			}

			String json = new String(IOUtil.getInputStreamBytes(entity.getContent()));

			return json;
		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

	public static void error(int status, HttpEntity entity) throws HttpFailure, IOException {
		// String body = null;
		// if (entity != null) {
		// InputStream is = entity.getContent();
		// try {
		// body = StreamUtil.readString(is);
		// } catch (IOException e) {
		//
		// } finally {
		// try {
		// is.close();
		// } catch (IOException ignored) {
		//
		// }
		// }
		// }
		// throw new HttpFailure(status, body);
	}

}