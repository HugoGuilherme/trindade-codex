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
import com.hugo.myapplication.pages.cliente.residencia.Residencia;
import com.hugo.myapplication.pages.cliente.residencia.ResidenciaAdapter;
import com.hugo.myapplication.utils.ApiHelper;
import com.hugo.myapplication.utils.SharedPrefHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientePerfilFragment extends Fragment {

    private TextView tvPerfilNome, tvPerfilEmail, tvPerfilTelefone, tvPerfilEndereco, tvPerfilGenero, tvPerfilDataNascimento, tvResidenciasTitle;
    private RecyclerView recyclerResidencias;
    private ResidenciaAdapter residenciaAdapter;
    private List<Residencia> listaResidencias;
    private Long clienteId = -1L;

    public ClientePerfilFragment() {
        // Construtor vazio necessário para o Fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_fragment_perfil, container, false);

        // 🔗 Vinculando componentes do layout
        tvPerfilEmail = view.findViewById(R.id.tvPerfilEmail);
        tvPerfilTelefone = view.findViewById(R.id.tvPerfilTelefone);
        tvPerfilEndereco = view.findViewById(R.id.tvPerfilEndereco);
        tvPerfilGenero = view.findViewById(R.id.tvPerfilGenero);
        tvPerfilDataNascimento = view.findViewById(R.id.tvPerfilDataNascimento);
        tvResidenciasTitle = view.findViewById(R.id.tvResidenciasTitle);
        recyclerResidencias = view.findViewById(R.id.recyclerResidencias);

        // 🛠 Configurar RecyclerView
        listaResidencias = new ArrayList<>();
        recyclerResidencias.setLayoutManager(new LinearLayoutManager(getContext()));
        residenciaAdapter = new ResidenciaAdapter(listaResidencias);
        recyclerResidencias.setAdapter(residenciaAdapter);

        // 🔍 Primeiro tenta recuperar `clienteId` dos argumentos, senão pega do SharedPreferences
        if (getArguments() != null && getArguments().containsKey("clienteId")) {
            clienteId = getArguments().getLong("clienteId", -1);
            SharedPrefHelper.salvarClienteId(requireContext(), clienteId); // Salva no SharedPreferences
        } else {
            clienteId = SharedPrefHelper.obterClienteId(requireContext());
        }

        // ⚠ Proteção extra
        if (clienteId != -1) {
            carregarPerfil();
            carregarResidencias();
        } else {
            Toast.makeText(getContext(), "Erro: clienteId inválido", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void carregarPerfil() {
        if (clienteId == -1) {
            Log.e("ClientePerfilFragment", "❌ clienteId inválido, não pode carregar perfil!");
            return;
        }

        String url = "clientes/" + clienteId;

        ApiHelper.get(getContext(), url, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);

                    getActivity().runOnUiThread(() -> {
                        tvPerfilEmail.setText("E-mail: " + json.optString("email", "Não informado"));
                        tvPerfilTelefone.setText("Telefone: " + json.optString("telefone", "Não informado"));
                        tvPerfilEndereco.setText("Endereço: " + json.optString("endereco", "Não informado"));
                        tvPerfilGenero.setText("Gênero: " + json.optString("genero", "Não informado"));
                        tvPerfilDataNascimento.setText("Data de Nascimento: " + json.optString("dataNascimento", "Não informado"));
                    });

                } catch (Exception e) {
                    Log.e("ClientePerfilFragment", "❌ Erro ao processar JSON", e);
                }
            }

            @Override
            public void onError(String errorMessage) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Erro ao buscar perfil", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void carregarResidencias() {
        if (clienteId == -1) {
            Log.e("ClientePerfilFragment", "❌ clienteId inválido, não pode buscar residências!");
            return;
        }

        String url = "clientes/" + clienteId + "/residencias";

        ApiHelper.get(getContext(), url, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray contentArray = new JSONArray(response);
                    List<Residencia> novasResidencias = new ArrayList<>();

                    for (int i = 0; i < contentArray.length(); i++) {
                        JSONObject obj = contentArray.getJSONObject(i);
                        novasResidencias.add(new Residencia(
                                obj.optLong("id"),
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

                        if (!listaResidencias.isEmpty()) {
                            tvResidenciasTitle.setVisibility(View.VISIBLE);
                            recyclerResidencias.setVisibility(View.VISIBLE);
                        } else {
                            tvResidenciasTitle.setText("Nenhuma residência encontrada");
                        }
                    });

                } catch (Exception e) {
                    Log.e("ClientePerfilFragment", "❌ Erro ao processar JSON", e);
                }
            }

            @Override
            public void onError(String errorMessage) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Erro ao buscar residências", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
