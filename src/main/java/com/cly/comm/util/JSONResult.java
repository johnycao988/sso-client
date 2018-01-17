package com.cly.comm.util;

import net.sf.json.JSONObject;

public class JSONResult {

	private JSONObject jsr;

	public JSONResult(String js) {

		jsr = JSONObject.fromObject(js);

	}

	public JSONResult(JSONObject jso) {

		jsr = jso;
	}

	public boolean isSuccess() {
		return jsr.getBoolean(JSONUtil.SUCCESS);
	}

	public String getErrorMessage() {
		return jsr.getString(JSONUtil.ERR_MSG);
	}


	public JSONObject getJSONObject() {
		return jsr;
	}

	public String getString() {
		return jsr.toString();
	}

}
