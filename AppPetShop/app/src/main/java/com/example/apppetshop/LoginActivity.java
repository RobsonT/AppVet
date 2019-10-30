package com.example.apppetshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

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

    TextView textView;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailUsuario);
        password = findViewById(R.id.senhaUsuario);
        clienteDAO = ClienteDAO.getInstance();
//        Sublinhar texto
        TextView textView = findViewById(R.id.recuperarSenha);
        SpannableString content = new SpannableString(textView.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, textView.getText().toString().length(), 0);
        textView.setText(content);

        googleSignInButton = findViewById(R.id.loginGoogle);
//Muda texto do botao google
        textView = (TextView) googleSignInButton.getChildAt(0);
        textView.setText("Google");

        loginButton = findViewById(R.id.loginFacebook);
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            Cliente client = new Cliente();
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    client.setEmail(email);
                                    client.setNome(name);
                                    client.setId(clienteDAO.getAll().size());
                                    clienteDAO.save(client);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email");
                request.setParameters(parameters);
                request.executeAsync();


                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("clientId", String.valueOf(client.getId()));
                startActivity(i);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken()
                .requestEmail()
                .requestProfile()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

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
            Cliente client;
            client = clienteDAO.getByEmail(emailText);
            if (client != null && client.getSenha().equals(passwordText)) {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("clientId", String.valueOf(client.getId()));
                startActivity(i);
            } else if (client == null) {
                Toast.makeText(this, "Email não cadastrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
            }
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

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("clientId", String.valueOf(clienteDAO.getAll().size()));

        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w("Tag", "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }

    }

}
