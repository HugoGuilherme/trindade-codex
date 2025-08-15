package com.hugo.myapplication.pages.LimpezaPainel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hugo.myapplication.adapters.AgendamentoAdapter;
import com.hugo.myapplication.pages.BaseActivity;
import com.hugo.myapplication.pages.LimpezaPainel.actions.AgendamentoLimpezaPainelCreate;
import com.hugo.myapplication.utils.AgendamentoUtils;
import com.hugo.myapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LimpezaPainelActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private AgendamentoAdapter adapter;
    private List<JSONObject> listaAgendamentos = new ArrayList<>(); // Alterado para List<JSONObject>
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.agendamento_limpeza_painel_activity, false);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AgendamentoAdapter(listaAgendamentos);

        recyclerView.setAdapter(adapter);

        fetchAgendamentos();
        swipeRefreshLayout.setOnRefreshListener(this::fetchAgendamentos);

        FloatingActionButton botaoAdicionar = findViewById(R.id.adicionar_limpeza);
        botaoAdicionar.setOnClickListener(view -> {
            Intent intent = new Intent(LimpezaPainelActivity.this, AgendamentoLimpezaPainelCreate.class);
            startActivity(intent);
        });
    }

    private void fetchAgendamentos() {
        swipeRefreshLayout.setRefreshing(true);

        new Thread(() -> {
            try {
                URL url = new URL("http://" + getString(R.string.server_ip) + ":8080/api/agendamento-limpeza/futuros");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");

                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                String token = sharedPreferences.getString("authToken", null);

                if (token != null) {
                    connection.setRequestProperty("Authorization", "Bearer " + token);
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Token não encontrado!", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    });
                    return;
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    runOnUiThread(() -> parseAgendamentosResponse(response.toString()));
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Erro ao buscar agendamentos: " + responseCode, Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    });
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erro ao conectar com a API", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        }).start();
    }

    private void parseAgendamentosResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("content")) {
                JSONArray agendamentos = jsonResponse.getJSONArray("content");

                // 🚨 Log para verificar se os dados estão vindo corretamente
                System.out.println("JSON recebido da API: " + agendamentos.toString());
                listaAgendamentos.clear();
                for (int i = 0; i < agendamentos.length(); i++) {
                    listaAgendamentos.add(agendamentos.getJSONObject(i)); // Correto para List<JSONObject>
                }

                runOnUiThread(() -> {
                    adapter.updateData(listaAgendamentos); // ✅ Método correto para atualizar os dados
                    adapter.notifyDataSetChanged(); // ✅ Atualiza a RecyclerView
                    swipeRefreshLayout.setRefreshing(false);
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erro: JSON não contém 'content'", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                Toast.makeText(this, "Erro ao processar resposta da API: " + e.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            });
        }
    }
}
