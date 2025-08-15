package com.hugo.myapplication.pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hugo.myapplication.R;
import com.hugo.myapplication.utils.Utils;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String senha = etSenha.getText().toString().trim();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                new LoginTask().execute(email, senha);
            }
        });
    }

    // Classe AsyncTask para fazer login na API
    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String senha = params[1];

            try {
                URL url = new URL("http://" + getString(R.string.server_ip) + ":8080/auth/signin/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Monta o JSON com email e senha
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("username", email);
                jsonParam.put("password", senha);

                // Envia os dados
                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                // Se a resposta for OK (200)
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // Converte resposta JSON
                    String response = Utils.readStream(conn.getInputStream());
                    Log.d("LoginTask", "Resposta da API: " + response);

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean authenticated = jsonResponse.getBoolean("authenticated");

                    if (authenticated) {
                        // Obtém o token
                        String token = jsonResponse.getString("accessToken");

                        // Obtém o objeto perfil
                        JSONObject perfilJson = jsonResponse.getJSONObject("perfil");

                        // Extrai os dados do perfil
                        String id = jsonResponse.getString("id");
                        String nome = perfilJson.getString("nome");
                        String emailPerfil = perfilJson.getString("email");
                        String endereco = perfilJson.getString("endereco");
                        String telefone = perfilJson.getString("telefone");
                        String genero = perfilJson.getString("genero");
                        String dataNascimento = perfilJson.getString("dataNascimento");

                        // Salva os dados no SharedPreferences
                        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putString("authToken", token); // Salva o token
                        editor.putString("perfilId", id);
                        editor.putString("perfilNome", nome);
                        editor.putString("perfilEmail", emailPerfil);
                        editor.putString("perfilEndereco", endereco);
                        editor.putString("perfilTelefone", telefone);
                        editor.putString("perfilGenero", genero);
                        editor.putString("perfilDataNascimento", dataNascimento);

                        editor.apply();

                        return "success";
                    } else {
                        return "fail";
                    }
                } else {
                    return "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "exception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(MainActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                abrirTelaPrincipal();
            } else if (result.equals("fail")) {
                Toast.makeText(MainActivity.this, "E-mail ou senha incorretos!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Erro no processamento do login!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Abre a HomeActivity após o login bem-sucedido
    private void abrirTelaPrincipal() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
