package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.model.Cliente;

public class RecuperacaoSenha extends AppCompatActivity {

//    final int SEND_SMS_PERMISSION_REQUEST_ONE = 1;
//
//    EditText number;
//    EditText email;
//    String message = "deu certo";
//    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacao_senha);

//        email = findViewById(R.id.emailRecuperarSenha);
//        number = findViewById(R.id.numeroSMS);
//        send = findViewById(R.id.button3);
//
//        send.setEnabled(false);
//        if(checkPermission(Manifest.permission.SEND_SMS)){
//            send.setEnabled(true);
//        }else {
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_ONE);
//        }
    }

    public void enviarSMS(View v){
//        String numero = number.getText().toString();
//        String smsMessagem = message;
//
//        if(numero == null || numero.length() == 0 || smsMessagem == null || smsMessagem.length() == 0){
//            return;
//        }

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("85991869244", null,"teste",null,null);
    }

//    public boolean checkPermission(String permission){
//        int check = ContextCompat.checkSelfPermission(this, permission);
//        return (check == PackageManager.PERMISSION_GRANTED);
//    }
}
