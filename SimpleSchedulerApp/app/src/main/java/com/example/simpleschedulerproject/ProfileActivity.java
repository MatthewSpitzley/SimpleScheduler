package com.example.simpleschedulerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView name;
    private TextView email;
    private Button signOut;

    //private GoogleApiClient googleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profileImage);
        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        signOut = findViewById(R.id.signOut);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestEmail().build();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                switch (v.getId()) {
                    case R.id.signOut:
                        signUserOut();
                        break;
                }
            }
        });
    }

    private void signUserOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(ProfileActivity.this, GoogleSignInActivity.class));
                        finish();
                    }
                });
    }

    private void handleSignInResult (GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            name.setText(account.getDisplayName());
            email.setText(account.getEmail());
        }
        else{
            startActivity(new Intent(ProfileActivity.this, GoogleSignInActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ProfileActivity.this);
        if (acct != null) {
            name.setText(acct.getDisplayName());
            email.setText(acct.getEmail());
            profileImage.setImageURI(acct.getPhotoUrl());
        }
    }
}