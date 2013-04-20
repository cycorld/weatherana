package net.likelion.weatheralarm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;

public class DownloadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);

		AlarmManager dm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(DownloadActivity.this, UpdateReceiver.class);
		PendingIntent down = PendingIntent.getService(DownloadActivity.this, 0, intent, 0);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = "2013-03-03 01:00:00";
		Date dateType = null;
		try {
			dateType = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long set_time = dateType.getTime();
		if (!isDmActivated()) {
			dm.setRepeating(AlarmManager.RTC_WAKEUP, set_time,
					24 * 60 * 60 * 1000, down);
		}
		// ���� �ٿ�ε�...
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			// fetch data
			downLoad();
		} else {
			// display error
		}

	}

	public void saveRemoteFile(InputStream is, OutputStream os)
			throws IOException {
		int c = 0;
		while ((c = is.read()) != -1)
			os.write(c);
		os.flush();
	}

	private boolean isDmActivated() {
		boolean result;
		PendingIntent down;

		Intent intentToSend = new Intent();
		intentToSend.setAction("SOME_ACTION");

		down = PendingIntent.getService(getBaseContext(), 0, intentToSend,
				PendingIntent.FLAG_NO_CREATE);

		result = down != null;
		return result;
	}

	private void downLoad() {
		try {
			// �ٿ�ε� ���� URL�� ���� ��ü�� �����Ѵ�.
			//
			URL url = new URL("http://www.choiyongchol.com/weatheralarm/data/recent.m4a");

			// URL�� ���� connection ��ü�� �� �����Ѵ�.
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			// ������ Ŀ�ؼǿ� ���� �߰����� ������ �����Ѵ�. ���⼭�� get����� ��û�� ����
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);

			// �������� �����Ѵ�.
			urlConnection.connect();

			// ������ �����ϱ� ���� ��θ� �����Ѵ�.
			// �� ���������� sdcard�� root ���丮�� ������ �����Ѵ�.
			//
			File SDCardRoot = Environment.getExternalStorageDirectory();
			// SDCard root ���丮�� somefile.txt ���Ͽ� ������ �����ϱ� ����
			// ���� ��ü�� �����Ѵ�.
			File file = new File(SDCardRoot, "recent.m4u");
			// ������ �����Ѵ�.
			FileOutputStream fileOutput = new FileOutputStream(file);

			// ���ͳ����� ���� �����͸� �о���̱� ���� �Է½�Ʈ���� ���´�.
			InputStream inputStream = urlConnection.getInputStream();

			// ������ ��ü ũ�⸦ ���´�.
			int totalSize = urlConnection.getContentLength();
			// �ٿ�ε� ���� ��ü ����Ʈ ũ�⸦ ������ �����Ѵ�.
			int downloadedSize = 0;

			// ���۸� �����Ѵ�.
			byte[] buffer = new byte[1024];
			int bufferLength = 0; // �ӽ÷� ����� ������ ũ�� ����

			// �Է¹��۷� ���� �����͸� �о ������ ���Ͽ� ����.
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				// ���ۿ� �о���� �����͸� ���Ͽ� ����.
				fileOutput.write(buffer, 0, bufferLength);
				// �ٿ�ε� ���� ����Ʈ���� ����Ѵ�.
				downloadedSize += bufferLength;
				// progressDialog�� �ٿ�ε� ���� ����Ʈ���� ǥ���� �ش�. <-���� progressDialog�����带
				// �ۼ��صѰ�
				// updateProgress(downloadedSize, totalSize);

			}
			// �۾��� ������ ������ close�Ͽ� �����Ѵ�.
			fileOutput.close();

			// ���ܰ� �߻��� ��� ���� catch ��ƾ�� ó���Ѵ�.
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
