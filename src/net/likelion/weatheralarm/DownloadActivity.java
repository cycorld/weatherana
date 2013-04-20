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
		// 파일 다운로드...
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
			// 다운로드 받을 URL에 대한 객체를 생성한다.
			//
			URL url = new URL("http://www.choiyongchol.com/weatheralarm/data/recent.m4a");

			// URL로 부터 connection 객체를 를 생성한다.
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			// 생성된 커넥션에 대해 추가적인 설정을 지정한다. 여기서는 get방식의 요청을 설정
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);

			// 웹서버에 연결한다.
			urlConnection.connect();

			// 파일을 저장하기 위한 경로를 지정한다.
			// 이 예제에서는 sdcard의 root 디렉토리에 파일을 저장한다.
			//
			File SDCardRoot = Environment.getExternalStorageDirectory();
			// SDCard root 디렉토리에 somefile.txt 파일에 파일을 저장하기 위해
			// 파일 객체를 생성한다.
			File file = new File(SDCardRoot, "recent.m4u");
			// 파일을 오픈한다.
			FileOutputStream fileOutput = new FileOutputStream(file);

			// 인터넷으로 부터 데이터를 읽어들이기 위한 입력스트림을 얻어온다.
			InputStream inputStream = urlConnection.getInputStream();

			// 파일의 전체 크기를 얻어온다.
			int totalSize = urlConnection.getContentLength();
			// 다운로드 받을 전체 바이트 크기를 변수에 저장한다.
			int downloadedSize = 0;

			// 버퍼를 생성한다.
			byte[] buffer = new byte[1024];
			int bufferLength = 0; // 임시로 사용할 버퍼의 크기 지정

			// 입력버퍼로 부터 데이터를 읽어서 내용을 파일에 쓴다.
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				// 버퍼에 읽어들인 데이터를 파일에 쓴다.
				fileOutput.write(buffer, 0, bufferLength);
				// 다운로드 받은 바이트수를 계산한다.
				downloadedSize += bufferLength;
				// progressDialog에 다운로드 받은 바이트수를 표시해 준다. <-따로 progressDialog스레드를
				// 작성해둘것
				// updateProgress(downloadedSize, totalSize);

			}
			// 작업이 끝나면 파일을 close하여 저장한다.
			fileOutput.close();

			// 예외가 발생한 경우 다음 catch 루틴을 처리한다.
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
