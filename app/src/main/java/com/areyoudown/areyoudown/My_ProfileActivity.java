package com.areyoudown.areyoudown;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class My_ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //spinners
    Spinner spinnerIntention;
    String[] SPINNERVALUESIntentions = {"No intention","Hook-up","Long-term relationship","Meeting new people","A good time","Potential Date"};

    Spinner spinnergender;
    String[] SPINNERVALUESgenderp = {"Select Gender preference","Male","Female"};

    Spinner spinnerAge;
    String[] SPINNERVALUESAge = {"Select Age preference","18-25 years old","25-30 years old","30-35 years old","35+"};


    Spinner spinnerTimw;
    String[] SPINNERVALUESTime = {"Select Time Commitment","4 hours","8 hours"};
    //spinners


    //button
    private Button  buttonimdown;
    //private Button  buttonback;
    //button


    //app variables
    private String inntention;
    private String intrestedin;
    private String agerange;
    private String time;
    private Boolean noIntent=false;
    //app variables

    final Context context=this;

    //time mechanism
    Calendar now = Calendar.getInstance();
    int nextpossibbleintentionchange;
    int realtimerightnow;
    //time mechanism

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    //Db data
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    //Db data

    /**protected LocationManager locationManager;
    double longitudeBest, latitudeBest;
    double longitudeGPS, latitudeGPS;
    double longitudeNetwork, latitudeNetwork;*/


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__profile);

        //spinnerIntent
        spinnerIntention =(Spinner)findViewById(R.id.spinnerIntention);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(My_ProfileActivity.this, android.R.layout.simple_list_item_1, SPINNERVALUESIntentions)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getDropDownView(position, convertView, parent));
            }

            private View setCentered(View view)
            {
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                return view;
            }
        };
        spinnerIntention.setAdapter(adapter);

        spinnerIntention.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                switch ( spinnerIntention.getSelectedItem().toString())
                {
                    case ("Hook-up"):
                    {
                        inntention="1";
                        noIntent=false;
                    }
                        break;
                    case ("Long-term relationship"):
                    {
                        inntention="2";
                        noIntent=false;
                    }
                    break;
                    case ("Meeting new people"):
                    {
                        inntention="3";
                        noIntent=false;
                    }
                    break;
                    case ("A good time"):
                    {
                        inntention="4";
                        noIntent=false;
                    }
                    break;
                    case ("Potential Date"):
                    {
                        inntention="5";
                        noIntent=false;
                    }
                    break;
                    case ("No intention"):
                    {
                        inntention="";
                        noIntent=true;
                    }
                    break;

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //

        //spinnerSexPref
        spinnergender =(Spinner)findViewById(R.id.spinnerSexPrefrence);
        ArrayAdapter<String> adaptersex = new ArrayAdapter<String>(My_ProfileActivity.this, android.R.layout.simple_list_item_1, SPINNERVALUESgenderp)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getDropDownView(position, convertView, parent));
            }

            private View setCentered(View view)
            {
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                return view;
            }
        };
        spinnergender.setAdapter(adaptersex);


        spinnergender.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                switch ( spinnergender.getSelectedItem().toString()) {
                    case ("Male"): {
                        intrestedin = "1";
                    }
                    break;
                    case ("Female"): {
                        intrestedin = "2";
                    }
                    break;
                    case ("Select Gender preference"): {
                        intrestedin = "";
                    }
                    break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        //
        //spinner

        spinnerAge =(Spinner)findViewById(R.id.spinnerAgePrefrence);
        ArrayAdapter<String> adapterage = new ArrayAdapter<String>(My_ProfileActivity.this, android.R.layout.simple_list_item_1, SPINNERVALUESAge){
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getDropDownView(position, convertView, parent));
            }

            private View setCentered(View view)
            {
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                return view;
            }
        };
        spinnerAge.setAdapter(adapterage);


        spinnerAge.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                switch ( spinnerAge.getSelectedItem().toString()) {

                    case ("Select Age preference"): {
                        agerange = "";
                    }
                    break;
                    case ("18-25 years old"): {
                        agerange = "1";
                    }
                    break;
                    case ("25-30 years old"): {
                        agerange = "2";
                    }
                    break;
                    case ("30-35 years old"): {
                        agerange = "3";
                    }
                    break;
                    case ("35+"): {
                        agerange = "4";
                    }
                    break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        //
        //spinner
        spinnerTimw =(Spinner)findViewById(R.id.spinnerTimecommitement);
        ArrayAdapter<String> adaptertime = new ArrayAdapter<String>(My_ProfileActivity.this, android.R.layout.simple_list_item_1, SPINNERVALUESTime)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                return setCentered(super.getDropDownView(position, convertView, parent));
            }

            private View setCentered(View view)
            {
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                return view;
            }
        };
        spinnerTimw.setAdapter(adaptertime);


        spinnerTimw.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                switch ( spinnerTimw.getSelectedItem().toString()) {
                    case ("Select Time Commitment"): {
                        time = "";
                    }
                    break;
                    case ("4 hours"): {
                        time = "1";
                    }
                    break;
                    case ("8 hours"): {
                        time = "2";
                    }
                    break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        //


        realtimerightnow=now.get(Calendar.HOUR_OF_DAY);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        buttonimdown=(Button) findViewById(R.id.buttonImDown);
        buttonimdown.setOnClickListener(this);


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_profile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.back:
                finish();
                startActivity(new Intent(this,ProfileActivity.class));
                break;

        }

        return true;

    }











    private void userCreateintention(){

        if(!noIntent && TextUtils.isEmpty(inntention))
        {
            Toast.makeText(this,"please select your intention",Toast.LENGTH_SHORT).show();

            return;
        }

        if(TextUtils.isEmpty(intrestedin)){

            Toast.makeText(this,"please select your preferred sex ",Toast.LENGTH_LONG).show();

            return;
        }
        if(TextUtils.isEmpty(agerange)){
            Toast.makeText(this,"please select your age preference.",Toast.LENGTH_LONG).show();

            return;
        }
        if(TextUtils.isEmpty(time)){

            Toast.makeText(this,"please select your time commitment.",Toast.LENGTH_LONG).show();

            return;
        }

        //get current time on system
        int realtimerightnow=now.get(Calendar.HOUR_OF_DAY);


        //add them determine next possible change
        if(time=="1"){
            if(realtimerightnow+4 >24){
                nextpossibbleintentionchange =(realtimerightnow+4)-24;
            }
            else{
                nextpossibbleintentionchange=realtimerightnow+4;
            }


        }
        if(time=="2") {

            if (realtimerightnow + 8 > 24) {
                nextpossibbleintentionchange = (realtimerightnow + 8) - 24;
            } else {
                nextpossibbleintentionchange = realtimerightnow + 8;
            }
        }


        progressDialog.setMessage("Making magic...");

        progressDialog.show();


        if(noIntent)
        {
            DatabaseReference ref1 = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid());
            Map newUserDatatrue = new HashMap();
            newUserDatatrue.put("intention","");
            newUserDatatrue.put("intrestedIn","");
            newUserDatatrue.put("agerange","");
            newUserDatatrue.put("timecommitment","0 0");
            newUserDatatrue.put("timeenforced",false);
            ref1.updateChildren(newUserDatatrue);

        }
        else
        {
            DatabaseReference ref1 = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid());
            Map newUserData = new HashMap();
            newUserData.put("intention", inntention);
            newUserData.put("intrestedIn", intrestedin);
            newUserData.put("agerange",agerange);
            newUserData.put("timecommitment",realtimerightnow +" "+nextpossibbleintentionchange);
            newUserData.put("timeenforced",true);
            ref1.updateChildren(newUserData);

        }

        Intent backprilenavagation=new Intent(context,ProfileActivity.class);
        //Toast.makeText(My_ProfileActivity.this,"Ta da!",Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        finish();
        startActivity(backprilenavagation);

    }

    @Override
    public void onClick(View v) {

        if(v==buttonimdown){
            userCreateintention();

        }
    }


}
