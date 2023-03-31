package com.project.thevoice;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mAuth = FirebaseAuth.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button mapviewbutton= findViewById(R.id.MapViewButton);
        final Button loginbutton = findViewById(R.id.LoginButton);

        final EditText emailid = findViewById(R.id.EmailAddress);
        final EditText password = findViewById(R.id.Password);
        final TextView signup = findViewById(R.id.signup);
        final TextView forgotpassword = findViewById(R.id.forgot_password);

        forgotpassword.setOnTouchListener((v, event) -> {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 1.2f);
                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 1.2f);
                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, scaleX, scaleY);
                    animator.setDuration(200);
                    animator.start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    PropertyValuesHolder scaleX2 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f, 1.0f);
                    PropertyValuesHolder scaleY2 = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f, 1.0f);
                    ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(v, scaleX2, scaleY2);
                    animator2.setDuration(200);
                    animator2.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            if (TextUtils.isEmpty(emailid.getText().toString()))
                            {
                                // Display an error message
                                emailid.setError("This field is required.");
                                emailid.requestFocus();
                            }
                            else
                            {
                                mAuth.sendPasswordResetEmail(emailid.getText().toString().trim())
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Password reset email has been sent successfully", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    });
                    animator2.start();
                    break;
            }
            return true;
        });





        signup.setOnTouchListener((v, event) -> {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 1.2f);
                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 1.2f);
                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, scaleX, scaleY);
                    animator.setDuration(200);
                    animator.start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    PropertyValuesHolder scaleX2 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f, 1.0f);
                    PropertyValuesHolder scaleY2 = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f, 1.0f);
                    ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(v, scaleX2, scaleY2);
                    animator2.setDuration(200);
                    animator2.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startActivity(new Intent(MainActivity.this,SignUpActivity.class));
                        }
                    });
                    animator2.start();
                    break;
            }
            return true;
        });

        loginbutton.setOnClickListener(view -> {
            // Check if the text is empty
            if (TextUtils.isEmpty(emailid.getText().toString()))
            {
                // Display an error message
                emailid.setError("This field is required.");
                emailid.requestFocus();
            }
            else if (TextUtils.isEmpty(password.getText().toString()))
            {
                password.setError("This field is required.");
                password.requestFocus();
            }
            else
            {
                mAuth.signInWithEmailAndPassword(emailid.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(MainActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null && !user.isEmailVerified()) {
                                    Toast.makeText(getApplicationContext(), "Your email address is not yet verified. Please follow the instructions in the verification email we sent you to complete the process.", Toast.LENGTH_LONG).show();
                                }
                                if (user != null && user.isEmailVerified()) {
                                    startActivity(new Intent(MainActivity.this,home.class));
                                }

                            } else {
                                // Handle errors
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                        });
            }
        });

        mapviewbutton.setOnClickListener(v -> startActivity(new Intent(v.getContext(), MapsActivity.class)));
    }
    }
