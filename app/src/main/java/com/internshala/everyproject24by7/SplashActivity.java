package com.internshala.everyproject24by7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    public static FirebaseAuth mAuth;
    public static FirebaseFirestore firestore;
    public static ProfileModel myProfile=new ProfileModel("NA",null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth=FirebaseAuth.getInstance();
        this.firestore=FirebaseFirestore.getInstance();
        new Thread(){
            @Override
            public void run(){

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mAuth.getCurrentUser()!=null){

                    getUserData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("welcome",myProfile.getName());
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }

                        @Override
                        public void onFailure() {
                            Intent intent= new Intent(SplashActivity.this,SignUpActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }
                    });


                }
                else {
                    Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }


            }

        }
                .start();

    }
    public static  void getUserData(MyCompleteListener completeListener) {
        firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    myProfile.setName(documentSnapshot.getString("NAME"));
                    myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));
                    completeListener.onSuccess();
                })
                .addOnFailureListener(e -> completeListener.onFailure());
    }

}