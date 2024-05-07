package com.malinduliyanage.pustakaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowLendedBookActivity extends AppCompatActivity {

    boolean areBookslended;
    private String Lib_ID = "";
    private RecyclerView lendedbookContainer;
    private LendedBookAdapter lendedbookAdapter;
    private List<LendedBook> lendedbookList;
    private SQLiteHelper dbHelper;
    private TextView bookLendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lended_book);

        String lib_id = getIntent().getStringExtra("lib_id");
        Lib_ID = lib_id;

        loadLendedBooks();

    }

    private void loadLendedBooks() {

        lendedbookContainer = findViewById(R.id.book_container);
        dbHelper = new SQLiteHelper(this);
        lendedbookList = new ArrayList<>();
        bookLendText = findViewById(R.id.book_lendcheck);
        lendedbookAdapter = new LendedBookAdapter(lendedbookList, this,bookLendText);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lendedbookContainer.setLayoutManager(layoutManager);
        lendedbookContainer.setAdapter(lendedbookAdapter);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"lib_id", "book_id", "timestamp"};
        String selection = "lib_id = ?";
        String[] selectionArgs = {Lib_ID};
        Cursor cursor = db.query("userlendings", projection, selection, selectionArgs, null, null, "timestamp DESC");

        if (cursor != null && cursor.moveToFirst()) {
            areBookslended = true;
            bookLendText.setVisibility(View.GONE);
            do {
                String lib_id = cursor.getString(cursor.getColumnIndexOrThrow("lib_id"));
                String book_id = cursor.getString(cursor.getColumnIndexOrThrow("book_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                String bookTitle = getBookTitle(db, book_id);

                LendedBook lendedbook = new LendedBook(book_id, bookTitle, lib_id, date);
                lendedbookList.add(lendedbook);

            } while (cursor.moveToNext());

            cursor.close();
            lendedbookAdapter.notifyDataSetChanged();
        }else{
            areBookslended = false;
            lendedbookContainer.setVisibility(View.GONE);
        }
        db.close();
    }

    private String getBookTitle(SQLiteDatabase db, String bookId) {
        String[] projection = {"book_title"};
        String selection = "book_id = ?";
        String[] selectionArgs = {bookId};

        Cursor cursor = db.query("books", projection, selection, selectionArgs, null, null, null);
        String bookTitle = "";

        if (cursor != null && cursor.moveToFirst()) {
            bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("book_title"));
            cursor.close();
        }

        return bookTitle;
    }
}