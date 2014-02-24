package eu.epitech.android.app6r;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;
	private Camera camera = null;
	private boolean inPreview = false;
	private boolean cameraConfigured = false;
	private boolean focused = false;
	private View overlay = null;
	private Integer orient = 0;
	private TextView infos = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		preview = (SurfaceView) findViewById(R.id.surfaceView1);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		overlay = getLayoutInflater().inflate(R.layout.overlay, null);
		infos = (TextView)overlay.findViewById(R.id.textView1);
		infos.setText("Hello");
		addContentView(overlay, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		
		Button button = (Button)overlay.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				camera.takePicture(null, null, new PictureCallback() {

					@Override
					public void onPictureTaken(byte[] arg0, Camera arg1) {
						infos.setText("Picture: size(" + arg0.length + ")");
						arg1.startPreview();
						try {
							File file = new File(createImageFileName());
							Log.d("Save", file.getAbsolutePath());
							FileOutputStream outputStream = new FileOutputStream(file);
							outputStream.write(arg0);
							outputStream.close();
							updateGallery(file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}}
				);
			}
		});
		OrientationEventListener orientationListener = new OrientationEventListener(this) {
			
			@Override
			public void onOrientationChanged(int orientation) {
				if (orientation == ORIENTATION_UNKNOWN) return;
			     android.hardware.Camera.CameraInfo info =
			            new android.hardware.Camera.CameraInfo();
			     android.hardware.Camera.getCameraInfo(0, info);
			     orientation = (orientation + 45) / 90 * 90;
			     int rotation = 0;
			     if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			         rotation = (info.orientation - orientation + 360) % 360;
			     } else {  // back-facing camera
			         rotation = (info.orientation + orientation) % 360;
			     }
			     orient = rotation;
			     if (camera != null) {
			    	 Camera.Parameters parameters = camera.getParameters();
			    	 Log.d("onOrientationChanged", "Rotation: " + orient);
			    	 parameters.setRotation(rotation);
			     }
			     
			}
		};
		orientationListener.enable();
	}
	
	private void updateGallery(File f) {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (camera != null) {
			camera.release();
			camera = null;
		}
		camera = Camera.open(0);
		startPreview();
	}

	@Override
	protected void onPause() {
		if (inPreview) {
			camera.stopPreview();
		}
		camera.release();
		camera = null;
		inPreview = false;

		super.onPause();
	}
	
	private String createImageFileName() {
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    return storageDir.getAbsolutePath() + "/" + imageFileName + ".jpg";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;
		if (orient == 90) {
			int tmp = width;
			width = height;
			height = width;
		}
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					if (size.width * size.height > resultArea) {
						result = size;
					}
				}
			}
		}
		return (result);
	}
	
	 

	private void initPreview(int width, int height) {
		if (camera != null && previewHolder.getSurface() != null) {
			try {
				camera.setPreviewDisplay(previewHolder);
			} catch (Throwable t) {
				// TODO Auto-generated catch block
				t.printStackTrace();
			}

			if (!cameraConfigured) {
				Camera.Parameters parameters = camera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, parameters);

				List<String> focusModes = parameters.getSupportedFocusModes();
				if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
				    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
				}
				if (size != null) {
					parameters.setPreviewSize(size.width, size.height);
					camera.setParameters(parameters);
					cameraConfigured = true;
				}
				camera.setDisplayOrientation(orient);
			}
		}
	}

	private void startPreview() {
		if (cameraConfigured && camera != null) {
			camera.startPreview();
			inPreview = true;
		}
	}

	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			initPreview(width, height);
			startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
		}
	};

}
