package net.likelion.weatheralarm;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.support.v4.app.NavUtils;

public class WeatherInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_info);
		// Show the Up button in the action bar.
		setupActionBar();
		
		WebView wv= (WebView) findViewById(R.id.webView);
		wv.loadUrl("http://m.kma.go.kr/m/observation/observation_01.jsp");
		
		WebSettings set = wv.getSettings();
		set.setJavaScriptEnabled(true);
		//set.setJavaScriptCanOpenWindowsAutomtically (true);   // javascript가 window.open()을 사용할 수 있도록 설정
		set.setBuiltInZoomControls(true); // 안드로이드에서 제공하는 줌 아이콘을 사용할 수 있도록 설정
		set.setPluginState(WebSettings.PluginState.ON_DEMAND); // 플러그인을 사용할 수 있도록 설정
		set.setSupportMultipleWindows(true); // 여러개의 윈도우를 사용할 수 있도록 설정
		set.setSupportZoom(true); // 확대,축소 기능을 사용할 수 있도록 설정
		set.setBlockNetworkImage(false); // 네트워크의 이미지의 리소스를 로드하지않음
		set.setLoadsImagesAutomatically(true); // 웹뷰가 앱에 등록되어 있는 이미지 리소스를 자동으로 로드하도록 설정
		set.setUseWideViewPort(true); // wide viewport를 사용하도록 설정
		set.setCacheMode(WebSettings.LOAD_NO_CACHE); // 웹뷰가 캐시를 사용하지 않도록 설정
		
		//wv.setWebViewClient(new TabWebViewClient());
		 
        
		
		
		
	}


	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather_info, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void gotoHomeClicked(View view) {
		finish();
	}

}
