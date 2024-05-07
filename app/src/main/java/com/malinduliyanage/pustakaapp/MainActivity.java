package com.malinduliyanage.pustakaapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rejowan.cutetoast.CuteToast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    boolean isLoggedin = false;
    boolean areBookslended;
    private String Lib_ID = "";
    private FrameLayout contentViewContainer;
    private LinearLayout bottomNavigationBar;
    private ImageButton homeButton;
    private ImageButton lendABookButton;
    private ImageButton profileButton;
    private RecyclerView bookContainer;
    private SQLiteHelper dbHelper;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private RecyclerView lendedbookContainer;
    private LendedBookAdapter lendedbookAdapter;
    private List<LendedBook> lendedbookList;
    private RecyclerView searchbookContainer;
    private SearchBookAdapter searchbookAdapter;
    private List<SearchBook> searchbookList;
    private TextView bookLendText;
    private int view = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentViewContainer = findViewById(R.id.content_view_container);
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        homeButton = findViewById(R.id.home_button);
        lendABookButton = findViewById(R.id.lend_a_book_button);
        profileButton = findViewById(R.id.profile_button);

        if (!checkLoginStatus()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else {

            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showHomeView();
                }
            });

            lendABookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSearchABookView();
                }
            });

            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProfileView();
                }
            });

            showHomeView();
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(view == 1){
            showHomeView();
        }else if(view == 2){
            showSearchABookView();
        }else if(view == 3){
            showProfileView();
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
                view = 1;

                return true;

            } else {

                isLoggedin = false;
                view = 0;

                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }
    private void showHomeView() {

        contentViewContainer.removeAllViews();
        View homeView = getLayoutInflater().inflate(R.layout.home_view, null);
        contentViewContainer.addView(homeView);

        view = 1;

        lendedbookContainer = findViewById(R.id.lended_container);
        bookLendText = findViewById(R.id.book_lend_check);

        loadRecentBooks();
        loadLendedBooks();
        loadGreetingandId();

        if(!areBookslended){
            lendedbookContainer.setVisibility(View.GONE);
        }else{
            bookLendText.setVisibility(View.GONE);
        }

    }
    private void loadGreetingandId() {

        TextView greetingText = findViewById(R.id.time_Text);
        TextView libidText = findViewById(R.id.libid_Text);

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        libidText.setText("Library ID : " + Lib_ID);

        if (hour >= 0 && hour < 12) {
            greetingText.setText("Good Morning!");
        } else if (hour >= 12 && hour < 15) {
            greetingText.setText("Good Afternoon!");
        } else {
            greetingText.setText("Good Evening!");
        }

    }
    private void loadLendedBooks() {

        bookLendText = findViewById(R.id.book_lend_check);
        lendedbookContainer = findViewById(R.id.lended_container);
        dbHelper = new SQLiteHelper(this);
        lendedbookList = new ArrayList<>();
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
    private void loadRecentBooks() {

        bookContainer = findViewById(R.id.book_container);
        dbHelper = new SQLiteHelper(this);
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        bookContainer.setLayoutManager(layoutManager);
        bookContainer.setAdapter(bookAdapter);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"book_id", "book_title", "book_author", "book_desc","book_thumb","book_isbn","book_pages","book_publisher","book_copies_count"};
        Cursor cursor = db.query("books", projection, null, null, null, null, "id DESC");

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String book_id = cursor.getString(cursor.getColumnIndexOrThrow("book_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("book_title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("book_author"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("book_desc"));
                String thumb = cursor.getString(cursor.getColumnIndexOrThrow("book_thumb"));
                String isbn = cursor.getString(cursor.getColumnIndexOrThrow("book_isbn"));
                String pages = cursor.getString(cursor.getColumnIndexOrThrow("book_pages"));
                String publisher = cursor.getString(cursor.getColumnIndexOrThrow("book_publisher"));
                int copies = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("book_copies_count")));
                String copiesAsString = String.valueOf(copies);

                Book book = new Book(book_id, title, author, desc, thumb, isbn, pages, publisher, copiesAsString);
                bookList.add(book);
                count++;

            } while (cursor.moveToNext() && count < 8);

            cursor.close();
            bookAdapter.notifyDataSetChanged();
        }

        db.close();
    }
    private void showSearchABookView() {

        contentViewContainer.removeAllViews();
        View lendABookView = getLayoutInflater().inflate(R.layout.search_a_book_view, null);
        contentViewContainer.addView(lendABookView);

        view = 2;

        loadSearchBooks();
        setupSearchListener();

    }
    private void setupSearchListener() {

        EditText lendText = findViewById(R.id.lendText);

        lendText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().trim();
                searchBooks(searchText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void searchBooks(String searchText) {

        searchbookList.clear();

        if (searchText.isEmpty()) {
            loadSearchBooks();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"book_id", "book_title", "book_author", "book_desc","book_thumb","book_isbn","book_pages","book_publisher"};
        String selection = "book_title LIKE ? OR book_author LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchText + "%", "%" + searchText + "%"};

        Cursor cursor = db.query("books", projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String book_id = cursor.getString(cursor.getColumnIndexOrThrow("book_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("book_title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("book_author"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("book_desc"));
                String thumb = cursor.getString(cursor.getColumnIndexOrThrow("book_thumb"));
                String isbn = cursor.getString(cursor.getColumnIndexOrThrow("book_isbn"));
                String pages = cursor.getString(cursor.getColumnIndexOrThrow("book_pages"));
                String publisher = cursor.getString(cursor.getColumnIndexOrThrow("book_publisher"));

                SearchBook searchbook = new SearchBook(book_id, title, author, desc, thumb, isbn, pages, publisher);
                searchbookList.add(searchbook);
            } while (cursor.moveToNext());

            cursor.close();
            searchbookAdapter.notifyDataSetChanged();
        }else{
            searchbookList.clear();
            searchbookAdapter.notifyDataSetChanged();
            CuteToast.ct(this,"No Books Found!", CuteToast.LENGTH_LONG, CuteToast.INFO, true).show();
        }

        db.close();
    }
    private void loadSearchBooks() {

        searchbookContainer = findViewById(R.id.book_container);
        dbHelper = new SQLiteHelper(this);
        searchbookList = new ArrayList<>();
        searchbookAdapter = new SearchBookAdapter(searchbookList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchbookContainer.setLayoutManager(layoutManager);
        searchbookContainer.setAdapter(searchbookAdapter);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"book_id", "book_title", "book_author", "book_desc","book_thumb","book_isbn","book_pages","book_publisher"};
        Cursor cursor = db.query("books", projection, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String book_id = cursor.getString(cursor.getColumnIndexOrThrow("book_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("book_title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("book_author"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("book_desc"));
                String thumb = cursor.getString(cursor.getColumnIndexOrThrow("book_thumb"));
                String isbn = cursor.getString(cursor.getColumnIndexOrThrow("book_isbn"));
                String pages = cursor.getString(cursor.getColumnIndexOrThrow("book_pages"));
                String publisher = cursor.getString(cursor.getColumnIndexOrThrow("book_publisher"));

                SearchBook searchbook = new SearchBook(book_id, title, author, desc, thumb, isbn, pages, publisher);
                searchbookList.add(searchbook);

            } while (cursor.moveToNext());

            cursor.close();
            searchbookAdapter.notifyDataSetChanged();
        }
        db.close();

    }
    private void showProfileView() {
        contentViewContainer.removeAllViews();
        View profileView = getLayoutInflater().inflate(R.layout.profile_view, null);
        contentViewContainer.addView(profileView);

        view = 3;

        TextView libID = findViewById(R.id.lib_id);
        libID.setText("Library ID : " + Lib_ID);

        CardView lendBook = findViewById(R.id.lendbook_btn);
        CardView donateBook = findViewById(R.id.donatebook_btn);
        CardView profileBtn = findViewById(R.id.profile_btn);
        CardView logoutBtn = findViewById(R.id.logout_btn);

        lendBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowLendedBookActivity.class);
                intent.putExtra("lib_id", Lib_ID);
                startActivity(intent);
            }
        });

        donateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DonateActivity.class);
                intent.putExtra("lib_id", Lib_ID);
                startActivity(intent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("lib_id", Lib_ID);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.pustaka_vec_log_reg);
                builder.setMessage("Are you sure you want to logout ?");
                builder.setTitle("   Pustaka App");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    logoutUser();
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }
    private void logoutUser() {
        try {
            String fileName = "login.pustaka";
            File file = new File(getFilesDir(), fileName);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    isLoggedin = false;
                    Lib_ID = "";
                    CuteToast.ct(this, "Logged out Success! ", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    CuteToast.ct(this, "Operation failed!", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
                }
            } else {

                CuteToast.ct(this, "App error! ", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            CuteToast.ct(this, "Error!", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
        }
    }

}