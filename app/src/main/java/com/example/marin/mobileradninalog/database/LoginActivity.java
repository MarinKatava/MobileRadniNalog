package com.example.marin.mobileradninalog.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marin.mobileradninalog.R;
import com.example.marin.mobileradninalog.main.MainActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final AutoCompleteTextView email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        Button prijava = findViewById(R.id.email_sign_in_button);


        prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("mkatav00@fesb.hr") && password.getText().toString().equals("admin123") ) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Unesite točne vrijednosti", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
