package com.hugo.myapplication.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Date;


public class AgendamentoUtils {

    public static JSONArray ordenarAgendamentos(JSONArray agendamentos) {
        List<JSONObject> lista = new ArrayList<>();

        // Converte JSONArray para List<JSONObject>
        for (int i = 0; i < agendamentos.length(); i++) {
            lista.add(agendamentos.optJSONObject(i));
        }

        // Ordena pela próxima limpeza mais próxima da data atual
        Collections.sort(lista, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                try {
                    String dataUltimaA = a.optString("dataUltimaLimpeza", "");
                    String dataUltimaB = b.optString("dataUltimaLimpeza", "");
                    int mesesA = a.optInt("meses", 6);
                    int mesesB = b.optInt("meses", 6);

                    Date proximaA = DateUtils.calcularProximaLimpezaDate(dataUltimaA, mesesA);
                    Date proximaB = DateUtils.calcularProximaLimpezaDate(dataUltimaB, mesesB);

                    return proximaA.compareTo(proximaB);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        // Converte List<JSONObject> de volta para JSONArray
        return new JSONArray(lista);
    }
}
