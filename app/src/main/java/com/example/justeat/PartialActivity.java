package com.example.justeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PartialActivity extends AppCompatActivity {
Button btnSignUp,btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial);


        btnSignIn = findViewById(R.id.btSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(PartialActivity.this,MainActivity.class);
                startActivity(registerIntent);

            }
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(PartialActivity.this,SignupActivity.class);
                startActivity(registerIntent);

            }
        });
    }
}
