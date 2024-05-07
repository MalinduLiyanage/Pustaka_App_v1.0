package com.malinduliyanage.pustakaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rejowan.cutetoast.CuteToast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LoginActivity extends AppCompatActivity {

    private EditText text_libid;
    private EditText text_logpass;
    private Button log_btn;
    private TextView textView;
    public String libId_global = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text_libid = findViewById(R.id.libidText);
        text_logpass = findViewById(R.id.logpassTxt);
        log_btn = findViewById(R.id.logBtn);
        textView = findViewById(R.id.regTxt);

        setRegLink();

        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void setRegLink() {

        TextView textView = findViewById(R.id.regTxt);
        String text = "Don’t have an account? Register";
        SpannableString spannableString = new SpannableString(text);

        int orangeColor = getResources().getColor(R.color.colorPrimary);
        int registerIndex = text.indexOf("Register");
        spannableString.setSpan(new ForegroundColorSpan(orangeColor),
                registerIndex, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        };
        spannableString.setSpan(clickableSpan, registerIndex, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    private void loginUser() {
        String libId = text_libid.getText().toString();
        libId_global = libId;
        String password = text_logpass.getText().toString();

        authenticateUser(libId, password);
    }

    private void authenticateUser(String libId, String password) {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "lib_id = ? AND password = ?";
        String[] selectionArgs = {libId, password};
        Cursor cursor = db.query("users", null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put("isloggedin", 1);
            int rowsAffected = db.update("users", values, selection, selectionArgs);
            cursor.close();
            db.close();

            if (rowsAffected > 0) {
                writeLibIdToFile(libId);

            } else {
                CuteToast.ct(this, "Database error occurred!", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();
            }
        } else {
            CuteToast.ct(this, "No match found!", CuteToast.LENGTH_LONG, CuteToast.WARN, true).show();
        }
    }

    private void writeLibIdToFile(String libId) {
        try {
            String fileName = "login.pustaka";
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(libId);
            outputStreamWriter.close();
            navigateToMainActivity();

        } catch (IOException e) {
            e.printStackTrace();
            CuteToast.ct(this,"Logged in Success!", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
            finish();

        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        CuteToast.ct(this,"Logged in Success!", CuteToast.LENGTH_LONG, CuteToast.SUCCESS, true).show();
        intent.putExtra("LIBRARY_ID", libId_global);
        startActivity(intent);
        finish();
    }
}
