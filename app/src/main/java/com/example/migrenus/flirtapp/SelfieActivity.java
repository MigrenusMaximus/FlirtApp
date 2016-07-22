package com.example.migrenus.flirtapp;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Migrenus on 7/18/2016.
 *
 * Here the user takes their photo
 * before proceeding to match selection
 */
public class SelfieActivity extends AppCompatActivity {
    // used for logging and debugging
    private static String classLogTag = "SelfieActivity";
    // holds a reference to our camera
    private Camera camera = null;
    // used for storing the current camera id
    // for some reason you can't find out the
    // id of the current camera programmatically
    private int cameraId = -1;

    private CameraPreview cameraPreview;
    // callback for taking pictures
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null){
                Log.d(classLogTag, "Error creating media file, check storage permissions.");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(classLogTag, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(classLogTag, "Error accessing file: " + e.getMessage());
            } catch (Exception e) {
                Log.d(classLogTag, "Unknown error while saving photo: " + e.getMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);

        final Button selfieButton = (Button) findViewById(R.id.selfieButton);
        final Button yesButton    = (Button) findViewById(R.id.yesButton);
        final Button noButton     = (Button) findViewById(R.id.noButton);
        final LinearLayout confirmationLinearLayout = (LinearLayout) findViewById(R.id.confirmationLinearLayout);
        final TextView confirmationText = (TextView) findViewById(R.id.confirmationTextView);

        resetButtons(false);

        assert selfieButton != null;
        selfieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    camera.takePicture(null, null, pictureCallback);
                } catch (Exception e) {
                    Log.d(classLogTag, "Error while taking picture: " + e.getMessage());
                }

                resetButtons(true);
            }
        });

        assert noButton != null;
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtons(false);

                // we need to restart the preview manually, since it stops
                // after taking a photo
                try {
                    camera.startPreview();
                } catch (Exception e) {
                    Log.d(classLogTag, "Error while starting preview from onPictureTaken: " + e.getMessage());
                }
            }
        });

        assert yesButton != null;
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtons(false);

                Intent intent = new Intent(SelfieActivity.this, ViewSelectionActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
            }
        });
    }

    private void resetButtons(boolean pictureTaken) {
        final Button selfieButton = (Button) findViewById(R.id.selfieButton);
        final LinearLayout confirmationLinearLayout = (LinearLayout) findViewById(R.id.confirmationLinearLayout);
        final TextView confirmationText = (TextView) findViewById(R.id.confirmationTextView);

        confirmationLinearLayout.bringToFront();
        confirmationText.bringToFront();
        selfieButton.bringToFront();

        if (pictureTaken) {
            selfieButton.setVisibility(View.INVISIBLE);
            selfieButton.setEnabled(false);

            confirmationLinearLayout.setVisibility(View.VISIBLE);
            confirmationLinearLayout.setEnabled(true);

            confirmationText.setVisibility(View.VISIBLE);
            confirmationText.setEnabled(true);
        } else {
            selfieButton.setVisibility(View.VISIBLE);
            selfieButton.setEnabled(true);

            confirmationLinearLayout.setVisibility(View.INVISIBLE);
            confirmationLinearLayout.setEnabled(false);

            confirmationText.setVisibility(View.INVISIBLE);
            confirmationText.setEnabled(false);
        }
    }

    private void resetCameraPreview() {
        cameraPreview = new CameraPreview(this, camera);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mainLayout.addView(cameraPreview);

        resetButtons(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
            camera = null;
            cameraId = -1;
        }
        //cameraPreview.
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
            cameraId = -1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        camera = getCameraInstance();
        if (camera == null)
            Log.d(classLogTag, "Unable to open camera in onStart.");

        resetCameraPreview();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        camera = getCameraInstance();
        if (camera == null)
            Log.d(classLogTag, "Unable to open camera in onStart.");

        resetCameraPreview();
    }

    // returns an instance of the Camera object
    // favors the front-facing camera
    private Camera getCameraInstance() {
        Camera camera;

        // we first try to open the front-facing camera and
        // if that doesn't work, we try opening the rear camera
        for (int i = 1; i >= 0; i--) {
            try {
                camera = Camera.open(i);
                cameraId = i;
                CameraPreview.setCameraDisplayOrientation(this, cameraId, camera);
                return camera;
            } catch (Exception e) {
                Log.d(classLogTag, "Unable to open camera " + i + "\n" + e.getMessage());
            }
        }

        // if neither work, we return null
        // this shouldn't happen if the correct
        // permissions and features are set up
        // in the manifest
        return null;
    }

    // create a file uri for saving an image or video
    private Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    // create a file for saving an image or video
    private File getOutputMediaFile(){
        // to be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "FlirtApp");
        // this location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(classLogTag, "Failed to create directory.");
                return null;
            }
        }

        // create a media file name
        // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "selfie.jpg");

        return mediaFile;
    }
}
