package com.hugo.myapplication.pages.cliente.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hugo.myapplication.R;
import com.hugo.myapplication.models.Agendamento;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ClienteAgendamentoAdapter extends RecyclerView.Adapter<ClienteAgendamentoAdapter.ViewHolder> {

    private List<Agendamento> listaAgendamentos;

    public ClienteAgendamentoAdapter(List<Agendamento> listaAgendamentos) {
        this.listaAgendamentos = listaAgendamentos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_agendamento_limpeza_painel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Agendamento agendamento = listaAgendamentos.get(position);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataUltima = LocalDate.parse(agendamento.getDataUltimaLimpeza(), formatter);
        LocalDate dataProximaLimpeza = dataUltima.plusMonths(agendamento.getMeses());
        long diasRestantes = ChronoUnit.DAYS.between(dataAtual, dataProximaLimpeza);


        holder.tvDataProximaLimpeza.setText("Próxima Limpeza: " + agendamento.getDataProximaLimpeza());
        holder.tvDiasRestantes.setText(String.valueOf(diasRestantes));
    }

    @Override
    public int getItemCount() {
        return listaAgendamentos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTelefoneAgendamentoCliente, tvEnderecoCliente, tvDataInstalacao, tvDataUltimaLimpeza, tvDataProximaLimpeza, tvDiasRestantes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTelefoneAgendamentoCliente = itemView.findViewById(R.id.tvTelefoneAgendamentoCliente);
            tvEnderecoCliente = itemView.findViewById(R.id.tvEnderecoCliente);
            tvDataProximaLimpeza = itemView.findViewById(R.id.tv_next_cleaning);
            tvDiasRestantes = itemView.findViewById(R.id.diasRestantes);
        }
    }
}
