package com.areyoudown.areyoudown;

import android.*;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Vibrator;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.SettingInjectorService;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.measurement.internal.UserAttributeParcel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

public class ProfileActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener  {

    private FirebaseAuth firebaseAuth;
    private TextView textheading;

    private ProgressDialog progressDialog;
    private ImageView imgPicture;
    private boolean needtoshow=false;
    private boolean needtoshowresponses=false;
    private boolean lockondatachange;
    private boolean lockondatachangetwo;



    //make next time allowed variable
    Calendar now = Calendar.getInstance();

    //Databse Refrence
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refuser = mRootRef.child("Users");
    private StorageReference mStorationref= FirebaseStorage.getInstance().getReference().child("profilepictures");



    //Location
    LocationManager locationManager;
    double longitudeBest, latitudeBest;
    //Location

    //getting other users
    private ArrayList<User> usersSearchedFor;
    private ArrayList<User> usersthatrequest;
    private ArrayList<String>userstanswerresponses;
    private ArrayList<User> usersthatresponses;
    private ArrayList<User> usersSearchedcompleteNames;
    private List<intentions> myIntentions=new ArrayList<>();
    //public static final String PREFS_NAME = "Test";
    //public CheckBox check;




    private GestureDetectorCompat gestureObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        //FirebaseUser user = firebaseAuth.getCurrentUser();


        textheading = (TextView) findViewById(R.id.textView9);
        textheading.setVisibility(View.INVISIBLE);
        progressDialog=new ProgressDialog(this);

        //textViewUserEmail.setText("Welcome " + user.getEmail());

        //Setting up views
        imgPicture=(ImageView) findViewById(R.id.profile_image_home);
        getpropic();
        timecheck();



