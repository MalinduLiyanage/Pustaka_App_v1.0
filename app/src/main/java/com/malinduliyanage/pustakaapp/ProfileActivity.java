package com.malinduliyanage.pustakaapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rejowan.cutetoast.CuteToast;

public class ProfileActivity extends AppCompatActivity {

    private String name_Text = "", pass_Text = "";
    private TextView libId;
    private EditText nameText;
    private EditText passText;
    private EditText townText;
    private EditText loginstatusText;
    private EditText bookCount;
    private Button profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.profile_name);
        passText = findViewById(R.id.profile_password);

        String lib_id = getIntent().getStringExtra("lib_id");

        libId = findViewById(R.id.lib_id);
        profileBtn = findViewById(R.id.btn_unlockchanges);

        libId.setText("Library ID : " + lib_id);
        loadProfile(lib_id);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = profileBtn.getText().toString();
                if (buttonText.equals("Edit Account Info")) {

                    name_Text = nameText.getHint().toString();
                    pass_Text = passText.getHint().toString();
                    nameText.setEnabled(true);
                    passText.setEnabled(true);
                    nameText.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorPrimaryDark));
                    passText.setBackgroundColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorPrimaryDark));
                    profileBtn.setText("Save Changes");
                    CuteToast.ct(ProfileActivity.this,"Now you can Edit Name or Password", CuteToast.LENGTH_LONG, CuteToast.INFO, true).show();

                } else {

                    nameText.setEnabled(false);
                    passText.setEnabled(false);
                    nameText.setBackground(ContextCompat.getDrawable(ProfileActivity.this, R.drawable.edittext_background));
                    passText.setBackground(ContextCompat.getDrawable(ProfileActivity.this, R.drawable.edittext_background));
                    profileBtn.setText("Edit Account Info");
                    changeUserData(lib_id, name_Text, pass_Text);
                    name_Text = "";
                    pass_Text = "";
                    loadProfile(lib_id);
                }
            }
        });

        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name_Text = s.toString().trim();
            }
        });

        passText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pass_Text = s.toString().trim();
            }
        });


    }
    private void changeUserData(String libId, String newName, String newPassword) {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("password", newPassword);

        String selection = "lib_id = ?";
        String[] selectionArgs = {libId};

        int rowsAffected = db.update("users", values, selection, selectionArgs);

        if (rowsAffected > 0) {
            CuteToast.ct(this, "User data updated!", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();

        } else {
            CuteToast.ct(this, "Failed to update user data", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
        }
        db.close();
    }
    private void loadProfile(String lib_id) {

        nameText = findViewById(R.id.profile_name);
        passText = findViewById(R.id.profile_password);
        townText = findViewById(R.id.profile_town);
        loginstatusText = findViewById(R.id.profile_loginstatus);
        bookCount = findViewById(R.id.profile_lendedBookscount);

        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(
                "users",
                new String[]{"name", "password", "location", "isloggedin"},
                "lib_id = ?",
                new String[]{lib_id},
                null,
                null,
                null
        );

        String name = null;
        String password = null;
        String location = null;
        int logstatus = 0;
        int count = 0;

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            logstatus = cursor.getInt(cursor.getColumnIndexOrThrow("isloggedin"));
            cursor.close();
        }

        Cursor countCursor = db.rawQuery("SELECT COUNT(*) FROM userlendings WHERE lib_id = ?", new String[]{lib_id});
        if (cursor != null && countCursor.moveToFirst()) {
            count = countCursor.getInt(0);
            countCursor.close();
        }

        nameText.setHint(name);
        passText.setHint(password);
        townText.setHint(location);
        loginstatusText.setHint(String.valueOf(logstatus));
        bookCount.setHint(String.valueOf(count));

        db.close();
    }
}