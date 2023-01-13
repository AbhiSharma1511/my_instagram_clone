package com.appdeb.myinstagram.Fragments;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.appdeb.myinstagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


public class SharePictureTab extends Fragment {

    private ImageView imgShare;
    private Button btnShareImage;
    private EditText edtDescription;

    private Bitmap receivedImageBitMap;


    public SharePictureTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        imgShare = view.findViewById(R.id.imgChooseImage);
        btnShareImage = view.findViewById(R.id.btnShareImage);
        edtDescription = view.findViewById(R.id.edtDescription);

        final ProgressDialog progressDialog = new ProgressDialog(getContext());

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >=23 && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                }
                else{
                    getChosenImage();
                }
            }
        });

        btnShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receivedImageBitMap != null){
                    FancyToast.makeText(getContext(),"Choose image!!!",FancyToast.LENGTH_SHORT,FancyToast.DEFAULT,false).show();
                    if (edtDescription.getText().toString().equals("")){
                        FancyToast.makeText(getContext(),"Enter description first...",FancyToast.LENGTH_SHORT,FancyToast.DEFAULT,false).show();
                    }else{
                        btnShareImage.setBackgroundColor(Color.GRAY);
                        progressDialog.setMessage("Uploading...");
                        progressDialog.show();

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedImageBitMap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("pic.png",bytes);
                        ParseObject parseObject = new ParseObject("Photos");
                        parseObject.put("picture",parseFile);
                        parseObject.put("pic_dec",edtDescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());


                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null){
                                    FancyToast.makeText(getContext(),"Upload Successful.",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                    imgShare.setImageResource(R.drawable.choose_image);
                                    edtDescription.setText("");
                                }
                                else {
                                    FancyToast.makeText(getContext(),"Error: "+e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                                }
                                progressDialog.dismiss();
                                btnShareImage.setBackgroundColor(Color.BLUE);
                            }
                        });
                    }

                }else{

                }
            }
        });

        return view;
    }


    private void getChosenImage() {
//        FancyToast.makeText(getContext(),"Now we can select the image.",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==1000){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                FancyToast.makeText(getContext(),"Now we can select the image.",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==2000){
            if (resultCode == RESULT_OK && data!=null ){
                try{

                    Uri selectImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectImage,filePathColumn,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    cursor.close();

                    receivedImageBitMap = BitmapFactory.decodeFile(picturePath);

                    imgShare.setImageBitmap(receivedImageBitMap);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}