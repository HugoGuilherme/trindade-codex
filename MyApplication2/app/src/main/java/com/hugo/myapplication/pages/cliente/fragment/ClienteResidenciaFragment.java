package com.hugo.myapplication.pages.cliente.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hugo.myapplication.R;
import com.hugo.myapplication.utils.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClienteResidenciaFragment extends Fragment {

    private RecyclerView recyclerView;
    private ResidenciaAdapter residenciaAdapter;
    private List<Residencia> listaResidencias;
    private Long clienteId;

    public void ResidenciasFragment() {
        // Construtor vazio necessário para o Fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_residencias, container, false);

        recyclerView = view.findViewById(R.id.recyclerResidencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaResidencias = new ArrayList<>();
        residenciaAdapter = new ResidenciaAdapter(listaResidencias);
        recyclerView.setAdapter(residenciaAdapter);

        if (getArguments() != null) {
            clienteId = getArguments().getLong("clienteId", -1);
        }

        if (clienteId != -1) {
            carregarResidencias();
        } else {
            Log.e("ResidenciasFragment", "❌ clienteId inválido!");
        }

        return view;
    }

    private void carregarResidencias() {
        if (clienteId == -1) {
            Log.e("ResidenciasFragment", "❌ clienteId inválido, não pode buscar residências!");
            return;
        }

        String url = "api/clientes/"+ clienteId + "/residencias";

        ApiHelper.get(getContext(), url, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("ResidenciasFragment", "✔ Resposta da API: " + response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray contentArray = jsonResponse.getJSONArray("content");

                    List<Residencia> novasResidencias = new ArrayList<>();
                    for (int i = 0; i < contentArray.length(); i++) {
                        JSONObject obj = contentArray.getJSONObject(i);
                        novasResidencias.add(new Residencia(
                                obj.optString("descricao"),
                                obj.optString("endereco"),
                                obj.optString("cidade"),
                                obj.optString("estado"),
                                obj.optString("cep")
                        ));
                    }

                    getActivity().runOnUiThread(() -> {
                        listaResidencias.clear();
                        listaResidencias.addAll(novasResidencias);
                        residenciaAdapter.notifyDataSetChanged();
                    });

                } catch (Exception e) {
                    Log.e("ResidenciasFragment", "❌ Erro ao processar JSON", e);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("ResidenciasFragment", "❌ Erro ao buscar residências: " + errorMessage);
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Erro ao buscar residências", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    // Classe interna para representar uma Residência
    private static class Residencia {
        private String descricao;
        private String endereco;
        private String cidade;
        private String estado;
        private String cep;

        public Residencia(String descricao, String endereco, String cidade, String estado, String cep) {
            this.descricao = descricao;
            this.endereco = endereco;
            this.cidade = cidade;
            this.estado = estado;
            this.cep = cep;
        }
    }

    // Adapter dentro do próprio Fragment
    private class ResidenciaAdapter extends RecyclerView.Adapter<ResidenciaAdapter.ViewHolder> {

        private List<Residencia> listaResidencias;

        public ResidenciaAdapter(List<Residencia> lista) {
            this.listaResidencias = lista;
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
            holder.tvDescricao.setText(residencia.descricao);
            holder.tvEndereco.setText(residencia.endereco + ", " + residencia.cidade + " - " + residencia.estado);
            holder.tvCep.setText("CEP: " + residencia.cep);
        }

        @Override
        public int getItemCount() {
            return listaResidencias.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvDescricao, tvEndereco, tvCep;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDescricao = itemView.findViewById(R.id.tvDescricao);
                tvEndereco = itemView.findViewById(R.id.tvEndereco);
                tvCep = itemView.findViewById(R.id.tvCep);
            }
        }
    }
}
