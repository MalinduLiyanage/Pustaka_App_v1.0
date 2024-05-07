package com.malinduliyanage.pustakaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
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

import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 101;
    private EditText text_name;
    private EditText text_pwd;
    private Button reg_btn;
    private TextView textView;
    private SQLiteHelper dbHelper;
    private String townSaver = "None";
    private MapView map;
    private List<Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new SQLiteHelper(this);

        text_name = findViewById(R.id.nameText);
        text_pwd = findViewById(R.id.passwordTxt);
        reg_btn = findViewById(R.id.regBtn);
        textView = findViewById(R.id.logTxt);

        setLoginLink();

        Configuration.getInstance().setUserAgentValue("com.example.map");

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); // Set the tile source
        map.setMultiTouchControls(true); // Enable multi-touch zoom and pan
        GeoPoint startPoint = new GeoPoint(6.9177, 79.9214);
        map.getController().setCenter(startPoint);
        map.getController().setZoom(15.0);

        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                double latitude = p.getLatitude();
                double longitude = p.getLongitude();

                clearMarkers();

                Marker startMarker = new Marker(map);
                startMarker.setPosition(p);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);
                startMarker.setTitle("Are you here?");
                markers.add(startMarker);
                map.invalidate();
                reverseGeocode(latitude, longitude);

                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return true;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        map.getOverlays().add(0, mapEventsOverlay);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    registerUser();
                } else {
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
                }
            }
        });
    }
    private void setLoginLink() {
        String text = "Already have an Account? Login";
        SpannableString spannableString = new SpannableString(text);

        int orangeColor = getResources().getColor(R.color.colorPrimary);
        int registerIndex = text.indexOf("Login");
        spannableString.setSpan(new ForegroundColorSpan(orangeColor),
                registerIndex, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        };
        spannableString.setSpan(clickableSpan, registerIndex, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }
    private void registerUser() {
        String name = text_name.getText().toString();
        String password = text_pwd.getText().toString();

        if (!name.isEmpty() && !password.isEmpty()) {
            String libId = generateLibraryId();
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("lib_id", libId);
            values.put("name", name);
            values.put("password", password);
            values.put("location", townSaver);
            values.put("isloggedin", 0);

            long result = db.insert("users", null, values);
            db.close();

            if (result != -1) {
                sendSMS(libId);
                CuteToast.ct(this, "See your SMS inbox for Library ID!", CuteToast.LENGTH_LONG, CuteToast.SUCCESS, true).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();

            } else {
                CuteToast.ct(this, "Registration Failed!", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();

            }
        } else {
            CuteToast.ct(this, "Did you fill all fields?", CuteToast.LENGTH_LONG, CuteToast.WARN, true).show();

        }
    }
    private String generateLibraryId() {
        Random random = new Random();
        int libId = random.nextInt(900000) + 100000;
        return String.valueOf(libId);
    }
    private void sendSMS(String libId) {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("phoneNumber", null, "Your Pustaka library ID is: " + libId, null, null);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerUser();

            } else {
                CuteToast.ct(this, "SMS permission denied. You will not get Library ID!.", CuteToast.LENGTH_LONG, CuteToast.ERROR, true).show();

            }
        }
    }
    private void reverseGeocode(double latitude, double longitude) {

        final String requestString = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" +
                Double.toString(latitude) + "&lon=" + Double.toString(longitude) + "&zoom=18&addressdetails=1";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(requestString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        JSONObject address = jsonResponse.getJSONObject("address");

                        String townName = "";
                        if (address.has("town")) {
                            townName = address.getString("town");
                        } else if (address.has("village")) {
                            townName = address.getString("village");
                        }

                        final String finalTownName = townName;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!finalTownName.isEmpty()) {
                                    townSaver = finalTownName;
                                    CuteToast.ct(RegisterActivity.this, "Selected : " + finalTownName, CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                                } else {
                                    CuteToast.ct(RegisterActivity.this, "Not a Town, retry?", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                                }
                            }
                        });
                    }

                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void clearMarkers() {
        for (Marker marker : markers) {
            map.getOverlays().remove(marker);
        }
        markers.clear();
        map.invalidate();
    }
}
