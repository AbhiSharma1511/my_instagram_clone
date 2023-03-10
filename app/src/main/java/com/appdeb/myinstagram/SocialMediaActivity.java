package com.appdeb.myinstagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.appdeb.myinstagram.Adapters.TabAdapter;
import com.appdeb.myinstagram.MenuActivities.Setting;
import com.appdeb.myinstagram.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_main);

        final ParseUser user = ParseUser.getCurrentUser();
        setTitle((CharSequence) user.getUsername()+" 🧡");

        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager,true);
    }

/******************************************* Menu **************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.itmPostImage){
            if (Build.VERSION.SDK_INT >=23 &&
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE},3000);
            }
            else{
                captureImage();
            }
        }
        else if (item.getItemId() == R.id.itmProfile){
        }
        else if (item.getItemId() == R.id.itmInsta){
//            startActivity(new Intent(SocialMediaActivity.this,OfficialInstagram.class));
        }
        else if (item.getItemId() == R.id.itmCamera){
            openCamera();
        }
        else if (item.getItemId() == R.id.itmSetting){
            startActivity(new Intent(SocialMediaActivity.this, Setting.class));
        }
        else if (item.getItemId() == R.id.itmLogout){
            ParseUser.logOut();
            Intent intent = new Intent(SocialMediaActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openCamera() {

    }



/******************************************* Request Permission For the user ******************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==3000){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                captureImage();
            }
        }
    }



/********************************************* Choosing Image from the Gallery ****************************/
    private void captureImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,4000);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==4000 && resultCode == RESULT_OK && data!=null){

            try{
                Uri captureImage = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), captureImage);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                ParseFile parseFile = new ParseFile("img.png", bytes);
                ParseObject parseObject = new ParseObject("Photo");
                parseObject.put("picture", parseFile);
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                        FancyToast.makeText(SocialMediaActivity.this, "Picture Uploaded!", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
                        }
                        else{
                            FancyToast.makeText(SocialMediaActivity.this, "Unknown error: "+e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
                        }
                        progressDialog.dismiss();
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}