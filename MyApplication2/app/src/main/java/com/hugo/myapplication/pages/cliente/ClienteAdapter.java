package com.hugo.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.cliente.Cliente;
import com.hugo.myapplication.pages.cliente.ClienteArea;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private final List<Cliente> listaClientes;

    public ClienteAdapter(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = listaClientes.get(position);
        holder.tvNome.setText(cliente.getNome());
        holder.tvEndereco.setText(cliente.getEmail());
        holder.tvTelefone.setText(cliente.getTelefone());

        // Evento de clique no card do cliente
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ClienteArea.class);
            intent.putExtra("id", cliente.getId());
            intent.putExtra("nome", cliente.getNome());
            intent.putExtra("email", cliente.getEmail());
            intent.putExtra("telefone", cliente.getTelefone());
            intent.putExtra("endereco", cliente.getEndereco());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvEndereco, tvTelefone;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvTitle);
            tvEndereco = itemView.findViewById(R.id.tvEnderecoCliente);
            tvTelefone = itemView.findViewById(R.id.tvTelefone);
        }
    }
}
