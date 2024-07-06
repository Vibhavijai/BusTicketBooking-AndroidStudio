package com.example.busticketbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    Button button;
    TextView textView2;
    EditText email, password, name;
    DBHelper dbHelper;
    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.textView2);
        email = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        name = findViewById(R.id.username);
        loader = new ProgressDialog(this);
        dbHelper = new DBHelper(this);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1 = password.getText().toString();
                String email1 = email.getText().toString();
                String name1 = name.getText().toString();

                if (!name1.isEmpty() && !email1.isEmpty() && !password1.isEmpty()) {
                    if (isValidEmail(email1) && isValidPassword(password1)) {
                        loader.setMessage("Sign Up in progress...");
                        loader.setCanceledOnTouchOutside(false);
                        loader.show();

                        // Insert user data into SQLite database
                        if (insertUserData(name1, email1, password1)) {
                            Toast.makeText(SignUp.this, "Registration successful!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, Customer_Dashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUp.this, "Failed to register! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (!isValidEmail(email1)) {
                            email.setError("Invalid email");
                        }
                        if (!isValidPassword(password1)) {
                            password.setError("Invalid password");
                        }
                    }
                } else {
                    if (name1.isEmpty()) {
                        name.setError("Name field can't be empty");
                    }
                    if (email1.isEmpty()) {
                        email.setError("Email field can't be empty");
                    }
                    if (password1.isEmpty()) {
                        password.setError("Password field can't be empty");
                    }
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        // Add your email validation logic here
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Add your password validation logic here
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
    }

    private boolean insertUserData(String name, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_USERNAME, name);
        values.put(DBHelper.COLUMN_EMAIL, email);
        values.put(DBHelper.COLUMN_PASSWORD, password);

        long newRowId = db.insert(DBHelper.TABLE_USERS, null, values);
        db.close();

        return newRowId != -1;
    }
}
