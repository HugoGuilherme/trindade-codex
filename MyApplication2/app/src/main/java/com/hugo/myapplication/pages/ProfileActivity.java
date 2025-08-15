package com.hugo.myapplication.pages;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hugo.myapplication.R;

public class ProfileActivity extends BaseActivity {

    private TextView tvUsername, tvEmail, tvPhone, tvGender, tvDob, tvChangePhoto;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarAndNavigation(R.layout.profile_activity, false);

        // Inicializar elementos da UI
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvGender = findViewById(R.id.tv_gender);
        tvDob = findViewById(R.id.tv_dob);
        profileImage = findViewById(R.id.profile_image);
        tvChangePhoto = findViewById(R.id.tv_change_photo);

        // Recuperar os dados salvos no SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String nome = prefs.getString("perfilNome", "Nome não disponível");
        String email = prefs.getString("perfilEmail", "Email não disponível");
        String telefone = prefs.getString("perfilTelefone", "Telefone não disponível");
        String genero = prefs.getString("perfilGenero", "Gênero não disponível");
        String dataNascimento = prefs.getString("perfilDataNascimento", "Data de nascimento não disponível");

        // Preencher os TextViews com os dados do perfil
        tvUsername.setText(nome);
        tvEmail.setText(email);
        tvPhone.setText(telefone);
        tvGender.setText(genero);
        tvDob.setText(dataNascimento);

        // Evento de clique para mudar a foto (apenas um placeholder)
        tvChangePhoto.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Função de alterar foto ainda não implementada!", Toast.LENGTH_SHORT).show();
        });
    }
}
