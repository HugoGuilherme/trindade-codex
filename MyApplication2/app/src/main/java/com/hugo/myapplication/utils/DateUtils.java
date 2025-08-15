package com.hugo.myapplication.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String INPUT_DATE_FORMAT = "yyyy-MM-dd"; // Formato esperado
    private static final String OUTPUT_DATE_FORMAT = "dd/MM/yyyy"; // Formato desejado
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Converte uma data no formato yyyy-MM-dd para dd/MM/yyyy.
     *
     * @param dateString Data no formato yyyy-MM-dd
     * @return Data formatada no formato dd/MM/yyyy ou "Data inválida" em caso de erro
     */
    public static String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "Data inválida";
        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.getDefault());

            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Data inválida";
        }
    }

    /**
     * Formata um objeto Date para a string dd/MM/yyyy.
     */
    public static String formatDate(Date date) {
        if (date == null) return "Data inválida";
        SimpleDateFormat sdf = new SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * Converte uma string de data yyyy-MM-dd para um objeto Date.
     */
    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calcula a próxima limpeza adicionando meses à data de instalação.
     */
    public static String calcularProximaLimpeza(String dataInstalacao, int meses) {
        Date data = parseDate(dataInstalacao);
        if (data == null) return "Data inválida";

        Calendar proximaLimpeza = Calendar.getInstance();
        proximaLimpeza.setTime(data);
        Calendar hoje = Calendar.getInstance();

        // Adiciona meses repetidamente até que a próxima limpeza seja no futuro
        while (!proximaLimpeza.after(hoje)) {
            proximaLimpeza.add(Calendar.MONTH, meses);
        }

        return formatDate(proximaLimpeza.getTime());
    }

    public static Date calcularProximaLimpezaDate(String dataUltimaLimpeza, int meses) {
        try {
            if (dataUltimaLimpeza == null || dataUltimaLimpeza.isEmpty()) {
                return new Date(); // Retorna a data atual caso não tenha última limpeza
            }

            Date dataUltima = sdf.parse(dataUltimaLimpeza);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataUltima);
            calendar.add(Calendar.MONTH, meses); // Adiciona os meses
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return new Date(); // Retorna a data atual em caso de erro
        }
    }
    public static String convertDateToISO(String dateStr) {
        try {
            // Formato de entrada (DD/MM/yyyy)
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            // Formato de saída (yyyy-MM-dd)
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date date = inputFormat.parse(dateStr); // Converte para Date
            return outputFormat.format(date); // Converte para String no novo formato
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Retorna null se der erro na conversão
        }
    }
    public static String convertISOToDate(String isoDate) {
        try {
            // Formato original (YYYY-MM-DD)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            // Formato desejado (DD/MM/YYYY)
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date date = inputFormat.parse(isoDate); // Converte para Date
            return outputFormat.format(date); // Retorna no novo formato
        } catch (ParseException e) {
            e.printStackTrace();
            return isoDate; // Se der erro, retorna a string original
        }
    }
}
