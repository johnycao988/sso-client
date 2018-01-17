package com.cly.comm.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;;

public class JSONUtil {

	public final static String SUCCESS = "success";
	public final static String ERR_CODE = "errCode";
	public final static String ERR_MSG = "errMsg";

	private JSONUtil() {

	}
	
	public static String getMSHealthCheckResponse(){
		return "{\"status\":\"UP\"}";
	}

	public static String xmlToJson(String xmlCont) {
		return new XMLSerializer().read(xmlCont).toString();
	}

	public static String getString(JSONObject json, String... nodePaths) {

		int index = nodePaths.length - 1;

		JSONObject jof = findParentJSONNode(json, nodePaths);
		if (jof != null && jof.containsKey(nodePaths[index]))
			return jof.getString(nodePaths[index]);
		else
			return null;
	}

	public static Boolean getBoolean(JSONObject json, String... nodePaths) {

		int index = nodePaths.length - 1;

		JSONObject jof = findParentJSONNode(json, nodePaths);
		if (jof != null && jof.containsKey(nodePaths[index])) {
			return jof.getBoolean(nodePaths[index]);
		} else
			return null;
	}

	public static Double getDouble(JSONObject json, String... nodePaths) {

		int index = nodePaths.length - 1;

		JSONObject jof = findParentJSONNode(json, nodePaths);
		if (jof != null && jof.containsKey(nodePaths[index])) {
			return jof.getDouble(nodePaths[index]);
		} else
			return null;
	}

	public static Integer getInt(JSONObject json, String... nodePaths) {

		int index = nodePaths.length - 1;

		JSONObject jof = findParentJSONNode(json, nodePaths);
		if (jof != null && jof.containsKey(nodePaths[index])) {
			return jof.getInt(nodePaths[index]);
		} else
			return null;
	}

	public static Long getLong(JSONObject json, String... nodePaths) {

		int index = nodePaths.length - 1;

		JSONObject jof = findParentJSONNode(json, nodePaths);
		if (jof != null && jof.containsKey(nodePaths[index])) {
			return jof.getLong(nodePaths[index]);
		} else
			return null;
	}

	public static boolean getBoolean(JSONObject json, boolean defautlValue, String... nodePaths) {

		Boolean b = getBoolean(json, nodePaths);
		if (b != null)
			return b;
		else
			return defautlValue;
	}

	public static double getDouble(JSONObject json, double defautlValue, String... nodePaths) {

		Double d = getDouble(json, nodePaths);
		if (d != null)
			return d;
		else
			return defautlValue;
	}

	public static int getInt(JSONObject json, int defautlValue, String... nodePaths) {
		Integer i = getInt(json, nodePaths);
		if (i != null)
			return i;
		else
			return defautlValue;

	}

	public static long getLong(JSONObject json, long defautlValue, String... nodePaths) {

		Long l = getLong(json, nodePaths);
		if (l != null)
			return l;
		else
			return defautlValue;
	}

	public static Object getObject(JSONObject json, String... nodePaths) {

		int index = nodePaths.length - 1;

		JSONObject jof = findParentJSONNode(json, nodePaths);
		if (jof != null && jof.containsKey(nodePaths[index]))
			return jof.get(nodePaths[index]);
		else
			return null;
	}

	public static JSONArray getJSONArray(JSONObject json, String... nodePaths) {

		int index = nodePaths.length - 1;

		JSONObject jof = findParentJSONNode(json, nodePaths);
		if (jof != null && jof.containsKey(nodePaths[index]))
			return jof.getJSONArray(nodePaths[index]);
		else
			return null;
	}

	public static JSONObject getJSONObject(JSONObject json, String... nodePaths) {

		int index = nodePaths.length - 1;

		JSONObject jof = findParentJSONNode(json, nodePaths);
		if (jof != null && jof.containsKey(nodePaths[index]))
			return jof.getJSONObject(nodePaths[index]);
		else
			return null;
	}

	private static JSONObject findParentJSONNode(JSONObject json, String... nodePaths) {

		if (json == null)
			return null;

		JSONObject jof = json;

		for (int i = 0; i < nodePaths.length - 1; i++) {
			if (jof.containsKey(nodePaths[i])) {
				jof = jof.getJSONObject(nodePaths[i]);
			} else {
				return null;
			}
		}

		return jof;

	}

	public static JSONObject initSuccess() {

		return initSuccssFailed(true, null, null);
	}

	public static JSONObject initFailed(String errCode, String errMsg) {

		return initSuccssFailed(false, errCode, errMsg);
	}
	
	public static JSONObject initFailed(Exception e){
		return initFailed("",e.getMessage());
	}
	

	private static JSONObject initSuccssFailed(boolean isSuccess, String errCode, String errMsg) {

		JSONObject js = new JSONObject();

		js.put(SUCCESS, isSuccess);

		if (!isSuccess) {
			js.put(ERR_CODE, errCode);
			js.put(ERR_MSG, errMsg);
		}

		return js;
	}

}
