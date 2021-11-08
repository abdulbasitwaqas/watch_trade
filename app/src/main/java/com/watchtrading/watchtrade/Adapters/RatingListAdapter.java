package com.watchtrading.watchtrade.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.watchtrading.watchtrade.Models.RatingListModel;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;


import java.util.List;

public class RatingListAdapter extends RecyclerView.Adapter<RatingListAdapter.MyViewHolder> {
    private List<RatingListModel> ratingListModelList;
    private Context context;
    private Clicks clicks;

    public RatingListAdapter(List<RatingListModel> ratingListModelList, Context context, Clicks clicks) {
        this.ratingListModelList = ratingListModelList;
        this.context = context;
        this.clicks = clicks;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_list, null);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(""+ratingListModelList.get(position).getName());
        holder.date.setText(""+ratingListModelList.get(position).getCreated());
        holder.reviewMessageTV.setText(""+ratingListModelList.get(position).getReviewmessage());

        int rating = Integer.parseInt(ratingListModelList.get(position).getRatingpoints());
        int totalPersons = ratingListModelList.size();


        clicks.ratingData(rating, totalPersons);

        if (rating<=1){
            holder.oneStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.twoStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
            holder.threeStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
            holder.fourStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
            holder.fiveStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
        }
        else if (rating==2){

            holder.oneStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.twoStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.threeStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
            holder.fourStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
            holder.fiveStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
        }
        else if (rating==3){

            holder.oneStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.twoStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.threeStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.fourStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
            holder.fiveStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
        }
        else if (rating==4){

            holder.oneStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.twoStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.threeStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.fourStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.fiveStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
        }
        else if (rating==5){

            holder.oneStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.twoStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.threeStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.fourStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
            holder.fiveStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_selected_star));
        }
        else if (rating==0){
            holder.oneStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
            holder.twoStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
            holder.threeStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
            holder.fourStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
            holder.fiveStar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_star_unselcted));
        }


        if (SharedPreferencesSotreData.getInstance().getID().equals(""+ratingListModelList.get(position).getRatinguserID())){
            holder.reviewReportBtn.setVisibility(View.VISIBLE);
        }


        holder.reviewReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks.ratingReview(v, ratingListModelList.get(position).getRatingID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return ratingListModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, reviewMessageTV;
        private Button reviewReportBtn;
        private ImageView oneStar, twoStar, threeStar, fourStar, fiveStar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewMessageTV = itemView.findViewById(R.id.reviewMessageTV);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            reviewReportBtn = itemView.findViewById(R.id.reviewReportBtn);
            oneStar = itemView.findViewById(R.id.oneStar);
            twoStar = itemView.findViewById(R.id.twoStar);
            threeStar = itemView.findViewById(R.id.threeStar);
            fourStar = itemView.findViewById(R.id.fourStar);
            fiveStar = itemView.findViewById(R.id.fiveStar);
        }
    }


    public interface Clicks {
        void ratingReview(View view, String ratingID);

        void ratingData(int totalRating, int totalPersons);
    }
}