        new LovelyInfoDialog(this)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.mipmap.ic_launcher)
                //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
                .setNotShowAgainOptionEnabled(0)
                .setTitle("Navigation")
                .setMessage("Swipe left to set your intentions. \n \n"+"Change your profile picture by accessing the top right menu of your screen")
                .show();





        //For Refresh button
        returnsintention();

        //Location
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        toggleBestUpdates();
        //Location



        //populateListView();

        //returnsRequests();
        //returnsRequests();

        //DatabaseReference timecheckrefrencepeople = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Peopledown");
        //timecheckrefrencepeople.removeValue();

        //





    }

        //











    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    public static float rotationForImage(Context context, Uri uri) {
        if (uri.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(
                    uri, projection, null, null, null);
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        } else if (uri.getScheme().equals("file")) {
            try {
                ExifInterface exif = new ExifInterface(uri.getPath());
                int rotation = (int)exifOrientationToDegrees(
                        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL));
                return rotation;
            } catch (IOException e) {

            }
        }
        return 0f;
    }

    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotateImage(Uri pathToImage,int rot) {
        Bitmap sourceBitmap=null;

        // 1. figure out the amount of degrees


        // 2. rotate matrix by postconcatination
        Matrix matrix = new Matrix();
        matrix.postRotate(rot);

        // 3. create Bitmap from rotated matrix
        try {
            Bitmap  sBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pathToImage);
            sourceBitmap=sBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    public void onImageGalleryClick()
    {

        Intent photoPickerIntent=new Intent(Intent.ACTION_PICK);

        File picdirectory=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        String picDirectoryPath=picdirectory.getPath();

        Uri data=Uri.parse(picDirectoryPath);

        photoPickerIntent.setDataAndType(data,"image/*");

        startActivityForResult(photoPickerIntent,20);

        //PPPPPPPPPPPPP


        //PPPPPPPPP






    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 20)
            {
                Uri imageuri = data.getData();
                String picDirectoryPath=data.getDataString();
                int rotateImage = getCameraPhotoOrientation(ProfileActivity.this, imageuri, picDirectoryPath);

                //Bitmap rotpic=rotateImage(imageuri,rotateImage);
                //Uri auri=getImageUri(ProfileActivity.this, rotpic);


                progressDialog.setMessage("Retrieving.."+ rotateImage);
                progressDialog.show();


                StorageReference unigeref = FirebaseStorage.getInstance().getReference().child("profilepictures").child(firebaseAuth.getCurrentUser().getUid());

                unigeref.putFile(imageuri ).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        progressDialog.dismiss();
                        getpropic();

                    }
                });

            }

        }
    }


    public void  getpropic(){

        StorageReference unigetpic=FirebaseStorage.getInstance().getReference().child("profilepictures/"+firebaseAuth.getCurrentUser().getUid());
        unigetpic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Picasso.with(ProfileActivity.this).load(uri).resize(150,150).centerCrop().into(imgPicture);

            }
        });

    }







    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profileactivity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.refresh: {
                needtoshow=false;
                needtoshowresponses=false;
                progressDialog.setMessage("Refreshing");
                progressDialog.show();

                imgPicture.setVisibility(View.VISIBLE);
                ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);
                lastpic.setVisibility(View.GONE);
                SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id
                        .switch_compat2);
                switchCompat.setVisibility(View.GONE);
                ListView list2=(ListView) findViewById(R.id.listViewUserssecondlevel);
                list2.setVisibility(View.GONE);

                ListView list=(ListView) findViewById(R.id.listViewUsers);
                list.setVisibility(View.VISIBLE);
                Button buttonBackStalk = (Button) findViewById(R.id.buttonbackstalk);
                buttonBackStalk.setVisibility(View.GONE);
                Button request= (Button) findViewById(R.id.buttonAreYouDownRequest);
                request.setVisibility(View.GONE);

                TextView headtreq=(TextView) findViewById(R.id.textViewrequests);
                headtreq.setVisibility(View.GONE);
                ListView listreq=(ListView) findViewById(R.id.listViewRequests);
                listreq.setVisibility(View.GONE);

                Button buttonBack= (Button) findViewById(R.id.buttonbackrequest);
                Button accept= (Button) findViewById(R.id.button2Accept);
                Button decline=(Button) findViewById(R.id.buttonDecline);
                buttonBack.setVisibility(View.GONE);
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.GONE);
                TextView number=(TextView) findViewById(R.id.textView10facebookv2);
                TextView fb=(TextView) findViewById(R.id.textView11numberv2);
                number.setVisibility(View.GONE);
                fb.setVisibility(View.GONE);
                ListView listres=(ListView) findViewById(R.id.listViewResponses);
                listres.setVisibility(View.INVISIBLE);






                DatabaseReference refintenion = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("intention");
                refintenion.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currenTntent = dataSnapshot.getValue(String.class);
                        if (currenTntent.equals("1") | currenTntent.equals("2") | currenTntent.equals("3") | currenTntent.equals("4") | currenTntent.equals("5")) {
                            getUsersShowUSers();
                            progressDialog.dismiss();
                        } else {

                            Toast.makeText(getApplicationContext(), "You need to indicate your intention first.", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            }
            case R.id.Logout:{
                stopupdates();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
            }
            break;

            case R.id.ProfilePicture:{
                onImageGalleryClick();
            }
            break;
            case R.id.Peoplethataredown:{
                needtoshow=true;
                //callingviews();
                ListView listres=(ListView) findViewById(R.id.listViewResponses);
                listres.setVisibility(View.GONE);
                Button buttonBack= (Button) findViewById(R.id.buttonbackrequest);
                buttonBack.setVisibility(View.GONE);
                TextView number=(TextView) findViewById(R.id.textView10facebookv2);
                TextView fb=(TextView) findViewById(R.id.textView11numberv2);
                number.setVisibility(View.GONE);
                fb.setVisibility(View.GONE);
                getUserRequest();


            }
            break;
            case R.id.Responses:{
                ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);
                lastpic.setVisibility(View.GONE);
                ListView listreq=(ListView) findViewById(R.id.listViewRequests);
                listreq.setVisibility(View.GONE);
                Button buttonBack= (Button) findViewById(R.id.buttonbackrequest);
                Button accept= (Button) findViewById(R.id.button2Accept);
                Button decline=(Button) findViewById(R.id.buttonDecline);
                buttonBack.setVisibility(View.GONE);
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.GONE);
                TextView number=(TextView) findViewById(R.id.textView10facebookv2);
                TextView fb=(TextView) findViewById(R.id.textView11numberv2);
                number.setVisibility(View.INVISIBLE);
                fb.setVisibility(View.INVISIBLE);

                needtoshowresponses=true;
                callingviews();
                getUserRespones();


            }
            break;
            case R.id.back:{
                ListView listreq=(ListView) findViewById(R.id.listViewRequests);
                listreq.setVisibility(View.GONE);
                TextView textreqhead = (TextView) findViewById(R.id.textViewrequests);
                textreqhead.setVisibility(View.GONE);
                TextView number=(TextView) findViewById(R.id.textView10facebookv2);
                TextView fb=(TextView) findViewById(R.id.textView11numberv2);
                number.setVisibility(View.GONE);
                fb.setVisibility(View.GONE);
                ListView list=(ListView) findViewById(R.id.listViewUsers);
                list.setVisibility(View.VISIBLE);
                finish();
                startActivity(new Intent(this,ProfileActivity.class));
                break;
               /* needtoshow=false;
                progressDialog.setMessage("Refreshing");
                progressDialog.show();
                textheading.setVisibility(View.VISIBLE);
                imgPicture.setVisibility(View.VISIBLE);
                ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);
                lastpic.setVisibility(View.GONE);
                SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id
                        .switch_compat2);
                switchCompat.setVisibility(View.GONE);
                ListView list2=(ListView) findViewById(R.id.listViewUserssecondlevel);
                list2.setVisibility(View.GONE);

                ListView list=(ListView) findViewById(R.id.listViewUsers);
                list.setVisibility(View.VISIBLE);
                Button buttonBackStalk = (Button) findViewById(R.id.buttonbackstalk);
                buttonBackStalk.setVisibility(View.GONE);
                Button request= (Button) findViewById(R.id.buttonAreYouDownRequest);
                request.setVisibility(View.GONE);
                textheading.setText("Peoples' Intentions near you");
                TextView headtreq=(TextView) findViewById(R.id.textViewrequests);
                headtreq.setVisibility(View.GONE);
                ListView listreq=(ListView) findViewById(R.id.listViewRequests);
                listreq.setVisibility(View.GONE);
                progressDialog.dismiss();*/


            }
            //break;


        }

        return true;
    }
    public void  deletereqnit(DatabaseReference deleref){
        deleref.removeValue();
    }


    public void returnsRequests()
    {

        DatabaseReference otherpersonoty = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("NotificationPeopledown");


        otherpersonoty.addChildEventListener(new ChildEventListener() {
                                         @Override
                                         public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                             // Vibrate for 500 milliseconds
                                            // Toast.makeText(getApplicationContext(), currentUser.toString()+" ", Toast.LENGTH_LONG).show();

                                             //
                                             NotificationCompat.Builder buider= new NotificationCompat.Builder(ProfileActivity.this);
                                             buider.setSmallIcon(R.mipmap.ic_launcher);
                                             buider.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                                             buider.setLights(Color.GREEN, 500, 2000);
                                             buider.setContentTitle("AreYourDown?");
                                             buider.setContentText("You got a new AreYouDown request");
                                             buider.setAutoCancel(true);
                                             Intent intent = new Intent(ProfileActivity.this,ProfileActivity.class);
                                             TaskStackBuilder stbulder= TaskStackBuilder.create(ProfileActivity.this);
                                             stbulder.addParentStack(ProfileActivity.class);
                                             stbulder.addNextIntent(intent);
                                             PendingIntent pi=stbulder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                                             buider.setContentIntent(pi);

                                             NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                             nm.notify(0,buider.build());



                                             //

                                            /* Intent intent = new Intent();
                                             PendingIntent pIntent=PendingIntent.getActivity(ProfileActivity.this,0,intent,0);




                                             Notification noti=new Notification.Builder(ProfileActivity.this)
                                                     .setVibrate()
                                                     .setTicker("AreYouDown?")
                                                     .setContentTitle("AreYouDown?")
                                                     .setContentText("You have one new AreYouDown request.")
                                                     .setSmallIcon(R.mipmap.ic_launcher)
                                                     .setContentIntent(pIntent).getNotification();

                                             noti.flags=Notification.FLAG_AUTO_CANCEL;

                                             NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                             nm.notify(0,noti);*/

                                             //Toast.makeText(getApplicationContext(), dataSnapshot.getValue()+" ", Toast.LENGTH_LONG).show();
                                             DatabaseReference deleteonoty = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("NotificationPeopledown").child(dataSnapshot.getKey());
                                             deletereqnit(deleteonoty);
                                             //Map<String,String> amap=dataSnapshot.getValue(Map.class);
                                             //Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                             //final DatabaseReference refuse = mRootRef.child("Users").child(map.get("user")+"");

                                             //refuse.addValueEventListener(new ValueEventListener() {
                                                 //@Override
                                                 //public void onDataChange(DataSnapshot dataSnapshot) {
                                                     //User currentUser = dataSnapshot.getValue(User.class);





                                                 //}

                                                // @Override
                                                // public void onCancelled(DatabaseError databaseError) {

                                                // }
                                             //});








                                         }

                                         @Override
                                         public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                             //Toast.makeText(getApplicationContext(), dataSnapshot+"", Toast.LENGTH_LONG).show();
                                             //DatabaseReference refreq = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Peopledown").child(dataSnapshot.getKey());



                                         }

                                         @Override
                                         public void onChildRemoved(DataSnapshot dataSnapshot) {

                                             //Toast.makeText(getApplicationContext(), dataSnapshot.getValue()+" ", Toast.LENGTH_LONG).show();

                                         }

                                         @Override
                                         public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                         }

                                         @Override
                                         public void onCancelled(DatabaseError databaseError) {

                                         }
                                     });




    }



    private void populateIntentList(){
        myIntentions.add(new intentions("Hook-up","1"));
        myIntentions.add(new intentions("Long-term Relationship","2"));
        myIntentions.add(new intentions("Meeting new people","3"));
        myIntentions.add(new intentions("A good time","4"));
        myIntentions.add(new intentions("Potential date","5"));

    }


    public void returnsintention()
    {

        DatabaseReference refintenion = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("intention");
        refintenion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currenTntent=dataSnapshot.getValue(String.class);
                if(currenTntent.equals("1")|currenTntent.equals("2")|currenTntent.equals("3")|currenTntent.equals("4")|currenTntent.equals("5")){
                    //buttonShowRefreshButton.setVisibility(View.VISIBLE);
                    getUsersShowUSers();


                }
                else{

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Location
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    private LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();

            DatabaseReference refLoc = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid());

            Map newUserData = new HashMap();
            newUserData.put("location", longitudeBest+" "+latitudeBest);
            refLoc.updateChildren(newUserData);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return
                locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
    }


    public void toggleBestUpdates()
    {
        if (!checkLocation())
            return;

        else {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            String provider = locationManager.getBestProvider(criteria, true);
            if (provider != null)
            {
                locationManager.requestLocationUpdates(provider, 480000,15, locationListenerBest);
                //Toast.makeText(this, "Best Provider is " + provider, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void stopupdates(){
        if (!checkLocation())
            return;
        else
            locationManager.removeUpdates(locationListenerBest);

    }

    //Location


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }




//GETTING USERS
    private void addValuEventListener(final DatabaseReference userRef, final String sexpref, final String intention,final String loc,final User myuser)
    {


        userRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (!lockondatachange)
                {
                    usersSearchedFor=new ArrayList<>();
                    usersSearchedcompleteNames=new ArrayList<>();
                    myIntentions=new ArrayList();
                    int hookup=0;
                    int longtermrelationship=0;
                    int meetingnewpeople=0;
                    int agoodtime=0;
                    int apotentialdate=0;

                    Iterable<DataSnapshot> snapshotiterator=dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator=snapshotiterator.iterator();
                    while(iterator.hasNext())
                    {

                        try
                        {
                            User auser=iterator.next().getValue(User.class);
                            if(auser.getProfilePicture().contains(myuser.getProfilePicture())){}
                            else {
                                usersSearchedFor.add(auser);
                            }

                            if(auser.getIntention().contains(intention) && auser.getSex().equals(sexpref) && !(auser.getProfilePicture().contains(myuser.getProfilePicture())))
                                usersSearchedcompleteNames.add(auser);
                            if(auser.getIntention().contains("1") && !(auser.getProfilePicture().contains(myuser.getProfilePicture())) && (auser.getSex().contains(myuser.getIntrestedIn())))
                                hookup++;
                            if(auser.getIntention().contains("2") && !(auser.getProfilePicture().contains(myuser.getProfilePicture())) && (auser.getSex().contains(myuser.getIntrestedIn())))
                                longtermrelationship++;
                            if(auser.getIntention().contains("3") && !(auser.getProfilePicture().contains(myuser.getProfilePicture())) && (auser.getSex().contains(myuser.getIntrestedIn())))
                                meetingnewpeople++;
                            if(auser.getIntention().contains("4") && !(auser.getProfilePicture().contains(myuser.getProfilePicture())) && (auser.getSex().contains(myuser.getIntrestedIn())))
                                agoodtime++;
                            if(auser.getIntention().contains("5") && !(auser.getProfilePicture().contains(myuser.getProfilePicture())) && (auser.getSex().contains(myuser.getIntrestedIn())))
                                apotentialdate++;
                        }
                        catch (Exception e )
                        {
                            Toast.makeText(getApplicationContext(), "solved", Toast.LENGTH_LONG).show();

                        }


                    }
                    if(usersSearchedFor!=null)
                    {

                        populateIntentList();
                        textheading.setVisibility(View.VISIBLE);
                        textheading.setText("Peoples' Intentions near you");

                        ArrayAdapter<intentions> adapterintention=new MyIntentionListAdapter(hookup,longtermrelationship,meetingnewpeople,agoodtime,apotentialdate);
                        ListView listint=(ListView) findViewById(R.id.listViewUsers);
                        listint.setAdapter(adapterintention);
                        registerClickCallBack(loc);
                        lockondatachange=true;


                        //populateIntentListview();





                        //ArrayAdapter<User>adapter=new MyUserListAdapter(loc);
                        // ListView list=(ListView) findViewById(R.id.listViewUsers);
                        //list.setAdapter(adapter);

                        ///ArrayAdapter<String >adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.usersfound,usersSearchedcompleteNames);
                        //ListView list=(ListView) findViewById(R.id.listViewUsers);
                        //list.setAdapter(adapter);

                    }
                    else
                    {
                        String[] sorryNoUsers={"Sorry No User Found"};

                        ArrayAdapter<String >adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.usersfound,sorryNoUsers);
                        ListView list=(ListView) findViewById(R.id.listViewUsers);
                        list.setAdapter(adapter);
                        lockondatachange=true;

                    }
                }
                else
                {


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void registerClickCallBack(String location){
        final String loc=location;
        ListView list=(ListView) findViewById(R.id.listViewUsers);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final intentions clickedintention=myIntentions.get(position);

                DatabaseReference reftime = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("intention");
                reftime.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String dbintent = dataSnapshot.getValue(String.class);
                        if(dbintent.contains(clickedintention.getId()))
                        {

                            ArrayAdapter<User>adapter=new MyUserListAdapter(loc);
                            ListView list2=(ListView) findViewById(R.id.listViewUserssecondlevel);
                            ListView list=(ListView) findViewById(R.id.listViewUsers);
                            list2.setVisibility(View.VISIBLE);
                            imgPicture.setVisibility(View.INVISIBLE);
                            textheading.setVisibility(View.INVISIBLE);
                            list.setVisibility(View.GONE);
                            list2.setAdapter(adapter);
                            registerClickCallBackleveltwo();

                        }
                        else
                            Toast.makeText(ProfileActivity.this,"You cannot access this list, Your current intention is different",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }
    public void addListenerOnButton() {

        final Button buttonBackStalk;
        final Button request;

        buttonBackStalk = (Button) findViewById(R.id.buttonbackstalk);
        request= (Button) findViewById(R.id.buttonAreYouDownRequest);


        buttonBackStalk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {



                final ListView listsecond=(ListView) findViewById(R.id.listViewUserssecondlevel);
                final ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);
                final SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switch_compat2);
                buttonBackStalk.setVisibility(View.INVISIBLE);
                listsecond.setVisibility(View.VISIBLE);
                lastpic.setVisibility(View.INVISIBLE);
                switchCompat.setVisibility(View.INVISIBLE);
                request.setVisibility(View.INVISIBLE);

            }

        });

    }

    public void addListenerOnButtont(final User acurrentuser)
    {

        final Button buttonBackStalk;
        final Button request;

        buttonBackStalk = (Button) findViewById(R.id.buttonbackstalk);
        request= (Button) findViewById(R.id.buttonAreYouDownRequest);

        final Button buttonreqk;
        final TextView lastpic=(TextView) findViewById(R.id.textView9);
        lastpic.setText("");



        buttonreqk = (Button) findViewById(R.id.buttonAreYouDownRequest);


        buttonreqk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {



                DatabaseReference otherpersonnode = mRootRef.child("Users").child(acurrentuser.getUid()).child("Peopledown");
                Map<String, String> post1 = new HashMap<String, String>();
                post1.put("user",firebaseAuth.getCurrentUser().getUid()+"" );
                otherpersonnode.push().setValue(post1);

                DatabaseReference otherpersonoty = mRootRef.child("Users").child(acurrentuser.getUid()).child("NotificationPeopledown");
                otherpersonoty.push().setValue(post1);

                final ListView listsecond=(ListView) findViewById(R.id.listViewUserssecondlevel);
                final ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);
                final SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switch_compat2);
                buttonBackStalk.setVisibility(View.INVISIBLE);
                listsecond.setVisibility(View.VISIBLE);
                lastpic.setVisibility(View.INVISIBLE);
                switchCompat.setVisibility(View.INVISIBLE);
                request.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();


            }

        });


    }


    private void registerClickCallBacklevelRequests(){

        final ListView listsecond=(ListView) findViewById(R.id.listViewRequests);
        final ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);

        //TextView textreqhead = (TextView) findViewById(R.id.textViewrequests);
        //textreqhead.setVisibility(View.INVISIBLE);
        listsecond.setVisibility(View.VISIBLE);
        //lastpic.setVisibility(View.VISIBLE);

        final Button Back;
        final Button Accept;
        final Button decline;

        Accept = (Button) findViewById(R.id.button2Accept);
        Back= (Button) findViewById(R.id.buttonbackrequest);
        decline=(Button) findViewById(R.id.buttonDecline);

        final TextView number=(TextView) findViewById(R.id.textView10facebookv2);
        final TextView fb=(TextView) findViewById(R.id.textView11numberv2);









        listsecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final User auser=usersthatrequest.get(position);


                Picasso.with(ProfileActivity.this).cancelRequest(lastpic);
                addListenerOnButtonindividrequest();
                addListenerOnButtonindividrequestAccept(auser);
                addListenerOnButtonindividrequestDecline(auser);
                //addListenerOnButtont(auser); one butoon
                lastpic.setImageResource(0);
                lastpic.setImageResource(android.R.color.transparent);
                lastpic.setVisibility(View.VISIBLE);
                textheading.setVisibility(View.INVISIBLE);

                listsecond.setVisibility(View.INVISIBLE);


                Accept.setVisibility(View.VISIBLE);
                Back.setVisibility(View.VISIBLE);
                decline.setVisibility(View.VISIBLE);

                number.setVisibility(View.VISIBLE);
                fb.setVisibility(View.VISIBLE);

                number.setText("FaceBook:"+auser.getFbdetails());
                fb.setText("Whatsapp:"+auser.getNumber());


                //addListenerOnButtonindividrequest(auser);


                StorageReference unigetpic=FirebaseStorage.getInstance().getReference().child(usersthatrequest.get(position).getProfilePicture());
                unigetpic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public  void  onSuccess(Uri uri) {
                        lastpic.setImageResource(android.R.color.transparent);
                        Picasso.with(ProfileActivity.this).cancelRequest(lastpic);
                        Picasso.with(ProfileActivity.this).load(uri).resize(250,250).centerCrop().into(lastpic);

                    }


                });

                //Toast.makeText(getApplicationContext(), auser.getLastName()+" "+usersSearchedcompleteNames.get(position), Toast.LENGTH_SHORT).show();




            }
        });




    }

    public void addListenerOnButtonindividrequest() {

        final Button buttonBack;
        final Button accept;
        final Button decline;

        buttonBack= (Button) findViewById(R.id.buttonbackrequest);
        accept= (Button) findViewById(R.id.button2Accept);
        decline=(Button) findViewById(R.id.buttonDecline);
        final TextView number=(TextView) findViewById(R.id.textView10facebookv2);
        final TextView fb=(TextView) findViewById(R.id.textView11numberv2);

        final ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);


        //final ListView listsecond=(ListView) findViewById(R.id.listViewRequests);

        final TextView last=(TextView) findViewById(R.id.textView9);
        last.setText("");



        buttonBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                buttonBack.setVisibility(View.GONE);
                lastpic.setVisibility(View.INVISIBLE);
                //listsecond.setVisibility(View.VISIBLE);lastpic.setVisibility(View.GONE);
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.GONE);
                number.setVisibility(View.GONE);
                fb.setVisibility(View.GONE);
                needtoshow=true;
                getUserRequest();

            }

        });


    }

    public void addListenerOnButtonindividrequestAccept(final User user) {


        final Button accept;

        accept= (Button) findViewById(R.id.button2Accept);


        accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DatabaseReference refcurrentuser = mRootRef.child("Users").child(user.getUid()).child("Responses");

                    Map<String, String> post1 = new HashMap<String, String>();
                    post1.put("user",firebaseAuth.getCurrentUser().getUid()+" y");
                    refcurrentuser.push().setValue(post1);


                final Button buttonBack;
                final Button accept;
                final Button decline;

                buttonBack= (Button) findViewById(R.id.buttonbackrequest);
                accept= (Button) findViewById(R.id.button2Accept);
                decline=(Button) findViewById(R.id.buttonDecline);
                final TextView number=(TextView) findViewById(R.id.textView10facebookv2);
                final TextView fb=(TextView) findViewById(R.id.textView11numberv2);

                final ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);


                //final ListView listsecond=(ListView) findViewById(R.id.listViewRequests);

                final TextView last=(TextView) findViewById(R.id.textView9);
                last.setText("");

                    buttonBack.setVisibility(View.GONE);
                    lastpic.setVisibility(View.INVISIBLE);
                    //listsecond.setVisibility(View.VISIBLE);lastpic.setVisibility(View.GONE);
                    accept.setVisibility(View.GONE);
                    decline.setVisibility(View.GONE);
                    fb.setVisibility(View.GONE);
                    number.setVisibility(View.GONE);
                    needtoshow=true;
                    getUserRequest();

                Vibrator v = (Vibrator) (ProfileActivity.this.getSystemService(Context.VIBRATOR_SERVICE));
                // Vibrate for 500 milliseconds
                v.vibrate(500);

                Toast.makeText(getApplicationContext(), "Response sent", Toast.LENGTH_SHORT).show();



            }

        });


    }

    public void addListenerOnButtonindividrequestDecline(final User user) {


        final Button Decline;

        Decline= (Button) findViewById(R.id.buttonDecline);


        Decline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DatabaseReference refcurrentuser = mRootRef.child("Users").child(user.getUid()).child("Responses");

                Map<String, String> post1 = new HashMap<String, String>();
                post1.put("user",firebaseAuth.getCurrentUser().getUid()+" n");
                refcurrentuser.push().setValue(post1);


                final Button buttonBack;
                final Button accept;
                final Button decline;

                buttonBack= (Button) findViewById(R.id.buttonbackrequest);
                accept= (Button) findViewById(R.id.button2Accept);
                decline=(Button) findViewById(R.id.buttonDecline);
                final TextView number=(TextView) findViewById(R.id.textView10facebookv2);
                final TextView fb=(TextView) findViewById(R.id.textView11numberv2);


                final ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);


                //final ListView listsecond=(ListView) findViewById(R.id.listViewRequests);

                final TextView last=(TextView) findViewById(R.id.textView9);
                last.setText("");

                buttonBack.setVisibility(View.GONE);
                lastpic.setVisibility(View.INVISIBLE);
                //listsecond.setVisibility(View.VISIBLE);lastpic.setVisibility(View.GONE);
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.GONE);
                number.setVisibility(View.GONE);
                fb.setVisibility(View.GONE);
                needtoshow=true;
                getUserRequest();



                Toast.makeText(getApplicationContext(), "Response sent", Toast.LENGTH_SHORT).show();



            }

        });


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.switch_compat2:
                if(isChecked)

                break;
        }

    }
    public void getUserRespones(){

        DatabaseReference refcurrentuser = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid());


        refcurrentuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                DatabaseReference refresponse = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Responses");

                addValuEventListenerresponses(refresponse ,currentUser);

                /*if(refrequests.){
                    Toast.makeText(getApplicationContext(), "Please set your preferences by swiping left ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    addValuEventListener(refuser,currentUser.getIntrestedIn(),currentUser.getIntention(),currentUser.getLocation(),currentUser);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
    private void addValuEventListenerresponses(final DatabaseReference userRef,final User userperspective)
    {
        DatabaseReference refdatabase = mRootRef.child("Users");
        final ArrayList<User> usersSearchedForreqsponse = new ArrayList<>();
        final String answer;



        refdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotiterator=dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator=snapshotiterator.iterator();
                while(iterator.hasNext())
                {
                    try
                    {
                        User auser=iterator.next().getValue(User.class);
                        if(auser.getProfilePicture().contains(userperspective.getProfilePicture())){}
                        else
                        {
                            usersSearchedForreqsponse.add(auser);

                        }
                    }
                    catch (Exception e )
                    {
                        Toast.makeText(getApplicationContext(), "solved", Toast.LENGTH_LONG).show();

                    }





                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

                //Toast.makeText(getApplicationContext(), "A Network error occurred", Toast.LENGTH_LONG).show();


            }
        });








        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersthatresponses=new ArrayList<>();
                userstanswerresponses=new ArrayList<>();

                Iterable<DataSnapshot> snapshotiterator=dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator=snapshotiterator.iterator();

                while(iterator.hasNext())
                {

                    Map<String, Object> map = (Map<String, Object>) iterator.next().getValue();


                    for(User u:usersSearchedForreqsponse){
                        String[] splited = map.get("user").toString().split("\\s+");

                        if(u.getUid().equals(splited[0]) && !usersthatresponses.contains(u) ){
                            usersthatresponses.add(u);
                            userstanswerresponses.add(splited[1]);
                        }
                        else{}
                    }

                }

                if((usersthatresponses.size()!=0) ){
                    callingviews();


                    //Toast.makeText(getApplicationContext(), usersthatrequest.size()+"", Toast.LENGTH_LONG).show();


                    ArrayAdapter<User> adaptereq=new MyresponseListAdapter(userperspective.getLocation());
                    //TextView textreqhead = (TextView) findViewById(R.id.textViewrequests);
                    //textreqhead.setVisibility(View.VISIBLE);
                    ListView listreq=(ListView) findViewById(R.id.listViewResponses);
                    listreq.setVisibility(View.VISIBLE);

                    listreq.setAdapter(adaptereq);

                    //registerClickCallBack(loc);

                    //populateIntentListview();





                    //ArrayAdapter<User>adapter=new MyUserListAdapter(loc);
                    // ListView list=(ListView) findViewById(R.id.listViewUsers);
                    //list.setAdapter(adapter);

                    ///ArrayAdapter<String >adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.usersfound,usersSearchedcompleteNames);
                    //ListView list=(ListView) findViewById(R.id.listViewUsers);
                    //list.setAdapter(adapter);
                    //needtoshow=false;

                }

                else if((usersthatresponses.size()==0) && (needtoshowresponses) )
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "No Responses to show", Toast.LENGTH_LONG).show();
                    needtoshowresponses=false;


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), "A Network error occurred", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void getUserRequest(){


        DatabaseReference refcurrentuser = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid());


        refcurrentuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                DatabaseReference refrequests = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Peopledown");

                addValuEventListenerrequests(refrequests,currentUser);

                /*if(refrequests.){
                    Toast.makeText(getApplicationContext(), "Please set your preferences by swiping left ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    addValuEventListener(refuser,currentUser.getIntrestedIn(),currentUser.getIntention(),currentUser.getLocation(),currentUser);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });






    }
    private void registerClickCallBackleveltwo(){

        final ListView listsecond=(ListView) findViewById(R.id.listViewUserssecondlevel);
        final ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);
        final SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switch_compat2);


        final Button buttonBackStalk;
        final Button request;
        switchCompat.setSwitchPadding(40);
        switchCompat.setOnCheckedChangeListener(this);
        buttonBackStalk = (Button) findViewById(R.id.buttonbackstalk);
        request= (Button) findViewById(R.id.buttonAreYouDownRequest);





        listsecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final User auser=usersSearchedcompleteNames.get(position);

                Picasso.with(ProfileActivity.this).cancelRequest(lastpic);
                addListenerOnButtont(auser);
                lastpic.setImageResource(0);
                lastpic.setImageResource(android.R.color.transparent);
                textheading.setVisibility(View.INVISIBLE);

                listsecond.setVisibility(View.INVISIBLE);
                lastpic.setVisibility(View.VISIBLE);
                switchCompat.setVisibility(View.INVISIBLE);
                buttonBackStalk.setVisibility(View.VISIBLE);
                request.setVisibility(View.VISIBLE);
                addListenerOnButton();


                StorageReference unigetpic=FirebaseStorage.getInstance().getReference().child(usersSearchedcompleteNames.get(position).getProfilePicture());
                unigetpic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public  void  onSuccess(Uri uri) {
                        lastpic.setImageResource(android.R.color.transparent);
                        Picasso.with(ProfileActivity.this).cancelRequest(lastpic);
                        Picasso.with(ProfileActivity.this).load(uri).resize(250,250).centerCrop().into(lastpic);

                    }


                });

                //Toast.makeText(getApplicationContext(), auser.getLastName()+" "+usersSearchedcompleteNames.get(position), Toast.LENGTH_SHORT).show();




            }
        });




    }




    private void addValuEventListenerrequests(final DatabaseReference userRef,final User userperspective)
    {
        DatabaseReference refdatabase = mRootRef.child("Users");
        final ArrayList<User> usersSearchedForrequests = new ArrayList<>();

        refdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotiterator=dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator=snapshotiterator.iterator();
                while(iterator.hasNext())
                {
                    try
                    {
                        User auser=iterator.next().getValue(User.class);
                        if(auser.getProfilePicture().contains(userperspective.getProfilePicture())){}
                        else if (auser.getIntention().equals(userperspective.getIntention()))
                        {
                            usersSearchedForrequests.add(auser);

                        }
                    }
                    catch (Exception e )
                    {
                        Toast.makeText(getApplicationContext(), "solved", Toast.LENGTH_LONG).show();

                    }


                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

                //Toast.makeText(getApplicationContext(), "A Network error occurred", Toast.LENGTH_LONG).show();


            }
        });








        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersthatrequest=new ArrayList<>();

                Iterable<DataSnapshot> snapshotiterator=dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator=snapshotiterator.iterator();

                while(iterator.hasNext())
                {

                    Map<String, Object> map = (Map<String, Object>) iterator.next().getValue();


                    for(User u:usersSearchedForrequests){
                        if(u.getUid().equals(map.get("user")+"") && !usersthatrequest.contains(u) ){
                            usersthatrequest.add(u);
                        }
                        else{}
                    }

                }

                if((usersthatrequest.size()!=0)  && (needtoshow)){
                    callingviews();


                    //Toast.makeText(getApplicationContext(), usersthatrequest.size()+"", Toast.LENGTH_LONG).show();



                    ArrayAdapter<User> adaptereq=new MyrequestListAdapter(userperspective.getLocation());
                    //TextView textreqhead = (TextView) findViewById(R.id.textViewrequests);
                    //textreqhead.setVisibility(View.VISIBLE);
                    ListView listreq=(ListView) findViewById(R.id.listViewRequests);
                    listreq.setVisibility(View.VISIBLE);

                    listreq.setAdapter(adaptereq);
                    registerClickCallBacklevelRequests();
                    //registerClickCallBack(loc);

                    //populateIntentListview();





                    //ArrayAdapter<User>adapter=new MyUserListAdapter(loc);
                    // ListView list=(ListView) findViewById(R.id.listViewUsers);
                    //list.setAdapter(adapter);

                    ///ArrayAdapter<String >adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.usersfound,usersSearchedcompleteNames);
                    //ListView list=(ListView) findViewById(R.id.listViewUsers);
                    //list.setAdapter(adapter);
                    //needtoshow=false;

                }

                else if((usersthatrequest.size()==0) && (needtoshow) )
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "No Requests to show", Toast.LENGTH_LONG).show();
                    needtoshow=false;


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), "A Network error occurred", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void callingviews(){
        //note dialog error if hectic remove lialog here
        //progressDialog.setMessage("Refreshing");
       // progressDialog.show();


        textheading.setVisibility(View.GONE);
        imgPicture.setVisibility(View.GONE);
        ImageView lastpic=(ImageView) findViewById(R.id.profile_image_lastlevel);
        lastpic.setVisibility(View.GONE);
        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id
                .switch_compat2);
        switchCompat.setVisibility(View.GONE);
        ListView list2=(ListView) findViewById(R.id.listViewUserssecondlevel);
        list2.setVisibility(View.GONE);

        ListView list=(ListView) findViewById(R.id.listViewUsers);
        list.setVisibility(View.GONE);
        Button buttonBackStalk = (Button) findViewById(R.id.buttonbackstalk);
        buttonBackStalk.setVisibility(View.GONE);
        Button request= (Button) findViewById(R.id.buttonAreYouDownRequest);
        request.setVisibility(View.GONE);
        textheading.setText("");
        Button accept= (Button) findViewById(R.id.button2Accept);
        Button declinet= (Button) findViewById(R.id.buttonDecline);
        Button requestsback= (Button) findViewById(R.id.buttonAreYouDownRequest);
        accept.setVisibility(View.GONE);
        declinet.setVisibility(View.GONE);
        requestsback.setVisibility(View.GONE);
        ListView listres=(ListView) findViewById(R.id.listViewResponses);
        listres.setVisibility(View.GONE);
        TextView number=(TextView) findViewById(R.id.textView10facebookv2);
        TextView fb=(TextView) findViewById(R.id.textView11numberv2);
        number.setVisibility(View.GONE);
        fb.setVisibility(View.GONE);
       // progressDialog.dismiss();
    }




    public void getUsersShowUSers()
    {
        DatabaseReference refcurrentuser = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid());
        refcurrentuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
               // String currentIntention;
                //String currentLocation;
                //String currentSexPref;
                if(currentUser.getIntention().equals("") |currentUser.getIntrestedIn().equals("")){
                    Toast.makeText(getApplicationContext(), "Please set your preferences by swiping left ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    lockondatachange=false;
                    addValuEventListener(refuser,currentUser.getIntrestedIn(),currentUser.getIntention(),currentUser.getLocation(),currentUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //GETTING USERS



    public void timecheck(){
        DatabaseReference timecheckrefrence = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("timecommitment");
        final DatabaseReference timecheckrefrencepeople = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Peopledown");
        timecheckrefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dbtimecommit = dataSnapshot.getValue(String.class);
                int realtimerightnow=now.get(Calendar.HOUR_OF_DAY);

                String[] splited = dbtimecommit.split("\\s+");


                if(((Integer.parseInt(splited[1])<Integer.parseInt(splited[0])) ) && (( (realtimerightnow > (Integer.parseInt(splited[1])))   && (realtimerightnow < (Integer.parseInt(splited[0]))) ))){
                    DatabaseReference reftime = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid());


                    Map newUserData = new HashMap();
                    newUserData.put("timecommitment", "0 0");
                    newUserData.put("intention","");

                    reftime.updateChildren(newUserData);

                    timecheckrefrencepeople.removeValue();
                    DatabaseReference deleteonoty = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("NotificationPeopledown");
                    deletereqnit(deleteonoty);



                }
                else if(Integer.parseInt(splited[0])==0 && Integer.parseInt(splited[1])==0 )
                {




                }
                else if((((Integer.parseInt(splited[1])>Integer.parseInt(splited[0])) ) && !(( (realtimerightnow >= (Integer.parseInt(splited[0])))   && (realtimerightnow < (Integer.parseInt(splited[1]))) ))))
                {
                    DatabaseReference reftime = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid());


                    Map newUserData = new HashMap();
                    newUserData.put("timecommitment","0 0");
                    newUserData.put("intention","");

                    reftime.updateChildren(newUserData);
                    timecheckrefrencepeople.removeValue();
                    DatabaseReference deleteonoty = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("NotificationPeopledown");
                    deletereqnit(deleteonoty);



                }
                else{
                    returnsRequests();

                }





            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    class LearnGesture extends GestureDetector.SimpleOnGestureListener{


        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){


            if(event2.getX()>event1.getX()){

            }
            else

            if (event2.getX()<event1.getX()  )
            {
                DatabaseReference reftime = mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("timecommitment");
                reftime.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String dbtimecommit = dataSnapshot.getValue(String.class);
                        int realtimerightnow=now.get(Calendar.HOUR_OF_DAY);

                        String[] splited = dbtimecommit.split("\\s+");

                        if(Integer.parseInt(splited[0])==0 && Integer.parseInt(splited[1])==0 )
                        {
                            Intent intent =new Intent(ProfileActivity.this,My_ProfileActivity.class);
                            stopupdates();
                            finish();
                            startActivity(intent);
                        }
                        else if(((Integer.parseInt(splited[1])<Integer.parseInt(splited[0])) ) && ((realtimerightnow> (Integer.parseInt(splited[1])))|(realtimerightnow<Integer.parseInt(splited[0])))){
                            Toast.makeText(ProfileActivity.this,"Access denied!",Toast.LENGTH_SHORT).show();
                        }
                        else if(!((realtimerightnow>=Integer.parseInt(splited[0])) && (realtimerightnow<=Integer.parseInt(splited[1])))){
                            Intent intent =new Intent(ProfileActivity.this,My_ProfileActivity.class);
                            stopupdates();
                            finish();
                            startActivity(intent);
                        }
                        else

                            Toast.makeText(ProfileActivity.this,"Access denied!",Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            return true;
        }

    }


    private  class MyUserListAdapter extends ArrayAdapter<User> {
        private String loc;

        public MyUserListAdapter(String location){
            super(ProfileActivity.this,R.layout.oneuserdisplay,usersSearchedcompleteNames);
            loc=location;
        }


        @Override
        public synchronized View getView(int position, View convertView, ViewGroup parent) {
            View userview=convertView;

            User currentuser=usersSearchedcompleteNames.get(position);

            if(userview==null){
                userview=getLayoutInflater().inflate(R.layout.oneuserdisplay,parent,false);
                //ImageView imageview=(ImageView) userview.findViewById(R.id.profile_image);
                //Picasso.with(ProfileActivity.this).cancelRequest(imageview);
                //imageview.setImageResource(0);
                //imageview.setImageResource(android.R.color.transparent);

            }
            TextView settingatributeagepref=(TextView) userview.findViewById(R.id.textView9displayagepref);


            final ImageView imageview=(ImageView) userview.findViewById(R.id.profile_image);
            Picasso.with(ProfileActivity.this).cancelRequest(imageview);
            imageview.setImageResource(0);
            imageview.setImageResource(android.R.color.transparent);








            //fill the view



            StorageReference unigetpic=FirebaseStorage.getInstance().getReference().child(usersSearchedcompleteNames.get(position).getProfilePicture());
            unigetpic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public  void  onSuccess(Uri uri) {
                    imageview.setImageResource(android.R.color.transparent);
                    Picasso.with(ProfileActivity.this).cancelRequest(imageview);
                    Picasso.with(ProfileActivity.this).load(uri).resize(150,150).centerCrop().into(imageview);

                    // Picasso.with(ProfileActivity.this).load(uri).resize(150,150).centerCrop().noFade().into(imageview);

                }


            });






            //Picasso.with(ProfileActivity.this).load(unigetpic.getDownloadUrl().getResult()).resize(150,150).centerCrop().into(imageview);





            TextView settingatributenameandsurname=(TextView) userview.findViewById(R.id.textView9NameSurname);

            settingatributenameandsurname.setText((currentuser.getFirstName()+" "+currentuser.getLastName()+", "+currentuser.getAge()));

            if(currentuser.getAgerange().equals("1"))
            {

                settingatributeagepref.setText("18yr-25yr.");
            }
            if(currentuser.getAgerange().equals("2"))
            {
                //TextView settingatributeagepref=(TextView) userview.findViewById(R.id.textView9displayagepref);
                settingatributeagepref.setText("25yr-30yr.");
            }
            if(currentuser.getAgerange().equals("3"))
            {
                //TextView settingatributeagepref=(TextView) userview.findViewById(R.id.textView9displayagepref);
                settingatributeagepref.setText("30yr-35yr.");
            }
            if(currentuser.getAgerange().equals("4"))
            {
                // TextView settingatributeagepref=(TextView) userview.findViewById(R.id.textView9displayagepref);
                settingatributeagepref.setText("35+ yrs.");
            }

            if(currentuser.getLocation().equals("")|loc.equals("") ){
                TextView settingatributeLoc=(TextView) userview.findViewById(R.id.textView9DisplayDistance);
                settingatributeLoc.setText("Not known");
            }
            else
            {
                String[] splited = loc.split("\\s+");
                String[] splitedcurrentuser = currentuser.getLocation().split("\\s+");
                Double lon1,lat1,lon2,lat2;

                lon1=Double.valueOf(splited[0]);
                lat1=Double.valueOf(splited[1]);

                lon2=Double.valueOf(splitedcurrentuser[0]);
                lat2=Double.valueOf(splitedcurrentuser[1]);

                double answering=distance(lat1,lat2,lon1,lon2,0.0,0.0);
                TextView settingatributeLoc=(TextView) userview.findViewById(R.id.textView9DisplayDistance);
                settingatributeLoc.setText(Math.round(answering)+" metres");

            }

            return userview;

        }


    }
    private  class MyresponseListAdapter extends ArrayAdapter<User> {
        private String loc;

        public MyresponseListAdapter(String location){
            super(ProfileActivity.this,R.layout.usersresponseaccepts,usersthatresponses);
            loc=location;

        }


        @Override
        public synchronized View getView(int position, View convertView, ViewGroup parent) {
            View userview=convertView;

            User currentuser=usersthatresponses.get(position);
            String currentanswer=userstanswerresponses.get(position);

            if(userview==null){
                userview=getLayoutInflater().inflate(R.layout.usersresponseaccepts,parent,false);
                //ImageView imageview=(ImageView) userview.findViewById(R.id.profile_image);
                //Picasso.with(ProfileActivity.this).cancelRequest(imageview);
                //imageview.setImageResource(0);
                //imageview.setImageResource(android.R.color.transparent);

            }
            //TextView settingatributeagepref=(TextView) userview.findViewById(R.id.textView9displayagepref);


            final ImageView imageview=(ImageView) userview.findViewById(R.id.profile_image);
            Picasso.with(ProfileActivity.this).cancelRequest(imageview);
            imageview.setImageResource(0);
            imageview.setImageResource(android.R.color.transparent);

            if(currentanswer.equals("y")){
                TextView number=(TextView) userview.findViewById(R.id.textView10facebook);
                TextView fb=(TextView) userview.findViewById(R.id.textView11number);
                number.setText("FB:"+currentuser.getFbdetails());
                fb.setText("Whatsapp:"+currentuser.getNumber());
            }
            else{
                TextView number=(TextView) userview.findViewById(R.id.textView10facebook);
                TextView fb=(TextView) userview.findViewById(R.id.textView11number);
                number.setText("FB:"+"Not available");
                fb.setText("Whatsapp:"+"Not available");

            }


            if(currentanswer.equals("y")){
                TextView response=(TextView) userview.findViewById(R.id.answer);
                response.setText(currentuser.getFirstName()+" has accepted your AYD request");


            }
            else{
                TextView response=(TextView) userview.findViewById(R.id.answer);
                response.setText(currentuser.getFirstName()+" has declined your AYD request");

            }






            //fill the view

            StorageReference unigetpic=FirebaseStorage.getInstance().getReference().child(usersthatresponses.get(position).getProfilePicture());
            unigetpic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public  void  onSuccess(Uri uri) {
                    imageview.setImageResource(android.R.color.transparent);
                    Picasso.with(ProfileActivity.this).cancelRequest(imageview);
                    Picasso.with(ProfileActivity.this).load(uri).resize(150,150).centerCrop().into(imageview);

                    // Picasso.with(ProfileActivity.this).load(uri).resize(150,150).centerCrop().noFade().into(imageview);

                }


            });






            //Picasso.with(ProfileActivity.this).load(unigetpic.getDownloadUrl().getResult()).resize(150,150).centerCrop().into(imageview);





            TextView settingatributenameandsurname=(TextView) userview.findViewById(R.id.textView9NameSurname);

            settingatributenameandsurname.setText((currentuser.getFirstName()+" "+currentuser.getLastName()+", "+currentuser.getAge()));



            if(currentuser.getLocation().equals("")|loc.equals("") ){
                TextView settingatributeLoc=(TextView) userview.findViewById(R.id.textView9DisplayDistance);
                settingatributeLoc.setText("Not known");
            }
            else
            {
                String[] splited = loc.split("\\s+");
                String[] splitedcurrentuser = currentuser.getLocation().split("\\s+");
                Double lon1,lat1,lon2,lat2;

                lon1=Double.valueOf(splited[0]);
                lat1=Double.valueOf(splited[1]);

                lon2=Double.valueOf(splitedcurrentuser[0]);
                lat2=Double.valueOf(splitedcurrentuser[1]);

                double answering=distance(lat1,lat2,lon1,lon2,0.0,0.0);
                TextView settingatributeLoc=(TextView) userview.findViewById(R.id.textView9DisplayDistance);
                settingatributeLoc.setText(Math.round(answering)+" metres away.");

            }

            return userview;

        }


    }
    private  class MyrequestListAdapter extends ArrayAdapter<User> {
        private String loc;

        public MyrequestListAdapter(String location){
            super(ProfileActivity.this,R.layout.oneuserreq,usersthatrequest);
            loc=location;
        }


        @Override
        public synchronized View getView(int position, View convertView, ViewGroup parent) {
            View userview=convertView;

            User currentuser=usersthatrequest.get(position);

            if(userview==null){
                userview=getLayoutInflater().inflate(R.layout.oneuserreq,parent,false);
                //ImageView imageview=(ImageView) userview.findViewById(R.id.profile_image);
                //Picasso.with(ProfileActivity.this).cancelRequest(imageview);
                //imageview.setImageResource(0);
                //imageview.setImageResource(android.R.color.transparent);

            }
            //TextView settingatributeagepref=(TextView) userview.findViewById(R.id.textView9displayagepref);


            final ImageView imageview=(ImageView) userview.findViewById(R.id.profile_image);
            Picasso.with(ProfileActivity.this).cancelRequest(imageview);
            imageview.setImageResource(0);
            imageview.setImageResource(android.R.color.transparent);







            //fill the view



            StorageReference unigetpic=FirebaseStorage.getInstance().getReference().child(usersthatrequest.get(position).getProfilePicture());
            unigetpic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public  void  onSuccess(Uri uri) {
                    imageview.setImageResource(android.R.color.transparent);
                    Picasso.with(ProfileActivity.this).cancelRequest(imageview);
                    Picasso.with(ProfileActivity.this).load(uri).resize(150,150).centerCrop().into(imageview);

                    // Picasso.with(ProfileActivity.this).load(uri).resize(150,150).centerCrop().noFade().into(imageview);

                }


            });






            //Picasso.with(ProfileActivity.this).load(unigetpic.getDownloadUrl().getResult()).resize(150,150).centerCrop().into(imageview);





            TextView settingatributenameandsurname=(TextView) userview.findViewById(R.id.textView9NameSurname);

            settingatributenameandsurname.setText((currentuser.getFirstName()+" "+currentuser.getLastName()+", "+currentuser.getAge()));



            if(currentuser.getLocation().equals("")|loc.equals("") ){
                TextView settingatributeLoc=(TextView) userview.findViewById(R.id.textView9DisplayDistance);
                settingatributeLoc.setText("Not known");
            }
            else
            {
                String[] splited = loc.split("\\s+");
                String[] splitedcurrentuser = currentuser.getLocation().split("\\s+");
                Double lon1,lat1,lon2,lat2;

                lon1=Double.valueOf(splited[0]);
                lat1=Double.valueOf(splited[1]);

                lon2=Double.valueOf(splitedcurrentuser[0]);
                lat2=Double.valueOf(splitedcurrentuser[1]);

                double answering=distance(lat1,lat2,lon1,lon2,0.0,0.0);
                TextView settingatributeLoc=(TextView) userview.findViewById(R.id.textView9DisplayDistance);
                settingatributeLoc.setText(Math.round(answering)+" metres away");

            }

            return userview;

        }


    }






    //RECYCLE

    //RECYCLE
    private class MyIntentionListAdapter extends ArrayAdapter<intentions>{
        private int intention1;
        private int intention2;
        private int intention3;
        private int intention4;
        private int intention5;

        public MyIntentionListAdapter(int numintent1,int numintent2,int numintent3,int numintent4,int numintent5){
            super(ProfileActivity.this,R.layout.intentionview,myIntentions);
            intention1=numintent1;
            intention2=numintent2;
            intention3=numintent3;
            intention4=numintent4;
            intention5=numintent5;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View Intentionview=convertView;
            if(Intentionview==null){
                Intentionview=getLayoutInflater().inflate(R.layout.intentionview,parent,false);
            }

            intentions currentintention=myIntentions.get(position);

            //fill the view

            TextView settinganame=(TextView) Intentionview.findViewById(R.id.textView10IntentionnameviewIntentionName);
            settinganame.setText(currentintention.getIntentionname());


            if(currentintention.getIntentionname().equals("Hook-up")){
                TextView settingnumberofpeople=(TextView) Intentionview.findViewById(R.id.textView10numberofpeople);
                settingnumberofpeople.setText(intention1+"");
            }
            if(currentintention.getIntentionname().equals("Long-term Relationship")){
                TextView settingnumberofpeople=(TextView) Intentionview.findViewById(R.id.textView10numberofpeople);
                settingnumberofpeople.setText(intention2+"");
            }
            if(currentintention.getIntentionname().equals("Meeting new people")){
                TextView settingnumberofpeople=(TextView) Intentionview.findViewById(R.id.textView10numberofpeople);
                settingnumberofpeople.setText(intention3+"");
            }
            if(currentintention.getIntentionname().equals("A good time")){
                TextView settingnumberofpeople=(TextView) Intentionview.findViewById(R.id.textView10numberofpeople);
                settingnumberofpeople.setText(intention4+"");
            }
            if(currentintention.getIntentionname().equals("Potential date")){
                TextView settingnumberofpeople=(TextView) Intentionview.findViewById(R.id.textView10numberofpeople);
                settingnumberofpeople.setText(intention5+"");
            }

            return Intentionview;
        }
    }




    @Override
    public void onDestroy() {

        super.onDestroy();
        stopupdates();


    }
}