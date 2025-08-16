package com.hugo.myapplication.pages.agenda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hugo.myapplication.R;

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

        // Se vier null, String.valueOf converte em "null"
        String hora = String.valueOf(item.getHora());
        String titulo = String.valueOf(item.getTitulo());
        String descricao = String.valueOf(item.getDescricao());

        holder.txtHoraTitulo.setText(hora + " - " + titulo);
        holder.txtDescricao.setText(descricao);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHoraTitulo, txtDescricao;

        ViewHolder(View itemView) {
            super(itemView);
            txtHoraTitulo = itemView.findViewById(R.id.txtHoraTitulo);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
        }
    }

}