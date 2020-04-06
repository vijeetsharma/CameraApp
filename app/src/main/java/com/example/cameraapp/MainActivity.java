package com.example.cameraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static Button mCaptureButton;
    private static ImageView mImageView;
    private static TextView errorMSG;
    private static final int IMAGE_CAPTURE = 102;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static Uri fileUri;
    private static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        mCaptureButton = (Button) findViewById(R.id.captureImageButton);
        mImageView = (ImageView) findViewById(R.id.imageView);
        errorMSG = (TextView) findViewById(R.id.textView);

        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            mCaptureButton.setEnabled(true);

        }
        else{

            mCaptureButton.setEnabled(false);
        }

        errorMSG.setText("state: "+ Environment.getExternalStorageState());

     }

     private static File getOutputMediaFile(int type){

     File myDir = new File(Environment.getExternalStoragePublicDirectory(
       Environment.DIRECTORY_PICTURES),"MyCameraApp"
     );

     if(!myDir.exists()){
        if(!myDir.mkdirs())
        {
            Log.d("MyCameraApp", "failed to create directory");
            return null;
        }
     }


       String myTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File myMediaFile;
        if(type == MEDIA_TYPE_IMAGE)
        {
            myMediaFile = new File(myDir.getPath()+ File.separator + "IMG_" + myTimeStamp + ".jpg");
        }

        else{
         return null;
        }
        return myMediaFile;
    }

    private static Uri getOutMediaFileUri(int type)
    {
        Uri URI = FileProvider.getUriForFile(context,context.getApplicationContext().getPackageName()
                + ".provider",getOutputMediaFile(type));

        return URI;

    }

    public void onCaptureImage(View view)
    {
        Intent myIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutMediaFileUri(MEDIA_TYPE_IMAGE);
        errorMSG.setText(errorMSG.getText() + "\nfileURI: " + fileUri.getPath());
        myIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        startActivityForResult(myIntent,IMAGE_CAPTURE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == IMAGE_CAPTURE)
        {
            if(resultCode == RESULT_OK)
            {
                mImageView.setImageURI(fileUri);
                Toast.makeText(this, "Image returned.", Toast.LENGTH_LONG).show();
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "Capture Canceled", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_LONG).show();

        }



    }




    }




