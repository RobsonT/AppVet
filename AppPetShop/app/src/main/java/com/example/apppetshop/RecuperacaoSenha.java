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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.model.Cliente;

public class RecuperacaoSenha extends AppCompatActivity {

//    final int SEND_SMS_PERMISSION_REQUEST_ONE = 1;
//    EditText number;
//    EditText email;
//    String message = "deu certo";
//    Button send;
//    EditText emailE;
//    Button recover;
//
//    ClienteDAO clienteDAO;

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
//        clienteDAO = ClienteDAO.getInstance();
//
//        emailE = findViewById(R.id.emailRecuperacao);
//        recover = findViewById(R.id.recuperarSenha);
//
//        recover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Cliente cliente = clienteDAO.getByEmail(emailE.getText().toString());
//                if(cliente != null){
////                Enviar senha
//                }else{
//                    Toast.makeText(getApplicationContext(), "Insira um email válido",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }


//    public boolean checkPermission(String permission){
//        int check = ContextCompat.checkSelfPermission(this, permission);
//        return (check == PackageManager.PERMISSION_GRANTED);
//    }
    }

    public void enviarSMS(View v){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("08591869244", null, "teste envio", null, null);
    }
}
