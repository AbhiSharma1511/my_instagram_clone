package com.appdeb.myinstagram.Fragments;

import android.app.ProgressDialog;
import android.database.DatabaseUtils;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appdeb.myinstagram.R;
import com.appdeb.myinstagram.databinding.FragmentProfileBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Objects;

public class ProfileTab extends Fragment {

    private FragmentProfileBinding binding;
    private ProgressDialog progressDialog;
    String profileName, userBio, userProfession, hobbies, favSports;


    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
//        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        View view =  binding.getRoot();


        final ParseUser parseUser = ParseUser.getCurrentUser();
        progressDialog = new ProgressDialog(getContext());

        binding.edtProfileName.setText((CharSequence) parseUser.get("profileName"));
        binding.edtBio.setText((CharSequence) parseUser.get("profileBio"));
        binding.edtProfession.setText( (CharSequence) parseUser.get("profileProfession"));
        binding.edtHobbies.setText((CharSequence) parseUser.get("profileHobbies"));
        binding.edtSports.setText((CharSequence) parseUser.get("profileFavSports"));

        binding.btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Hello World", Toast.LENGTH_SHORT).show();

                profileName = binding.edtProfileName.getText().toString();
                userBio = binding.edtBio.getText().toString();
                userProfession = binding.edtProfession.getText().toString();
                hobbies = binding.edtHobbies.getText().toString();
                favSports = binding.edtSports.getText().toString();

                parseUser.put("profileName",profileName);
                parseUser.put("profileBio",userBio);
                parseUser.put("profileProfession",userProfession);
                parseUser.put("profileHobbies",hobbies);
                parseUser.put("profileFavSports",favSports);

                progressDialog.show();

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                            FancyToast.makeText(getContext(),"Information Update successfully.",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                            progressDialog.dismiss();
                        }
                        else {
                            FancyToast.makeText(getContext(),e.getMessage() ,FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });


        return view;
    }
}