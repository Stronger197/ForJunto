package com.junior.forjunto;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.junior.forjunto.mvp.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
    private List<Post> mDataset;
    private View.OnClickListener clickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductRecyclerViewAdapter(List<Post> myDataset, View.OnClickListener listener) {
        clickListener = listener;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, clickListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.productNameTextView.setText(mDataset.get(position).getName());
        holder.productDescriptionTextView.setText(mDataset.get(position).getTagline());
        // holder.upvotesTextView.setText(mDataset.get(position).getVotesCount());
        holder.upvotesTextView.setText(mDataset.get(position).getVotesCount().toString());
        Picasso.with(App.getContextApp()).load(mDataset.get(position).getThumbnail().getImageUrl()).fit().into(holder.productImageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView productImageView;
        public TextView productNameTextView;
        public TextView productDescriptionTextView;
        public TextView upvotesTextView;

        public ViewHolder(View v, View.OnClickListener listener) {
            super(v);
            v.setOnClickListener(listener);
            productImageView = v.findViewById(R.id.product_image);
            productNameTextView = v.findViewById(R.id.product_name_text_view);
            productDescriptionTextView = v.findViewById(R.id.product_description_text_view);
            upvotesTextView = v.findViewById(R.id.upvotes_text_view);
        }
    }
}