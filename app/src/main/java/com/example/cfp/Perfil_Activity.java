package com.example.cfp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cfp.databinding.ActivityPerfilBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Perfil_Activity extends AppCompatActivity {

    private PieChart pieChart;
    private CircularProgressIndicator circularProgressIndicator;
    private LinearLayout linearLayout;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ActivityPerfilBinding binding;

    String usuarioID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        linearLayout = binding.linearLayout;
        pieChart = binding.pieChart;

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference categoriaRef = database.getReference("Usuarios").child(usuarioID).child("Gastos_Gerais");

        categoriaRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Double> valoresPorCategoria = new HashMap<>();

                // Iterar sobre as categorias
                for (DataSnapshot categoriaSnapshot : dataSnapshot.getChildren()) {
                    String categoria = categoriaSnapshot.getKey();
                    double total = 0.0;

                    // Iterar sobre os valores dentro da categoria
                    for (DataSnapshot valorSnapshot : categoriaSnapshot.getChildren()) {
                        double valor = valorSnapshot.getValue(Double.class);
                        total += valor;
                    }

                    // Adicionar o valor total ao mapa de valores por categoria
                    valoresPorCategoria.put(categoria, total);
                }


                ArrayList<PieEntry> categorias = new ArrayList<>();
                PieDataSet pieDataSet = new PieDataSet(categorias, "");
                for (Map.Entry<String, Double> entry : valoresPorCategoria.entrySet()) {
                    String categoria = entry.getKey();
                    double valor = entry.getValue();
                    categorias.add(new PieEntry((float) valor, categoria));
                }
                pieDataSet.setColors(getResources().getColor(R.color.purple_200),
                        getResources().getColor(R.color.green),
                        getResources().getColor(R.color.red),
                        getResources().getColor(R.color.teal_700),
                        getResources().getColor(R.color.purple_500),
                        getResources().getColor(R.color.yellow));
                pieDataSet.setSliceSpace(2f);
                pieDataSet.setValueTextColor(Color.WHITE);
                pieDataSet.setValueTextSize(10f);
                pieDataSet.setSliceSpace(1f);

                PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.animate();
                pieChart.setHoleColor(android.R.color.transparent);
                pieChart.setEntryLabelTextSize(10f);
                pieChart.invalidate();
                pieChart.setDrawEntryLabels(false);
                pieChart.setHoleRadius(75);
                pieChart.getDescription().setEnabled(false);
                pieChart.getPaddingRight();

                Legend legend = pieChart.getLegend();
                legend.setForm(Legend.LegendForm.CIRCLE);
                legend.setTextSize(10);
                legend.setTextColor(Color.WHITE);
                legend.setFormSize(15);
                legend.setFormToTextSpace(4);
                legend.setDrawInside(false);
                legend.setXEntrySpace(6f);
                legend.setYEntrySpace(10f);
                legend.setWordWrapEnabled(true);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);


                usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(usuarioID);
                DatabaseReference gastosGeraisRef = userRef.child("Gastos_Gerais");

                gastosGeraisRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        linearLayout.removeAllViews();

                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){
                            String categoryKey = categorySnapshot.getKey();
                            Long valueLong = categorySnapshot.getValue(Long.class);

                            String value = String.valueOf(valueLong);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            TextView textView = new TextView(Perfil_Activity.this);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                            textView.setTextColor(Color.WHITE);
                            textView.setBackgroundColor(Color.LTGRAY);
                            textView.setText(categoryKey + ": " + value);
                            layoutParams.setMargins(0, 0, 0, 16);
                            textView.setLayoutParams(layoutParams);
                            linearLayout.addView(textView);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}