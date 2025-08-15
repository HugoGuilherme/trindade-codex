package com.hugo.myapplication.pages;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.hugo.myapplication.R;

public class HomeActivity extends BaseActivity {

    private TextView tvWelcome, tvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.home_activity, false);

        // Inicializa os componentes da tela
        tvWelcome = findViewById(R.id.tvWelcome);
        tvMessage = findViewById(R.id.tvMessage);


        // Recuperar o nome do usuário (Exemplo: armazenado no SharedPreferences)
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("perfilNome", "Usuário");

        // Exibir nome do usuário na mensagem de boas-vindas
        tvWelcome.setText("Olá, " + username + "!");
    }
}
