package com.hugo.myapplication.pages;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.cliente.ClientListActivity;
import com.hugo.myapplication.pages.LimpezaPainel.LimpezaPainelActivity;

import android.content.Intent;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;
import android.content.SharedPreferences;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
    protected ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupToolbarAndNavigation(int layoutResId, boolean showBackButton) {
        setContentView(layoutResId);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            if (showBackButton) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Ativa o botão de voltar
                getSupportActionBar().setHomeButtonEnabled(true);
                toolbar.setNavigationOnClickListener(v -> finish());
            }
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        if (drawerLayout != null && toolbar != null) {
            toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                Intent intent = null;

                if (id == R.id.nav_home) {
                    startActivity(new Intent(this, HomeActivity.class));
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(this, ProfileActivity.class));
                } else if (id == R.id.nav_clientes) {
                    startActivity(new Intent(this, ClientListActivity.class));
                } else if (id == R.id.nav_agendamentos) {
                    startActivity(new Intent(this, AgendamentosMenuActivity.class));
                } else if (id == R.id.nav_agenda) {
                    startActivity(new Intent(this, AgendaActivity.class));
                } else if (id == R.id.nav_logout) {
                    logout();
                }
                if (intent != null) {
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }

                drawerLayout.closeDrawers(); // Fecha o menu após a seleção
                return true;
            });
        }
    }


    protected void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Limpa os dados do usuário
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showPopupMenu(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(this, anchorView);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_agendamentos, popupMenu.getMenu());

        // Lidar com cliques nos itens do PopupMenu
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_limpeza_painel) {
                startActivity(new Intent(this, LimpezaPainelActivity.class));
            } else if (item.getItemId() == R.id.nav_outro_item) {
                Toast.makeText(this, "Outro item selecionado!", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        popupMenu.show();
    }

}
