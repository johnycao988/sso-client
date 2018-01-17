package com.cly.comm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

public class YamlParser {

	HashMap<String, Object> ymlMap;

	public YamlParser(HashMap<String, Object> hm) {
		this.ymlMap = hm;
	}

	public YamlParser() {
		this.ymlMap = new HashMap<String, Object>();
	}

	public void parse(InputStream ymlInputStream) {

		Yaml yaml = new Yaml();

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Object> linkedMap = (LinkedHashMap<String, Object>) yaml.load(ymlInputStream);

		parseMap(ymlMap, linkedMap, null);

	}

	public String[] getKeys() {

		return ymlMap.keySet().toArray(new String[0]);

	}

	public Object getObjectValue(String key) {

		return getObjectValue(key, null);

	}

	public Object getObjectValue(String key, Object defaultValue) {

		Object ob = ymlMap.get(key);
		if (ob == null)
			return defaultValue;
		else
			return ob.toString();

	}

	public String getStringValue(String key) {

		return getStringValue(key, null);
	}

	public String getStringValue(String key, String defaultValue) {

		Object ob = ymlMap.get(key);

		if (ob != null)
			return ob.toString();

		else
			return defaultValue;

	}

	private Integer getIntegerValue(String key, Integer defaultValue) {

		Object ob = ymlMap.get(key);

		if (ob == null || !(ob instanceof Integer))
			return defaultValue;

		else
			return (Integer) ob;

	}

	private Double getDoubleValue(String key, Double defaultValue) {

		Object ob = ymlMap.get(key);

		if (ob == null || !(ob instanceof Double))
			return defaultValue;

		else
			return (Double) ob;

	}

	public Double getDoubleValue(String key, double daultValue) {

		return getDoubleValue(key, Double.valueOf(daultValue));

	}

	public Double getDoubleValue(String key) {

		return getDoubleValue(key, null);

	}

	private Boolean getBooleanValue(String key, Boolean defaultValue) {

		Object ob = ymlMap.get(key);

		if (ob == null || !(ob instanceof Boolean))
			return defaultValue;

		else
			return (Boolean) ob;

	}

	public Boolean getBooleanValue(String key, boolean daultValue) {

		return getBooleanValue(key, Boolean.valueOf(daultValue));

	}

	public Boolean getBooleanValue(String key) {

		return getBooleanValue(key, null);

	}

	public Integer getIntegerValue(String key, int daultValue) {

		return getIntegerValue(key, Integer.valueOf(daultValue));

	}

	public Integer getIntegerValue(String key) {

		return getIntegerValue(key, null);

	}

	@SuppressWarnings("unchecked")
	public YamlParser[] getArray(String key) {

		Object v = this.ymlMap.get(key);

		if (v != null && v instanceof HashMap<?, ?>) {

			HashMap<String, Object> hm = (HashMap<String, Object>) v;

			YamlParser[] yps = new YamlParser[hm.size()];

			Iterator<Entry<String, Object>> it = hm.entrySet().iterator();

			int i = 0;
			while (it.hasNext()) {
				yps[i] = new YamlParser((HashMap<String, Object>) it.next().getValue());
				i++;
			}
			return yps;

		}

		return new YamlParser[0];
	}

	@SuppressWarnings("unchecked")
	private void parseArrayMap(HashMap<String, Object> hm, ArrayList<Object> al, String parentKey) {

		for (int i = 0; i < al.size(); i++) {
			HashMap<String, Object> hma = new HashMap<String, Object>();
			parseMap(hma, (LinkedHashMap<String, Object>) al.get(i), null);
			hm.put(String.valueOf(i), hma);
		}
	}

	@SuppressWarnings("unchecked")
	private void parseMap(HashMap<String, Object> hm, LinkedHashMap<String, Object> linkedMap, String parentKey) {

		Iterator<String> it = linkedMap.keySet().iterator();

		while (it.hasNext()) {

			String key = (String) it.next();

			Object value = linkedMap.get(key);

			String pn = key;
			if (parentKey != null)
				pn = parentKey + "." + key;

			if (value instanceof LinkedHashMap<?, ?>) {

				parseMap(hm, (LinkedHashMap<String, Object>) value, pn);

			} else if (value instanceof ArrayList<?>) {

				HashMap<String, Object> hma = new HashMap<String, Object>();
				parseArrayMap(hma, (ArrayList<Object>) value, pn);
				hm.put(pn, hma);

			} else {

				hm.put(pn, value);

			}

		}

	}

	public void parse(String ymlFileName) throws IOException {

		File f = new File(ymlFileName);

		FileInputStream fi = new FileInputStream(f);

		parse(fi);

		fi.close();

	}

}
