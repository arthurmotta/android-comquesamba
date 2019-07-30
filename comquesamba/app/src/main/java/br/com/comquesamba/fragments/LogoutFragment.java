package br.com.comquesamba.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import br.com.comquesamba.activities.MainActivity;

public class LogoutFragment extends Fragment {

    FirebaseAuth mAuth;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }
}
