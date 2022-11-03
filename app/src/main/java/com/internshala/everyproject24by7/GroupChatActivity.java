package com.internshala.everyproject24by7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GroupChatActivity extends AppCompatActivity {
    public TextView code;
    public ImageButton sendMessage;
    public RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MessageAdapter messageAdapter;
    ArrayList<ChatRoom> list;
    public EditText chatBox;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        Intent intent = getIntent();
        String randomString = intent.getStringExtra("codes");
        String enteredCode=intent.getStringExtra("enteredCode");
        code = findViewById(R.id.codee);
        code.setText(randomString);
        chatBox=findViewById(R.id.chat_box);
        recyclerView=findViewById(R.id.chat_list);
        databaseReference=FirebaseDatabase.getInstance().getReference(enteredCode);
//        databaseReference.addListenerForSingleValueEvent(valueEventListener);
//
////        Query query=FirebaseDatabase.getInstance().getReference("Messages")
////                        .orderByChild()

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        messageAdapter=new MessageAdapter(this,list);
        recyclerView.setAdapter(messageAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatRoom chatRoom=dataSnapshot.getValue(ChatRoom.class);
                    list.add(chatRoom);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendMessage=findViewById(R.id.sendMsg);
        sendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                list.clear();
                if (chatBox.getText().toString().trim().equals("")) {
                    Toast.makeText(GroupChatActivity.this, "Please enter some texts!", Toast.LENGTH_SHORT).show();

                } else {

                    ChatRoom chatRoom=new ChatRoom(chatBox.getText().toString(),SplashActivity.myProfile.getName());
                    FirebaseDatabase.getInstance()
                            .getReference(enteredCode)
                            .push()
                            .setValue(chatRoom);
                    ;
                    chatBox.setText("");

                }
            }
        });




    }
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                ChatRoom chatRoom=dataSnapshot.getValue(ChatRoom.class);
                list.add(chatRoom);
            }
            messageAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}