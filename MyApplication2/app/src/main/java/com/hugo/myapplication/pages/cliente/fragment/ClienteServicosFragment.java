package com.hugo.myapplication.pages.cliente.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.cliente.adapters.TarefaAdapter;
import com.hugo.myapplication.pages.cliente.dtos.TarefaResponseDTO;
import com.hugo.myapplication.utils.ApiHelper;
import com.hugo.myapplication.utils.SharedPrefHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClienteServicosFragment extends Fragment {

    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<TarefaResponseDTO> listaTarefas;
    private Long clienteId = -1L;

    public ClienteServicosFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_fragment_servicos, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTarefas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaTarefas = new ArrayList<>();
        tarefaAdapter = new TarefaAdapter(listaTarefas);
        recyclerView.setAdapter(tarefaAdapter);

        if (getArguments() != null && getArguments().containsKey("clienteId")) {
            clienteId = getArguments().getLong("clienteId", -1);
            SharedPrefHelper.salvarClienteId(requireContext(), clienteId);
        } else {
            clienteId = SharedPrefHelper.obterClienteId(requireContext());
        }

        if (clienteId != -1) {
            carregarTarefas();
        } else {
            Toast.makeText(getContext(), "Erro: clienteId inválido", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void carregarTarefas() {
        String url = "tarefas/cliente/" + clienteId;

        ApiHelper.get(getContext(), url, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray contentArray = new JSONArray(response);
                    List<TarefaResponseDTO> novasTarefas = new ArrayList<>();

                    for (int i = 0; i < contentArray.length(); i++) {
                        JSONObject obj = contentArray.getJSONObject(i);

                        TarefaResponseDTO tarefa = new TarefaResponseDTO();
                        tarefa.setId(obj.optLong("id"));
                        tarefa.setDescricao(obj.optString("descricao"));
                        tarefa.setDataHora(obj.optString("dataHora"));
                        tarefa.setStatus(obj.optString("status"));
                        tarefa.setClienteNome(obj.optString("clienteNome"));
                        tarefa.setResidenciaEndereco(obj.optString("residenciaEndereco"));

                        // 🔹 se backend mandar colaboradores como lista de strings
                        if (obj.has("colaboradoresNomes")) {
                            JSONArray colaboradoresArray = obj.getJSONArray("colaboradoresNomes");
                            List<String> colaboradores = new ArrayList<>();
                            for (int j = 0; j < colaboradoresArray.length(); j++) {
                                colaboradores.add(colaboradoresArray.getString(j));
                            }
                            tarefa.setColaboradoresNomes(colaboradores);
                        }

                        novasTarefas.add(tarefa);
                    }

                    getActivity().runOnUiThread(() -> {
                        listaTarefas.clear();
                        listaTarefas.addAll(novasTarefas);
                        tarefaAdapter.notifyDataSetChanged();
                    });

                } catch (Exception e) {
                    Log.e("ClienteServicosFragment", "❌ Erro ao processar JSON", e);
                }
            }

            @Override
            public void onError(String errorMessage) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Erro ao buscar tarefas", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
