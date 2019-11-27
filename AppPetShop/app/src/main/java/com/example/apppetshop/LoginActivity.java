package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.model.Cliente;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private SignInButton googleSignInButton;
    private GoogleSignInClient googleSignInClient;

    private EditText email;
    private EditText password;
    private ClienteDAO clienteDAO;

    private TextView recuperarSenha;
    private FirebaseAuth mAuth;

    TextView textView;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailUsuario);
        password = findViewById(R.id.senhaUsuario);
        clienteDAO = ClienteDAO.getInstance();
        recuperarSenha = findViewById(R.id.recuperarSenha);


        //Sublinhar texto
        TextView textView = findViewById(R.id.recuperarSenha);
        SpannableString content = new SpannableString(textView.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, textView.getText().toString().length(), 0);
        textView.setText(content);

        googleSignInButton = findViewById(R.id.loginGoogle);

        //Muda texto do botao google
        textView = (TextView) googleSignInButton.getChildAt(0);
        textView.setText("Login com Google");
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

    public boolean validatePassword() {
        String passwordText = password.getText().toString().trim();

        if (passwordText.isEmpty()) {
            password.setError("preencha o campo de senha");
            return false;
        }

        password.setError(null);
        return true;
    }

    public void enter(View view) {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString();
        if (validateEmail() && validatePassword()) {
            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "E-mail ou senha incorretos",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void newUser(View view) {
        Intent i = new Intent(this, Cadastro.class);
        startActivity(i);
    }

    public void recoverPasssword(View view) {
        Intent i = new Intent(this, RecuperacaoSenha.class);
        startActivity(i);
    }
}