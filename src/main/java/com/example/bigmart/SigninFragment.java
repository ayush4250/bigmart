package com.example.bigmart;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SigninFragment extends Fragment {


    public SigninFragment() {
        // Required empty public constructor
    }

    private TextView donthaveanaccount;
    private FrameLayout parentFrameLayout;
    private EditText email,password;
    private ImageButton closebutton;
    private TextView forgotPassword;
    private Button signinbtn;
    private String EmailPattern="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseAuth firebaseAuth;
    public static boolean disableclosebtn = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_signin, container, false);

        donthaveanaccount=view.findViewById(R.id.tv_no_have_acc);
        parentFrameLayout=getActivity().findViewById(R.id.register_framelayout);
        email=view.findViewById(R.id.sign_in_email);
        password=view.findViewById(R.id.sign_in_cpassword);
        forgotPassword=view.findViewById(R.id.sigin_in_forgot);
        closebutton=view.findViewById(R.id.sign_in_close_btn);
        signinbtn=view.findViewById(R.id.sign_in_btn);
        firebaseAuth=FirebaseAuth.getInstance();
        if(disableclosebtn){
            closebutton.setVisibility(View.GONE);
        }else{
            closebutton.setVisibility(View.VISIBLE);
        }


        return view;


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        donthaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignupFragment());
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // setFragment(new ResetPasswordFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkemailAndPassword();
            }
        });

        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainIntent();

            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
       // fragmentTransaction.setCustomAnimations(R.anim.right_slide_in,R.anim.right_slide_out);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void checkinputs(){
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText())){
                signinbtn.setEnabled(true);
                signinbtn.setTextColor(Color.argb(255,255,255,255));
            }else {
                signinbtn.setEnabled(false);
                signinbtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            signinbtn.setEnabled(false);
            signinbtn.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private void checkemailAndPassword(){
        if(email.getText().toString().matches(EmailPattern)){
            if(password.length()>=8){

                signinbtn.setEnabled(false);
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mainIntent();
                                }else{
                                    signinbtn.setEnabled(true);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(getActivity(),"Incorrect email or password",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getActivity(),"Incorrect email or password", Toast.LENGTH_SHORT).show();

        }
    }
    private void mainIntent(){
        if(disableclosebtn){
            disableclosebtn=false;
        }else {
            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);
        }
        getActivity().finish();
    }


}
