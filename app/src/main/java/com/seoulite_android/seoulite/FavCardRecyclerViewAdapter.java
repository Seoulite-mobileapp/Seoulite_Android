package com.seoulite_android.seoulite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FavCardRecyclerViewAdapter extends RecyclerView.Adapter<FavCardViewHolder> {
    // TODO: change generic type to FavEntry .?
    private List<FavVO> mFavList;

    public FavCardRecyclerViewAdapter(List<FavVO> favList) {
        this.mFavList = favList;
    }

    @NonNull
    @Override
    public FavCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_favorites, parent, false);
        return new FavCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavCardViewHolder holder, int position) {
        if (mFavList != null && position < mFavList.size()) {
            FavVO fav = mFavList.get(position);
            holder.mNameTextView.setText(fav.getName());
            holder.mMemoTextView.setText(fav.getMemo());
        }
    }

    @Override
    public int getItemCount() {
        return mFavList.size();
    }
}
