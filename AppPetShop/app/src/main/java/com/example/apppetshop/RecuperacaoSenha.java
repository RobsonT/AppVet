package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

<<<<<<< HEAD
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
=======
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
>>>>>>> 8b3f7d5ee26920d6beb67de42eba5de78ec9c76d

import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.model.Cliente;

public class RecuperacaoSenha extends AppCompatActivity {

<<<<<<< HEAD
//    final int SEND_SMS_PERMISSION_REQUEST_ONE = 1;
//
//    EditText number;
//    EditText email;
//    String message = "deu certo";
//    Button send;
=======
    EditText emailE;
    Button recover;

    ClienteDAO clienteDAO;
>>>>>>> 8b3f7d5ee26920d6beb67de42eba5de78ec9c76d

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacao_senha);

<<<<<<< HEAD
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
=======
        clienteDAO = ClienteDAO.getInstance();

        emailE = findViewById(R.id.emailRecuperacao);
        recover = findViewById(R.id.recuperarSenha);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cliente cliente = clienteDAO.getByEmail(emailE.getText().toString());
                if(cliente != null){
//                Enviar senha
                }else{
                    Toast.makeText(getApplicationContext(), "Insira um email válido",Toast.LENGTH_SHORT).show();
                }
            }
        });
>>>>>>> 8b3f7d5ee26920d6beb67de42eba5de78ec9c76d
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
