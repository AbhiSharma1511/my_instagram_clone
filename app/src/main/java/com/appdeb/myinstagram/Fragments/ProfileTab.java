package com.appdeb.myinstagram.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdeb.myinstagram.R;
import com.appdeb.myinstagram.databinding.FragmentProfileBinding;

public class ProfileTab extends Fragment {

    private FragmentProfileBinding binding;


    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }
}