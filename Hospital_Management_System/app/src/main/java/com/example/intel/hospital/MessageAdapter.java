package com.example.intel.hospital;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Intel on 21/06/2016.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageModelHolder> {


    ArrayList<MessageModel> messageModelArrayList = new ArrayList<>();
    Context context;

    @Override
    public MessageAdapter.MessageModelHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        MessageModelHolder messageModelHolder = new MessageModelHolder(view);

        return messageModelHolder;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.MessageModelHolder holder, int position) {

        holder.tvName.setText(messageModelArrayList.get(position).getName().toString());
        holder.tvUserName.setText(messageModelArrayList.get(position).getUserName().toString());
        holder.tvPassword.setText(messageModelArrayList.get(position).getPassword().toString());
        holder.tvMobile.setText(messageModelArrayList.get(position).getMobile().toString());
    }

    @Override
    public int getItemCount() {
        return messageModelArrayList.size();
    }

    public class MessageModelHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvUserName,tvPassword,tvMobile;

        public MessageModelHolder(View itemView) {
            super(itemView);

            tvName = (TextView)itemView.findViewById(R.id.tvName);
            tvUserName = (TextView)itemView.findViewById(R.id.tvUserName);
            tvPassword = (TextView)itemView.findViewById(R.id.tvPassword);
            tvMobile = (TextView)itemView.findViewById(R.id.tvMobile);
        }
    }

    public MessageAdapter(ArrayList<MessageModel> messageModelArrayList, Context context) {
        this.messageModelArrayList = messageModelArrayList;
        this.context = context;
    }
}
