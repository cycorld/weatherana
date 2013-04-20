package net.likelion.weatheralarm;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class Popup extends Activity {
	private static MySoundPlay mplay = null;
	private TextView timer_text;
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

		cdt = new CountDownTimer(300 * 1000, 1000) {  
	        public void onTick(long millisUntilFinished) {  
	        	timer_remain--;
	            timer_text.setText("���� �˶����� " + timer_remain + "�� ���ҽ��ϴ�");  
	        }  
	 
	        public void onFinish() {  
	            timer_text.setText("���� ��ħ�Դϴ�~");
	            timer_remain = 300;
	            mplay.replay();
	        }  
	    };  
		Context context = getApplicationContext();
		AudioManager am = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
				(int) (am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)),
				AudioManager.FLAG_PLAY_SOUND);

		try {
			if (mplay == null) {
				mplay = new MySoundPlay(context, R.raw.a20130308);
			}

			mplay.play();
		} catch (Exception e) {

		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onPauseClicked(View view) throws InterruptedException {
		mplay.stop();
		cdt.start();
		//mplay.replay();

	}

	public void onStopClicked(View view) {
		cdt.cancel();
		mplay.stop();
		finish();
	}

	public class MySoundPlay {
		MediaPlayer mp = null;

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
	}

}