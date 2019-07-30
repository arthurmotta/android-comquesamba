package br.com.comquesamba.fragments;


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
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import br.com.comquesamba.R;

public class RegisterFragment extends Fragment implements Validator.ValidationListener{

    private FirebaseAuth mAuth;
    private Button registerBtn;
    private Validator validator;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    @Email
    private EditText registerEmail;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    @Password(message = "Senha inválida (mínimo 6 dígitos)")
    private EditText registerPassword;

    @NotEmpty(message = "Este campo precisa ser preenchido.")
    @ConfirmPassword(message = "As senhas não conferem")
    private EditText registerConfirmPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        validator = new Validator(this);
        validator.setValidationListener(this);

        mAuth = FirebaseAuth.getInstance();
        registerEmail = (EditText) getActivity().findViewById(R.id.frag_register_email);
        registerPassword = (EditText) getActivity().findViewById(R.id.frag_register_password);
        registerConfirmPassword = (EditText) getActivity().findViewById(R.id.frag_register_confirm_password);

        registerBtn = (Button) getActivity().findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    private void registerUser(){
        String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Usuario cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    LoginFragment loginFragment = new LoginFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main, loginFragment);
                    ft.commit();
                } else {
                    Toast.makeText(getContext(), "O email já está sendo usado por outra conta.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        registerUser();
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
