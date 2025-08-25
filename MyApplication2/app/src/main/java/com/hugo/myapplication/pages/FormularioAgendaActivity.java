package com.hugo.myapplication.pages;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.hugo.myapplication.R;
import com.hugo.myapplication.utils.ApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormularioAgendaActivity extends AppCompatActivity {

    private TextInputEditText inputNomeTarefa, inputData, inputHora;
    private AutoCompleteTextView spinnerClientes, spinnerResidencias, spinnerTiposTarefa;
    private MultiAutoCompleteTextView spinnerColaboradores;
    private Button btnSalvar;

    // Listas e mapas
    private List<String> listaClientes = new ArrayList<>();
    private List<String> listaResidencias = new ArrayList<>();
    private List<String> listaColaboradores = new ArrayList<>();
    private List<String> listaTipos = new ArrayList<>();

    private Map<String, Integer> mapClientes = new HashMap<>();
    private Map<String, Integer> mapResidencias = new HashMap<>();
    private Map<String, Integer> mapColaboradores = new HashMap<>();
    private Map<String, Integer> mapTipos = new HashMap<>();

    private Integer clienteSelecionadoId = null;
    private Integer residenciaSelecionadaId = null;
    private Integer tipoSelecionadoId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_agenda_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Nova tarefa");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Campos
        inputNomeTarefa = findViewById(R.id.inputNomeTarefa);
        spinnerClientes = findViewById(R.id.spinner_clientes);
        spinnerResidencias = findViewById(R.id.spinner_residencias);
        spinnerColaboradores = findViewById(R.id.spinner_colaboradores);
        spinnerTiposTarefa = findViewById(R.id.spinner_tipos_tarefa);
        inputData = findViewById(R.id.inputData);
        inputHora = findViewById(R.id.inputHora);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Carregar listas
        carregarClientes();
        carregarColaboradores();
        carregarTiposTarefa();

        // Picker Data
        inputData.setFocusable(false);
        inputData.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, monthOfYear, dayOfMonth) ->
                            inputData.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year)),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Picker Hora
        inputHora.setFocusable(false);
        inputHora.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute1) ->
                            inputHora.setText(String.format("%02d:%02d", hourOfDay, minute1)),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true
            );
            timePickerDialog.show();
        });

        // Botão salvar
        btnSalvar.setOnClickListener(v -> salvarAgenda());
    }

    private void salvarAgenda() {
        try {
            String nome = getText(inputNomeTarefa);
            String data = getText(inputData); // dd/MM/yyyy
            String hora = getText(inputHora); // HH:mm

            // Validações
            if (nome.isEmpty()) {
                Toast.makeText(this, "Digite o nome da tarefa", Toast.LENGTH_SHORT).show();
                return;
            }
            if (clienteSelecionadoId == null) {
                Toast.makeText(this, "Selecione um cliente", Toast.LENGTH_SHORT).show();
                return;
            }
            if (residenciaSelecionadaId == null) {
                Toast.makeText(this, "Selecione uma residência", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tipoSelecionadoId == null) {
                Toast.makeText(this, "Selecione um tipo de tarefa", Toast.LENGTH_SHORT).show();
                return;
            }
            if (data.isEmpty() || hora.isEmpty()) {
                Toast.makeText(this, "Selecione data e hora", Toast.LENGTH_SHORT).show();
                return;
            }

            // Converter para ISO
            String[] dataParts = data.split("/");
            String dataISO = dataParts[2] + "-" + dataParts[1] + "-" + dataParts[0];
            String dataHoraISO = dataISO + "T" + hora + ":00";

            // Verificar se data/hora é futura
            Calendar agora = Calendar.getInstance();
            Calendar dataSelecionada = Calendar.getInstance();
            dataSelecionada.set(
                    Integer.parseInt(dataParts[2]),
                    Integer.parseInt(dataParts[1]) - 1,
                    Integer.parseInt(dataParts[0]),
                    Integer.parseInt(hora.split(":")[0]),
                    Integer.parseInt(hora.split(":")[1])
            );
            if (dataSelecionada.before(agora)) {
                Toast.makeText(this, "A data/hora deve ser no futuro", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject agendaJson = new JSONObject();
            agendaJson.put("titulo", nome);
            agendaJson.put("descricao", "");
            agendaJson.put("dataInicio", dataHoraISO);
            agendaJson.put("dataFim", dataHoraISO);
            agendaJson.put("userId", 3); // TODO: substituir pelo usuário logado

            JSONArray tarefasArray = new JSONArray();
            JSONObject tarefa = new JSONObject();
            tarefa.put("descricao", nome);
            tarefa.put("dataHora", dataHoraISO);
            tarefa.put("status", "PENDENTE");
            tarefa.put("clienteId", clienteSelecionadoId);
            tarefa.put("residenciaId", residenciaSelecionadaId);
            tarefa.put("tipoTarefaId", tipoSelecionadoId);

            // Colaboradores
            JSONArray colaboradoresIds = new JSONArray();
            String[] nomesSelecionados = spinnerColaboradores.getText().toString().split(",");
            for (String nomeSel : nomesSelecionados) {
                nomeSel = nomeSel.trim();
                if (mapColaboradores.containsKey(nomeSel)) {
                    colaboradoresIds.put(mapColaboradores.get(nomeSel));
                }
            }
            if (colaboradoresIds.length() == 0) {
                Toast.makeText(this, "Selecione pelo menos 1 colaborador", Toast.LENGTH_SHORT).show();
                return;
            }
            tarefa.put("colaboradoresIds", colaboradoresIds);

            tarefasArray.put(tarefa);
            agendaJson.put("tarefas", tarefasArray);

            // Desabilita botão para evitar cliques múltiplos
            btnSalvar.setEnabled(false);

            ApiHelper.post(this, "agendas", agendaJson.toString(), new ApiHelper.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> {
                        btnSalvar.setEnabled(true);
                        Toast.makeText(FormularioAgendaActivity.this, "Agenda salva com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> {
                        btnSalvar.setEnabled(true);
                        Toast.makeText(FormularioAgendaActivity.this, "Erro ao salvar agenda: " + errorMessage, Toast.LENGTH_LONG).show();
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            btnSalvar.setEnabled(true);
            Toast.makeText(this, "Erro ao montar JSON da agenda!", Toast.LENGTH_LONG).show();
        }
    }


    private void carregarClientes() {
        ApiHelper.get(this, "clientes", new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    listaClientes.clear();
                    mapClientes.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        int id = obj.optInt("id");
                        String nome = obj.optString("nome");
                        listaClientes.add(nome);
                        mapClientes.put(nome, id);
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapterClientes = new ArrayAdapter<>(
                                FormularioAgendaActivity.this,
                                android.R.layout.simple_dropdown_item_1line,
                                listaClientes
                        );
                        spinnerClientes.setAdapter(adapterClientes);
                        spinnerClientes.setOnClickListener(v -> spinnerClientes.showDropDown());

                        spinnerClientes.setOnItemClickListener((parent, view, position, id) -> {
                            String nomeCliente = listaClientes.get(position);
                            clienteSelecionadoId = mapClientes.get(nomeCliente);
                            carregarResidencias(clienteSelecionadoId);
                        });
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(FormularioAgendaActivity.this, "Erro API clientes: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void carregarResidencias(Integer clienteId) {
        ApiHelper.get(this, "clientes/" + clienteId + "/residencias", new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    listaResidencias.clear();
                    mapResidencias.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        int id = obj.optInt("id");
                        String endereco = obj.optString("endereco");
                        listaResidencias.add(endereco);
                        mapResidencias.put(endereco, id);
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapterResidencias = new ArrayAdapter<>(
                                FormularioAgendaActivity.this,
                                android.R.layout.simple_dropdown_item_1line,
                                listaResidencias
                        );
                        spinnerResidencias.setAdapter(adapterResidencias);
                        spinnerResidencias.setOnClickListener(v -> spinnerResidencias.showDropDown());

                        spinnerResidencias.setOnItemClickListener((parent, view, position, id) -> {
                            String endereco = listaResidencias.get(position);
                            residenciaSelecionadaId = mapResidencias.get(endereco);
                        });
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(FormularioAgendaActivity.this, "Erro API residências: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void carregarColaboradores() {
        ApiHelper.get(this, "user", new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    listaColaboradores.clear();
                    mapColaboradores.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        int id = obj.optInt("id");
                        String nome = obj.optString("fullname");
                        listaColaboradores.add(nome);
                        mapColaboradores.put(nome, id);
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapterColab = new ArrayAdapter<>(
                                FormularioAgendaActivity.this,
                                android.R.layout.simple_dropdown_item_1line,
                                listaColaboradores
                        );
                        spinnerColaboradores.setAdapter(adapterColab);
                        spinnerColaboradores.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                        spinnerColaboradores.setOnClickListener(v -> spinnerColaboradores.showDropDown());
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(FormularioAgendaActivity.this, "Erro API colaboradores: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void carregarTiposTarefa() {
        ApiHelper.get(this, "tipos-tarefa", new ApiHelper.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    listaTipos.clear();
                    mapTipos.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        int id = obj.optInt("id");
                        String nome = obj.optString("nome");
                        listaTipos.add(nome);
                        mapTipos.put(nome, id);
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(
                                FormularioAgendaActivity.this,
                                android.R.layout.simple_dropdown_item_1line,
                                listaTipos
                        );
                        spinnerTiposTarefa.setAdapter(adapterTipos);
                        spinnerTiposTarefa.setOnClickListener(v -> spinnerTiposTarefa.showDropDown());

                        spinnerTiposTarefa.setOnItemClickListener((parent, view, position, id) -> {
                            String nomeTipo = listaTipos.get(position);
                            tipoSelecionadoId = mapTipos.get(nomeTipo);
                        });
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(FormularioAgendaActivity.this, "Erro API tipos: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
