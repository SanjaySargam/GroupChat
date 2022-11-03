package com.internshala.everyproject24by7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
private Button newGrp,chatRoom,logout;
private Dialog progressDialog;
private TextView dialogText,welcomee;
public String randomString;
public EditText enterCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        String name=intent.getStringExtra("welcome");
        welcomee=findViewById(R.id.welcome);
        logout=findViewById(R.id.logOut);
        welcomee.setText("Welcome "+name+"!");
        newGrp=findViewById(R.id.newGroup);
        enterCode=findViewById(R.id.enterCode);
        chatRoom=findViewById(R.id.chatRoom);
        progressDialog = new Dialog(MainActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);

        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Creating Chat Room...");
        chatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String EnterCode=enterCode.getText().toString();
                    String code="Your Chat Room code is: "+EnterCode;
                    Intent intent=new Intent(MainActivity.this,GroupChatActivity.class);
                    intent.putExtra("codes",code);
                    intent.putExtra("enteredCode",EnterCode);
                    startActivity(intent);
                }
            }
        });
        newGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomCode();
                createRoom();
               // Toast.makeText(MainActivity.this,"Only Admins can create Chat room",Toast.LENGTH_SHORT).show();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }
    public void randomCode(){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        // specify length of random string
        int length = 7;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        randomString = sb.toString();
//        System.out.println("Random String is: " + randomString);
    }
public void createRoom(){
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    builder.setCancelable(true);
    View view = getLayoutInflater().inflate(R.layout.alert_dialog, null);
    Button OK=view.findViewById(R.id.confirmB);
    TextView code=view.findViewById(R.id.code);
    code.setText("Your Chat Room Code is: "+randomString);
    String str=code.getText().toString();
    builder.setView(view);
    AlertDialog alertDialog = builder.create();
    OK.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        alertDialog.dismiss();
        Intent intent =new Intent(MainActivity.this,GroupChatActivity.class);
        intent.putExtra("codes",str);
        intent.putExtra("enteredCode",randomString);
        startActivity(intent);

        }
    });
    alertDialog.show();
}
private boolean validate(){
        if (enterCode.getText().toString().isEmpty()){
            enterCode.setError("This field cannot be empty");
            return false;
        }

        return true;
}
    @Override
    public void onBackPressed(){

            super.onBackPressed();

    }

}