package com.example.ss.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ss.R;

import java.util.ArrayList;
import java.util.List;
import com.example.ss.model.Produto;
import com.squareup.picasso.Picasso;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.ProdutoViewHolder>{
    private List<Produto> produtos = new ArrayList<>();
    private Context context;

    public AdapterProduto(List<Produto> produtos) {
        this.produtos = produtos;
        this.context = context;
    }
    @Override
    public ProdutoViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.hardware_item, parent, false);
        return new ProdutoViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(ProdutoViewHolder holder, int position){

        Produto produto = produtos.get(position);

        holder.foto.setImageResource(produto.getFoto());
        holder.nome.setText(produto.getNome());
        holder.descricao.setText(produto.getDescricao());
        holder.preco.setText(produto.getPreco());

    }

    public int getItemCount(){
        return produtos.size();
    }

    public class ProdutoViewHolder extends RecyclerView.ViewHolder{
        public ImageView foto;
        public TextView nome, descricao,preco;
        public ProdutoViewHolder(View itemView){
            super(itemView);
            foto = itemView.findViewById(R.id.fotoProduto);
            nome = itemView.findViewById(R.id.nomeProduto);
            descricao = itemView.findViewById(R.id.descricaoProduto);
            preco = itemView.findViewById(R.id.precoProduto);
        }

    }
}