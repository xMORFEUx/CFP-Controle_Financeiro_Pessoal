package com.example.ss;

import static com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ss.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    private TextView text_tela_cadastro;
    private EditText edit_email, edit_senha;
    private Button bt_google;
    private Button bt_acessar;
    private ProgressBar progressBar;
    private TextView text_tela_principal;
    private BeginSignInRequest signInRequest;
    private SignInClient oneTapClient;
    String[] mensagens = {"Preencha todos os Campos!", "Login Efetuado com Sucesso!", "As senhas digitadas não são iguais!"};
    private ActivityLoginBinding binding;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String acesso = getColoredSpanned("Primeiro Acesso? ", "#000000");
        String cadastro = getColoredSpanned("Cadastre-se", "#0000ff");

        binding.textTelaCadastro.setText(Html.fromHtml(acesso + cadastro));

        String senha = getColoredSpanned("Esqueceu a senha? ", "#000000");
        String alterar = getColoredSpanned("Alterar", "#0000ff");

//        binding.textEsqSenha.setText(Html.fromHtml(senha + alterar));

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        binding.googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onCLick: begin Google SignIn");
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        firebaseAuth = FirebaseAuth.getInstance();



        IniciarComponentes();

        binding.textTelaCadastro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login_Activity.this, Cadastro_Activity.class);
                startActivity(intent);
            }
        });
        binding.btAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()){

                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }else{
                    AutenticarUsuario(v);

                    }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google Signin intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{

                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);

            }catch (Exception e){
                Log.d(TAG,"onActivityResult:" +e.getMessage());
            }
        }

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        // Get an ID token from Google. Use to Auth
                        // With Firebase.
                        Log.d(TAG, "Got ID token.");
                    }
                } catch (ApiException e) {
                    // ...
                }
                break;
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {

        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Login Success
                        Log.d(TAG, "onSuccess: Logged In");

                        //get logged in user
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();
                        String name = firebaseUser.getDisplayName();

                        //get user info
                        Log.d(TAG, "onSuccess: Email "+email);
                        Log.d(TAG, "onSuccess: UID "+uid);
                        Log.d(TAG, "onSuccess: Nem "+name);

                        Map<String, Object> usuarios = new HashMap<>();
                        usuarios.put("UID",uid);
                        usuarios.put("nome",name);
                        usuarios.put("email",email);

                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference();

                        databaseReference.child("Usuarios").child("UID").setValue(usuarios);

                        //check if is a new or existing user
                        if (authResult.getAdditionalUserInfo().isNewUser()){


                            //new user - Account Created
                            Log.d(TAG, "onSuccess: Account Created...\n"+email);
                            Toast.makeText(Login_Activity.this, "Conta Criada!\n"+email, Toast.LENGTH_SHORT).show();

                        }else{
                            //user already exist - Logged In
                            Log.d(TAG, "Logado com Sucesso!\n"+email);
                            Toast.makeText(Login_Activity.this, "Logado com Sucesso!\n"+email, Toast.LENGTH_SHORT).show();
                        }

                        //start profile activity
                        startActivity(new Intent(Login_Activity.this,PrimeirosPassos_Activity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Falha ao Logar
                        Log.d(TAG, "onFailure: Loggin failed "+e.getMessage());
                    }
                });
    }
    private void AutenticarUsuario(View view) {

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    binding.progressbar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();

                        }
                    },2000);
                }else{
                    String erro;
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Login ou Senha incorretos!";
                    }catch(Exception e){
                        erro = "Erro ao logar";
                    }

                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }

            }
        });

    }

    protected void onStart(){
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioAtual != null){
            TelaPrincipal();
        }
    }

    private void TelaPrincipal(){

        Intent intent = new Intent(Login_Activity.this, PrimeirosPassos_Activity.class);
        startActivity(intent);
        finish();
    }

    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        text_tela_principal = findViewById(R.id.bt_acessar);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_acessar = findViewById(R.id.bt_acessar);
        progressBar = findViewById(R.id.progressbar);
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + "><u>" + text + "</u></font>";
        return input;
    }
}