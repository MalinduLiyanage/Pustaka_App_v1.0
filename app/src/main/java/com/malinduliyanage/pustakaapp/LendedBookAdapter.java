package com.malinduliyanage.pustakaapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.rejowan.cutetoast.CuteToast;

import java.util.List;

public class LendedBookAdapter extends RecyclerView.Adapter<LendedBookAdapter.ViewHolder> {

    private List<LendedBook> lendedbookList;
    private Context context;
    private TextView emptyBooksTextView;
    public LendedBookAdapter(List<LendedBook> lendedbookList, Context context,TextView emptyBooksTextView) {
        this.lendedbookList = lendedbookList;
        this.context = context;
        this.emptyBooksTextView = emptyBooksTextView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_lended_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LendedBook lendedbook = lendedbookList.get(position);
        holder.titleTextView.setText(lendedbook.getbookTitle());
        holder.dateTextView.setText(lendedbook.getTimestamp());

        holder.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.pustaka_vec_log_reg);
                builder.setMessage("You are going to return your lended book " + "'" + lendedbook.getbookTitle() + "'" + "!");
                builder.setTitle("   Return book");
                builder.setCancelable(true);
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                builder.setPositiveButton("Return", (DialogInterface.OnClickListener) (dialog, which) -> {
                    deleteRecordFromUserLendings(lendedbook.getBookId(), lendedbook.getbookTitle(), lendedbook.getLibId(), lendedbook.getTimestamp());
                    lendedbookList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), lendedbookList.size());
                    dialog.cancel();
                    if (lendedbookList.size() == 0){
                        emptyBooksTextView.setVisibility(View.VISIBLE);
                    }

                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void deleteRecordFromUserLendings(String bookId, String booktitle, String libId, String timestamp) {
        SQLiteHelper dbHelper = new SQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int currentCopiesCount = getCurrentCopiesCount(db, bookId);
        int newCopiesCount = currentCopiesCount + 1;
        ContentValues values = new ContentValues();
        values.put("book_copies_count", newCopiesCount);
        db.update("books", values, "book_id = ?", new String[]{bookId});

        String selection = "book_id = ? AND lib_id = ? AND timestamp = ?";
        String[] selectionArgs = {bookId, libId, timestamp};
        db.delete("userlendings", selection, selectionArgs);

        db.close();
        CuteToast.ct(context,"Book returned successfully!", CuteToast.LENGTH_LONG, CuteToast.SUCCESS, true).show();
    }


    private int getCurrentCopiesCount(SQLiteDatabase db, String bookId) {
        int currentCopiesCount = 0;
        String[] projection = {"book_copies_count"};
        String selection = "book_id = ?";
        String[] selectionArgs = {bookId};
        Cursor cursor = db.query("books", projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            currentCopiesCount = cursor.getInt(cursor.getColumnIndexOrThrow("book_copies_count"));
            cursor.close();
        }
        return currentCopiesCount;
    }

    @Override
    public int getItemCount() {
        return lendedbookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        ImageButton returnBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.lend_title);
            dateTextView = itemView.findViewById(R.id.lend_date);
            returnBtn = itemView.findViewById(R.id.returnbook_btn);
        }
    }
}
