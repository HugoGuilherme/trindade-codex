package com.hugo.myapplication.pages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hugo.myapplication.R;
import com.hugo.myapplication.pages.agenda.AgendaAdapter;
import com.hugo.myapplication.pages.agenda.AgendaItem;

import java.util.ArrayList;
import java.util.List;

public class AgendaActivity extends AppCompatActivity {

    private RecyclerView recyclerAgenda;
    private AgendaAdapter adapter;
    private List<AgendaItem> listaMock;

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
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        // RecyclerView
        recyclerAgenda = findViewById(R.id.agendaRecyclerView);
        recyclerAgenda.setLayoutManager(new LinearLayoutManager(this));

        // Mock de dados
        listaMock = new ArrayList<>();
        listaMock.add(new AgendaItem("09:00", "Instalação elétrica", "Cliente: ACME Corp - Rua X, 123"));
        listaMock.add(new AgendaItem("11:00", "Revisão de sistema", "Cliente: João Silva - Av. Central, 45"));
        listaMock.add(new AgendaItem("14:00", "Troca de disjuntores", "Cliente: Maria Souza - Rua das Flores, 89"));

        adapter = new AgendaAdapter(listaMock);
        recyclerAgenda.setAdapter(adapter);

        // CalendarView
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // aqui futuramente você chama o backend
                // por enquanto só limpa e adiciona mock do dia
                listaMock.clear();
                listaMock.add(new AgendaItem("10:00",
                        "Visita técnica",
                        "Cliente de teste em " + dayOfMonth + "/" + (month+1) + "/" + year));
                adapter.notifyDataSetChanged();
            }
        });

        // FAB (adicionar nova tarefa)
        FloatingActionButton fab = findViewById(R.id.addFab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(AgendaActivity.this, FormularioAgendaActivity.class);
            startActivity(intent);
        });
    }
}