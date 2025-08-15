    package com.hugo.myapplication.utils;

    import android.content.Context;
    import android.content.SharedPreferences;
    import android.os.Handler;
    import android.os.Looper;
    import android.widget.Toast;

    import com.hugo.myapplication.R;

    import java.io.BufferedReader;
    import java.io.OutputStream;
    import java.io.InputStreamReader;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.nio.charset.StandardCharsets;

    public class ApiHelper {
        private static final String PREFS_NAME = "MyAppPrefs";

        // Método genérico para requisições HTTP
        public static void makeRequest(Context context, String endpointPath, String method, String jsonData, ApiCallback callback) {
            new Thread(() -> {
                try {
                    String baseUrl = "http://" + context.getString(com.hugo.myapplication.R.string.server_ip) + ":8080/api/";
                    String fullUrl = baseUrl + endpointPath;
                    URL url = new URL(fullUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod(method);
                    connection.setRequestProperty("Content-Type", "application/json");

                    // Recuperar token salvo no SharedPreferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    String token = sharedPreferences.getString("authToken", null);

                    if (token != null) {
                        connection.setRequestProperty("Authorization", "Bearer " + token);
                    } else {
                        new Handler(Looper.getMainLooper()).post(() ->
                                Toast.makeText(context, "Token não encontrado!", Toast.LENGTH_SHORT).show()
                        );
                        return;
                    }

                    // Se for POST ou PUT, enviar os dados no corpo da requisição
                    if (("POST".equals(method) || "PUT".equals(method)) && jsonData != null) {
                        connection.setDoOutput(true);
                        try (OutputStream os = connection.getOutputStream()) {
                            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                            os.write(input, 0, input.length);
                        }
                    }

                    // Ler a resposta do servidor
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(response.toString()));
                    } else {
                        new Handler(Looper.getMainLooper()).post(() ->
                                Toast.makeText(context, "Erro na requisição: " + responseCode, Toast.LENGTH_SHORT).show()
                        );
                        callback.onError("Erro na requisição: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Erro ao conectar com a API", Toast.LENGTH_SHORT).show()
                    );
                    callback.onError("Erro ao conectar com a API: " + e.getMessage());
                }
            }).start();
        }

        // Métodos específicos para GET, POST, PUT e DELETE
        public static void get(Context context, String endpoint, ApiCallback callback) {
            makeRequest(context, endpoint, "GET", null, callback);
        }

        public static void post(Context context, String endpoint, String jsonData, ApiCallback callback) {
            makeRequest(context, endpoint, "POST", jsonData, callback);
        }

        public static void put(Context context, String endpoint, String jsonData, ApiCallback callback) {
            makeRequest(context, endpoint, "PUT", jsonData, callback);
        }

        public static void delete(Context context, String endpoint, ApiCallback callback) {
            makeRequest(context, endpoint, "DELETE", null, callback);
        }

        // Interface para callback da requisição
        public interface ApiCallback {
            void onSuccess(String response);
            void onError(String errorMessage);
        }
    }
