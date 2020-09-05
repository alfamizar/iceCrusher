package com.complementarycode.icecrusher;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static SharedPreferences prefs = null;
    TextView highScoreTxt;
    //public static SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
                //finish();
            }
        });

        highScoreTxt = findViewById(R.id.highScoreTxt);

        prefs = getSharedPreferences("game", MODE_PRIVATE);

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Menu.class));
            }
        });
        updateHighScore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHighScore();
    }

    public void updateHighScore() {
        highScoreTxt.setText("HighScore: " + prefs.getInt("highscore", 0));
    }

}