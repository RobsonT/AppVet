package com.example.apppetshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.model.Pet;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CadastroPet3 extends AppCompatActivity {

    ImageView imageView;
    Button btnCamera, confirmar,button;
    Pet pet;
    PetDAO petDAO;
    Bitmap bitmap;
    Uri uri;
    String pathToFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet3);

        bitmap = null;
        uri = null;
        pet = (Pet) getIntent().getSerializableExtra("pet");
        petDAO = PetDAO.getInstance();

        //teste camera
        btnCamera = findViewById(R.id.buttonCamera);
        imageView = findViewById(R.id.camera);

        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchPictureTakerAction();
            }
        });


        button = findViewById(R.id.voltarPet3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent retorno = new Intent();

                setResult(Activity.RESULT_CANCELED, retorno);
                finish();
            }
        });


        confirmar = findViewById(R.id.cadastrarPet);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap != null) {
                    final String id = FirebaseFirestore.getInstance().collection("pets").document().getId();
                    pet.setId(id);
                    petDAO.save(pet, uri);
                    Intent retorno = new Intent();

                    setResult(Activity.RESULT_OK, retorno);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Insira a imagem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dispatchPictureTakerAction(){
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getPackageManager()) != null){
            File photoFile = createPhotoFile();
            if(photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                uri = FileProvider.getUriForFile(getApplicationContext(), "com.example.apppetshop.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(takePic, 1);
            }
        }
    }

    private File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("CadastroPet3", e.getMessage());
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            bitmap = BitmapFactory.decodeFile(pathToFile);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            btnCamera.setText("Tirar outra foto");
            confirmar.setClickable(true);
        }
    }
}
