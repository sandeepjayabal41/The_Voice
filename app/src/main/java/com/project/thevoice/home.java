package com.project.thevoice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class home extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView textView = findViewById(R.id.greeting_Text);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(3000); // in milliseconds
        //animation.setRepeatCount(Animation.INFINITE); // repeat indefinitely

        textView.startAnimation(animation);

        TextView textView1 = findViewById(R.id.user_Name);

        Animation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(3000); // in milliseconds
        //animation.setRepeatCount(Animation.INFINITE); // repeat indefinitely

        textView.startAnimation(animation1);

    }
}
