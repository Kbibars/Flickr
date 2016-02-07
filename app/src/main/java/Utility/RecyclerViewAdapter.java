package Utility;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kbibars.com.flickr.R;
import kbibars.com.flickr.UserActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public int mActivyID;
    private ArrayList<SingleResponse> singleResponses;
    private Context mContext;

    /*Adapter Constructor*/
    public RecyclerViewAdapter(ArrayList<SingleResponse> myDataset, Context context, int mActivityID) {
        singleResponses = myDataset;
        mContext = context;
        mActivyID = mActivityID;
    }

    /*Create new views*/
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item, parent, false);
        return new ViewHolder(v);
    }

    /*Replace the contents of a view*/
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /*Get the data from the arraylist and put them into your views*/
        holder.mTextView.setText(singleResponses.get(position).getTitle());
        //Make the URL to call in picasso to load the picture
        String mImageURL = "https://farm" + singleResponses.get(position).getFarm() + ".staticflickr.com/" + singleResponses.get(position).getServer() + "/" + singleResponses.get(position).getId()
                + "_" + singleResponses.get(position).getSecret() + ".jpg";
        /* Loading the Image using Picasso Library and adding a placeHolder icon*/
        Picasso.with(mContext)
                .load(mImageURL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.mImageView);
        /*Set RecylerView onclicklistner*/
        holder.mSingleLatyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivyID == 1) {
                    Intent mIntent = new Intent(mContext, UserActivity.class);
                    mIntent.putExtra("mOwnerID", singleResponses.get(position).getOwner());
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(mIntent);
                }
            }
        });

    }

    /*Return the size of your Arraylist of singleResponse*/
    @Override
    public int getItemCount() {
        return singleResponses.size();
    }

    /*Provide a reference to the views for each data item usinga ViewHodler*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public LinearLayout mSingleLatyout;

        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.mImage_view);
            mTextView = (TextView) v.findViewById(R.id.mTextview);
            mSingleLatyout = (LinearLayout) v.findViewById(R.id.mSingleLatyout);
        }


    }
}