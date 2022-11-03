package com.internshala.everyproject24by7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText email,pass;
    public Button loginBtn;
    private FirebaseAuth mAuth;
    private Dialog progressDialog;
    private TextView signUp,dialogText;
    private static FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.passwords);
        signUp=findViewById(R.id.signUpBt);
        loginBtn=findViewById(R.id.loginBt);
        mAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        progressDialog = new Dialog(LoginActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Signing in...");
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    login();
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }
    public boolean validateData() {


        if (email.getText().toString().isEmpty()) {
            email.setError("Enter E-mail ID");
            return false;
        }
        if (pass.getText().toString().isEmpty()) {
            pass.setError("Enter Password");
            return false;
        }


        return true;
    }

    private void login() {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            loadData(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("welcome",SplashActivity.myProfile.getName());
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure() {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Something went wrong",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    public void loadData(final MyCompleteListener completeListener){
            SplashActivity.getUserData(new MyCompleteListener() {
                @Override
                public void onSuccess() {
                   completeListener.onSuccess();
                }

                @Override
                public void onFailure() {
                    completeListener.onFailure();
                }
            });
    }
//    public  void getUserData(MyCompleteListener completeListener) {
//        firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    completeListener.onSuccess();
//                })
//                .addOnFailureListener(e -> completeListener.onFailure());
    }

