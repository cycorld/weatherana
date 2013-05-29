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
		//set.setJavaScriptCanOpenWindowsAutomtically (true);   // javascript�� window.open()�� ����� �� �ֵ��� ����
		set.setBuiltInZoomControls(true); // �ȵ���̵忡�� �����ϴ� �� �������� ����� �� �ֵ��� ����
		set.setPluginState(WebSettings.PluginState.ON_DEMAND); // �÷������� ����� �� �ֵ��� ����
		set.setSupportMultipleWindows(true); // �������� �����츦 ����� �� �ֵ��� ����
		set.setSupportZoom(true); // Ȯ��,��� ����� ����� �� �ֵ��� ����
		set.setBlockNetworkImage(false); // ��Ʈ��ũ�� �̹����� ���ҽ��� �ε���������
		set.setLoadsImagesAutomatically(true); // ���䰡 �ۿ� ��ϵǾ� �ִ� �̹��� ���ҽ��� �ڵ����� �ε��ϵ��� ����
		set.setUseWideViewPort(true); // wide viewport�� ����ϵ��� ����
		set.setCacheMode(WebSettings.LOAD_NO_CACHE); // ���䰡 ĳ�ø� ������� �ʵ��� ����
		
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
