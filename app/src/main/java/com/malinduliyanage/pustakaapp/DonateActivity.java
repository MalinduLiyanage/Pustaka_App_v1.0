package com.malinduliyanage.pustakaapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rejowan.cutetoast.CuteToast;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class DonateActivity extends AppCompatActivity {

    private EditText titleText, authText, descText, isbnText, pageText, pubText, copiesText;
    private Button btnDonate;
    private String image_path = "null";
    private static final int REQUEST_IMAGE_GALLERY = 101;
    private ImageView bookThumb;
    private TextView libId;
    private boolean isImageloaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        String lib_id = getIntent().getStringExtra("lib_id");
        libId = findViewById(R.id.lib_id);
        libId.setText("Library ID : " + lib_id);

        titleText = findViewById(R.id.book_title);
        authText = findViewById(R.id.book_author);
        descText = findViewById(R.id.book_desc);
        isbnText = findViewById(R.id.book_isbn);
        pageText = findViewById(R.id.book_pages);
        pubText = findViewById(R.id.book_publisher);
        copiesText = findViewById(R.id.book_copies);
        btnDonate = findViewById(R.id.btn_donate);
        bookThumb = findViewById(R.id.book_thumb);

        bookThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleText.getText().toString();
                String author = authText.getText().toString();
                String desc = descText.getText().toString();
                String isbn = isbnText.getText().toString();
                String pages = pageText.getText().toString();
                String pub = pubText.getText().toString();
                String copies = copiesText.getText().toString();

                if (title.isEmpty() || author.isEmpty() || desc.isEmpty() || isbn.isEmpty() || pages.isEmpty() || pub.isEmpty() || copies.isEmpty()) {
                    CuteToast.ct(DonateActivity.this, "Fill all the fields!", CuteToast.LENGTH_LONG, CuteToast.WARN, true).show();
                }else{
                    if (isImageloaded == true){
                        saveImageToStorage();
                        donateBook(image_path, title, author, desc, isbn, pages, pub, copies);
                        titleText.setText("");
                        authText.setText("");
                        descText.setText("");
                        isbnText.setText("");
                        pubText.setText("");
                        copiesText.setText("");
                        Picasso.get()
                                .load(R.drawable.pustaka_logo)
                                .fit()
                                .centerCrop()
                                .into(bookThumb);

                    }else{
                        donateBook(image_path, title, author, desc, isbn, pages, pub, copies);
                        titleText.setText("");
                        authText.setText("");
                        descText.setText("");
                        isbnText.setText("");
                        pubText.setText("");
                        copiesText.setText("");
                    }
                }
            }
        });
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            isImageloaded = true;
            Uri imageUri = data.getData();
            Picasso.get().load(imageUri).fit().centerCrop().into(bookThumb);
        }
    }
    private void saveImageToStorage() {
        String filename = UUID.randomUUID().toString() + ".jpg";
        File file = new File(getFilesDir(), filename);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            Bitmap bitmap = ((BitmapDrawable) bookThumb.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            image_path = file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            CuteToast.ct(DonateActivity.this, "Error!", CuteToast.LENGTH_LONG, CuteToast.WARN, true).show();

        }
    }
    private void donateBook(String image_path, String title, String author, String desc, String isbn, String pages, String pub, String copies) {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        String bookId = generateBookId();
        values.put("book_id", bookId);
        values.put("book_title", title);
        values.put("book_author", author);
        values.put("book_desc", desc);
        values.put("book_thumb", image_path);
        values.put("book_isbn", isbn);
        values.put("book_pages", pages);
        values.put("book_publisher", pub);
        values.put("book_copies_count", copies);

        long newRowId = db.insert("books", null, values);
        db.close();

        if (newRowId != -1) {
            CuteToast.ct(DonateActivity.this, "Thank you!", CuteToast.LENGTH_LONG, CuteToast.SUCCESS, true).show();

        } else {
            CuteToast.ct(DonateActivity.this, "Error occured!", CuteToast.LENGTH_LONG, CuteToast.WARN, true).show();
        }
    }
    private String generateBookId() {
        Random random = new Random();
        int bookId = random.nextInt(900000) + 100000;
        return String.valueOf(bookId);
    }

}