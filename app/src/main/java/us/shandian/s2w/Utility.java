package us.shandian.s2w;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Utility
{
	
	public static enum State {
		DISABLED,
		DT2W,
		S2W
	}
	
	public static final String KOBJ_DIR = "/sys/android_touch/";
	public static final String KOBJ_S2W = KOBJ_DIR + "s2w_enabled";
	public static final String KOBJ_DT2W = KOBJ_DIR + "dt2w_enabled";
	
	public static final String PREF = "pref";
	public static final String PREF_STATE = "state";
	
	public static State readState() throws IOException {
		String dt2w = execWithSu("cat " + KOBJ_DT2W).substring(0, 1);
		String s2w = execWithSu("cat " + KOBJ_S2W).substring(0, 1);
		
		if (dt2w.equals("1") && s2w.equals("0")) {
			return State.DT2W;
		} else if (dt2w.equals("0") && s2w.equals("1")) {
			return State.S2W;
		} else {
			return State.DISABLED;
		}
	}
	
	public static void setState(State state) throws IOException {
		if (state == State.DT2W) {
			execWithSu("echo '1' > " + KOBJ_DT2W);
			execWithSu("echo '0' > " + KOBJ_S2W);
		} else if (state == State.S2W) {
			execWithSu("echo '0' > " + KOBJ_DT2W);
			execWithSu("echo '1' > " + KOBJ_S2W);
		} else if (state == State.DISABLED) {
			execWithSu("echo '0' > " + KOBJ_DT2W);
			execWithSu("echo '0' > " + KOBJ_S2W);
		}
	}
	
	public static void writeStateToPref(Context context, State state) {
		SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_WORLD_READABLE);
		int i = 0;
		if (state == State.DT2W) {
			i = 1;
		} else if (state == State.S2W) {
			i = 2;
		}
		
		pref.edit().putInt(PREF_STATE, i).commit();
	}
	
	public static State getStateFromPref(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF, Context.MODE_WORLD_READABLE);
		int i = pref.getInt(PREF_STATE, 0);
		switch (i) {
			case 0:
				return State.DISABLED;
			case 1:
				return State.DT2W;
			case 2:
				return State.S2W;
			default:
				return State.DISABLED;
		}
	}
	
	private static String readInputStream(InputStream ipt) throws IOException {
		StringBuilder sb = new StringBuilder();
		int len = -1;
		byte[] buf = new byte[512];
		while((len = ipt.read(buf)) != -1) {
			sb.append(new String(buf, 0, len, "UTF-8"));
		}
		ipt.close();
		return sb.toString();
	}
	
	private static String execWithSu(String cmd) throws IOException {
		Process p = Runtime.getRuntime().exec("su");
		OutputStream o = p.getOutputStream();
		
		o.write(cmd.getBytes());
		o.flush();
		o.close();
		
		return readInputStream(p.getInputStream());
	}
}
