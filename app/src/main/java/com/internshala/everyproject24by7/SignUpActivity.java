package com.internshala.everyproject24by7;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
public EditText name,email_id,pass,confirmPass;
public Button signUpBtn;
public String emailStr,passStr,confirmPassStr,nameStr;
public Dialog progressDialog;
public FirebaseAuth mAuth;
public TextView login;
public static FirebaseFirestore fireStore;

public TextView dialogText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fireStore=FirebaseFirestore.getInstance();
        name=findViewById(R.id.username);

        email_id=findViewById(R.id.emailID);
        pass=findViewById(R.id.password);
        login=findViewById(R.id.loginB);
        confirmPass=findViewById(R.id.confirm_pass);
        signUpBtn=findViewById(R.id.signUp_btn);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText=progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering User...");
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    signupNewUser();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });

    }
    private boolean validate(){
        nameStr=name.getText().toString().trim();
        passStr=pass.getText().toString().trim();
        confirmPassStr=confirmPass.getText().toString().trim();
        emailStr=email_id.getText().toString().trim();

        if (nameStr.isEmpty()){
            name.setError("Enter Your Name");
            return false;
        }
        if (emailStr.isEmpty()){
            email_id.setError("Enter EmailID");
            return false;
        }

        if (passStr.isEmpty()){
            pass.setError("Enter Password");
            return false;
        }
        if (confirmPassStr.isEmpty()){
            confirmPass.setError("Enter Password");
            return false;
        }
        if (passStr.compareTo(confirmPassStr)!=0){
            Toast.makeText(SignUpActivity.this,"Password and Confirm Password must be same !",Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
    }
public void signupNewUser(){
    String user=name.getText().toString();
    progressDialog.show();
    mAuth.createUserWithEmailAndPassword(emailStr, passStr)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(SignUpActivity.this,"Sign Up Sucsessfull",Toast.LENGTH_SHORT).show();
                    createUserData(emailStr,nameStr,new MyCompleteListener(){
                        @Override
                        public void onSuccess() {
                                    progressDialog.dismiss();
                                    Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                                     intent.putExtra("welcome",user);
                                    startActivity(intent);
                                    SignUpActivity.this.finish();
                                }
                                @Override
                                public void onFailure() {
                                    Toast.makeText(SignUpActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });


                        }
                else {
                    progressDialog.dismiss();
                    // If sign in fails, display a message to the user.
                    Toast.makeText(SignUpActivity.this, "Email-id already exists",
                            Toast.LENGTH_SHORT).show();

                }
                    });



}
public static void createUserData(String email, String name, MyCompleteListener completeListener) {
        Map<String, Object> userData = new ArrayMap<>();
        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        DocumentReference userDoc = fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
        WriteBatch batch = fireStore.batch();
        batch.set(userDoc, userData);
        DocumentReference countDoc = fireStore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));
        batch.commit()
                .addOnSuccessListener(
                        unused -> completeListener.onSuccess())
                .addOnFailureListener(e -> completeListener.onFailure());


    }
}