package com.seoulite_android.seoulite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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
            final FavVO fav = mFavList.get(position);
            holder.mStarIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                            .setTitle("ALERT")
                            .setMessage("Do you want to delete it from favorites ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DbHelper dbHelper = new DbHelper(v.getContext());
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    db.delete("FAVORITES", "_id=" + fav.getId(), null);
                                    db.close();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create();
                    dialog.show();
                }
            });
            holder.mNameTextView.setText(fav.getName());
            holder.mMemoTextView.setText(fav.getMemo());
        }
    }

    @Override
    public int getItemCount() {
        return mFavList.size();
    }
}
