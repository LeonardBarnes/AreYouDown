package com.areyoudown.areyoudown;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class capturingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSubmit;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private EditText edittextwhatsappnumber;
    private EditText edittextfacebookdetails;

    private String sex;
    private String FirstName;
    private String LastName;
    private String age;
    private String whatsnumber;
    private String facebook;



    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturing);
        firebaseAuth=FirebaseAuth.getInstance();


        progressDialog=new ProgressDialog(this);

        editTextFirstName=(EditText) findViewById(R.id.editText);
        editTextLastName=(EditText) findViewById(R.id.editText2);
        edittextwhatsappnumber =(EditText) findViewById(R.id.editText4);
        edittextfacebookdetails=(EditText) findViewById(R.id.editText5);
        editTextAge=(EditText) findViewById(R.id.editText3);
        buttonSubmit=(Button) findViewById(R.id.buttonSubmit);


        buttonSubmit.setOnClickListener(this);
    }

    public void selectSex(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();


        switch (view.getId())
        {
            case R.id.radioButton:
                if (checked) {
                    sex="1";
                }
                else
                {
                    sex = "";
                }
                break;

            case R.id.radioButton2:
                if (checked) {
                    sex="2";
                }
                else
                {
                    sex = "";
                }
                break;
        }
    }



    private void userCreateDetails(){

        FirstName=editTextFirstName.getText().toString().trim();
        LastName=editTextLastName.getText().toString().trim();
        age=editTextAge.getText().toString().trim();
        whatsnumber=edittextwhatsappnumber.getText().toString().trim();
        facebook=edittextfacebookdetails.getText().toString().trim();


        if(TextUtils.isEmpty(FirstName)){
            //email empty
            Toast.makeText(this,"please enter your first name",Toast.LENGTH_LONG).show();

            //Stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(LastName)){
            //password is empty
            Toast.makeText(this,"please enter last name",Toast.LENGTH_LONG).show();

            //Stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(age)){
            //password is empty
            Toast.makeText(this,"please enter your age.",Toast.LENGTH_LONG).show();

            //Stopping the function execution further
            return;
        }
        if(Integer.parseInt(age)>=2001){
            //password is empty
            Toast.makeText(this,"You're too young!.",Toast.LENGTH_LONG).show();

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));

            //Stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(sex)){
            //password is empty
            Toast.makeText(this,"please select your sex.",Toast.LENGTH_LONG).show();

            //Stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(facebook)){
            //password is empty
            Toast.makeText(this,"please enter your full Facebook name",Toast.LENGTH_LONG).show();

            //Stopping the function execution further
            return;
        }

        if(TextUtils.isEmpty(whatsnumber)  ){
            //password is empty
            Toast.makeText(this,"please enter your Whatsapp contact number",Toast.LENGTH_LONG).show();

            //Stopping the function execution further
            return;
        }
        //if validations are ok
        //we will first show a progressbar

        progressDialog.setMessage("Submitting data...");
        progressDialog.show();

        String email = getIntent().getExtras().getString("e");
        String password = getIntent().getExtras().getString("p");

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference ref1 = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid());



                            ref1.setValue(firebaseAuth.getCurrentUser().getUid())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            DatabaseReference ref = mRootRef.child("Users");
                                            if(task.isSuccessful())
                                            {
                                                User user=new User();

                                                user.setAge(String.valueOf(2016-Integer.parseInt(age)));
                                                user.setFirstName(FirstName);
                                                user.setLastName(LastName);
                                                user.setSex(sex);
                                                user.setIntrestedIn("");
                                                user.setLocation("");
                                                user.setIntention("");
                                                user.setTimecommitment("0"+" "+"0");
                                                user.setAgerange("");
                                                user.setTimeenforced(false);
                                                user.setProfilePicture("profilepictures/"+firebaseAuth.getCurrentUser().getUid());
                                                user.setUid(firebaseAuth.getCurrentUser().getUid());
                                                user.setFbdetails(facebook);
                                                user.setNumber(whatsnumber);


                                                Uri uri = Uri.parse("android.resource://com.areyoudown.areyoudown/drawable/blankpropic");
                                                StorageReference unigeref = FirebaseStorage.getInstance().getReference().child("profilepictures").child(firebaseAuth.getCurrentUser().getUid());
                                                unigeref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    }
                                                });

                                                ref.child(firebaseAuth.getCurrentUser().getUid()).setValue(user)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(capturingActivity.this,"Successful!",Toast.LENGTH_LONG).show();
                                                                finish();
                                                                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                                                            }
                                                        });

                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(capturingActivity.this,"An error occurred...please try again",Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });


                        }
                        else {
                            Toast.makeText(capturingActivity.this,"Could not register user...please try again",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                });


























        /**StorageReference unigetpic=FirebaseStorage.getInstance().getReference().child("profilepictures/"+firebaseAuth.getCurrentUser().getUid());
        unigetpic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Uri uri = Uri.parse("android.resource://com.areyoudown.areyoudown/drawable/blankpropic");
                StorageReference unigeref = FirebaseStorage.getInstance().getReference().child("profilepictures").child(firebaseAuth.getCurrentUser().getUid());
                unigeref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
            }
        });*/

    }

    @Override
    public void onClick(View v) 
    {
        userCreateDetails();

    }
}
