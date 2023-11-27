package com.example.ss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ss.databinding.ActivityPrimeirosPassosBinding;
import com.example.ss.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PrimeirosPassos_Activity extends AppCompatActivity {

    private ActivityPrimeirosPassosBinding binding;
    private FirebaseAuth firebaseAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrimeirosPassosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.btExtrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrimeirosPassos_Activity.this, Rendimento_Activity.class);
                startActivity(intent);
            }
        });

        binding.btObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrimeirosPassos_Activity.this, Gastos_Activity.class);
                startActivity(intent);
            }
        });

        binding.btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PrimeirosPassos_Activity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}