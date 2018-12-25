package com.example.nitya.cameraml;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nitya.cameraml.Helper.GraphicOverlay;
import com.example.nitya.cameraml.Helper.TextGraphic;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.L;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Select extends AppCompatActivity {

    int flag;
    ImageView imageView;

    GraphicOverlay graphicOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        imageView = findViewById(R.id.image);
        graphicOverlay = findViewById(R.id.graphic);

        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);
        Log.i("flagggggg111111111", String.valueOf(flag));

        
        Intent cameraIntent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));


        startActivityForResult(cameraIntent, 100);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {

            Bitmap picture = (Bitmap) data.getExtras().get("data");
            //picture.setPixel(5312,2988,1);
            //picture = BitmapFactory.decodeResource(getResources(),R.drawable.);
            //Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            final int maxSize = 2048;
            int outWidth;
            int outHeight;
            int inWidth = picture.getWidth();
            int inHeight = picture.getHeight();
            if(inWidth > inHeight){
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            picture=Bitmap.createScaledBitmap(picture,outWidth,outHeight,false);//this is your bitmap image and now you can do whatever you want with this
            imageView.setImageBitmap(picture); //for example I put bmp in an ImageView

            if (flag==1){
                //text recognition
                TextRecognize.recognizeText(graphicOverlay,picture);
            }

            if (flag==2){
                //label images
            }

            if (flag==3){
                //recognize face

                FaceDetector obj=new FaceDetector();
                obj.detect(graphicOverlay,picture);
            }

            if(flag==4){
                //scan barcode
            }
        }
        else{
            //if image not found jump back to main home page with a toast
            Toast.makeText(getBaseContext(),"Error in Image Fetching",Toast.LENGTH_LONG).show();
            Intent select=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(select);

        }
    }
}
