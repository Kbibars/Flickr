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
import kbibars.com.flickr.User_Activity;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public int mActivyID;
    private ArrayList<SingleResponse> mDataset;
    private Context mcontext;

     /*Provide a reference to the views for each data item usinga ViewHodler*/

    /*Adapter Constructor*/
    public MyAdapter(ArrayList<SingleResponse> myDataset, Context context, int mActivityID) {
        mDataset = myDataset;
        mcontext = context;
        mActivyID = mActivityID;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getTitle());
        //Make the URL to call in picasso to load the picture
        String mImageURL = "https://farm" + mDataset.get(position).getFarm() + ".staticflickr.com/" + mDataset.get(position).getServer() + "/" + mDataset.get(position).getId()
                + "_" + mDataset.get(position).getSecret() + ".jpg";
/*        Loading the Image using Picasso Library and adding a placeHolder icon*/
        Picasso.with(mcontext)
                .load(mImageURL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.mImageView);
        /*Set RecylerView onclicklistner*/
        holder.mSingleLatyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivyID == 1) {
                    Intent mIntent = new Intent(mcontext, User_Activity.class);
                    mIntent.putExtra("mOwnerID", mDataset.get(position).getOwner());
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mcontext.startActivity(mIntent);
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public LinearLayout mSingleLatyout;

        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.mImage_view);
            mTextView = (TextView) v.findViewById(R.id.mTextview);
            mSingleLatyout = (LinearLayout) v.findViewById(R.id.mSingleLatyout);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(v.getContext(), "Toast", Toast.LENGTH_SHORT).show();
                }
            });


        }


    }
}