package us.shandian.s2w;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;

public class BootReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Utility.setState(Utility.getStateFromPref(context));
		} catch (IOException e) {
			
		}
	}
}
