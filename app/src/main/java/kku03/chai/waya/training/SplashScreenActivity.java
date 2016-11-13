package kku03.chai.waya.training;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //SoundEffect
        MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.intro_tata);
        mediaPlayer.start();

        //Auto intent
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        }, 9000); // 9 Sec / 9000 MilliSec

    }   //Main Method
}   //Main Class
