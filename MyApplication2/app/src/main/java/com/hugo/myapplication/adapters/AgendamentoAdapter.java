package com.hugo.myapplication.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.hugo.myapplication.pages.LimpezaPainel.actions.AgendamentoLimpezaPainelEdit;
import com.hugo.myapplication.utils.DateUtils;
import com.hugo.myapplication.R;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoAdapter.ViewHolder> {

    private final List<JSONObject> agendamentos;

    // ✅ Agora recebe uma List<JSONObject> diretamente
    public AgendamentoAdapter(List<JSONObject> listaAgendamentos) {
        this.agendamentos = new ArrayList<>(listaAgendamentos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agendamento_limpeza_painel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject agendamento = agendamentos.get(position);
            String dataInstalacao = agendamento.optString("dataInstalacao", "");
            String dataUltimaInstalacao = agendamento.optString("dataUltimaLimpeza", "");
            int meses = agendamento.optInt("meses", 6);

            // Formatar e calcular datas corretamente
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataAtual = LocalDate.now();
            LocalDate dataUltima = LocalDate.parse(dataUltimaInstalacao, formatter);
            LocalDate dataProximaLimpeza = dataUltima.plusMonths(meses);

            // Calculando a diferença de dias corretamente
            long diasRestantes = ChronoUnit.DAYS.between(dataAtual, dataProximaLimpeza);

            // Atualizando os TextViews
            holder.tvNome.setText(agendamento.optString("nome", "Nome não disponível"));
            holder.tvEmail.setText(agendamento.optString("email", "Email não disponível"));
            holder.tvEndereco.setText(agendamento.optString("endereco", "Endereço não disponível"));
            holder.tvDataInstalacao.setText("Data de instalação: " + DateUtils.formatDate(dataInstalacao));
            holder.tvDataUltimaInstalacao.setText("Última limpeza: " + DateUtils.formatDate(dataUltimaInstalacao));
            holder.tvNextCleaning.setText("Próxima Limpeza: " +  DateUtils.formatDate(dataProximaLimpeza.toString()));
            holder.tvDiasRestantes.setText(String.valueOf(diasRestantes));

            // Clique longo para abrir PopupMenu
            holder.itemView.setOnLongClickListener(v -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.menu_opcoes, popup.getMenu());

                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.menu_editar) {
                        editarAgendamento(v.getContext(), position);
                        return true;
                    } else if (item.getItemId() == R.id.menu_excluir) {
                        confirmarExclusao(v.getContext(), position);
                        return true;
                    }
                    return false;
                });

                popup.show();
                return true;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return agendamentos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvEmail, tvEndereco, tvDataInstalacao, tvDataUltimaInstalacao, tvNextCleaning, tvDiasRestantes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvTitle);
            tvEmail = itemView.findViewById(R.id.tvTelefoneAgendamentoCliente);
            tvEndereco = itemView.findViewById(R.id.tvEnderecoCliente);
            tvDataInstalacao = itemView.findViewById(R.id.tvDataInstalacao);
            tvDataUltimaInstalacao = itemView.findViewById(R.id.tvDataUltimaInstalacao);
            tvNextCleaning = itemView.findViewById(R.id.tv_next_cleaning);
            tvDiasRestantes = itemView.findViewById(R.id.diasRestantes);
        }
    }

    // ✅ Método para atualizar os dados no Adapter corretamente
    public void updateData(List<JSONObject> newData) {
        this.agendamentos.clear();
        this.agendamentos.addAll(newData);
        notifyDataSetChanged();
    }

    private void editarAgendamento(Context context, int position) {
        Log.d("DEBUG_INTENT", "🔍 " + agendamentos );
        JSONObject agendamento = agendamentos.get(position);

        Intent intent = new Intent(context, AgendamentoLimpezaPainelEdit.class);
        intent.putExtra("agendamento", agendamento.toString()); // ✅ Passando os dados corretamente
        context.startActivity(intent);

        Toast.makeText(context, "Editar: " + agendamento.optString("nome"), Toast.LENGTH_SHORT).show();
    }

    private void confirmarExclusao(Context context, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmar exclusão")
                .setMessage("Tem certeza que deseja excluir este agendamento?")
                .setPositiveButton("Sim", (dialog, which) -> excluirAgendamento(context, position))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void excluirAgendamento(Context context, int position) {
        try {
            JSONObject agendamento = agendamentos.get(position);
            int id = agendamento.optInt("id", -1);

            if (id == -1) {
                Toast.makeText(context, "Erro: ID inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            String serverIp = context.getString(R.string.server_ip);
            String url = "http://" + serverIp + ":8080/api/agendamento-limpeza/" + id;

            SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("authToken", null);

            if (token == null) {
                Toast.makeText(context, "Erro: Token não encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                HttpURLConnection connection = null;
                try {
                    URL urlObj = new URL(url);
                    connection = (HttpURLConnection) urlObj.openConnection();
                    connection.setRequestMethod("DELETE");
                    connection.setRequestProperty("Authorization", "Bearer " + token);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.connect();

                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200 || responseCode == 204) {
                        ((Activity) context).runOnUiThread(() -> {
                            agendamentos.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        ((Activity) context).runOnUiThread(() ->
                                Toast.makeText(context, "Falha ao excluir (Erro " + responseCode + ")", Toast.LENGTH_SHORT).show()
                        );
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Erro ao excluir", Toast.LENGTH_SHORT).show()
                    );
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro ao excluir", Toast.LENGTH_SHORT).show();
        }
    }
}
