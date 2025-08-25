package com.hugo.myapplication.pages.agenda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hugo.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {

    private final List<AgendaItem> lista;

    public AgendaAdapter(List<AgendaItem> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agenda, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AgendaItem item = lista.get(position);

        holder.txtTitulo.setText(item.getTitulo());
        holder.txtStatus.setText("Status: " + item.getStatus());
        holder.txtDataHora.setText(item.getDataHora());

        // Novo: tipo de tarefa
        holder.txtTipoTarefa.setText("Tipo: " + item.getTipoTarefaNome());

        // junta colaboradores em uma única linha
        if (item.getColaboradores() != null && !item.getColaboradores().isEmpty()) {
            List<String> nomesFormatados = new ArrayList<>();
            for (String nome : item.getColaboradores()) {
                String[] partes = nome.trim().split("\\s+");
                if (partes.length >= 2) {
                    nomesFormatados.add(partes[0] + " " + partes[1]);
                } else {
                    nomesFormatados.add(partes[0]);
                }
            }
            holder.txtColaboradores.setText("Colaboradores: " + String.join(", ", nomesFormatados));
        } else {
            holder.txtColaboradores.setText("Colaboradores: -");
        }

        // mostra endereço + cliente
        String enderecoCliente = item.getResidenciaEndereco() + " - Cliente: " + item.getClienteNome();
        holder.txtEnderecoCliente.setText(enderecoCliente);
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtTipoTarefa, txtStatus, txtDataHora, txtColaboradores, txtEnderecoCliente;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtTipoTarefa = itemView.findViewById(R.id.txtTipoTarefa); // novo
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtDataHora = itemView.findViewById(R.id.txtDataHora);
            txtColaboradores = itemView.findViewById(R.id.txtColaboradores);
            txtEnderecoCliente = itemView.findViewById(R.id.txtEnderecoCliente);
        }
    }

}
