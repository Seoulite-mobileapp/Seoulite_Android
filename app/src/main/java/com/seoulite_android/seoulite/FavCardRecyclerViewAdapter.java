package com.seoulite_android.seoulite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class FavCardRecyclerViewAdapter extends RecyclerView.Adapter<FavCardViewHolder> {
    // TODO: change generic type to FavEntry .?
    private List<String> mFavList;

    @NonNull
    @Override
    public FavCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FavCardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
