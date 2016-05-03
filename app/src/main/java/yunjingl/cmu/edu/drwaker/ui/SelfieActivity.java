package yunjingl.cmu.edu.drwaker.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.media.FaceDetector.Face;
import android.widget.ImageView;

import java.io.IOException;

import yunjingl.cmu.edu.drwaker.R;

/**
 * Selfie Activity, user needs to complete a selfie before turning off the alarm
 */
public class SelfieActivity extends AppCompatActivity {
    private Camera mCamera = openFrontFacingCamera();
    private CameraPreview mPreview;
    public Bitmap bmp_image, canvasBitmap;
    Matrix matrix=new Matrix();
    private FaceDetector.Face[] face;
    private int face_count;
    Canvas canvas=new Canvas();
    float myEyesDistance;
    ImageView drawMask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_selfie);

        mPreview=new CameraPreview(this, mCamera);
        Camera.Parameters param = mCamera.getParameters();
        switch((int) mPreview.getRotation()){
            case Surface.ROTATION_0:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO){
                    mCamera.setDisplayOrientation(90);
                    Log.d("Rotation_0", "whatever");
                }
                else{
                    Log.d("Rotation_0", "whatever");
                    param.setRotation(90);
                    mCamera.setParameters(param);
                }
                break;
            case Surface.ROTATION_90:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO){
                    mCamera.setDisplayOrientation(0);
                    Log.d("Rotation_0", "whatever");
                }
                else{
                    Log.d("Rotation_90", "whatever");
                    param.setRotation(0);
                    mCamera.setParameters(param);
                }
                break;
        }

        final FrameLayout preview=(FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        final Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setText("Test");

        drawMask = new ImageView(getApplicationContext());
        matrix.postRotate(270);

        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(SelfieActivity.this, MainActivity.class);
//                        startActivity(intent);
                        final Intent myIntent = new Intent(SelfieActivity.this, RingtonePlayingService.class);
                        if(captureButton.getText()=="Test") { //in Preview Mode

                            // get an image from the camera
                            mCamera.takePicture(null, null, mpicture);
                            captureButton.setText("Unlock");
                        }
                        else if(captureButton.getText()=="Unlock") { //in Mask Mode
                            if(face_count>0) {
                                preview.removeView(drawMask);

                                //if detect the user's eyes are open then return to main activity.
//                                Intent intent = new Intent(SelfieActivity.this, MainActivity.class);
//                                startActivity(intent);
                                myIntent.putExtra("extra", "off");
                                startService(myIntent);
                                // sendBroadcast(myIntent);
                                finish();
                            }
                            else {
                                AlertDialog.Builder builder=new AlertDialog.Builder(SelfieActivity.this);
                                builder.setTitle("Try again!");
                                builder.setMessage("No face detected");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mCamera.startPreview();
                                        captureButton.setText("Test");
                                    }
                                }).create();
                                builder.show();
//                                mCamera.startPreview();
//                                captureButton.setText("Take Picture");
                            }

                        }
                    }
                }
        );
    }

    Camera.PictureCallback mpicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try{
                BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
                bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmp_image=BitmapFactory.decodeByteArray(data, 0, data.length, bitmap_options);

                bmp_image=Bitmap.createBitmap(bmp_image, 0, 0, bmp_image.getWidth(), bmp_image.getHeight(), matrix, true);
                face_detection();
                canvasBitmap = Bitmap.createBitmap(bmp_image.getWidth(), bmp_image.getHeight(),Bitmap.Config.ARGB_8888);
                canvas=new Canvas(canvasBitmap);
                draw(canvas);

            }
            catch(Exception e){}
        }
    };

    public void face_detection(){
        FaceDetector detector = new FaceDetector (bmp_image.getWidth(), bmp_image.getHeight(), 1);
        face=new FaceDetector.Face[1];
        face_count= detector.findFaces(bmp_image,face);

//        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
//                .setTrackingEnabled(false)
//                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
//                .build();
//        SparseArray<Face> faces = faceDetector.detect(frame);
//        float smilingProbability;
//        float leftEyeOpenProbability;
//        float rightEyeOpenProbability;
//        float eulerY;
//        float eulerZ;
//        for( int i = 0; i < mFaces.size(); i++ ) {
//            Face face = mFaces.valueAt(i);
//
//            smilingProbability = face.getIsSmilingProbability();
//            leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
//            rightEyeOpenProbability = face.getIsRightEyeOpenProbability();
//            eulerY = face.getEulerY();
//            eulerZ = face.getEulerZ();
//
//            Log.e( "Tuts+ Face Detection", "Smiling: " + smilingProbability );
//            Log.e( "Tuts+ Face Detection", "Left eye open: " + leftEyeOpenProbability );
//            Log.e( "Tuts+ Face Detection", "Right eye open: " + rightEyeOpenProbability );
//            Log.e( "Tuts+ Face Detection", "Euler Y: " + eulerY );
//            Log.e( "Tuts+ Face Detection", "Euler Z: " + eulerZ );
    }

    private void draw(Canvas canvas){
        canvas.drawBitmap(bmp_image, 0, 0, null);

        Paint myPaint = new Paint();
        myPaint.setColor(Color.GREEN);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(3);


            FaceDetector.Face faces = face[0];
            PointF myMidPoint = new PointF();
            faces.getMidPoint(myMidPoint);
            myEyesDistance = faces.eyesDistance();
            canvas.drawRect(
                    (int)(myMidPoint.x - myEyesDistance*2),
                    (int)(myMidPoint.y - myEyesDistance*2),
                    (int)(myMidPoint.x + myEyesDistance*2),
                    (int)(myMidPoint.y + myEyesDistance*2),
                    myPaint);

    }
    //method to open the front camera
    private Camera openFrontFacingCamera(){
        int cameraCount = 0;
        Camera camera = null;
        Camera.CameraInfo  cameraInfo = new  Camera.CameraInfo();
        cameraCount =Camera.getNumberOfCameras();
        for(int camIndex=0; camIndex<cameraCount; camIndex++){
            Camera.getCameraInfo(camIndex,cameraInfo);
            if(cameraInfo.facing== Camera.CameraInfo.CAMERA_FACING_FRONT){
                try {
                    camera= Camera.open(camIndex);
                }catch(RuntimeException e){
//                    Log.e("Camera fail to open"+e.getLocalizedMessage());
                }
            }
        }
        return camera;
    }

    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public Camera getCameraInstance(){
        Camera c=null;
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
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera){
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
    class MyFaceDetectionListener implements Camera.FaceDetectionListener {
        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
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
        Camera.Parameters params = mCamera.getParameters();

        // start face detection only *after* preview has started
        if (params.getMaxNumDetectedFaces() > 0){
            // camera supports face detection, so can start it:
            mCamera.startFaceDetection();
        }
    }
}
