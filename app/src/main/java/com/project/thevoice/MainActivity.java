package com.project.thevoice;

import static android.icu.text.ListFormatter.Type.OR;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button mapviewbutton= findViewById(R.id.MapViewButton);
        final Button loginbutton = findViewById(R.id.LoginButton);

        final EditText emailid = findViewById(R.id.EmailAddress);
        final EditText password = findViewById(R.id.Password);
        final TextView signup = findViewById(R.id.signup);

        signup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
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
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mAuth.fetchSignInMethodsForEmail("user@example.com")
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                List<? extends String> signInMethods = task.getResult().getSignInMethods();
                                if (signInMethods != null && signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                    // The email address is registered with Firebase Authentication and the user has signed in with email and password
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if (currentUser != null) {
                                        // User is signed in
                                    } else {
                                        // User is not signed in
                                    }
                                } else {
                                    // The email address is not registered with Firebase Authentication
                                }
                            } else {
                                // Error occurred
                                Exception exception = task.getException();
                            }
                        });
            }
        });

        mapviewbutton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(v.getContext(), MapsActivity.class));
            }
        });
    }
    }
