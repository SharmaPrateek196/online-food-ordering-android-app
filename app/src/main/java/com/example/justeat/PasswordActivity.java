package com.example.justeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PasswordActivity extends AppCompatActivity {
    Button btnsubmit,btnback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        btnsubmit = findViewById(R.id.btnSubmit);
        btnback = findViewById(R.id.btnBack);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(PasswordActivity.this,MainActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
