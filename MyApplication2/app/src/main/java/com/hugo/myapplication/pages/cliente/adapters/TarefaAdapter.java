package com.hugo.myapplication.pages.cliente.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.cliente.dtos.TarefaResponseDTO;

import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder> {

    private final List<TarefaResponseDTO> tarefas;

    public TarefaAdapter(List<TarefaResponseDTO> tarefas) {
        this.tarefas = tarefas;
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarefa, parent, false);
        return new TarefaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        TarefaResponseDTO tarefa = tarefas.get(position);
        holder.txtDescricao.setText(tarefa.getDescricao());
        holder.txtStatus.setText("Status: " + tarefa.getStatus());
        holder.txtCliente.setText("Cliente: " + tarefa.getClienteNome());
        holder.txtEndereco.setText("Endereço: " + tarefa.getResidenciaEndereco());
    }

    @Override
    public int getItemCount() {
        return tarefas.size();
    }

    static class TarefaViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescricao, txtStatus, txtCliente, txtEndereco;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtCliente = itemView.findViewById(R.id.txtCliente);
            txtEndereco = itemView.findViewById(R.id.txtEndereco);
        }
    }
}