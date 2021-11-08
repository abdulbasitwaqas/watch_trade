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
import com.watchtrading.watchtrade.Activities.UpdateWatchActivity;
import com.watchtrading.watchtrade.Activities.UserProfileActivity;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.SearchModel;
import com.watchtrading.watchtrade.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<SearchModel> brandModelList;
    private Context context;
    private int pos;
//    private String userID;
    public static Clicks clicks;

    public SearchAdapter(List<SearchModel> searchModelList, Clicks clicks, Context context) {
        this.brandModelList = searchModelList;
        this.context = context;
        this.clicks = clicks;
    }


    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, null);
        SearchAdapter.MyViewHolder viewHolder = new SearchAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {


        holder.makeTV.setText(brandModelList.get(position).getPostmake());
        holder.caseSizeTVBL.setText(brandModelList.get(position).getCasesize());
        holder.modelTV.setText(brandModelList.get(position).getPostmodel());
        holder.yearTV.setText(brandModelList.get(position).getPostyear());
        holder.typeeTV.setText(brandModelList.get(position).getPostype());
        holder.locationTV.setText(brandModelList.get(position).getPostaddress());
        holder.priceTVBL.setText(brandModelList.get(position).getEnterprice());

        holder.typeeTV.setText(""+brandModelList.get(position).getPostype());
        pos = position;
//        userID = brandModelList.get(position).getCreateuserID();
//        final String createuserID = brandModelList.get(position).getCreateuserID();

        String box = brandModelList.get(position).getWatchbox();
        String paper = brandModelList.get(position).getPaperwatch();


        if (box.equals("Yes") || box.equals("yes")||box.equals("checked")){
            holder.boxCB.setChecked(true);
        }
        else {
            holder.boxCB.setChecked(false);
        }



        holder.boxCB.setEnabled(false);
        holder.papersCB.setEnabled(false);

        if (paper.equals("Yes") || paper.equals("yes")|| paper.equals("checked")){
            holder.papersCB.setChecked(true);
        }
        else {
            holder.papersCB.setChecked(false);
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
                intent.putExtra("createuserID", ""+brandModelList.get(position).getCreateuserID());
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

    }

    @Override
    public int getItemCount() {
        Log.d("brandSize", ""+ brandModelList.size());
        return brandModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView makeTV, modelTV, locationTV, yearTV, typeeTV, caseSizeTVBL, priceTVBL;
        private CheckBox  papersCB, boxCB;
        private ImageView moveBLBtn, editBtn, menuIV, userImageCLSL;
        private RadioGroup paperAndBoxRG;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            moveBLBtn=itemView.findViewById(R.id.moveBLBtn);
            menuIV=itemView.findViewById(R.id.menuIV);
            editBtn=itemView.findViewById(R.id.editBtn);
            makeTV=itemView.findViewById(R.id.makeTV);
            modelTV=itemView.findViewById(R.id.modelTV);
            priceTVBL=itemView.findViewById(R.id.priceTVBL);
            locationTV=itemView.findViewById(R.id.locationTV);
            caseSizeTVBL=itemView.findViewById(R.id.caseSizeTVBL);
            locationTV=itemView.findViewById(R.id.locationTV);
            yearTV=itemView.findViewById(R.id.yearTV);
            papersCB=itemView.findViewById(R.id.papersCB);
            boxCB=itemView.findViewById(R.id.boxCB);
            paperAndBoxRG=itemView.findViewById(R.id.paperAndBoxRG);
            typeeTV=itemView.findViewById(R.id.typeeTV);
            userImageCLSL=itemView.findViewById(R.id.userImageCLSL);
        }
    }

    public void setProductList(List<SearchModel> productList) {
        this.brandModelList = productList;
        notifyDataSetChanged();
    }


    private void showPopupMenu(View view, int position) {
        android.widget.PopupMenu popup = new android.widget.PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.menubutton, popup.getMenu());

        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();

    }

    private class MyMenuItemClickListener implements android.widget.PopupMenu.OnMenuItemClickListener {
        private int position;
        public MyMenuItemClickListener(int position) {
            this.position=position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {


                case R.id.message:
                    Intent intentt = new Intent(context, ChattingActivity.class);
                    intentt.putExtra("uuuID", "" + brandModelList.get(position).getCreateuserID());
                    intentt.putExtra("uuuName", "" + brandModelList.get(position).getPostmake());
                    context.startActivity(intentt);

                    break;


                case R.id.profile:
//                    Toast.makeText(context, "profile", Toast.LENGTH_SHORT).show();
                    Intent profIntent = new Intent(context, UserProfileActivity.class);
                    profIntent.putExtra("userID", "" + brandModelList.get(position).getCreateuserID());
                    context.startActivity(profIntent);
                    break;


                case R.id.report:
                    Intent intent = new Intent(context, ReportActivity.class);
                    intent.putExtra("postID", "" + brandModelList.get(pos).getPostID());
                    intent.putExtra("createuserID", "" + brandModelList.get(position).getCreateuserID());
                    intent.putExtra("postType", "" + brandModelList.get(pos).getPostype());
                    intent.putExtra("postmake", "" + brandModelList.get(pos).getPostmake());
                    intent.putExtra("casesize", "" + brandModelList.get(pos).getCasesize());
                    intent.putExtra("postmode", "" + brandModelList.get(pos).getPostmodel());
                    intent.putExtra("enterprice", "" + brandModelList.get(pos).getEnterprice());
                    intent.putExtra("postyear", "" + brandModelList.get(pos).getPostyear());
                    intent.putExtra("postarea", "" + brandModelList.get(pos).getPostaddress());
                    intent.putExtra("postcountry", "" + brandModelList.get(pos).getPostcountry());
                    intent.putExtra("watchbox", "" + brandModelList.get(pos).getWatchbox());
                    intent.putExtra("paperwatch", "" + brandModelList.get(pos).getPaperwatch());
                    context.startActivity(intent);
                    break;

                case R.id.blockUser:
                    clicks.block(pos, brandModelList.get(position).getCreateuserID());
                    break;
            }
            return false;
        }
    }


    public interface Clicks {
        void message(int position);

        void block( int position, String userID);
    }

}
