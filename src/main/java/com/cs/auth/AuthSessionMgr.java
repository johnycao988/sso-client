package com.cs.auth;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class AuthSessionMgr {

	private final static HashMap<String, AuthSession> hmAuthSess = new HashMap<String, AuthSession>();

	private static SessionScan st;

	public static void init(long sessionExpiredTimeSeconds) {

		if (st == null) {
			st = new SessionScan(sessionExpiredTimeSeconds, hmAuthSess);
			st.start();
		}

	}

	public static void destroy() {

		if (st != null) {

			st.shutdown();

			st = null;

		}

		hmAuthSess.clear();

	}

	public static AuthSession getAuthSession(String key) {

		return hmAuthSess.get(key);

	}

	public static void putAuthSession(String key, AuthSession as) {
		hmAuthSess.put(key, as);
	}

}

class SessionScan extends Thread {

	private boolean bShutdown = false;
	private long sessionExpiredTimeSeconds;
	private HashMap<String, AuthSession> hmAuthSess;
	private long procTimeSeconds = 60;

	public SessionScan(long sessionExpiredTimeSeconds, HashMap<String, AuthSession> hmAuthSess) {

		this.sessionExpiredTimeSeconds = sessionExpiredTimeSeconds;

		this.hmAuthSess = hmAuthSess;

	}

	public void run() {

		long count = 0;
		long waintMS = 500;

		long msPt = procTimeSeconds * 1000;
		while (!bShutdown) {

			waitLoop(waintMS);
			count += waintMS;
			if (count >= msPt) {
				scanSession();
				count = 0;
			}

		}

	}

	private void scanSession() {

		System.out.println("Auth Session is scan...:");

		synchronized (this.hmAuthSess) {

			Iterator<Entry<String, AuthSession>> iter = hmAuthSess.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, AuthSession> entry = iter.next();
				AuthSession as = entry.getValue();
				as.increaseTimeCounts(procTimeSeconds);
				if (as.getTimeCounts() >= this.sessionExpiredTimeSeconds) {
					iter.remove();
					System.out.println("Auth Session is removed:" + as);
				}
			}

		}

	}

	private void waitLoop(long waitMS) {

		try {

			Thread.sleep(waitMS);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void shutdown() {

		this.bShutdown = true;

	}

}
