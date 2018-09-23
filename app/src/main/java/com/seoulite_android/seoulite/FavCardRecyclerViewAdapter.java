package com.seoulite_android.seoulite;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FavCardRecyclerViewAdapter extends RecyclerView.Adapter<FavCardViewHolder> {

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
    public void onBindViewHolder(@NonNull final FavCardViewHolder holder, int position) {
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
                                    Intent intent = new Intent("DB_CHANGED");
                                    intent.putExtra("isUpdate", false);
                                    v.getContext().sendBroadcast(intent);
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

            holder.mMemoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final EditText memoEdit = new EditText(v.getContext());
                    memoEdit.setText(((TextView)v).getText().toString());
                    AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                            .setTitle("Edit Memo")
                            .setMessage("Write a memo.")
                            .setView(memoEdit)
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("name", fav.getName());
                                    contentValues.put("is_district", fav.getIsDistrict());
                                    contentValues.put("is_agency", fav.getIsAgency());
                                    contentValues.put("memo", memoEdit.getText().toString());

                                    SQLiteDatabase db = new DbHelper(v.getContext()).getWritableDatabase();
                                    int updatedNum = db.update("FAVORITES", contentValues,
                                            "_id=" + fav.getId(), null);
                                    if (updatedNum == 0) {
                                        Toast.makeText(v.getContext(),"Error Editing.. Please try again.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(v.getContext(), "Edited.", Toast.LENGTH_SHORT).show();
                                        db.close();
                                        Intent intent = new Intent("DB_CHANGED");
                                        intent.putExtra("isUpdate", true);
                                        intent.putExtra("position", holder.getAdapterPosition());
                                        v.getContext().sendBroadcast(intent);
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
