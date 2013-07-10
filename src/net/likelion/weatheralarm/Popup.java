package net.likelion.weatheralarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class Popup extends Activity {
	private static MySoundPlay mplay = null;
	private TextView timer_text;
	private Button snooze_btn;
	private int timer_remain = 300;
	private CountDownTimer cdt;
	String m4aSource = "http://weather.cycorld.com/data/today.m4a";
	WebView mWv;
	private static int currentVolume;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup);
		//earlybird webview
		TelephonyManager systemService = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String PhoneNumber = systemService.getLine1Number();    //폰번호를 가져오는 겁니다..
        PhoneNumber = PhoneNumber.substring(PhoneNumber.length()-10,PhoneNumber.length());
        PhoneNumber="0"+PhoneNumber;
        //Toast.makeText(getApplicationContext(),PhoneNumber, Toast.LENGTH_SHORT).show();
		
        mWv = (WebView) findViewById(R.id.earlybird); 
        mWv.getSettings().setJavaScriptEnabled(true);  // 웹뷰에서 자바스크립트실행가능
        mWv.loadUrl("http://weather.cycorld.com/earlybird/?f=check?pn=" + PhoneNumber);  // 인터넷 경로 지정
        mWv.setWebViewClient(new HelloWebViewClient());  // WebViewClient 지정 

		// 이 부분이 바로 화면을 깨우는 부분 되시겠다.
		// 화면이 잠겨있을 때 보여주기
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						// 키잠금 해제하기
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						// 화면 켜기
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		timer_text = (TextView) findViewById(R.id.timer_text);
		snooze_btn = (Button) findViewById(R.id.snooze_btn);	
		
		cdt = new CountDownTimer(300 * 1000, 1000) {  
	        public void onTick(long millisUntilFinished) {  
	        	timer_remain--;
	            timer_text.setText("다음 알람까지 " + timer_remain + "초 남았습니다");  
	        }  

	        public void onFinish() {  
	            timer_text.setText("좋은 아침입니다~");
	            timer_remain = 300;
	            snooze_btn.setVisibility(View.INVISIBLE);
	            mplay.replay();
	        }  
	    };  
		Context context = getApplicationContext();
		
	
		AudioManager am = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
				(int) (am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)),
				AudioManager.FLAG_PLAY_SOUND);

		try {
			if (mplay == null) {
				mplay = new MySoundPlay(context, Uri.parse(m4aSource));
			}

			mplay.play();
		} catch (Exception e) {
			mplay = null;
			mplay = new MySoundPlay(context, R.raw.nointernet);
			timer_text.setText("좋은 아침입니다~! 인터넷 연결이 안되어 날씨 정보를 불러오지 못했습니다 ");
			mplay.play();
		}

	}
    private class HelloWebViewClient extends WebViewClient { //주소창 없앰 
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
    		if(url.startsWith("kakolink:")){
    			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    			startActivity(intent);
    	      }
    		else{
    			view.loadUrl(url);
    		}
    		return true; 
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
		//mplay.stop();
		mplay.end();
		mplay = null;
		finish();
		AudioManager am = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
				currentVolume,
				AudioManager.FLAG_PLAY_SOUND);
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