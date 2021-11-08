package com.watchtrading.watchtrade.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.watchtrading.watchtrade.Activities.ChattingActivity;
import com.watchtrading.watchtrade.Activities.ReportActivity;
import com.watchtrading.watchtrade.Activities.RepostActivity;
import com.watchtrading.watchtrade.Activities.UpdateWatchActivity;
import com.watchtrading.watchtrade.Activities.UserProfileActivity;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.BrandModel;
import com.watchtrading.watchtrade.Models.ForumModel;
import com.watchtrading.watchtrade.Models.StocksModel;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;

import java.util.ArrayList;
import java.util.List;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.MyViewHolder> {

    private final Object ClicksStocks;
    private List<BrandModel> brandModelList;
    private Context context;
    private int pos;
    private String userID;
    private String createuserID;
    public static ClicksStocks clicks;

    public StocksAdapter(List<BrandModel> brandModelList, Context context, ClicksStocks clicks) {
        this.brandModelList = brandModelList;
        this.context = context;
        this.ClicksStocks = clicks;
    }


    @NonNull
    @Override
    public StocksAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stocks_list, null);
        StocksAdapter.MyViewHolder viewHolder = new StocksAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StocksAdapter.MyViewHolder holder, int position) {



        holder.makeTV.setText(brandModelList.get(position).getPostmake());
        holder.modelTV.setText(brandModelList.get(position).getPostmodel());
        holder.locationTV.setText(brandModelList.get(position).getPostcountry());
        holder.yearTV.setText(brandModelList.get(position).getPostyear());
        holder.typeeTV.setText(brandModelList.get(position).getPostype());



        userID = brandModelList.get(position).getCreateuserID();
        createuserID = brandModelList.get(position).getCreateuserID();

        String box = brandModelList.get(position).getWatchbox();
        String paper = brandModelList.get(position).getPaperwatch();
        if (box.equals("Yes") || box.equals("yes")|| box.equals("checked")){
            holder.boxCB.setChecked(true);
        }
        if (paper.equals("Yes") || paper.equals("yes")||paper.equals("checked")){
            holder.papersCB.setChecked(true);
        }



        holder.moveBLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChattingActivity.class);
                intent.putExtra("uuuID", ""+brandModelList.get(position).getCreateuserID());
                intent.putExtra("uuuName", ""+brandModelList.get(position).getPostmake());
                context.startActivity(intent);
            }
        });
        holder.menuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v , position);

            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateWatchActivity.class);
                intent.putExtra("postID", ""+brandModelList.get(position).getPostID());
                intent.putExtra("createuserID", ""+createuserID);
                intent.putExtra("postType", ""+brandModelList.get(position).getPostype());
                intent.putExtra("postmake", ""+brandModelList.get(position).getPostmake());
                intent.putExtra("casesize", ""+brandModelList.get(position).getCasesize());
                intent.putExtra("postmode", ""+brandModelList.get(position).getPostmodel());
                intent.putExtra("enterprice", ""+brandModelList.get(position).getEnterprice());
                intent.putExtra("postyear", ""+brandModelList.get(position).getPostyear());
                intent.putExtra("postarea", ""+brandModelList.get(position).getPostaddress());
                intent.putExtra("postcountry", ""+brandModelList.get(position).getPostcountry());
                intent.putExtra("watchbox", ""+brandModelList.get(position).getWatchbox());
                intent.putExtra("paperwatch", ""+brandModelList.get(position).getPaperwatch());
                context.startActivity(intent);
            }
        });

        Glide.with(context)
                .load(APIContract.IMAGE_URL + brandModelList.get(position).getFile_name())
                .placeholder(R.drawable.app_icon)
                .into(holder.userImageCLSL);


        if (SharedPreferencesSotreData.getInstance().getID().equals(createuserID)){
            holder.menuIV.setVisibility(View.GONE);
        }

    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        android.widget.PopupMenu popup = new android.widget.PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.profile_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();

    }
    private class MyMenuItemClickListener implements android.widget.PopupMenu.OnMenuItemClickListener {
        int position;

        public MyMenuItemClickListener( int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {


                case R.id.repost:
                    Log.d("****poss",""+position);
                    Intent intent = new Intent(context, RepostActivity.class);
                    intent.putExtra("postID", ""+brandModelList.get(position).getPostID());
                    intent.putExtra("createuserID", ""+createuserID);
                    intent.putExtra("postType", ""+brandModelList.get(position).getPostype());
                    intent.putExtra("postmake", ""+brandModelList.get(position).getPostmake());
                    intent.putExtra("casesize", ""+brandModelList.get(position).getCasesize());
                    intent.putExtra("postmode", ""+brandModelList.get(position).getPostmodel());
                    intent.putExtra("enterprice", ""+brandModelList.get(position).getEnterprice());
                    intent.putExtra("postyear", ""+brandModelList.get(position).getPostyear());
                    intent.putExtra("postarea", ""+brandModelList.get(position).getPostaddress());
                    intent.putExtra("postcountry", ""+brandModelList.get(position).getPostcountry());
                    intent.putExtra("watchbox", ""+brandModelList.get(position).getWatchbox());
                    intent.putExtra("paperwatch", ""+brandModelList.get(position).getPaperwatch());
                    context.startActivity(intent);
                    break;


                case R.id.block:
                    break;

            }
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return brandModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView makeTV, modelTV, locationTV, yearTV, typeeTV;
        private CheckBox papersCB, boxCB;
        private ImageView moveBLBtn, editBtn, menuIV, userImageCLSL;
        private RadioGroup paperAndBoxRG;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            moveBLBtn=itemView.findViewById(R.id.moveBLBtn);
            menuIV=itemView.findViewById(R.id.menuIVV);
            editBtn=itemView.findViewById(R.id.editBtn);
            makeTV=itemView.findViewById(R.id.makeTV);
            modelTV=itemView.findViewById(R.id.modelTV);
            locationTV=itemView.findViewById(R.id.locationTV);
            yearTV=itemView.findViewById(R.id.yearTV);
            papersCB=itemView.findViewById(R.id.papersCB);
            boxCB=itemView.findViewById(R.id.boxCB);
            paperAndBoxRG=itemView.findViewById(R.id.paperAndBoxRG);
            typeeTV=itemView.findViewById(R.id.typeeTV);
            userImageCLSL=itemView.findViewById(R.id.userImageCLSL);
        }
    }


    public interface ClicksStocks {

        void repost(int position);

        void block( int position);
    }
}
