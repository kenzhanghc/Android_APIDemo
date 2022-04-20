package com.example.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.myalldemo.R;

public class SharedPreferencesActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;
    private CheckBox checkBox;
    private EditText editTextUser;
    private EditText editTextPassword;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_preferences_activity);
    }
}