package com.example.simpleschedulerproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZonedDateTime;

public class GoogleSignInActivity extends AppCompatActivity {
    SignInButton signInButton;
    /*private GoogleApiClient googleApiClient;*/
    private static final int SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private DBHelper mHelper;
    private RequestQueue mQueue;

    private void fetchCategoriesParse() {
        String url = "http://localhost/fetchCategories.php";
        mHelper = new DBHelper(this);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("categories");

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject category = jsonArray.getJSONObject(i);

                                String category_name = category.getString("category");

                                mHelper.addCategory(new Category(category_name));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        mQueue.add(request);
    }

    private void fetchTasksParse() {
        String url = "http://localhost/fetchTasks.php";
        mHelper = new DBHelper(this);

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("tasks");

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject task = jsonArray.getJSONObject(i);

                                String task_name = task.getString("task_name");
                                Category category = new Category(task.getString("category"));
                                ZonedDateTime time = ZonedDateTime.parse(task.getString("time"));
                                Recur recur = Recur.valueOf(task.getString("recur"));
                                ZonedDateTime email = ZonedDateTime.parse(task.getString("email_notification"));
                                ZonedDateTime push = ZonedDateTime.parse(task.getString("push_notification"));
                                boolean complete = task.getInt("completed") == 1;

                                mHelper.addTask(new TaskClass(task_name, category, time, recur, email, push, complete));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        mQueue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = findViewById(R.id.google_sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.google_sign_in_button:
                        signIn();
                        break;
                }
            }
        });
    }

    //Sign the user into google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            startActivity(new Intent(GoogleSignInActivity.this, ProfileActivity.class));
            finish();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //Was the sign in successful or not?
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(GoogleSignInActivity.this, ProfileActivity.class));
            finish();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(GoogleSignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}