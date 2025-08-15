package com.hugo.myapplication.pages.cliente.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.cliente.Cliente;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {

    private List<Cliente> listaClientes;

    public ClienteAdapter(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente cliente = listaClientes.get(position);

        if (cliente != null) {
            holder.nome.setText(cliente.getNome() != null ? cliente.getNome() : "Nome não disponível");
            holder.telefone.setText(cliente.getTelefone() != null ? cliente.getTelefone() : "Telefone não cadastrado");
            holder.endereco.setText(cliente.getEndereco() != null ? cliente.getEndereco() : "Endereço não disponível");
        }
    }

    @Override
    public int getItemCount() {
        return listaClientes != null ? listaClientes.size() : 0;
    }

    public void atualizarLista(List<Cliente> novaLista) {
        if (novaLista != null && !novaLista.equals(this.listaClientes)) {
            this.listaClientes = novaLista;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome, telefone, endereco;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.tvTitle);
            telefone = itemView.findViewById(R.id.tvTelefone);
            endereco = itemView.findViewById(R.id.tvEnderecoCliente);

            // 🔥 Log de erro para facilitar depuração
            if (nome == null || telefone == null || endereco == null) {
                Log.e("ClienteAdapter", "Erro: Verifique os IDs no layout client_item.xml");
            }
        }
    }
}
