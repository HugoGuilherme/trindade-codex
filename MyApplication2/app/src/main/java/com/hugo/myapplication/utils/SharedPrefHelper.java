package com.hugo.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    private static final String PREF_NAME = "AppPrefs";
    private static final String KEY_CLIENTE_ID = "clienteId";

    public static void salvarClienteId(Context context, Long clienteId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(KEY_CLIENTE_ID, clienteId);
        editor.apply();
    }

    public static Long obterClienteId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getLong(KEY_CLIENTE_ID, -1L); // Retorna -1L se não houver valor salvo
    }
}
