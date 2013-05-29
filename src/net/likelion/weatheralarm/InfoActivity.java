package net.likelion.weatheralarm;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}
	public void onCloseClicked(View view){
		finish();
	}

}
