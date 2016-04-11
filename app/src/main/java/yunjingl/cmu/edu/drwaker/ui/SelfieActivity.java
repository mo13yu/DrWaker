package yunjingl.cmu.edu.drwaker.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;

import yunjingl.cmu.edu.drwaker.R;

public class SelfieActivity extends AppCompatActivity {
    private android.hardware.Camera mCamera = openFrontFacingCamera();
    private CameraPreview mPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_selfie);

        mPreview=new CameraPreview(this, mCamera);
        FrameLayout preview=(FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SelfieActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    //method to open the front camera
    private android.hardware.Camera openFrontFacingCamera(){
        int cameraCount = 0;
        android.hardware.Camera camera = null;
        android.hardware.Camera.CameraInfo  cameraInfo = new  android.hardware.Camera.CameraInfo();
        cameraCount = android.hardware.Camera.getNumberOfCameras();
        for(int camIndex=0; camIndex<cameraCount; camIndex++){
            android.hardware.Camera.getCameraInfo(camIndex,cameraInfo);
            if(cameraInfo.facing== android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT){
                try {
                    camera= android.hardware.Camera.open(camIndex);
                }catch(RuntimeException e){
//                    Log.e("Camera fail to open"+e.getLocalizedMessage());
                }
            }
        }
        return camera;
    }

    public android.hardware.Camera getCameraInstance(){
        android.hardware.Camera c=null;
        try{
            c=openFrontFacingCamera();
        }
        catch (Exception e){

        }
        return c;
    }

    //inner class to generate a camera preview
    private class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

        private SurfaceHolder mHolder;
        private android.hardware.Camera mCamera;

        public CameraPreview(Context context, android.hardware.Camera camera){
            super(context);
            mCamera=camera;
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        }
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
                startFaceDetection();
            } catch (IOException e) {
//                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
                startFaceDetection();

            } catch (Exception e){
//                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    //set a listener for face detection events
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    class MyFaceDetectionListener implements android.hardware.Camera.FaceDetectionListener {
        @Override
        public void onFaceDetection(android.hardware.Camera.Face[] faces, android.hardware.Camera camera) {
            if (faces.length > 0){
                Log.d("FaceDetection", "face detected: "+ faces.length +
                        " Face 1 Location X: " + faces[0].rect.centerX() +
                        "Y: " + faces[0].rect.centerY() );
            }
        }
    }

    //API 14 start face detection
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void startFaceDetection(){
        // Try starting Face Detection
        android.hardware.Camera.Parameters params = mCamera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0){
            // camera supports face detection, so can start it:
            mCamera.startFaceDetection();
        }
    }
}
