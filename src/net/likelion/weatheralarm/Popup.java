package net.likelion.weatheralarm;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Popup extends Activity {
	private static MySoundPlay mplay = null;
	private TextView timer_text;
	private Button snooze_btn;
	private int timer_remain = 300;
	private CountDownTimer cdt;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup);

		// �� �κ��� �ٷ� ȭ���� ����� �κ� �ǽðڴ�.
		// ȭ���� ������� �� �����ֱ�
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						// Ű��� �����ϱ�
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						// ȭ�� �ѱ�
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		timer_text = (TextView) findViewById(R.id.timer_text);
		snooze_btn = (Button) findViewById(R.id.snooze_btn);	
		
		cdt = new CountDownTimer(300 * 1000, 1000) {  
	        public void onTick(long millisUntilFinished) {  
	        	timer_remain--;
	            timer_text.setText("���� �˶����� " + timer_remain + "�� ���ҽ��ϴ�");  
	        }  

	        public void onFinish() {  
	            timer_text.setText("���� ��ħ�Դϴ�~");
	            timer_remain = 300;
	            snooze_btn.setVisibility(View.INVISIBLE);
	            mplay.replay();
	        }  
	    };  
		Context context = getApplicationContext();
		/*
		try {
			//URL url = new URL("http://choiyongchol.com/weatheralarm/data/source.xml");
			//InputStream is = url.openStream();
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		AudioManager am = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
				(int) (am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)),
				AudioManager.FLAG_PLAY_SOUND);

		try {
			if (mplay == null) {
				mplay = new MySoundPlay(context, Uri.parse("http://choiyongchol.com/weatheralarm/data/today.m4a"));
			}

			mplay.play();
		} catch (Exception e) {
			mplay = null;
			mplay = new MySoundPlay(context, R.raw.a20130308);
			timer_text.setText("���� ��ħ�Դϴ�~! ���ͳ� ������ �ȵǾ� ���� ������ �ҷ����� ���߽��ϴ� ");
			mplay.play();
		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onSnoozeClicked(View view) throws InterruptedException {
		mplay.stop();
		cdt.start();
		//mplay.replay();
		snooze_btn.setVisibility(View.INVISIBLE);

	}

	public void onStopClicked(View view) {
		cdt.cancel();
		mplay.stop();
		mplay.end();
		mplay = null;
		finish();
	}

	public class MySoundPlay {
		MediaPlayer mp = null;

		public MySoundPlay(Context context, Uri id) {
			mp = MediaPlayer.create(context, id);
		}
		public MySoundPlay(Context context, int id) {
			mp = MediaPlayer.create(context, id);
		}

		public void play() {
			mp.setLooping(true);
			mp.seekTo(0);
			mp.start();
		}

		public void stop() {
			mp.stop();
			mp.prepareAsync();
		}
		public void replay() {
			mp.start();
		}
		public void end() {
			mp.release();
		}
	}

}