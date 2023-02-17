package com.example.my_hospital_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

public class Launcher_Screen extends AppCompatActivity {
 TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_screen);

         textView = findViewById(R.id.textViewLauncher);
        String text = "Appointments Scheduler";
        SpannableString spannableString = new SpannableString(text);
        int duration = 200; // Delay between each letter in milliseconds

        for (int i = 0; i < text.length(); i++) {
            TextAppearanceSpan appearanceSpan = new TextAppearanceSpan(this, R.style.TextAppearance);
            spannableString.setSpan(appearanceSpan, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            final int finalI = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView.setText(spannableString.subSequence(0, finalI + 1));
                }
            }, duration * i);
        }

    }
}