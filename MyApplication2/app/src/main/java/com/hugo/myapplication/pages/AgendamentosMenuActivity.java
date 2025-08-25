package com.hugo.myapplication.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.LimpezaPainel.LimpezaPainelActivity;

public class AgendamentosMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_agendamentos_activity);

        // Configurar Toolbar com botão de voltar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agendamentos");
        }

        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(android.R.color.white));
        }

        // Configurar clique no botão de voltar
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Configurar cliques nos itens do menu
        CardView itemLimpezaPainel = findViewById(R.id.item_limpeza_painel);
        CardView itemOutro = findViewById(R.id.item_outro);

        itemLimpezaPainel.setOnClickListener(v -> {
            startActivity(new Intent(this, LimpezaPainelActivity.class));
        });

        itemOutro.setOnClickListener(v -> {
            Toast.makeText(this, "Outro item selecionado!", Toast.LENGTH_SHORT).show();
        });
    }

}
