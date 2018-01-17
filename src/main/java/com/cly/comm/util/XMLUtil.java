package com.cly.comm.util;

import java.io.FileInputStream; 
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory; 

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtil {

	private XMLUtil() {

	}

	public static Document parse(String fileName) throws SAXException {

		try {

			FileInputStream is = new FileInputStream(fileName);

			return parse(is);

		} catch (Exception e) {
			throw new SAXException(e);
		}
	}

	public static Document parse(InputStream is) throws SAXException {

		try {

			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

		} catch (Exception e) {
			throw new SAXException(e);
		}
	}

	public static Element[] getChildElements(Element eParent) {

		NodeList nl = eParent.getChildNodes();

		ArrayList<Element> al = new ArrayList<Element>();

		for (int i = 0; i < nl.getLength(); i++) {

			Node nc = nl.item(i);

			if (nc.getNodeType() == Element.ELEMENT_NODE)
				al.add((Element) nc);

		}

		return al.toArray(new Element[0]);
	}

}
