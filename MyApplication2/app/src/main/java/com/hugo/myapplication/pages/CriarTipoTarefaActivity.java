package com.hugo.myapplication.pages;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hugo.myapplication.R;
import com.hugo.myapplication.utils.ApiHelper;

import org.json.JSONObject;

public class CriarTipoTarefaActivity extends BaseActivity {

    private EditText nomeEdit, descricaoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.tipo_tarefa_create_activity, true);

        // Ajuste de insets (se tiver id="main" no root do layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Campos do formulário
        nomeEdit = findViewById(R.id.nomeEdit);
        descricaoEdit = findViewById(R.id.descricaoEdit);
        Button btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(v -> salvarTipoTarefa());
    }

    private void salvarTipoTarefa() {
        String nome = nomeEdit.getText().toString().trim();
        String descricao = descricaoEdit.getText().toString().trim();

        if (nome.isEmpty()) {
            Toast.makeText(this, "Digite o nome", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("nome", nome);
            json.put("descricao", descricao);

            // POST para /api/tipos-tarefa
            ApiHelper.post(this, "tipos-tarefa", json.toString(), new ApiHelper.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        Toast.makeText(CriarTipoTarefaActivity.this, "Tipo de Tarefa criado!", Toast.LENGTH_SHORT).show();
                        finish(); // volta para tela de listagem
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() ->
                            Toast.makeText(CriarTipoTarefaActivity.this, "Erro: " + errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao montar JSON", Toast.LENGTH_SHORT).show();
        }
    }
}