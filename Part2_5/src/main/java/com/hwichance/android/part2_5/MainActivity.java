package com.hwichance.android.part2_5;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_Vibration;
    Button btn_PatternVibration;
    Button btn_System_Beep;
    Button btn_Custom_Sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Vibration = (Button) findViewById(R.id.btn_vibration);
        btn_PatternVibration = (Button) findViewById(R.id.btn_pattern_vibration);
        btn_System_Beep = (Button) findViewById(R.id.btn_system_beep);
        btn_Custom_Sound = (Button) findViewById(R.id.btn_custom_sound);

        btn_Vibration.setOnClickListener(this);
        btn_PatternVibration.setOnClickListener(this);
        btn_System_Beep.setOnClickListener(this);
        btn_Custom_Sound.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v == btn_Vibration) {
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);
        }
        else if(v == btn_PatternVibration) {
            Vibrator pattern_vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            pattern_vib.vibrate(new long[] {500, 1000, 500, 1000}, -1);
        }
        else if(v == btn_System_Beep) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();
        }
        else if(v == btn_Custom_Sound) {
            MediaPlayer player = MediaPlayer.create(this, R.raw.fallbackring);
            player.start();
        }
    }
}
