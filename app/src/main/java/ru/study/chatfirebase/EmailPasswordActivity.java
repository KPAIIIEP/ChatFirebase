package ru.study.chatfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);
        auth = FirebaseAuth.getInstance();

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonSighIn = findViewById(R.id.emailSignInButton);
        buttonSighIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(editTextEmail.getText().toString(),
                        editTextPassword.getText().toString());
            }
        });
        Button buttonRegister = findViewById(R.id.emailCreateAccountButton);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(editTextEmail.getText().toString(),
                        editTextPassword.getText().toString());
            }
        });
    }

    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            switchToMainActivity();
//                            Toast.makeText(EmailPasswordActivity.this,
//                                    "User is register", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("EmailPasswordActivity", "createUserWithEmail:failure",
                                    task.getException());
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Register failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            switchToMainActivity();
//                            Toast.makeText(EmailPasswordActivity.this,
//                                    "User is sigh in", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("EmailPasswordActivity", "signInWithEmail:failure",
                                    task.getException());
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void switchToMainActivity() {
        Intent intent = new Intent(EmailPasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}