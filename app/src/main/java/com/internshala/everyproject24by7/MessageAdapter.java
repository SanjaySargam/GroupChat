package com.internshala.everyproject24by7;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    Context context;
    ArrayList<ChatRoom> list;

    public MessageAdapter(Context context, ArrayList<ChatRoom> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.message,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ChatRoom chatRoom=list.get(position);
            holder.message.setText(chatRoom.getMessageText());
        holder.username.setText(chatRoom.getMessageUser());
        holder.time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                chatRoom.getMessageTime()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
            TextView message,username,time;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                message=itemView.findViewById(R.id.message_text);
                time=itemView.findViewById(R.id.message_time);
                username=itemView.findViewById(R.id.message_user);
            }
        }
}
