package com.hugo.myapplication.pages.agenda;

import android.content.Context;

import com.hugo.myapplication.utils.ApiHelper;

public class AgendaService {

    public static void listar(Context context, ApiHelper.ApiCallback callback) {
        ApiHelper.get(context, "agendas", callback);
    }

    public static void listarPorData(Context context, String data, ApiHelper.ApiCallback callback) {
        ApiHelper.get(context, "agendas?data=" + data, callback);
    }

    public static void buscarPorId(Context context, Long id, ApiHelper.ApiCallback callback) {
        ApiHelper.get(context, "agendas/" + id, callback);
    }

    public static void criar(Context context, String jsonAgenda, ApiHelper.ApiCallback callback) {
        ApiHelper.post(context, "agendas", jsonAgenda, callback);
    }

    public static void atualizar(Context context, Long id, String jsonAgenda, ApiHelper.ApiCallback callback) {
        ApiHelper.put(context, "agendas/" + id, jsonAgenda, callback);
    }

    public static void deletar(Context context, Long id, ApiHelper.ApiCallback callback) {
        ApiHelper.delete(context, "agendas/" + id, callback);
    }
}