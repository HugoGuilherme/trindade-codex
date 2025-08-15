package com.hugo.myapplication.pages.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hugo.myapplication.R;
import com.hugo.myapplication.adapters.ClienteAdapter;
import com.hugo.myapplication.pages.BaseActivity;
import com.hugo.myapplication.pages.LimpezaPainel.LimpezaPainelActivity;
import com.hugo.myapplication.pages.LimpezaPainel.actions.AgendamentoLimpezaPainelCreate;
import com.hugo.myapplication.pages.cliente.actions.ClientePainelCreate;
import com.hugo.myapplication.utils.ApiHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ClientListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ClienteAdapter clienteAdapter;
    private List<Cliente> listaClientes = new ArrayList<>();
    private List<Cliente> listaFiltrada = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.client_list_activity, false);

        recyclerView = findViewById(R.id.recyclerViewClientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        clienteAdapter = new ClienteAdapter(listaFiltrada);
        recyclerView.setAdapter(clienteAdapter);


        FloatingActionButton botaoAdicionar = findViewById(R.id.adicionar_cliente);
        botaoAdicionar.setOnClickListener(view -> {
            Intent intent = new Intent(ClientListActivity.this, ClientePainelCreate.class);
            startActivity(intent);
        });

        carregarClientesDoBackend();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        // Configurar SearchView corretamente
        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setQueryHint("Pesquisar clientes...");
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarClientes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarClientes(newText);
                return false;
            }
        });

        return true;
    }

    private void carregarClientesDoBackend() {
        ApiHelper.get(this, "clientes", new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<Cliente> clientes = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonCliente = jsonArray.getJSONObject(i);
                        Cliente cliente = new Cliente(
                                jsonCliente.getLong("id"),
                                jsonCliente.getString("nome"),
                                jsonCliente.getString("email"),
                                jsonCliente.getString("telefone"),
                                jsonCliente.getString("endereco")
                        );
                        clientes.add(cliente);
                    }

                    runOnUiThread(() -> {
                        listaClientes.clear();
                        listaClientes.addAll(clientes);
                        listaFiltrada.clear();
                        listaFiltrada.addAll(clientes);
                        clienteAdapter.notifyDataSetChanged();
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {});
            }
        });
    }

    private void filtrarClientes(String query) {
        listaFiltrada.clear();

        if (query.isEmpty()) {
            listaFiltrada.addAll(listaClientes);
        } else {
            for (Cliente cliente : listaClientes) {
                if (cliente.getNome().toLowerCase().contains(query.toLowerCase())) {
                    listaFiltrada.add(cliente);
                }
            }
        }

        clienteAdapter.notifyDataSetChanged();
    }
}
