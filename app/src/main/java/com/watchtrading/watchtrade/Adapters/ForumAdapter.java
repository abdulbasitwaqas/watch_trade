package com.watchtrading.watchtrade.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.watchtrading.watchtrade.Activities.ForumDetailActivity;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ForumModel;
import com.watchtrading.watchtrade.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.MyViewHolder> {
    private List<String> forumModelList;
    private Context context;
    private List<String> filteredList;
    public static ControlsAdapterlistener onClickListener;


    public ForumAdapter(List<String> forumModelList, ControlsAdapterlistener onClickListener, Context context) {
        this.forumModelList = forumModelList;
        this.onClickListener=onClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.forums_list, null);
        ForumAdapter.MyViewHolder viewHolder = new ForumAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = forumModelList.get(position);
        holder.brandNameFF.setText(capitalizeString(name));

        final String brand = forumModelList.get(position);


        holder.forumLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ForumDetailActivity.class);
                intent.putExtra("brand", ""+brand);
                context.startActivity(intent);
            }
        });
        holder.addToContactIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.addToContacts(v, position);
            }
        });

        Glide.with(context)
                .load(APIContract.IMAGE_URL + forumModelList.get(position)+".jpg")
                .placeholder(R.drawable.app_icon)
                .into(holder.brandImageFF);


    }

    @Override
    public int getItemCount() {
        Log.d("**forumSize", ""+forumModelList.size());
        return forumModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout forumLL;
        private TextView brandNameFF;
        private CircleImageView brandImageFF;
        private ImageView addToContactIV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            forumLL=itemView.findViewById(R.id.forumLL);
            brandImageFF=itemView.findViewById(R.id.brandImageFF);
            brandNameFF=itemView.findViewById(R.id.brandNameFF);
            addToContactIV=itemView.findViewById(R.id.addToContactIV);

        }
    }

    public interface ControlsAdapterlistener {

        void addToContacts(View v, int position);


    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }


}
