package com.hugo.myapplication.pages.cliente.residencia;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.BaseActivity;
import com.hugo.myapplication.utils.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResidenciaPainelCreate extends BaseActivity {

    private LinearLayout residenciasContainer;
    private Button botaoAdicionarResidencia, botaoSalvar;
    private List<JSONObject> listaResidencias = new ArrayList<>();
    private JSONObject clienteJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.residencia_painel_create_activity, true);

        residenciasContainer = findViewById(R.id.residenciasContainer);
        botaoAdicionarResidencia = findViewById(R.id.botaoAdicionarResidencia);
        botaoSalvar = findViewById(R.id.botaoSalvar);

        try {
            String clienteString = getIntent().getStringExtra("CLIENTE_JSON");
            clienteJson = new JSONObject(clienteString);
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao carregar cliente!", Toast.LENGTH_LONG).show();
            finish();
        }

        botaoAdicionarResidencia.setOnClickListener(v -> adicionarResidencia());
        botaoSalvar.setOnClickListener(v -> salvarDados());
    }

    private void adicionarResidencia() {
        View viewResidencia = getLayoutInflater().inflate(R.layout.item_residencia_create, null);

        EditText etCep = viewResidencia.findViewById(R.id.etCep);
        EditText etEndereco = viewResidencia.findViewById(R.id.etEndereco);
        EditText etCidade = viewResidencia.findViewById(R.id.etCidade);
        EditText etEstado = viewResidencia.findViewById(R.id.etEstado);

        // Buscar o CEP automaticamente ao perder o foco
        etCep.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String cep = etCep.getText().toString().trim();
                if (cep.length() == 8) {
                    buscarCEP(cep, etEndereco, etCidade, etEstado);
                } else {
                    Toast.makeText(this, "CEP inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        residenciasContainer.addView(viewResidencia);
    }

    private void salvarDados() {
        listaResidencias.clear();
        for (int i = 0; i < residenciasContainer.getChildCount(); i++) {
            View view = residenciasContainer.getChildAt(i);

            EditText descricao = view.findViewById(R.id.etDescricao);
            EditText endereco = view.findViewById(R.id.etEndereco);
            EditText numero = view.findViewById(R.id.etNumero);
            EditText cidade = view.findViewById(R.id.etCidade);
            EditText estado = view.findViewById(R.id.etEstado);
            EditText cep = view.findViewById(R.id.etCep);

            try {
                JSONObject jsonResidencia = new JSONObject();
                jsonResidencia.put("descricao", descricao.getText().toString().trim());
                jsonResidencia.put("endereco", endereco.getText().toString().trim() + ", " + numero.getText().toString().trim());
                jsonResidencia.put("cidade", cidade.getText().toString().trim());
                jsonResidencia.put("estado", estado.getText().toString().trim());
                jsonResidencia.put("cep", cep.getText().toString().trim());

                listaResidencias.add(jsonResidencia);
            } catch (Exception e) {
                Log.e("Residencia", "Erro ao criar JSON da residência", e);
            }
        }

        enviarClienteParaAPI();
    }

    private void enviarClienteParaAPI() {
        String url = "clientes";

        ApiHelper.post(this, url, clienteJson.toString(), new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    long clienteId = jsonResponse.getLong("id");
                    enviarResidencias(clienteId);
                } catch (Exception e) {
                    Log.e("Cliente", "Erro ao processar resposta do cliente", e);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Cliente", "Erro ao salvar cliente: " + errorMessage);
                runOnUiThread(() -> Toast.makeText(ResidenciaPainelCreate.this, "Erro ao salvar cliente", Toast.LENGTH_SHORT).show());
            }
        });
    }

    // 🔥 Atualização: Busca CEP de forma assíncrona
    private void buscarCEP(String cep, EditText etEndereco, EditText etCidade, EditText etEstado) {
        new Thread(() -> {
            try {
                URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());

                    // Atualiza a UI na Thread principal
                    runOnUiThread(() -> {
                        try {
                            if (!jsonResponse.has("erro")) {
                                etEndereco.setText(jsonResponse.getString("logradouro"));
                                etCidade.setText(jsonResponse.getString("localidade"));
                                etEstado.setText(jsonResponse.getString("uf"));
                            } else {
                                Toast.makeText(this, "CEP não encontrado!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("ViaCEP", "Erro ao processar JSON do CEP", e);
                        }
                    });

                } else {
                    Log.e("ViaCEP", "Erro na requisição: " + responseCode);
                }

            } catch (Exception e) {
                Log.e("ViaCEP", "Erro ao buscar CEP", e);
            }
        }).start();
    }

    private void enviarResidencias(long clienteId) {
        if (listaResidencias.isEmpty()) {
            Toast.makeText(this, "Nenhuma residência para salvar", Toast.LENGTH_SHORT).show();
            return;
        }

        for (JSONObject residencia : listaResidencias) {
            String url = "clientes/" + clienteId + "/residencias";

            ApiHelper.post(this, url, residencia.toString(), new ApiHelper.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    Log.d("Residencia", "Residência salva com sucesso!");
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e("Residencia", "Erro ao salvar residência: " + errorMessage);
                }
            });
        }

        Toast.makeText(this, "Cliente e residências salvos com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
