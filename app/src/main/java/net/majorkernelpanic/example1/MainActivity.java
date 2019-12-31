package net.majorkernelpanic.example1;

import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.SessionBuilderBottom;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspServer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;

/**
 * A straightforward example of how to use the RTSP server included in libstreaming.
 */
public class MainActivity extends Activity {

	private final static String TAG = "MainActivity";

	private SurfaceView mTopSurfaceView;
	private SurfaceView mBottomSurfaceView;

	private String[] permissions = { Manifest.permission.INTERNET,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.RECORD_AUDIO,
			Manifest.permission.CAMERA,
			Manifest.permission.WAKE_LOCK
	};

	private void startRequestPermission() {
		ActivityCompat.requestPermissions(this, permissions, 321);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mTopSurfaceView = (SurfaceView) findViewById(R.id.topSurface);
		mBottomSurfaceView = (SurfaceView) findViewById(R.id.bottomSurface);


		for (int j = 0; j < permissions.length; j++) {
			// 检查该权限是否已经获取
			int i = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[j]);
			// 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
			if (i != PackageManager.PERMISSION_GRANTED) {
				Log.d(TAG, "onCreate:  request extern storge");
				startRequestPermission();
			}
		}


		// Sets the port of the RTSP server to 1234
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putString(RtspServer.KEY_PORT, String.valueOf(1234));
		editor.commit();

		// Configures the SessionBuilder
		SessionBuilder.getInstance()
		.setSurfaceView(mTopSurfaceView) //back gray
		.setPreviewOrientation(90)
		.setContext(getApplicationContext())
		.setAudioEncoder(SessionBuilder.AUDIO_NONE)
		.setVideoEncoder(SessionBuilder.VIDEO_H264);

		// Configures the SessionBuilder
		SessionBuilderBottom.getInstance()
				.setSurfaceView(mBottomSurfaceView) //front color
				.setPreviewOrientation(270)
				.setContext(getApplicationContext())
				.setAudioEncoder(SessionBuilder.AUDIO_NONE)
				.setVideoEncoder(SessionBuilder.VIDEO_H264);


		// Starts the RTSP server
		this.startService(new Intent(this,RtspServer.class));

	}

}
