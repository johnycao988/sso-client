package com.cly.comm.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {

	private IOUtil() {

	}

	public static byte[] getInputStreamBytes(InputStream inputStream) throws IOException {

		try (InputStream is = inputStream; ByteArrayOutputStream output = new ByteArrayOutputStream()) {

			byte[] buffer = new byte[2048];

			int byteread = 0;

			while ((byteread = is.read(buffer)) != -1) {

				output.write(buffer, 0, byteread);
			}

			return output.toByteArray();

		}
	}

	public static void writeFile(InputStream is, String outputFile) throws IOException {

		try (InputStream input = is; OutputStream out = new FileOutputStream(outputFile)) {

			byte[] buffer = new byte[2048];

			int byteread = 0;

			while ((byteread = input.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}

		}

	}

	public static String readTextFile(String fileName) throws IOException {

		try (InputStream in = new FileInputStream(fileName)) {

			StringBuilder sb = new StringBuilder();
			byte[] buffer = new byte[2048];

			int byteread = 0;

			while ((byteread = in.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, byteread));
			}

			return sb.toString();

		}
	}
}
