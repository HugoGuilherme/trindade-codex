package com.hugo.myapplication.pages.tipoTarefa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.cliente.dtos.TipoTarefa;

import java.util.List;

public class TipoTarefaAdapter extends RecyclerView.Adapter<TipoTarefaAdapter.ViewHolder> {

    private List<TipoTarefa> tipos;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(TipoTarefa tipo);
    }

    public TipoTarefaAdapter(List<TipoTarefa> tipos, OnDeleteClickListener listener) {
        this.tipos = tipos;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tipo_tarefa, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TipoTarefa tipo = tipos.get(position);
        holder.txtNome.setText(tipo.getNome());
        holder.txtDescricao.setText(tipo.getDescricao());

        holder.btnExcluir.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(tipo));
    }

    @Override
    public int getItemCount() {
        return tipos != null ? tipos.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtDescricao;
        ImageButton btnExcluir;

        ViewHolder(View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeTipo);
            txtDescricao = itemView.findViewById(R.id.txtDescricaoTipo);
            btnExcluir = itemView.findViewById(R.id.btnExcluirTipo);
        }
    }
}
