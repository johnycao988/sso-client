package com.cs.sso;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.annotation.Order;

import com.cs.auth.base.AuthMgr;

@Order(1)
@WebFilter(filterName = "testFilter1", urlPatterns = "/*")
public class SSOFilter implements Filter {

	@Override
	public void destroy() {

		AuthMgr.destroy();

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		HttpServletResponse res = (HttpServletResponse) request;
		// System.out.println("Start filter....");

		// this.printSessions(req, res);

		this.printHeaders(req, res);

		this.printCookies(req, res);

		// this.printRequestParams(req, res);

		// AuthMgr.getServletFilter().doFilter(req, res, chain);

		AuthMgr.getServletFilter(req, res).doFilter(req, res, chain);

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		AuthMgr.init(filterConfig);

	}

	private void printHeaders(ServletRequest request, ServletResponse response) {

		HttpServletRequest req = (HttpServletRequest) request;
		Enumeration<String> eh = req.getHeaderNames();

		System.out.println("Print Request Headers :...........");

		while (eh.hasMoreElements()) {
			String hn = eh.nextElement();
			System.out.println(hn + ": " + req.getHeader(hn));

		}

		eh = req.getAttributeNames();

		System.out.println("Print Header Attributes:...........");

		while (eh.hasMoreElements()) {
			String hn = eh.nextElement();

			System.out.println(hn + ": " + req.getAttribute(hn));

		}

	}

	private void printRequestParams(ServletRequest request, ServletResponse response) {

		System.out.println("Print Request Parameters:...........");
		HttpServletRequest req = (HttpServletRequest) request;
		Enumeration<String> eh = req.getParameterNames();

		while (eh.hasMoreElements()) {
			String pn = eh.nextElement();

			String[] pvs = req.getParameterValues(pn);
			for (String pv : pvs)
				System.out.println(pn + ": " + pv);

		}

	}

	private void printCookies(ServletRequest request, ServletResponse response) {

		HttpServletRequest req = (HttpServletRequest) request;
		Cookie[] cs = req.getCookies();

		System.out.println("Print Request Cookies:...........");

		if (cs != null)
			for (Cookie c : cs) {

				System.out.println(c.getName() + ": " + c.getValue());

			}

	}

	private void printSessions(ServletRequest request, ServletResponse response) {

		System.out.println("Session ...");

		HttpServletRequest req = (HttpServletRequest) request;

		HttpSession ses = req.getSession(false);

		if (ses == null)
			return;

		@SuppressWarnings("deprecation")
		String[] vs = ses.getValueNames();

		System.out.println("Session Values....");

		for (String v : vs) {

			System.out.println(v + ": " + ses.getValue(v));

		}

		Enumeration<String> ets = ses.getAttributeNames();

		while (ets.hasMoreElements()) {

			String n = ets.nextElement();
			System.out.println(n + ": " + ses.getAttribute(n));

		}

	}

}
