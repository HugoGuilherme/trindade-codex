package com.hugo.myapplication.pages.LimpezaPainel.actions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.BaseActivity;
import com.hugo.myapplication.pages.LimpezaPainel.LimpezaPainelActivity;
import com.hugo.myapplication.utils.ApiHelper;
import com.hugo.myapplication.utils.DateUtils;
import com.hugo.myapplication.utils.MaskWatcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AgendamentoLimpezaPainelCreate extends BaseActivity {

    private AutoCompleteTextView spinnerClientes, spinnerResidencias;
    private TextInputEditText mesesData, dataInstalacaoData, dataUltimaLimpezaData;
    private Button botaoSalvarAgendamento;
    private List<Cliente> listaClientes = new ArrayList<>();
    private List<Residencia> listaResidencias = new ArrayList<>();
    private Cliente clienteSelecionado;
    private Residencia residenciaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.agendamento_limpeza_painel_create_activity, true);

        spinnerClientes = findViewById(R.id.spinner_clientes);
        spinnerResidencias = findViewById(R.id.spinner_residencias);
        mesesData = findViewById(R.id.mesesText);
        dataInstalacaoData = findViewById(R.id.dataInstalacaoText);
        dataUltimaLimpezaData = findViewById(R.id.dataUltimaLimpezaText);
        botaoSalvarAgendamento = findViewById(R.id.botao_salvar_agendamento);


        dataInstalacaoData.addTextChangedListener(new MaskWatcher());
        dataUltimaLimpezaData.addTextChangedListener(new MaskWatcher());

        carregarClientes();

        botaoSalvarAgendamento.setOnClickListener(v -> salvarAgendamento());
    }

    private void carregarClientes() {
        String url = "clientes"; // Endpoint para buscar os clientes

        ApiHelper.get(this, url, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    listaClientes.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Cliente cliente = new Cliente(
                                obj.optLong("id"),
                                obj.optString("nome"),
                                obj.optString("email")

                        );
                        listaClientes.add(cliente);
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<Cliente> adapter = new ArrayAdapter<>(
                                AgendamentoLimpezaPainelCreate.this,
                                android.R.layout.simple_dropdown_item_1line,
                                listaClientes
                        );

                        spinnerClientes.setAdapter(adapter);
                        spinnerClientes.setOnClickListener(v -> spinnerClientes.showDropDown());

                        spinnerClientes.setOnItemClickListener((parent, view, position, id) -> {
                            clienteSelecionado = (Cliente) parent.getItemAtPosition(position);
                            carregarResidencias(clienteSelecionado.getId()); // 🔥 Chama a API para buscar residências
                        });
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(AgendamentoLimpezaPainelCreate.this, "Erro ao carregar clientes!", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(AgendamentoLimpezaPainelCreate.this, "Erro ao buscar clientes: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void carregarResidencias(Long clienteId) {
        if (clienteId == null) return;

        String url = "clientes/" + clienteId + "/residencias"; // URL correta para buscar residências

        ApiHelper.get(this, url, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    listaResidencias.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Residencia residencia = new Residencia(
                                obj.optLong("id"),
                                obj.optString("endereco")
                        );
                        listaResidencias.add(residencia);
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<Residencia> adapter = new ArrayAdapter<>(
                                AgendamentoLimpezaPainelCreate.this,
                                android.R.layout.simple_dropdown_item_1line,
                                listaResidencias
                        );

                        spinnerResidencias.setAdapter(adapter);
                        spinnerResidencias.setOnClickListener(v -> spinnerResidencias.showDropDown());

                        spinnerResidencias.setOnItemClickListener((parent, view, position, id) -> {
                            residenciaSelecionada = (Residencia) parent.getItemAtPosition(position);
                        });
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(AgendamentoLimpezaPainelCreate.this, "Erro ao carregar residências!", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(AgendamentoLimpezaPainelCreate.this, "Erro ao buscar residências: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void salvarAgendamento() {
        if (clienteSelecionado == null || residenciaSelecionada == null) {
            Toast.makeText(this, "Selecione um cliente e uma residência!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String idUsuario = sharedPreferences.getString("perfilId", "0");
        String token = sharedPreferences.getString("authToken", null);

        if (token == null) {
            Toast.makeText(this, "Erro: Token não encontrado!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("clienteId", clienteSelecionado.getId());
            jsonBody.put("residenciaId", residenciaSelecionada.getId());
            jsonBody.put("meses", mesesData.getText().toString().trim());
            jsonBody.put("dataInstalacao", DateUtils.convertDateToISO(dataInstalacaoData.getText().toString().trim()));
            jsonBody.put("dataUltimaLimpeza", DateUtils.convertDateToISO(dataUltimaLimpezaData.getText().toString().trim()));
            jsonBody.put("userId", idUsuario);

            // 🔥 Removendo os campos se forem nulos
            if (clienteSelecionado.getNome() != null) {
                jsonBody.put("nome", clienteSelecionado.getNome());
            }
            if (clienteSelecionado.getEmail() != null) {
                jsonBody.put("email", clienteSelecionado.getEmail());
            }
            if (residenciaSelecionada.getEndereco() != null) {
                jsonBody.put("endereco", residenciaSelecionada.getEndereco());
            }

            ApiHelper.post(this, "agendamento-limpeza", jsonBody.toString(), new ApiHelper.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        Toast.makeText(AgendamentoLimpezaPainelCreate.this, "Agendamento salvo!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AgendamentoLimpezaPainelCreate.this, LimpezaPainelActivity.class));
                        finish();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() ->
                            Toast.makeText(AgendamentoLimpezaPainelCreate.this, "Erro ao salvar: " + errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao criar JSON", Toast.LENGTH_SHORT).show();
        }

    }
}

// ✅ Classe Cliente
class Cliente {
    private final Long id;
    private final String nome;
    private final String email;


    public Cliente(Long id, String nome, String email) { // 🔥 Atualizando o construtor
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() { // 🔥 Método getEmail() para acessar o e-mail
        return email;

    }
    public String getNome() { // 🔥 Adicione este método
        return nome;
    }

    @Override
    public String toString() {
        return nome; // Faz com que a lista exiba apenas o nome
    }
}


// ✅ Classe Residência
class Residencia {
    private final Long id;
    private final String endereco; // 🔥 Adicionando o campo endereço

    public Residencia(Long id, String endereco) { // 🔥 Atualizando o construtor
        this.id = id;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public String getEndereco() { // 🔥 Criando o método getEndereco()
        return endereco;
    }

    @Override
    public String toString() {
        return endereco; // Faz com que o AutoCompleteTextView exiba apenas o endereço
    }
}
