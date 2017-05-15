package cn.flying.rest.onlinefile.chat;

public class DebugLog {

	public static final boolean DEBUG = false;
	
	public static final int DEBUG_LEVEL = 2;
	
	public static void dbg(String msg) {
		dbg(msg, 0);
	}

	public static void dbg(String msg, int lvl) {
		if (!DEBUG)
			return;
		if (lvl < DEBUG_LEVEL)
			return;
		System.out.println("[" + lvl + "] " + msg);
	}
}
