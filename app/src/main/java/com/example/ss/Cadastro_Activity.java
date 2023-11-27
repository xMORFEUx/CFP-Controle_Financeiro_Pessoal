package com.example.ss;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ss.databinding.ActivityCadastroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Cadastro_Activity extends AppCompatActivity {

    private TextView text_tela_login;

    private EditText edit_name, edit_email_new, edit_senha_new, edit_senha_new_2;
    private Button bt_cadastrar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private ActivityCadastroBinding binding;

    String[] mensagens = {"Preencha todos os Campos!", "Cadastro Realizado com Sucesso!", "As senhas digitadas não são iguais!"};
    String usuarioID;

    private DatabaseReference usuariosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String senha = getColoredSpanned("Já possui uma conta? ", "#000000");
        String alterar = getColoredSpanned("Logar", "#0000ff");

        binding.textTelaLogin.setText(Html.fromHtml(senha + alterar));

        getSupportActionBar().hide();
        IniciarComponentes();

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = edit_name.getText().toString();
                String email = edit_email_new.getText().toString();
                String senha = edit_senha_new.getText().toString();
                String senha2 = edit_senha_new_2.getText().toString();

                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senha2.isEmpty()) {

                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {

                    if (senha.equals(senha2)){

                        CadastrarUsuario(v);

                    }else{

                        Snackbar snackbar = Snackbar.make(v, mensagens[2], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();

                    }
                }
            }
        });

        binding.textTelaLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Cadastro_Activity.this, Login_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void CadastrarUsuario(View v){

        String email = edit_email_new.getText().toString();
        String senha = edit_senha_new.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    SalvarDadosUsuario();

                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                    Intent intent = new Intent(Cadastro_Activity.this, PrimeirosPassos_Activity.class);
                    startActivity(intent);
                    finish();

                }else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no mínimo 6 caracteres";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "E-mail já cadastrado";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "E-mail Inválido";
                    } catch (Exception e) {
                        erro = "Erro ao cadastrar usuário";
                    }

                    Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private void SalvarDadosUsuario(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usuariosRef = database.getReference("Usuarios");
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long users = dataSnapshot.getChildrenCount();

                usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String nome = edit_name.getText().toString();
                String email = edit_email_new.getText().toString();

                FirebaseDatabase db = FirebaseDatabase.getInstance();

                Map<String, Object> usuarios = new HashMap<>();
                usuarios.put("UID",usuarioID);
                usuarios.put("nome",nome);
                usuarios.put("email",email);

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();


                databaseReference.child("Usuarios").child(usuarioID).setValue(usuarios);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void IniciarComponentes(){

        text_tela_login = binding.textTelaLogin;
        edit_name = binding.editName;
        edit_email_new = binding.editEmailNew;
        edit_senha_new = binding.editSenhaNew;
        edit_senha_new_2 = binding.editSenhaNew2;
        bt_cadastrar = binding.btCadastrar;

    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + "><u>" + text + "</u></font>";
        return input;
    }
}
