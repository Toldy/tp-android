package eu.epitech.android.app3;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {	
	ToggleButton button = null;
	Camera cam = null;
	
	void startFlash() {
		if (cam != null)
			stopFlash();
		cam = Camera.open();     
		Parameters p = cam.getParameters();
		p.setFlashMode(Parameters.FLASH_MODE_TORCH);
		cam.setParameters(p);
		cam.startPreview();
	}
	
	void stopFlash() {
		if (cam != null) {
			cam.stopPreview();
			cam.release();	
			cam = null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tw = (TextView)this.findViewById(R.id.message);
		if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) == false) {
			tw.setText("Flash feature isn't available.");
		} else
			tw.setText("Flash feature is available.");
		
		button = (ToggleButton)findViewById(R.id.toggleButton1);
		button.setChecked(true);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (button.isChecked()) {
					startFlash();
				} else {
					stopFlash();
				}
			}
		});
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		startFlash();
	}
	
	@Override
	protected void onStop() {
		stopFlash();
		super.onStop();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
