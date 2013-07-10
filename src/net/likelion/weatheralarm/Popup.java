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
        String PhoneNumber = systemService.getLine1Number();    //����ȣ�� �������� �̴ϴ�..
        PhoneNumber = PhoneNumber.substring(PhoneNumber.length()-10,PhoneNumber.length());
        PhoneNumber="0"+PhoneNumber;
        //Toast.makeText(getApplicationContext(),PhoneNumber, Toast.LENGTH_SHORT).show();
		
        mWv = (WebView) findViewById(R.id.earlybird); 
        mWv.getSettings().setJavaScriptEnabled(true);  // ���信�� �ڹٽ�ũ��Ʈ���డ��
        mWv.loadUrl("http://weather.cycorld.com/earlybird/?f=check?pn=" + PhoneNumber);  // ���ͳ� ��� ����
        mWv.setWebViewClient(new HelloWebViewClient());  // WebViewClient ���� 

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
			timer_text.setText("���� ��ħ�Դϴ�~! ���ͳ� ������ �ȵǾ� ���� ������ �ҷ����� ���߽��ϴ� ");
			mplay.play();
		}

	}
    private class HelloWebViewClient extends WebViewClient { //�ּ�â ���� 
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