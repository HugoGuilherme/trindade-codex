package com.hugo.myapplication.pages;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.hugo.myapplication.R;

public class FormularioAgendaActivity extends AppCompatActivity {

    private TextInputEditText inputNomeTarefa, inputCliente, inputColaborador, inputLocal, inputData, inputHora;
    private Button btnSalvar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_agenda_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        inputNomeTarefa = findViewById(R.id.inputNomeTarefa);
        inputCliente = findViewById(R.id.inputCliente);
        inputColaborador = findViewById(R.id.inputColaborador);
        inputLocal = findViewById(R.id.inputLocal);
        inputData = findViewById(R.id.inputData);
        inputHora = findViewById(R.id.inputHora);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(v -> {
            String nome = getText(inputNomeTarefa);
            String cliente = getText(inputCliente);
            String colaborador = getText(inputColaborador);
            String local = getText(inputLocal);
            String data = getText(inputData);
            String hora = getText(inputHora);

            // Por enquanto apenas mostra um Toast (mock)
            Toast.makeText(this,
                    "Tarefa: " + nome + "\nCliente: " + cliente +
                            "\nColaborador: " + colaborador +
                            "\nLocal: " + local +
                            "\nData: " + data +
                            "\nHora: " + hora,
                    Toast.LENGTH_LONG).show();

            finish(); // fecha tela e volta
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString() : "";
    }
}
