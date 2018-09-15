package com.naik.soft.snaik;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;


    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.signOut).setVisibility(View.GONE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void signOut() {
        mAuth.signOut();
        FirebaseAuth.getInstance().signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(@Nullable FirebaseUser firebaseUser) {
        LinearLayout authentication = findViewById(R.id.authentication);
        if (firebaseUser != null) {
            //findViewById(R.id.imageView2).setVisibility(View.GONE);
            findViewById(R.id.signOut).setVisibility(View.VISIBLE);
            (findViewById(R.id.username)).setVisibility(View.VISIBLE);
            (findViewById(R.id.phoneNumber)).setVisibility(View.VISIBLE);
            authentication.setVisibility(View.GONE);

            final String text = "Hi, " + firebaseUser.getDisplayName();
            Log.d(TAG, text);
            ((TextView)findViewById(R.id.username)).setText(text);

            for (UserInfo profile : firebaseUser.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                //String name = profile.getDisplayName();
                String emailUser = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                final String msg = "\nemail :" + emailUser + "\nphoto url :" + photoUrl + "\nuid :" + uid + "\nprovider Id:" + providerId;
                Log.d(TAG, msg);
                ((TextView)findViewById(R.id.phoneNumber)).setText(msg);
            }


        } else {
            //findViewById(R.id.imageView2).setVisibility(View.VISIBLE);
            findViewById(R.id.signOut).setVisibility(View.GONE);
            (findViewById(R.id.username)).setVisibility(View.GONE);
            (findViewById(R.id.phoneNumber)).setVisibility(View.GONE);
            authentication.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(email.getText().toString(), password.getText().toString());
            }
        });

        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(email.getText().toString(), password.getText().toString());
            }
        });

        findViewById(R.id.signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    private void signIn(String email, String password) {
        if(email.length() > 0 && password.length() > 0){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });

        }
    }

    private void signUp(String email, String password) {
        if(email.length() > 0 && password.length() > 0){

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }

}
