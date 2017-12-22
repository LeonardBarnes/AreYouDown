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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() !=null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

        }

        progressDialog=new ProgressDialog(this);

        buttonRegister=(Button)  findViewById(R.id.buttonRegister);

        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword);

        textViewSignin=(TextView) findViewById(R.id.textViewSignin);
        //TextView textViewmore = (TextView) findViewById(R.id.textView10);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }
    private void registerUser(){
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email empty
            Toast.makeText(this,"please enter email",Toast.LENGTH_SHORT).show();

            //Stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this,"please enter password",Toast.LENGTH_SHORT).show();

            //Stopping the function execution further
            return;
        }
        //if validations are ok
        //we will first show a progressbar

        progressDialog.setMessage("Registering User Please wait...");
        progressDialog.show();

        Intent intent = new Intent(MainActivity.this, capturingActivity.class);
        intent.putExtra("e",email);
        intent.putExtra("p",password);
        progressDialog.dismiss();
        startActivity(intent);
        finish();




    }

    @Override
    public void onClick(View v) {
        if(v==buttonRegister){
            registerUser();
        }

        if(v==textViewSignin){
            //will open login activity here
            startActivity(new Intent(this,LoginActivity.class));
        }

    }
}
