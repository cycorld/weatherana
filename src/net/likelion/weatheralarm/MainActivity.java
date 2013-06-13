package net.likelion.weatheralarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	private static final String       TAG_LOG                 = "MainActivity";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TimePicker tp = (TimePicker) findViewById(R.id.timePicker);		
		tp.setIs24HourView(true);
		Calendar now = Calendar.getInstance();
		SharedPreferences pref = getSharedPreferences("WeatherAlarm", 0);
		int hour = pref.getInt("Hour", now.get(Calendar.HOUR_OF_DAY));
		int minute = pref.getInt("Min", now.get(Calendar.MINUTE));
		tp.setCurrentHour(hour);
		tp.setCurrentMinute(minute);
		
		int alarmSet = pref.getInt("alarmSet", 0);
		if (alarmSet == 1) {
			ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmtoggle);
			alarmToggle.setChecked(true);
		}

		//boolean alarmUp = (PendingIntent.getBroadcast(MainActivity.this, 0, 
		//		new Intent(MainActivity.this, AlarmReceiver.class), 
		//        PendingIntent.FLAG_NO_CREATE) != null);
			
		//if (alarmUp)
		//{
		//	ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmtoggle);
		//   alarmToggle.setChecked(true);
		//}
		//Button setTime = (Button) findViewById(R.id.set_time);


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void onPreplayClicked(View view) {
		Context context = getApplicationContext();
		
		Intent i = new Intent( context, Popup.class );
		PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
		try {
		          pi.send();
		} catch (CanceledException e) {
		          e.printStackTrace();
		}
	}
	public void onAlarmToggleClicked(View view) throws ParseException {
		Output.i(TAG_LOG,"AlarmToggle");

	    SharedPreferences pref = getSharedPreferences("WeatherAlarm", 0);
	    SharedPreferences.Editor edit = pref.edit();
	    
		
	    // Is the toggle on?	
	    boolean on = ((ToggleButton) view).isChecked();
	    if (on) {
	        // Enable vibrate
	    	Output.i(TAG_LOG,"alarm_toggle_on");
	    	//textView.setText(String.valueOf(cal.getTimeInMillis()));
	    	edit.putInt("alarmSet", 1);
	    	edit.commit();
	    	alarmSet();
	    } else {
	        // Disable vibrate
	    	//textView.setText("alarm off");
	    	
	    	Output.i(TAG_LOG,"alarm_toggle_off");

	    	edit.putInt("alarmSet", 0);
	    	edit.commit();
	    	alarmSet();
	    }
	}
	public void onUpdateClicked(View view) {
		
		Context context = getApplicationContext();
		
		Intent i = new Intent( context, InfoActivity.class );
		PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
		try {
		          pi.send();
		} catch (CanceledException e) {
		          e.printStackTrace();
		}
		/*
		// update button clicked
			ConnectivityManager connMgr = (ConnectivityManager) 
		    getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    if (networkInfo != null && networkInfo.isConnected()) {
		        // fetch data
		    } else {
		        // display error
		    }
		    */
	}
	public void onSetTimeClicked(View view) throws ParseException {
		// Set Time Clicked
		ToggleButton toggleBtn = (ToggleButton) findViewById(R.id.alarmtoggle);
		toggleBtn.setChecked(true);
	    SharedPreferences pref = getSharedPreferences("WeatherAlarm", 0);
	    SharedPreferences.Editor edit = pref.edit();
    	edit.putInt("alarmSet", 1);
    	edit.commit();	    
		alarmSet();
	}
	public void onWeatherViewClicked(View view) {
		Context context = getApplicationContext();
		
		Intent i = new Intent( context, WeatherInfoActivity.class );
		PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
		try {
		          pi.send();
		} catch (CanceledException e) {
		          e.printStackTrace();
		}
	}
	public void alarmSet() throws ParseException {
		TimePicker tp = (TimePicker) findViewById(R.id.timePicker);
		SharedPreferences pref = getSharedPreferences("WeatherAlarm", 0);
	    SharedPreferences.Editor edit = pref.edit();
	    edit.putInt("Hour", tp.getCurrentHour());
	    edit.putInt("Min", tp.getCurrentMinute());
    	edit.commit();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String date = df.format(new Date(System.currentTimeMillis()));
    	StringTokenizer stk = new StringTokenizer(date, " ");
    	//Output.i(TAG_LOG, stk.nextToken());
    	date = stk.nextToken() + " " + tp.getCurrentHour()+":"+tp.getCurrentMinute()+":"+"00";
    	Date dateType;
    	dateType = df.parse(date);
    	Long set_time = dateType.getTime();
    	//Output.i(TAG_LOG, Integer.toString(set_time));

		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("net.likelion.weatheralarm.alarm");
		PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
		
		int SECS = 1000;
	    int MINS = 60 * SECS;
	    if(pref.getInt("alarmSet", -1) == 1) {
	    	if(System.currentTimeMillis() < set_time)	// 오늘 이전 시간 설정시 한번 실행되는 기능 제 (테스트 필요)
	    		am.setRepeating(AlarmManager.RTC_WAKEUP, set_time,  24 * 60 * MINS, sender);
	    	else
	    		am.setRepeating(AlarmManager.RTC_WAKEUP, set_time + 24 * 60 * MINS, 24 * 60 * MINS, sender);
	    	Context context = getApplicationContext();
		    Toast.makeText(context, "날씨아나 알람이 등록되었습니다", Toast.LENGTH_LONG).show();
	    } else if(pref.getInt("alarmSet", -1) == 0) {
	    	am.cancel(sender);
	    	sender.cancel();
	    }		
	    
		
	}

}
