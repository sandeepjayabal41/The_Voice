package com.project.thevoice;

import static android.icu.text.ListFormatter.Type.OR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    FirebaseAuth mFirebaseAuth;
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


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

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
