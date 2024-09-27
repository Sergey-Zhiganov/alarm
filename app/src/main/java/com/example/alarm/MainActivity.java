package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private EditText edtHour, edtMinute;
    private Button btnSetAlarm;
    private Vibrator vibrator;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtHour = findViewById(R.id.edtHour);
        edtMinute = findViewById(R.id.edtMinute);
        btnSetAlarm = findViewById(R.id.btnSetAlarm);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        timer = new Timer();

        btnSetAlarm.setOnClickListener(view -> {
            String hourStr = edtHour.getText().toString();
            String minuteStr = edtMinute.getText().toString();

            if (!hourStr.isEmpty() && !minuteStr.isEmpty()) {
                int hour = Integer.parseInt(hourStr);
                int minute = Integer.parseInt(minuteStr);
                setAlarm(hour, minute);
            } else {
                Toast.makeText(MainActivity.this, "Пожалуйста, введите время для будильника", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAlarm(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        Calendar alarmTime = (Calendar) calendar.clone();

        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minute);
        alarmTime.set(Calendar.SECOND, 0);

        if (alarmTime.before(calendar)) {
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
        }

        long timeUntilAlarm = alarmTime.getTimeInMillis() - calendar.getTimeInMillis();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    vibrate();
                });
            }
        }, timeUntilAlarm);

        Toast.makeText(MainActivity.this, "Будильник установлен", Toast.LENGTH_SHORT).show();
    }

    private void vibrate() {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(1000);
        } else {
            Toast.makeText(MainActivity.this, "Вибрация не поддерживается на этом устройстве", Toast.LENGTH_SHORT).show();
        }
    }
}
