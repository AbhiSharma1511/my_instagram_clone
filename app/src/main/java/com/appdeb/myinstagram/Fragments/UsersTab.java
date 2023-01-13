package com.appdeb.myinstagram.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appdeb.myinstagram.R;
import com.appdeb.myinstagram.UsersPosts;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private ProgressDialog progressDialog;

    public UsersTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_users_tab, container, false);

        listView = view.findViewById(R.id.listView1);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading users, please wait...");
        progressDialog.show();

        listView.setOnItemClickListener(UsersTab.this);
        listView.setOnItemLongClickListener(UsersTab.this);

        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e==null){
                    if (users.size()>0){
                        for (ParseUser user: users){
                            arrayList.add(user.getUsername());
                        }
                    }
                    progressDialog.dismiss();

                    listView.setAdapter(arrayAdapter);
                }
            }
        });




        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent= new Intent(getContext(), UsersPosts.class);
        intent.putExtra("username", arrayList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username",arrayList.get(position));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user!= null && e == null){

                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());
                    prettyDialog.setTitle("User Information")
                            .setMessage(user.get("profileBio")+"\n"
                            +user.get("profileProfession")+"\n"
                            +user.get("profileHobbies")+"\n"
                            +user.get("profileFavSports")+"\n");
                    prettyDialog.setIcon(R.drawable.ic_baseline_person_24)
                            .addButton(
                                    "OK", R.color.white,
                                    R.color.pdlg_color_green,
                                    new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            prettyDialog.dismiss();
                                        }
                                    }
                            ).show();
                }
                else{
                    FancyToast.makeText(getContext(),"OOPS! There is some error.!",FancyToast.LENGTH_SHORT,FancyToast.DEFAULT,false).show();
                }
            }
        });

        return true;
    }
}