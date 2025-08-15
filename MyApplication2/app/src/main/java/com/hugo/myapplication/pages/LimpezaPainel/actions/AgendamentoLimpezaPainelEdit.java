package com.hugo.myapplication.pages.LimpezaPainel.actions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.BaseActivity;
import com.hugo.myapplication.pages.LimpezaPainel.LimpezaPainelActivity;
import com.hugo.myapplication.utils.ApiHelper;
import com.hugo.myapplication.utils.DateUtils;
import com.hugo.myapplication.utils.MaskWatcher;

import org.json.JSONObject;

public class AgendamentoLimpezaPainelEdit extends BaseActivity {

    private TextInputEditText nomeDataEdit, emailDataEdit, enderecoDataEdit, mesesDataEdit, dataInstalacaoDataEdit, dataUltimaLimpezaDataEdit;
    private Button botaoSalvarAgendamentoDataEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.agendamento_limpeza_painel_edit_activity, true);

        nomeDataEdit = findViewById(R.id.nomeTextEdit);
        emailDataEdit = findViewById(R.id.emailTextEdit);
        enderecoDataEdit = findViewById(R.id.enderecoTextEdit);
        mesesDataEdit = findViewById(R.id.mesesTextEdit);
        dataInstalacaoDataEdit = findViewById(R.id.dataInstalacaoTextEdit);
        dataUltimaLimpezaDataEdit = findViewById(R.id.dataUltimaLimpezaTextEdit);
        botaoSalvarAgendamentoDataEdit = findViewById(R.id.botao_salvar_agendamentoEdit);


        dataInstalacaoDataEdit.addTextChangedListener(new MaskWatcher());
        dataUltimaLimpezaDataEdit.addTextChangedListener(new MaskWatcher());

        // Verifica se há um agendamento vindo do Intent
        String agendamentoJson = getIntent().getStringExtra("agendamento");

        if (agendamentoJson != null) {
            try {
                JSONObject agendamento = new JSONObject(agendamentoJson);

                // Preencher os campos com os dados do agendamento
                nomeDataEdit.setText(agendamento.optString("nome"));
                emailDataEdit.setText(agendamento.optString("email"));
                enderecoDataEdit.setText(agendamento.optString("endereco"));
                mesesDataEdit.setText(agendamento.optString("meses"));
                dataInstalacaoDataEdit.setText(DateUtils.convertISOToDate(agendamento.optString("dataInstalacao")));
                dataUltimaLimpezaDataEdit.setText(DateUtils.convertISOToDate(agendamento.optString("dataUltimaLimpeza")));

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao carregar agendamento!", Toast.LENGTH_SHORT).show();
            }
        }


        botaoSalvarAgendamentoDataEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                String idUsuario = sharedPreferences.getString("perfilId", "0");
                String token = sharedPreferences.getString("authToken", null);

                // Verificar se o token está salvo
                if (token == null) {
                    Toast.makeText(AgendamentoLimpezaPainelEdit.this, "Erro: Token de autenticação não encontrado!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nome = nomeDataEdit.getText().toString().trim();
                String email = emailDataEdit.getText().toString().trim();
                String endereco = enderecoDataEdit.getText().toString().trim();
                String meses = mesesDataEdit.getText().toString().trim();
                String dataInstalacao = dataInstalacaoDataEdit.getText().toString().trim();
                String dataUltimaLimpeza = dataUltimaLimpezaDataEdit.getText().toString().trim();

                // Verificar campos vazios
                if (nome.isEmpty() || email.isEmpty() || endereco.isEmpty() || meses.isEmpty() || dataInstalacao.isEmpty() || dataUltimaLimpeza.isEmpty()) {
                    Toast.makeText(AgendamentoLimpezaPainelEdit.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {

                    String agendamentoJson = getIntent().getStringExtra("agendamento");
                    JSONObject agendamento = new JSONObject(agendamentoJson);
                    // Criando o JSON com os dados do formulário
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("nome", nome);
                    jsonBody.put("email", email);
                    jsonBody.put("endereco", endereco);
                    jsonBody.put("meses", meses);
                    jsonBody.put("dataInstalacao", DateUtils.convertDateToISO(dataInstalacao));
                    jsonBody.put("dataUltimaLimpeza", DateUtils.convertDateToISO(dataUltimaLimpeza));
                    jsonBody.put("userId", idUsuario);

                    // URL da API para envio do agendamento
                    String url = "agendamento-limpeza/" + agendamento.optString("id");

                    // Enviar os dados via POST usando o ApiHelper
                    ApiHelper.put(AgendamentoLimpezaPainelEdit.this, url, jsonBody.toString(), new ApiHelper.ApiCallback() {
                        @Override
                        public void onSuccess(String response) {
                            runOnUiThread(() -> {
                                Toast.makeText(AgendamentoLimpezaPainelEdit.this, "Agendamento salvo com sucesso!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(AgendamentoLimpezaPainelEdit.this, LimpezaPainelActivity.class);
                                startActivity(intent);

                                finish();
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            runOnUiThread(() -> {
                                Toast.makeText(AgendamentoLimpezaPainelEdit.this, "Erro ao salvar: " + errorMessage, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AgendamentoLimpezaPainelEdit.this, "Erro ao criar JSON", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}