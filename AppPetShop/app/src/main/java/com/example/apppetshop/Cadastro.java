package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
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

        name = findViewById(R.id.nomeUsuario);
        email = findViewById(R.id.emailUsuario);
        cpf = findViewById(R.id.cpfUsuario);
        password = findViewById(R.id.senhaUsuario);
        confPassword = findViewById(R.id.senhaConfUsuario);
    }

    public boolean validateName() {
        String nameText = name.getText().toString();

        if (nameText.isEmpty()) {
            name.setError("Preencha o campo nome");
            return false;
        }

        name.setError(null);
        return true;
    }

    public boolean validatePassword() {
        String passwordText = password.getText().toString();
        String confpasswordText = confPassword.getText().toString();

        if (passwordText.isEmpty()) {
            password.setError("Preencha o campo senha");
            return false;
        }
        if (!passwordText.equals(confpasswordText)) {
            confPassword.setError("Senhas não conferem");
            return false;
        }

        password.setError(null);
        confPassword.setError(null);
        return true;
    }

    public boolean validateCPF() {
        cpf.setError(null);
        return true;
    }

    public boolean validateEmail() {
        String emailText = email.getText().toString().trim();

        if (emailText.isEmpty()) {
            email.setError("preencha o campo de email");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Insira um endereço de email válido");
            return false;
        }

        email.setError(null);
        return true;
    }

    public void register(View view) {

        Cliente cliente = new Cliente();
        cliente.setName(name.getText().toString());
        cliente.setCpf(cpf.getText().toString());
        cliente.setPassword(password.getText().toString());
        cliente.setEmail(email.getText().toString());
        if (validateName() && validateEmail() && validateCPF() && validatePassword()) {
            if (clienteDAO.getByEmail(cliente.getEmail()) == null) {
                clienteDAO.save(cliente);
                finish();
            } else {
                Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
