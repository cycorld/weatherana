package net.likelion.weatheralarm;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Popup extends Activity {
	private static final LayoutParams COVER_SCREEN_GRAVITY_CENTER = null;
	//private static MySoundPlay mplay = null;
	private TextView timer_text;
	private int timer_remain = 300;
	private boolean snooze = false;
	private CountDownTimer cdt;

	private WebView webView;
	
	FrameLayout mContentView;
	WebView mWebView;
	FrameLayout mCustomViewContainer;
	View mCustomView;
	CustomViewCallback mCustomViewCallback;
	//Object mCustomViewCallback;
	//View mCustomViewCallback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup);
		
		mContentView = (FrameLayout) findViewById(R.id.main_content);
	    mWebView = (WebView) findViewById(R.id.webView);
	    mCustomViewContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
		
		// 이 부분이 바로 화면을 깨우는 부분 되시겠다.
		// 화면이 잠겨있을 때 보여주기
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						// 키잠금 해제하기
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						// 화면 켜기
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//timer_text = (TextView) findViewById(R.id.timer_text);

		//webView = (WebView) findViewById(R.id.webView);
		

		
		
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setUseWideViewPort(false);
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);  
        webSettings.setLoadsImagesAutomatically(true);  
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  
        //webSettings.setUserAgent(1);  
        webSettings.setSupportMultipleWindows(false);  
        //webSettings.setPluginsEnabled(true);
        webSettings.setPluginState(PluginState.ON);
		mWebView.setWebChromeClient(new WebChromeClient() {
		});
		mWebView.setWebViewClient(new WebViewClient(){});
		
		//webView.setWebViewClient(new WebBrowserClient());
		//String url = "http://www.youtube.com/v/EjrqTsJBUUA?enablejsapi=1&version=3&playerapiid=ytplayer";
		//String url = "rtsp://v3.cache1.c.youtube.com/CjYLENy73wIaLQmwE4_cwEoWqBMYDSANFEIJbXYtZ29vZ2xlSARSBXdhdGNoYI2RrZrR6ryvUQw=/0/0/0/video.3gp";
		String url = "http://youtube.com/embed/EjrqTsJBUUA?autoplay=1&loop=1";
		//String url = "http://youtube.googleapis.com/v/EjrqTsJBUUA?version=2&autoplay=1&loop=1&autohide=1";
		//String url = "<html><body>Youtube <br> <iframe class=\"youtube-player\" type=\"text/html\" width=\"600\" height=\"300\" src=\"http://www.youtube.com/embed/bIPcobKMB94\" frameborder=\"0\"></body></html>";

		mWebView.loadUrl(url);
	
		////
		
		/*
		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new WebBrowserClient());		
		WebSettings set = webView.getSettings();		
		set.setJavaScriptEnabled(true);		
		set.setBuiltInZoomControls(true);		
		set.setLoadsImagesAutomatically(true);		
		set.setCacheMode(WebSettings.LOAD_NO_CACHE);		
		set.setUserAgent(1);		
		set.setSupportMultipleWindows(false);		
		set.setPluginState(PluginState. ON);	
		final String mimeType = "text/html";		
		final String encoding = "utf-8";		
		String url = "<html><body>Youtube <br> <iframe class=\"youtube-player\" type=\"text/html\" width=\"600\" height=\"300\" src=\"http://www.youtube.com/embed/bIPcobKMB94\" frameborder=\"0\"></body></html>";

		webView.loadData(url, mimeType, encoding);
		////
		
		 */
		
		cdt = new CountDownTimer(300 * 1000, 1000) {  
	        public void onTick(long millisUntilFinished) {  
	        	timer_remain--;
	            timer_text.setText("다음 알람까지 " + timer_remain + "초 남았습니다");  
	        }  
	 
	        public void onFinish() {  
	            timer_text.setText("좋은 아침입니다~");
	            timer_remain = 300;
	            snooze = false;
	            //mplay.replay();
	        }  
	    };  
		/*
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
		 */
	}
	
	@Override
	protected void onStop()
	{
	    super.onStop();
	    if (mCustomView != null)
	    {
	        
			if (mCustomViewCallback != null)
	            mCustomViewCallback.onCustomViewHidden();
	        mCustomView = null;
	    }
	}

	@Override
	public void onBackPressed()
	{
	    if (mCustomView != null)
	    {
	        onHideCustomView();
	    } else
	    {
	        finish();
	    }
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onPauseClicked(View view) throws InterruptedException {
		//mplay.stop();
		if(!snooze) cdt.start();
		//mplay.replay();

	}

	public void onStopClicked(View view) {
		cdt.cancel();
		//mplay.stop();
		snooze = false;
		finish();
	}
/*
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
	*/
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	
	
	void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
	{
	    // if a view already exists then immediately terminate the new one
	    if (mCustomView != null)
	    {
	        callback.onCustomViewHidden();
	        return;
	    }

	    // Add the custom view to its container.
	    mCustomViewContainer.addView(view, COVER_SCREEN_GRAVITY_CENTER);
	    mCustomView = view;
	    mCustomViewCallback = callback;

	    // hide main browser view
	    mContentView.setVisibility(View.GONE);

	    // Finally show the custom view container.
	    mCustomViewContainer.setVisibility(View.VISIBLE);
	    mCustomViewContainer.bringToFront();
	}

	void onHideCustomView()
	{
	    if (mCustomView == null)
	        return;

	    // Hide the custom view.
	    mCustomView.setVisibility(View.GONE);
	    // Remove the custom view from its container.	
	    mCustomViewContainer.removeView(mCustomView);
	    mCustomView = null;
	    mCustomViewContainer.setVisibility(View.GONE);
	    mCustomViewCallback.onCustomViewHidden();

	    // Show the content view.
	    mContentView.setVisibility(View.VISIBLE);
	}
/*
	public Bitmap getDefaultVideoPoster()
	{
	    if (mDefaultVideoPoster == null)
	    {
	        mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(), R.drawable.default_video_poster);
	    }
	    return mDefaultVideoPoster;
	}

	public View getVideoLoadingProgressView()
	{
	    if (mVideoProgressView == null)
	    {
	        LayoutInflater inflater = LayoutInflater.from(this);
	        mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
	    }
	    return mVideoProgressView;
	}
	*/
	
}

class WebBrowserClient extends WebViewClient {
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}	
}
