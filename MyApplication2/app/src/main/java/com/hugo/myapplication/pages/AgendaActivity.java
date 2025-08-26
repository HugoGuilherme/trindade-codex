package com.hugo.myapplication.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.agenda.AgendaAdapter;
import com.hugo.myapplication.pages.agenda.AgendaItem;
import com.hugo.myapplication.utils.ApiHelper;
import com.hugo.myapplication.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AgendaActivity extends BaseActivity {

    private RecyclerView recyclerAgenda;
    private AgendaAdapter adapter;
    private List<AgendaItem> listaAgenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.agenda_activity,  false);


        // RecyclerView
        recyclerAgenda = findViewById(R.id.agendaRecyclerView);
        recyclerAgenda.setLayoutManager(new LinearLayoutManager(this));
        listaAgenda = new ArrayList<>();
        adapter = new AgendaAdapter(listaAgenda);
        recyclerAgenda.setAdapter(adapter);
        String hojeISO = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        // Carrega agendas do dia atual
        long hojeMillis = System.currentTimeMillis();
        CalendarView calendarView = findViewById(R.id.calendarView);
        carregarAgendas(hojeISO); // função que formata yyyy-MM-dd

        // CalendarView → recarrega agendas ao trocar o dia
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String dataSelecionada = String.format("%04d-%02d-%02d", year, (month + 1), dayOfMonth);
            carregarAgendas(dataSelecionada);
        });

        // FAB (nova tarefa)
        FloatingActionButton fab = findViewById(R.id.addFab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(AgendaActivity.this, FormularioAgendaActivity.class);
            startActivity(intent);
        });
    }

    private void carregarAgendas(String data) {
        ApiHelper.get(this, "agendas?data=" + data, new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    listaAgenda.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String titulo = obj.optString("titulo", "");
                        JSONArray tarefas = obj.optJSONArray("tarefas");

                        if (tarefas != null && tarefas.length() > 0) {
                            // você pode iterar todas, mas por enquanto pega a primeira
                            JSONObject t = tarefas.getJSONObject(0);

                            String status = t.optString("status", "");
                            String dataHora = t.optString("dataHora", "");

                            // formata a data/hora
                            String dataHoraFormatada = DateUtils.formatDateTime(dataHora);

                            String clienteNome = t.optString("clienteNome", "");
                            String residenciaEndereco = t.optString("residenciaEndereco", "");
                            String tipoTarefaNome = t.optString("tipoTarefaNome", "-");


                            // lista de colaboradores
                            List<String> colaboradores = new ArrayList<>();
                            JSONArray colaboradoresArray = t.optJSONArray("colaboradoresNomes");
                            if (colaboradoresArray != null) {
                                for (int j = 0; j < colaboradoresArray.length(); j++) {
                                    colaboradores.add(colaboradoresArray.optString(j));
                                }
                            }

                            // monta o objeto para o adapter
                            AgendaItem item = new AgendaItem(
                                    titulo,
                                    status,
                                    dataHoraFormatada,
                                    colaboradores,
                                    residenciaEndereco,
                                    clienteNome,
                                    tipoTarefaNome // ✅ novo campo
                            );

                            listaAgenda.add(item);
                        }
                    }

                    runOnUiThread(() -> adapter.notifyDataSetChanged());

                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(AgendaActivity.this, "Erro ao processar dados", Toast.LENGTH_SHORT).show()
                    );
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(AgendaActivity.this, "Erro: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

}
