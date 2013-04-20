package net.likelion.weatheralarm;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {


		Intent i = new Intent( context, DownloadActivity.class );
		PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
		try {
		          pi.send();
		} catch (CanceledException e) {
		          e.printStackTrace();
		}
	}

}