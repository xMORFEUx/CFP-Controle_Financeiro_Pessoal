package com.example.ss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.ss.databinding.ActivityGastosBinding;
import com.example.ss.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Gastos_Activity extends AppCompatActivity implements View.OnClickListener {

    CheckBox jogos, trabalho, edicao, web, estudo , valor1, valor2, valor3, valor4, sim, nao;

    String usuarioID;

    String  categoria1, categoria2, categoria3;
    DatabaseReference databaseReference;
    String msg = "É necesário selecionar uma das opções para que possamos prosseguir.";
    private ActivityGastosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGastosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        jogos = (CheckBox) binding.jogos;
        jogos.setOnClickListener(this);
        trabalho = (CheckBox) binding.trabalho;
        trabalho.setOnClickListener(this);
        edicao = (CheckBox) binding.edicao;
        edicao.setOnClickListener(this);
        web = (CheckBox) binding.web;
        web.setOnClickListener(this);
        estudo = (CheckBox) binding.estudo;
        estudo.setOnClickListener(this);
        valor1 = (CheckBox) binding.valor1;
        valor1.setOnClickListener(this);
        valor2 = (CheckBox) binding.valor2;
        valor2.setOnClickListener(this);
        valor3 = (CheckBox) binding.valor3;
        valor3.setOnClickListener(this);
        valor4 = (CheckBox) binding.valor4;
        valor4.setOnClickListener(this);
        sim = (CheckBox) binding.sim;
        sim.setOnClickListener(this);
        nao = (CheckBox) binding.nao;
        nao.setOnClickListener(this);


        binding.btAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.question1.setText(R.string.question_2);
                binding.questionario1.setVisibility(View.GONE);
                binding.questionario2.setVisibility(View.VISIBLE);
                binding.btAvancar.setVisibility(View.GONE);
                binding.btAvancar2.setVisibility(View.VISIBLE);
            }
        });
        binding.btAvancar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.question1.setText(R.string.question_3);
                binding.questionario2.setVisibility(View.GONE);
                binding.questionario3.setVisibility(View.VISIBLE);
                binding.btAvancar2.setVisibility(View.GONE);
                binding.btAvancar3.setVisibility(View.VISIBLE);
            }
        });

        binding.btAvancar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.progressbar.setVisibility(View.VISIBLE);
                binding.txtLoad.setVisibility(View.VISIBLE);
                binding.btAvancar3.setVisibility(View.GONE);
                binding.questionario3.setVisibility(View.GONE);
                binding.question1.setVisibility(View.GONE);
                binding.image1.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TelaPrincipal();

                    }
                },2000);

            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Gastos_Activity.this, PrimeirosPassos_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void TelaPrincipal(){

        Intent intent = new Intent(Gastos_Activity.this, Rendimento_Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        
        switch (view.getId()){
            case R.id.jogos:
                if(jogos.isChecked())
//                    Toast.makeText(getApplicationContext(), "Jogos", Toast.LENGTH_LONG).show();
                    binding.trabalho.setChecked(false);
                    binding.edicao.setChecked(false);
                    binding.web.setChecked(false);
                    binding.estudo.setChecked(false);
                    categoria1 = "Jogos";
                break;
            case R.id.trabalho:
                if(trabalho.isChecked())
//                    Toast.makeText(getApplicationContext(), "Trabalho", Toast.LENGTH_LONG).show();
                    binding.jogos.setChecked(false);
                    binding.edicao.setChecked(false);
                    binding.web.setChecked(false);
                    binding.estudo.setChecked(false);
                    categoria1 = "Trabalho";
                break;
            case R.id.edicao:
                if(edicao.isChecked())
//                    Toast.makeText(getApplicationContext(), "Edição", Toast.LENGTH_LONG).show();
                    binding.trabalho.setChecked(false);
                    binding.jogos.setChecked(false);
                    binding.web.setChecked(false);
                    binding.estudo.setChecked(false);
                    categoria1 = "Edição";
                break;
            case R.id.web:
                if(web.isChecked())
//                    Toast.makeText(getApplicationContext(), "Web", Toast.LENGTH_LONG).show();
                    binding.trabalho.setChecked(false);
                    binding.edicao.setChecked(false);
                    binding.jogos.setChecked(false);
                    binding.estudo.setChecked(false);
                    categoria1 = "Web";
                break;
            case R.id.estudo:
                if(estudo.isChecked())
//                    Toast.makeText(getApplicationContext(), "Estudo", Toast.LENGTH_LONG).show();
                    binding.trabalho.setChecked(false);
                    binding.edicao.setChecked(false);
                    binding.web.setChecked(false);
                    binding.jogos.setChecked(false);
                    categoria1 = "Estudo";
                break;
            case R.id.valor_1:
                if(valor1.isChecked())
//                    Toast.makeText(getApplicationContext(), "Estudo", Toast.LENGTH_LONG).show();
                    binding.valor2.setChecked(false);
                binding.valor3.setChecked(false);
                binding.valor4.setChecked(false);
                categoria2 = "Valor1";
                break;
            case R.id.valor_2:
                if(valor2.isChecked())
//                    Toast.makeText(getApplicationContext(), "Estudo", Toast.LENGTH_LONG).show();
                    binding.valor1.setChecked(false);
                binding.valor3.setChecked(false);
                binding.valor4.setChecked(false);
                categoria2 = "Valor2";
                break;
            case R.id.valor_3:
                if(valor3.isChecked())
//                    Toast.makeText(getApplicationContext(), "Estudo", Toast.LENGTH_LONG).show();
                    binding.valor1.setChecked(false);
                binding.valor2.setChecked(false);
                binding.valor4.setChecked(false);
                categoria2 = "Valor3";
                break;
            case R.id.valor_4:
                if(valor4.isChecked())
//                    Toast.makeText(getApplicationContext(), "Estudo", Toast.LENGTH_LONG).show();
                    binding.valor1.setChecked(false);
                binding.valor2.setChecked(false);
                binding.valor3.setChecked(false);
                categoria2 = "Valor4";
                break;
            case R.id.sim:
                if(sim.isChecked())
//                    Toast.makeText(getApplicationContext(), "Estudo", Toast.LENGTH_LONG).show();
                    binding.nao.setChecked(false);
                categoria3 = "Sim";
                break;
            case R.id.nao:
                if(nao.isChecked())
//                    Toast.makeText(getApplicationContext(), "Estudo", Toast.LENGTH_LONG).show();
                    binding.sim.setChecked(false);
                categoria3 = "Não";
                break;
        }

        SalvarDadosUsuario();

    }
    private void SalvarDadosUsuario(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(usuarioID).child("Categoria 1").setValue(categoria1);
        databaseReference.child(usuarioID).child("Categoria 2").setValue(categoria2);
        databaseReference.child(usuarioID).child("Categoria 3").setValue(categoria3);


    }
}