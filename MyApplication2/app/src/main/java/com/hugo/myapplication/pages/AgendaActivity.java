package com.hugo.myapplication.pages;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hugo.myapplication.R;

public class AgendaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agenda");
        }

        // Clique no botão de voltar → abre HomeActivity
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(AgendaActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // fecha a AgendaActivity para não acumular na pilha
        });
    }
}