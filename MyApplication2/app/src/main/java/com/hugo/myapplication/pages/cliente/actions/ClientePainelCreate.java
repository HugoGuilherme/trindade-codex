package com.hugo.myapplication.pages.cliente.actions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.BaseActivity;
import com.hugo.myapplication.pages.cliente.residencia.ResidenciaPainelCreate;

import org.json.JSONObject;


public class ClientePainelCreate extends BaseActivity {

    private EditText nomeText, emailText, telefoneText, dataNascimentoText, enderecoText;
    private Button botaoProximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔥 Certifique-se de que este é o layout correto
        setupToolbarAndNavigation(R.layout.cliente_painel_create_activity, true);

        // 🔥 Verifique se os IDs correspondem ao XML
        nomeText = findViewById(R.id.nomeText);
        emailText = findViewById(R.id.emailText);
        telefoneText = findViewById(R.id.telefoneText);
        dataNascimentoText = findViewById(R.id.dataNascimentoText);
        enderecoText = findViewById(R.id.enderecoText);
        botaoProximo = findViewById(R.id.botaoAvancar);

        // 🔥 Verifica se o botão foi encontrado antes de usar
        if (botaoProximo == null) {
            Log.e("ClientePainelCreate", "Erro: Botão não encontrado no layout!");
            Toast.makeText(this, "Erro no layout! Verifique o botão.", Toast.LENGTH_LONG).show();
            return;
        }

        botaoProximo.setOnClickListener(v -> abrirTelaDeResidencias());
    }

    private void abrirTelaDeResidencias() {
        try {
            JSONObject jsonCliente = new JSONObject();
            jsonCliente.put("nome", nomeText.getText().toString().trim());
            jsonCliente.put("email", emailText.getText().toString().trim());
            jsonCliente.put("telefone", telefoneText.getText().toString().trim());
            jsonCliente.put("dataNascimento", dataNascimentoText.getText().toString().trim());
            jsonCliente.put("endereco", enderecoText.getText().toString().trim());

            Intent intent = new Intent(ClientePainelCreate.this, ResidenciaPainelCreate.class);
            intent.putExtra("CLIENTE_JSON", jsonCliente.toString());
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Log.e("Cliente", "Erro ao criar JSON", e);
            Toast.makeText(this, "Erro ao criar JSON", Toast.LENGTH_SHORT).show();
        }
    }
}
