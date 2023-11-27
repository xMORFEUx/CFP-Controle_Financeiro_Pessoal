package com.example.ss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import java.security.ProtectionDomain;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.anychart.core.annotations.Line;
import com.anychart.core.annotations.VerticalLine;
import com.example.ss.databinding.ActivityRendimentoBinding;
import com.example.ss.model.Produto;
import com.example.ss.R;
import com.example.ss.adapter.AdapterProduto;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

public class Rendimento_Activity extends AppCompatActivity {

    DatabaseReference databaseReference;

    String usuarioID;

    String msg = "É necesário inserir algum valor para que possamos prosseguir.";

    private ActivityRendimentoBinding binding;

    private RecyclerView recyclerView;

    private AdapterProduto adapterProduto;

    private List<Produto> productList;
    private NumberFormat numberFormat = new DecimalFormat("###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRendimentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        productList = new ArrayList<>();

        productList.add(new Produto(R.drawable.ssd, "SSD SanDisk Plus 480GB", "Confiável, rápido e muita capacidade. A SanDisk, pioneira em tecnologias de armazenamento de estado sólido é a marca de confiança dos profissionais da área, oferece maior velocidade e desempenho com o SanDisk SSD Plus.", "R$ 450,00"));
        productList.add(new Produto(R.drawable.processador, "Intel Core i5 10400F", "Os novos processadores da 10ª geração oferecem atualizações de desempenho incríveis para melhorar a produtividade e proporcionar entretenimento surpreendente.", "R$ 1050,00"));
        productList.add(new Produto(R.drawable.memoria, "Memória RAM Corsair 8GB DDR4", "Memória Corsair Vengeance LPX, 8GB, 2666MHz, DDR4, C16, Preto.", "R$ 369,00"));
        productList.add(new Produto(R.drawable.placadevideo, "GeForce RTX 3090 24GB", "Os blocos de construção para a GPU mais rápida e eficiente do mundo, o novo Ampere SM traz 2X a taxa de transferência do FP32 e maior eficiência de energia.", "R$ 18.499,00"));
        productList.add(new Produto(R.drawable.teclado, "Teclado Mecânico Gamer T-Dagger Corvette", "Teclado Mecânico Gamer T-Dagger Corvette, LED Rainbow, Switch Outemu DIY Blue, ABNT2 - T-TGK302 -BL (PT-BLUE).", "R$ 229,00"));
        productList.add(new Produto(R.drawable.gabinete, "Gabinete Gamer", "A série Carbide SPEC-DELTA RGB é uma caixa ATX de torre média de vidro temperado com estilo angular impressionante, fluxo de ar potente e três ventiladores de refrigeração RGB incluídos.", "R$ 799,00"));


        recyclerView = findViewById(R.id.containerCompoments_2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterProduto = new AdapterProduto(productList);
        recyclerView.setAdapter(adapterProduto);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rendimento_Activity.this, Gastos_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private String formatCurrency(double value){
        return numberFormat.format(value);
    }

}