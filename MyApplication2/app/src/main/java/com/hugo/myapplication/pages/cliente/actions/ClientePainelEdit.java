package com.hugo.myapplication.pages.cliente.actions;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hugo.myapplication.R;
import com.hugo.myapplication.utils.ApiHelper;

import org.json.JSONObject;

public class ClientePainelEdit extends AppCompatActivity {

    private EditText editNome, editEmail, editTelefone;
    private Button btnSalvar;
    private Long clienteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cliente_painel_edit_activity);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editTelefone = findViewById(R.id.editTelefone);
        btnSalvar = findViewById(R.id.btnSalvar);

        clienteId = getIntent().getLongExtra("clienteId", -1);

        if (clienteId != -1) {
            carregarCliente();
        } else {
            Toast.makeText(this, "ID do cliente inválido", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSalvar.setOnClickListener(v -> salvarCliente());
    }

    private void carregarCliente() {
        String url = "clientes/" + clienteId;

        ApiHelper.get(this, url, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);

                    runOnUiThread(() -> {
                        editNome.setText(json.optString("nome", ""));
                        editEmail.setText(json.optString("email", ""));
                        editTelefone.setText(json.optString("telefone", ""));
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(ClientePainelEdit.this, "Erro ao processar resposta", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(ClientePainelEdit.this, "Erro ao carregar cliente", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void salvarCliente() {
        try {
            JSONObject body = new JSONObject();
            body.put("nome", editNome.getText().toString());
            body.put("email", editEmail.getText().toString());
            body.put("telefone", editTelefone.getText().toString());

            // ⚠️ aqui depois adicionamos as residências em um JSONArray

            String url = "clientes/" + clienteId;

            ApiHelper.put(this, url, body.toString(), new ApiHelper.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        Toast.makeText(ClientePainelEdit.this, "Cliente atualizado!", Toast.LENGTH_SHORT).show();
                        finish(); // volta para tela anterior
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() ->
                            Toast.makeText(ClientePainelEdit.this, "Erro ao atualizar cliente", Toast.LENGTH_SHORT).show()
                    );
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao montar requisição", Toast.LENGTH_SHORT).show();
        }
    }
}
