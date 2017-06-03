package com.example.leidyzulu.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;


    public class MainActivity extends AppCompatActivity {

        ImageView imgViewCamera;
        ImageView imgViewGallery;
        static final int REQUEST_IMAGE_CAPTURE = 1;
        private final int GALLERY_INTENT_CALLED = 1;
        private final int GALLERY_KITKAT_INTENT_CALLED = 2;
        Uri mChosenImageUri;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            imgViewCamera = (ImageView) findViewById(R.id.imgViewCamera);
            imgViewGallery = (ImageView) findViewById(R.id.imgViewGallery);

            // Permisos para acceder a los diferentes rutas de almacenamiento.
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_DOCUMENTS};
            ActivityCompat.requestPermissions(this, permissions, 2);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            //Cuando abrió la camara
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imgViewCamera.setImageBitmap(imageBitmap);
            }

            //En caso de que no se seleccionen imagenes.
            if (resultCode != RESULT_OK || data == null) return;

            //Constante para las versiones mayores o iguales a Kitkat, para abrir la galería.
            if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
                mChosenImageUri = data.getData();
                    //Cuando se escoge una imagen
                    loadImage(mChosenImageUri);
            }
        }

        // Cargar una imagen a partir del URI con Glide
        private void loadImage(final Uri selectedImage) {

            Glide.with(this).load(selectedImage).into(new GlideDrawableImageViewTarget(imgViewGallery) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    imgViewGallery.setImageDrawable(resource);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    Toast.makeText(MainActivity.this, "Un error al cargar la imagen.", Toast.LENGTH_SHORT).show();
                }
            });
        }


        public void openCamera(View view){
            dispatchTakePictureIntent();


        }
        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

        // Abrir la galería.
        public void openGallery(View view) {

            //Abrir la galería si la versión es menor a KitKat
            if (Build.VERSION.SDK_INT < 19) {
                Intent intent = new Intent();
                intent.setType("video/*,image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_INTENT_CALLED);
            } else {
                Intent intent = new Intent();
                intent.setType("video/*,image/*");
                String[] mimetypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
            }
        }



    }
