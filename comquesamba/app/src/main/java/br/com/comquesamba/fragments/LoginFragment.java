package br.com.comquesamba.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import br.com.comquesamba.R;
import br.com.comquesamba.activities.MainActivity;

public class LoginFragment extends Fragment implements Validator.ValidationListener {

    private FirebaseAuth mAuth;
    private Button loginBtn;
    private Button registerBtn;
    private Validator validator;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    private EditText loginEmail;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    private EditText loginPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        validator = new Validator(this);
        validator.setValidationListener(this);

        mAuth = FirebaseAuth.getInstance();
        loginEmail = (EditText) getActivity().findViewById(R.id.frag_login_email);
        loginPassword = (EditText) getActivity().findViewById(R.id.frag_login_password);

        registerBtn = (Button) getActivity().findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterFragment registerFragment = new RegisterFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, registerFragment);
                ft.commit();
            }
        });

        loginBtn = (Button) getActivity().findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    private void userLogin(){
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            }

                        } else {
                            Toast.makeText(getContext(), "Email e/ou senha incorretos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onValidationSucceeded() {
        userLogin();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors){
            View v = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            if (v instanceof EditText) {
                ((EditText) v).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
