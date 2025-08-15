package com.hugo.myapplication.pages.cliente;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.BaseActivity;
import com.hugo.myapplication.pages.cliente.fragment.ClienteAgendamentosFragment;
import com.hugo.myapplication.pages.cliente.fragment.ClientePerfilFragment;
import com.hugo.myapplication.pages.cliente.fragment.ClienteServicosFragment;

public class ClienteArea extends BaseActivity {

    private TextView tvClienteNome, tvClienteEndereco, tvClienteTelefone;
    private ImageView btnWhatsApp;
    private String telefoneCliente;
    private Long userId;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.client_area_activity, true);

        // Encontrando os elementos do layout
        tvClienteNome = findViewById(R.id.tvClienteNome);
        tvClienteEndereco = findViewById(R.id.tvClienteEndereco);
        tvClienteTelefone = findViewById(R.id.tvClienteTelefone);
        btnWhatsApp = findViewById(R.id.btnWhatsApp);

        // Pegando os dados do Intent
        String nomeCliente = getIntent().getStringExtra("nome");
        String enderecoCliente = getIntent().getStringExtra("endereco");
        telefoneCliente = getIntent().getStringExtra("telefone");
        userId = getIntent().getLongExtra("id", -1);
        Log.d("ClienteArea", "✔ userId recebido: " + userId);

        // Setando os valores recebidos
        tvClienteNome.setText(nomeCliente != null ? nomeCliente : "Nome não disponível");
        tvClienteEndereco.setText(enderecoCliente != null ? enderecoCliente : "Endereço não disponível");
        tvClienteTelefone.setText(telefoneCliente != null ? telefoneCliente : "Telefone não disponível");

        // Ação do botão do WhatsApp
        btnWhatsApp.setOnClickListener(v -> abrirWhatsApp());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Configurar o clique nos itens do BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId(); // Obtém o ID do item clicado

            if (id == R.id.nav_agendamento_cliente) {
                carregarFragmentoAgendamentos();
                return true;
            } else if (id == R.id.nav_servicos_cliente) {
                abrirFragmento(new ClienteServicosFragment());
                return true;
            } else if (id == R.id.nav_perfil_cliente) {
                abrirFragmento(new ClientePerfilFragment());
                return true;
            }
            return false;
        });

        // Carregar fragmento com lista de agendamentos
        carregarFragmentoAgendamentos();
    }

    private void abrirWhatsApp() {
        if (telefoneCliente != null && !telefoneCliente.isEmpty()) {
            String url = "https://wa.me/+55" + telefoneCliente.replaceAll("[^0-9]", "");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Número de telefone não disponível", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarFragmentoAgendamentos() {
        ClienteAgendamentosFragment fragment = new ClienteAgendamentosFragment();
        Bundle args = new Bundle();
        args.putLong("clienteId", getIntent().getLongExtra("id", -1));
        args.putLong("userId", userId);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.commit();
    }


    private void abrirFragmento(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null); // Para permitir voltar ao fragmento anterior
        transaction.commit();
    }
}
