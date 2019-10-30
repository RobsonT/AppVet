package com.example.apppetshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.model.Pet;

public class CadastroPet3 extends AppCompatActivity {

    ImageView imageView;
    Button btnCamera, confirmar;
    Pet pet;
    PetDAO petDAO;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet3);

        bitmap = null;

        pet = (Pet) getIntent().getSerializableExtra("pet");
        petDAO = PetDAO.getInstance();

        //teste camera
        btnCamera = findViewById(R.id.buttonCamera);
        imageView = findViewById(R.id.camera);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        confirmar = findViewById(R.id.cadastrarPet);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap != null) {
                    pet.setImage(bitmap);
                    petDAO.save(pet);
                    Intent retorno = new Intent();

                    setResult(Activity.RESULT_OK, retorno);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Insira a imagem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
        btnCamera.setText("Tirar outra foto");
        confirmar.setClickable(true);
    }
}
