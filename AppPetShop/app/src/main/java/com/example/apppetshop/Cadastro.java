package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.model.Cliente;

public class Cadastro extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText cpf;
    private EditText password;
    private EditText confPassword;

    ClienteDAO clienteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        clienteDAO = ClienteDAO.getInstance();
    }

    public void register(View view){
        name = findViewById(R.id.nomeUsuario);
        email = findViewById(R.id.emailUsuario);
        cpf = findViewById(R.id.cpfUsuario);
        password = findViewById(R.id.senhaUsuario);
        confPassword = findViewById(R.id.senhaConfUsuario);

        Cliente cliente = new Cliente();
        cliente.setName(name.getText().toString());
        cliente.setCpf(cpf.getText().toString());
        cliente.setPassword(password.getText().toString());
        cliente.setEmail(email.getText().toString());

        if(!clienteDAO.getByEmail(cliente.getEmail()).equals(null)){
            if(password.equals(confPassword)){
                clienteDAO.save(cliente);
            }else{
                Toast.makeText(this, "Senhas não conrrespondem", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
        }

    }
}
