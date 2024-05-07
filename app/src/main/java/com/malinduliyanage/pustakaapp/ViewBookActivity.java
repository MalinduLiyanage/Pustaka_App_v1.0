package com.malinduliyanage.pustakaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejowan.cutetoast.CuteToast;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewBookActivity extends AppCompatActivity {

    boolean isLoggedin = false;
    private String Lib_ID = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        Button btn_lend = (Button) findViewById(R.id.LendBtn);
        TextView text_title = (TextView) findViewById(R.id.titleText);
        TextView author_title = (TextView) findViewById(R.id.authorText);
        TextView desc_title = (TextView) findViewById(R.id.descText);
        TextView isbn_title = (TextView) findViewById(R.id.isbnText);
        TextView publisher_title = (TextView) findViewById(R.id.publisherText);
        TextView pages_title = (TextView) findViewById(R.id.pagesText);
        TextView copies_title = (TextView) findViewById(R.id.copiesText);
        ImageView book_thumb = (ImageView) findViewById(R.id.viewbookThumb);

        String book_id = getIntent().getStringExtra("book_id");
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String desc = getIntent().getStringExtra("desc");
        String thumb = getIntent().getStringExtra("thumb");
        String isbn = getIntent().getStringExtra("isbn");
        String pages = getIntent().getStringExtra("pages");
        String publisher = getIntent().getStringExtra("publisher");

        Picasso.get()
                .load(thumb)
                .resize(450, 590)
                .placeholder(R.drawable.pustaka_logo)
                .into(book_thumb, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Image loaded successfully from URL
                    }

                    @Override
                    public void onError(Exception e) {
                        loadFromFile(book_thumb, thumb);
                    }
                });


        if (!checkLoginStatus()) {
            Intent intent = new Intent(ViewBookActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else {

            text_title.setText(title);
            author_title.setText("By " + author);
            desc_title.setText(desc);
            isbn_title.setText("ISBN : " + isbn);
            publisher_title.setText("Publisher : " + publisher);
            pages_title.setText("Pages : " + pages);

            copies_title.setText("Copies left : " + String.valueOf(getCopiescount(book_id)));

            if (getCopiescount(book_id) == 0){
                btn_lend.setText("Ran Out!");
                btn_lend.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
            }

            btn_lend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookAvailability(book_id);
                    copies_title.setText("Copies left : " + String.valueOf(getCopiescount(book_id)));
                    if (getCopiescount(book_id) == 0){
                        btn_lend.setText("Ran Out!");
                        btn_lend.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ViewBookActivity.this, R.color.colorPrimaryDark)));
                    }
                }
            });

        }
    }

    private void loadFromFile(ImageView imageView, String filePath) {
        File imageFile = new File(filePath);
        Picasso.get()
                .load(imageFile)
                .resize(450, 590)
                .placeholder(R.drawable.pustaka_logo)
                .into(imageView);
    }

    private int getCopiescount(String book_id) {

        int currentCount = 0;
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                "books",
                new String[]{"book_copies_count"},
                "book_id = ?",
                new String[]{book_id},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            currentCount = cursor.getInt(cursor.getColumnIndexOrThrow("book_copies_count"));
            cursor.close();


        }
        db.close();
        return currentCount;

    }

    private void bookAvailability(String bookId) {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (getCopiescount(bookId) > 0) {
            ContentValues values = new ContentValues();
            values.put("book_copies_count", getCopiescount(bookId) - 1);

            int rowsAffected = db.update("books", values, "book_id = ?", new String[]{bookId});

            if (rowsAffected > 0) {
                lendBooks(bookId);

            } else {
                CuteToast.ct(this, "Failed to update book count", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();

            }
        } else {
            CuteToast.ct(this, "No copies left for this book", CuteToast.LENGTH_LONG, CuteToast.INFO, true).show();

        }
        db.close();
    }


    private void lendBooks(String b_id) {
        if (!Lib_ID.isEmpty() && !b_id.isEmpty()) {

            SQLiteHelper dbHelper = new SQLiteHelper(this);

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String timestamp = getCurrentTimestamp();

            ContentValues values = new ContentValues();
            values.put("lib_id", Lib_ID);
            values.put("book_id", b_id);
            values.put("timestamp", timestamp);

            long result = db.insert("userlendings", null, values);
            db.close();

            if (result != -1) {
                CuteToast.ct(this, "Book lended successfully", CuteToast.LENGTH_LONG, CuteToast.SUCCESS, true).show();

            } else {
                CuteToast.ct(this, "Database Error!", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();

            }
        } else {
            CuteToast.ct(this, "App error!", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();

        }

    }

    private boolean checkLoginStatus() {
        try {
            String fileName = "login.pustaka";
            File file = new File(getFilesDir(), fileName);
            if (file.exists()) {
                FileInputStream fis = openFileInput(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String libId = bufferedReader.readLine();
                bufferedReader.close();

                Lib_ID = libId;
                isLoggedin = true;
                return true;
            } else {

                isLoggedin = false;
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getCurrentTimestamp() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        return sdf.format(new Date(currentTimeMillis));
    }

}