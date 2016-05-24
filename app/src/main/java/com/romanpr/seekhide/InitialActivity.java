package com.romanpr.seekhide;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class InitialActivity extends AppCompatActivity {

    EditText playersNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/blood_font.ttf");
        TextView nameTextView = (TextView) findViewById(R.id.askName);
        playersNameView = (EditText) findViewById(R.id.playersName);
        nameTextView.setTypeface(typeFace);
        playersNameView.setTypeface(typeFace);

    }

    public void changeActivity(View view) {
        final Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("playerName", playersNameView.getText().toString());
        startActivity(i);
    }

}
