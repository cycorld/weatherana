package net.likelion.weatheralarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;


public class SetAlarmReboot extends BroadcastReceiver {
	   
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences pref = context.getSharedPreferences("WeatherAlarm", 0);
		Calendar now = Calendar.getInstance();
		int hour = pref.getInt("Hour", now.get(Calendar.HOUR_OF_DAY));
		int minute = pref.getInt("Min", now.get(Calendar.MINUTE));
		
		int alarmSet = pref.getInt("alarmSet", 0);
		if (alarmSet == 1) {	
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	String date = df.format(new Date(System.currentTimeMillis()));
	    	StringTokenizer stk = new StringTokenizer(date, " ");
	    	//Output.i(TAG_LOG, stk.nextToken());
	    	date = stk.nextToken() + " " + String.valueOf(hour) +":"+ String.valueOf(minute) +":"+"00";
	    	Date dateType;
			try {
				dateType = df.parse(date);
				Long set_time = dateType.getTime();
				AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
				Intent alarm = new Intent("net.likelion.weatheralarm.alarm");
				PendingIntent sender = PendingIntent.getBroadcast(context, 0, alarm, 0);
				
				int SECS = 1000;
			    int MINS = 60 * SECS;
			    if(pref.getInt("alarmSet", -1) == 1) {
			    	if(System.currentTimeMillis() < set_time)	// 오늘 이전 시간 설정시 한번 실행되는 기능 제 (테스트 필요)
			    		am.setRepeating(AlarmManager.RTC_WAKEUP, set_time,  24 * 60 * MINS, sender);
			    	else
			    		am.setRepeating(AlarmManager.RTC_WAKEUP, set_time + 24 * 60 * MINS, 24 * 60 * MINS, sender);
			    } else if(pref.getInt("alarmSet", -1) == 0) {
			    	am.cancel(sender);
			    	sender.cancel();
			    }		
				Toast.makeText(context, "날씨아나 알람이 재시작되었습니다", Toast.LENGTH_LONG).show();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	//Output.i(TAG_LOG, Integer.toString(set_time));
	
			

		}
	
		}

}