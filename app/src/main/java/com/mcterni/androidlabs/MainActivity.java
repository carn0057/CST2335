package com.mcterni.androidlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

    public class MainActivity extends AppCompatActivity {

        SharedPreferences prefs;
        SharedPreferences.Editor editor;


            @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_main_linear);
            //setContentView(R.layout.activity_main_grid);
            setContentView(R.layout.activity_main);

            prefs = getSharedPreferences("email",MODE_PRIVATE);
            final EditText email = findViewById(R.id.type_email);
            email.setText(prefs.getString("email",""));

            //use a Lambda function to set a click listener
            Button button = (Button) findViewById(R.id.button_login);
            
            if(button != null){
                button.setOnClickListener(clk -> {
                    Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);

                    goToProfile.putExtra("email", email.getText().toString());

                    startActivityForResult(goToProfile, 30);
                });
            }

        }

        @Override
        protected void onPause() {
            super.onPause();

            editor = prefs.edit();

            editor.putString("email", ((EditText)findViewById(R.id.type_email)).getText().toString());
            editor.commit();

        }



}
