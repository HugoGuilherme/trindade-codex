package com.hugo.myapplication.pages.cliente.residencia;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.cliente.residencia.Residencia;

import java.util.List;

public class ResidenciaAdapter extends RecyclerView.Adapter<ResidenciaAdapter.ViewHolder> {

    private List<Residencia> listaResidencias;

    public ResidenciaAdapter(List<Residencia> listaResidencias) {
        this.listaResidencias = listaResidencias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_residencia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Residencia residencia = listaResidencias.get(position);

        holder.tvDescricao.setText(residencia.getDescricao());
        holder.tvEndereco.setText(residencia.getEndereco() + ", " + residencia.getCidade() + " - " + residencia.getEstado());
        holder.tvCep.setText("CEP: " + residencia.getCep());
    }

    @Override
    public int getItemCount() {
        return listaResidencias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescricao, tvEndereco, tvCep;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            tvEndereco = itemView.findViewById(R.id.tvEndereco);
            tvCep = itemView.findViewById(R.id.tvCep);
        }
    }
}