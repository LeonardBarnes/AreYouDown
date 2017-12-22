package com.areyoudown.areyoudown;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button buttonSingIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() !=null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

        }

        progressDialog=new ProgressDialog(this);

        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword);
        buttonSingIn=(Button) findViewById(R.id.buttonSignin);
        textViewSignup=(TextView) findViewById(R.id.textViewSignUp);


        buttonSingIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }

    private void userLogin(){
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email empty
            Toast.makeText(this,"please enter email",Toast.LENGTH_LONG).show();

            //Stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,"please enter password",Toast.LENGTH_LONG).show();

            //Stopping the function execution further
            return;
        }
        //if validations are ok
        //we will first show a progressbar

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"An error occurred. Please check login details and/or Network connection.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v==buttonSingIn){
            userLogin();
        }

        if(v==textViewSignup){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
