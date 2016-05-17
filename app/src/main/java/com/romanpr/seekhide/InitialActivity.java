package com.romanpr.seekhide;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        TextView nameTextView = (TextView) findViewById(R.id.askName);
        Typeface typeFace= Typeface.createFromAsset(getAssets(), "fonts/blood_font.ttf");
        nameTextView.setTypeface(typeFace);

        EditText playersName = (EditText) findViewById(R.id.playersName);
        playersName.setTypeface(typeFace);
    }

    public void changeActivity(View view) {
        // description of an operation
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
