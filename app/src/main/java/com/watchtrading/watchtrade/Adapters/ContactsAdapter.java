package com.watchtrading.watchtrade.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.watchtrading.watchtrade.Activities.ChattingActivity;
import com.watchtrading.watchtrade.Activities.ReportActivity;
import com.watchtrading.watchtrade.Activities.UserProfileActivity;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Fragments.ContactsFragment;
import com.watchtrading.watchtrade.Models.ContactsModel;
import com.watchtrading.watchtrade.Models.ForumModel;
import com.watchtrading.watchtrade.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>  {

    private List<ContactsModel> contactsModelList;
    private Context context;
    private String userID;
    private int pos;
    private String  name = "";
    private BlockClick blockClick;

    public ContactsAdapter(List<ContactsModel> contactsModelList, Context context, BlockClick blockClick) {
        this.contactsModelList = contactsModelList;
        this.context = context;
        this.blockClick = blockClick;
    }

    @NonNull
    @Override
    public ContactsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list, null);
        ContactsAdapter.MyViewHolder viewHolder = new ContactsAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.MyViewHolder holder, int position) {

        holder.unblockContact.setVisibility(View.GONE);
        holder.userNameCL.setText(contactsModelList.get(position).getName());

        pos= position;
        holder.blockIV.setVisibility(View.GONE);







        if (contactsModelList.get(position).getBlock().equals("block")){
            holder.blockIV.setVisibility(View.VISIBLE);
            holder.contactIVCL.setEnabled(false);
            holder.userNameCL.setEnabled(false);
            holder.userNameCL.setAlpha(0.5f);
            holder.unblockContact.setVisibility(View.VISIBLE);
        } else {
            holder.blockIV.setVisibility(View.GONE);
            holder.contactIVCL.setEnabled(true);
            holder.userNameCL.setEnabled(true);
            holder.unblockContact.setVisibility(View.GONE);
        }


        userID = contactsModelList.get(position).getUserID();
        Log.d("**contacts",""+userID);

        name = contactsModelList.get(position).getName();

        holder.unblockContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockClick.block(position, contactsModelList.get(position).getUserID());
            }
        });

        holder.userNameCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, position);
            }
        });



        Glide.with(context)
                .load(/*APIContract.USER_PROFILE_IMAGE +*/ contactsModelList.get(position).getImage())
                .placeholder(R.drawable.app_icon)
                .into(holder.contactIVCL);
    }

    @Override
    public int getItemCount() {
        return contactsModelList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout contactTopCL;
        private CircleImageView contactIVCL;
        private TextView userNameCL;
        private ImageView blockIV;
        private Button unblockContact;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contactTopCL=itemView.findViewById(R.id.contactTopCL);
            contactIVCL=itemView.findViewById(R.id.contactIVCL);
            userNameCL=itemView.findViewById(R.id.userNameCL);
            blockIV=itemView.findViewById(R.id.blockIVCL);
            unblockContact=itemView.findViewById(R.id.unblockContact);
        }
    }


    private void showPopupMenu(View view, int position) {
        // inflate menu
        android.widget.PopupMenu popup = new android.widget.PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.contactmenu, popup.getMenu());

        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();

    }

    private class MyMenuItemClickListener implements android.widget.PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {


                case R.id.profile:
//                    Toast.makeText(context, "profile", Toast.LENGTH_SHORT).show();
                    Intent profIntent = new Intent(context, UserProfileActivity.class);
                    profIntent.putExtra("userID", ""+contactsModelList.get(pos).getUserID());
                    context.startActivity(profIntent);

                    break;


                case R.id.chat:
                    Intent intentt = new Intent(context, ChattingActivity.class);
                    intentt.putExtra("uuuID", "" + contactsModelList.get(pos).getUserID());
                    intentt.putExtra("uuuName", "" + contactsModelList.get(pos).getName());
                    context.startActivity(intentt);
                    break;

            }
            return false;
        }
    }


    public interface BlockClick {

        void block( int position, String userID);
    }


}
