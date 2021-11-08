package com.watchtrading.watchtrade.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.watchtrading.watchtrade.Activities.ChattingActivity;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ChatsModel;
import com.watchtrading.watchtrade.Models.ForumModel;
import com.watchtrading.watchtrade.R;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> {
    private List<ChatsModel> chatsModelList;
    private Context context;
    private DeleteChat deleteChat;


    public ChatsAdapter(List<ChatsModel> chatsModelList, Context context, DeleteChat deleteChat) {
        this.chatsModelList = chatsModelList;
        this.context = context;
        this.deleteChat = deleteChat;

    }
    @NonNull
    @Override
    public ChatsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_list, null);
        ChatsAdapter.MyViewHolder viewHolder = new ChatsAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    //This method inflates view present in the RecyclerView




    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.MyViewHolder holder, int position) {

        holder.userNameTVCL.setText(chatsModelList.get(position).getName());
        String status =chatsModelList.get(position).getStatus();
        String createuserID = chatsModelList.get(position).getCuser();
        String createuserName = chatsModelList.get(position).getName();


        Glide.with(context)
                .load(chatsModelList.get(position).getImg())
                .placeholder(R.drawable.app_icon)
                .into(holder.userImageCLSL);

        /*if (chatsModelList.get(position).getStatus().toString().toLowerCase().trim().equals("online")){
            holder.statusIV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.online));
        }
        else {
            holder.statusIV.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.offline));
        }*/


        holder.chatsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChattingActivity.class);
                intent.putExtra("uuuID", ""+chatsModelList.get(position).getCuser());
                intent.putExtra("uuuName", ""+chatsModelList.get(position).getName());
                context.startActivity(intent);
            }
        });
        holder.delChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChat.delChat(position, chatsModelList.get(position).getCuser());
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout chatsLL;
        private TextView userNameTVCL;
        private ImageView statusIV, userImageCLSL, delChatBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chatsLL=itemView.findViewById(R.id.chatsLL);
            userNameTVCL=itemView.findViewById(R.id.userNameTVCL);
            statusIV=itemView.findViewById(R.id.statusIV);
            userImageCLSL=itemView.findViewById(R.id.userImageCLSL);
            delChatBtn=itemView.findViewById(R.id.delChatBtn);
        }
    }


    public interface DeleteChat {

        void delChat( int position, String userID);
    }

}
