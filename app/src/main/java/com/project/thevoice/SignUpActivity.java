package com.project.thevoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    DomainCheck dc = new DomainCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String[] items = { "Select Your University","Algoma University","Brock University","Carleton University","Lakehead University","Laurentian University","McMaster University","Nipissing University","OCAD University","Ontario Tech University","Queen's University","Royal Military College of Canada","Ryerson University","Trent University","University of Guelph","University of Ottawa","University of Toronto","University of Waterloo","University of Windsor","Western University","Wilfrid Laurier University","York University" };

        Button SignUpButton = findViewById(R.id.SignUpButton);
        EditText fn = findViewById(R.id.FirstName);
        EditText ln = findViewById(R.id.LastName);
        EditText eid = findViewById(R.id.EnterEmail);
        EditText pwd = findViewById(R.id.EnterPassword);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                SignUpButton.setEnabled(!spinner.getSelectedItem().toString().equals("Select Your University"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        progressDialog = new ProgressDialog(this);
        SignUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String firstname = fn.getText().toString();
                String lastname = ln.getText().toString();
                String emailid = eid.getText().toString().trim();
                String password = pwd.getText().toString();
                String university = spinner.getSelectedItem().toString();

                progressDialog.setMessage("We're setting up your account, this may take a moment...");

                if (dc.domainCheck(university,emailid))
                {
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(emailid, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            firebaseFirestore.collection("Users")
                                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                    .set(new UserModel(firstname, lastname, emailid, university));
                            progressDialog.cancel();
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setTitle("All set");
                            builder.setMessage("Great news! We have just sent a verification link to your email. Please check your inbox and click the link to verify your account.");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                }
                            });
                            AlertDialog dialog = builder.create();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    dialog.show();
                                                }
                                            }
                                        });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "Invalid email address. Please check and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}