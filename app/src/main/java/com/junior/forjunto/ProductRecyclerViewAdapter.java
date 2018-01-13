package com.junior.forjunto;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.junior.forjunto.mvp.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
    private List<Product> mDataset;
    private View.OnClickListener clickListener;


    public ProductRecyclerViewAdapter(List<Product> myDataset, View.OnClickListener listener) {
        clickListener = listener;
        mDataset = myDataset;
    }


    @Override
    public ProductRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);

        return new ViewHolder(v, clickListener);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productNameTextView.setText(mDataset.get(position).getName());
        holder.productDescriptionTextView.setText(mDataset.get(position).getTagline());
        holder.upvotesTextView.setText(mDataset.get(position).getVotesCount().toString());
        Picasso.with(App.getContextApp()).load(mDataset.get(position).getThumbnail().getImageUrl()).fit().into(holder.productImageView);
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView productImageView;
        TextView productNameTextView;
        TextView productDescriptionTextView;
        TextView upvotesTextView;

        ViewHolder(View v, View.OnClickListener listener) {
            super(v);
            v.setOnClickListener(listener);
            productImageView = v.findViewById(R.id.product_image);
            productNameTextView = v.findViewById(R.id.product_name_text_view);
            productDescriptionTextView = v.findViewById(R.id.product_description_text_view);
            upvotesTextView = v.findViewById(R.id.upvotes_text_view);
        }
    }
}