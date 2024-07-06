package com.example.busticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView2;
    EditText user_name, password;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_activity);

        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.textView2);
        user_name = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        dbHelper = new DBHelper(this);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1 = password.getText().toString();
                String username1 = user_name.getText().toString();

                if (password1.equals("admin") && username1.equals("admin")) {
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    Toast.makeText(MainActivity.this, "Welcome admin", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } else {
                    if (!username1.isEmpty() && !password1.isEmpty()) {
                        if (isValidUsername(username1) && isValidPassword(password1)) {
                            // Check username and password against SQLite database
                            if (checkCredentials(username1, password1)) {
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Customer_Dashboard.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (!isValidUsername(username1)) {
                                user_name.setError("Invalid username");
                            }
                            if (!isValidPassword(password1)) {
                                password.setError("Invalid password");
                            }
                        }
                    } else {
                        if (username1.isEmpty()) {
                            user_name.setError("Username field can't be empty");
                        }
                        if (password1.isEmpty()) {
                            password.setError("Password field can't be empty");
                        }
                    }
                }
            }
        });
    }

    private boolean isValidUsername(String username) {
        // Add your username validation logic here
        return Pattern.matches("^[a-z0-9_-]{3,15}$", username);
    }

    private boolean isValidPassword(String password) {
        // Add your password validation logic here
        return Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", password);
    }

    private boolean checkCredentials(String username, String password) {
        // Check username and password against SQLite database
        return dbHelper.checkUser(username, password);
    }
}
