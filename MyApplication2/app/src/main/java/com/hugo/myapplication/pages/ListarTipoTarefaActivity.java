package com.hugo.myapplication.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.cliente.dtos.TipoTarefa;
import com.hugo.myapplication.pages.tipoTarefa.adapter.TipoTarefaAdapter;
import com.hugo.myapplication.utils.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListarTipoTarefaActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TipoTarefaAdapter adapter;
    private List<TipoTarefa> tipos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.listar_tipo_tarefa_activity, false);

        recyclerView = findViewById(R.id.recyclerTipos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TipoTarefaAdapter(tipos, this::excluirTipo);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.addFab);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, CriarTipoTarefaActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarTipos();
    }

    private void carregarTipos() {
        ApiHelper.get(this, "tipos-tarefa", new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    tipos.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        TipoTarefa tipo = new TipoTarefa(
                                obj.getLong("id"),
                                obj.getString("nome"),
                                obj.optString("descricao", "")
                        );
                        tipos.add(tipo);
                    }
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(ListarTipoTarefaActivity.this, "Erro ao processar dados", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(ListarTipoTarefaActivity.this, "Erro: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void excluirTipo(TipoTarefa tipo) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Excluir Tipo de Tarefa")
                .setMessage("Tem certeza que deseja excluir \"" + tipo.getNome() + "\"?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    ApiHelper.delete(this, "tipos-tarefa/" + tipo.getId(), new ApiHelper.ApiCallback() {
                        @Override
                        public void onSuccess(String response) {
                            runOnUiThread(() -> {
                                Toast.makeText(ListarTipoTarefaActivity.this, "Excluído!", Toast.LENGTH_SHORT).show();
                                recreate(); // 👈 recria a tela e chama onCreate/onResume de novo
                            });
                        }


                        @Override
                        public void onError(String errorMessage) {
                            runOnUiThread(() ->
                                    Toast.makeText(ListarTipoTarefaActivity.this, "Erro ao excluir", Toast.LENGTH_SHORT).show()
                            );
                        }
                    });
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }


}