package com.cly.comm.util;

import java.io.IOException;
import java.util.UUID;

import net.iharder.Base64;

public class IDUtil {

	private IDUtil() {

	}

	public static String getRandomUUID() {
		return UUID.randomUUID().toString();
	}

	public static String getRandomBase64UUID() {

		String uid = getRandomUUID();

		return Base64.encodeBytes(uid.getBytes()).toString();
	}
	
	public static String Base64Encode(String code) {

	 	return Base64.encodeBytes(code.getBytes()).toString();
	}
	
	public static String Base64Decode(String code) throws IOException {

	 	return new String(Base64.decode(code));
	}


}
