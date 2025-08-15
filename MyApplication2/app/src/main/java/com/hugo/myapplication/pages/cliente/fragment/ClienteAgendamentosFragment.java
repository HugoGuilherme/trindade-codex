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
import com.hugo.myapplication.models.Agendamento;
import com.hugo.myapplication.pages.cliente.adapters.ClienteAgendamentoAdapter;
import com.hugo.myapplication.utils.ApiHelper;
import com.hugo.myapplication.utils.SharedPrefHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClienteAgendamentosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClienteAgendamentoAdapter agendamentoAdapter;
    private List<Agendamento> listaAgendamentos;

    private Long clienteId;

    public ClienteAgendamentosFragment() {
        // Construtor vazio necessário para o Fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_fragment_agendamentos, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAgendamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaAgendamentos = new ArrayList<>();
        agendamentoAdapter = new ClienteAgendamentoAdapter(listaAgendamentos);
        recyclerView.setAdapter(agendamentoAdapter);

        if (getArguments() != null) {
            clienteId = getArguments().getLong("clienteId", -1); // Corrigido para clienteId
        }

        if (clienteId != -1) {
            carregarAgendamentos();
        } else {
            Log.e("ClienteAgendamentosFragment", "❌ clienteId inválido!");
        }

        return view;
    }


    private void carregarAgendamentos() {
        if (clienteId == -1) {
            Log.e("ClienteAgendamentosFragment", "❌ clienteId inválido, não pode buscar agendamentos!");
            return;
        }
        if (getArguments() != null && getArguments().containsKey("clienteId")) {
            clienteId = getArguments().getLong("clienteId", -1);
            SharedPrefHelper.salvarClienteId(requireContext(), clienteId); // Salva no SharedPreferences
        } else {
            // Se não veio no argumento, tenta recuperar do SharedPreferences
            clienteId = SharedPrefHelper.obterClienteId(requireContext());
        }
        String url = "agendamento-limpeza/futuros/" + clienteId; // Garantir que clienteId está correto

        ApiHelper.get(getContext(), url, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("ClienteAgendamentosFragment", "✔ Resposta da API: " + response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray contentArray = jsonResponse.getJSONArray("content");

                    List<Agendamento> novosAgendamentos = new ArrayList<>();
                    for (int i = 0; i < contentArray.length(); i++) {
                        JSONObject obj = contentArray.getJSONObject(i);
                        Agendamento agendamento = new Agendamento(
                                obj.optString("nome"),
                                obj.optString("email"),
                                obj.optString("endereco"),
                                obj.optString("dataInstalacao"),
                                obj.optString("dataUltimaLimpeza"),
                                obj.optString("dataProximaLimpeza"),
                                obj.optInt("meses"),
                                obj.optInt("userId"),
                                obj.optLong("clienteId")
                        );
                        novosAgendamentos.add(agendamento);
                    }

                    getActivity().runOnUiThread(() -> {
                        listaAgendamentos.clear();
                        listaAgendamentos.addAll(novosAgendamentos);
                        agendamentoAdapter.notifyDataSetChanged();
                    });

                } catch (Exception e) {
                    Log.e("ClienteAgendamentosFragment", "❌ Erro ao processar JSON", e);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("ClienteAgendamentosFragment", "❌ Erro ao buscar agendamentos: " + errorMessage);
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Erro ao buscar agendamentos", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
