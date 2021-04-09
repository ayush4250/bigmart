package com.example.bigmart;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {


    public SignupFragment() {
        // Required empty public constructor
    }


    private TextView alreadyhaveanaccoutn;
    private FrameLayout parentFrameLayout;
    private EditText email,fullName,password,confirmPassword;
    private ImageButton closebutton;
    private Button signUpbtn;
    private FirebaseAuth firebaseAuth;
    private String emailPattenr="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseFirestore firebaseFirestore;
    public static boolean disableclosebtn=false;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_signup, container, false);


        alreadyhaveanaccoutn=view.findViewById(R.id.tv_already_acc);
        parentFrameLayout=getActivity().findViewById(R.id.register_framelayout);
        email=view.findViewById(R.id.sign_up_email);
        fullName=view.findViewById(R.id.sign_up_name);
        password=view.findViewById(R.id.sign_up_password);
        confirmPassword=view.findViewById(R.id.sign_up_cpassword);
        closebutton=view.findViewById(R.id.sign_up_close);
        signUpbtn=view.findViewById(R.id.sign_up_btn);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

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
        alreadyhaveanaccoutn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SigninFragment());
            }
        });

        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
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
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
       // fragmentTransaction.setCustomAnimations(R.anim.left_slide_in,R.anim.left_slide_out);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void checkInputs(){
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(fullName.getText())){
                if(!TextUtils.isEmpty(password.getText())  && password.length()>=8){
                    if(!TextUtils.isEmpty(confirmPassword.getText())){
                        signUpbtn.setEnabled(true);
                        signUpbtn.setTextColor(Color.argb(255,255,255,255));
                    }else{
                        signUpbtn.setEnabled(false);
                        signUpbtn.setTextColor(Color.argb(50,255,255,255));
                    }
                }else{
                    signUpbtn.setEnabled(false);
                    signUpbtn.setTextColor(Color.argb(50,255,255,255));
                }
            }else{
                signUpbtn.setEnabled(false);
                signUpbtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            signUpbtn.setEnabled(false);
            signUpbtn.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private void checkEmailAndPassword(){

        Drawable customErrorIcon=getResources().getDrawable(R.drawable.ic_error_black,null);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());
        if(email.getText().toString().matches(emailPattenr)){
            if(password.getText().toString().equals(confirmPassword.getText().toString())){
                //Progress Bar

                signUpbtn.setEnabled(false);
                signUpbtn.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    Map<String,Object> userdata=new HashMap<>();
                                    userdata.put("fullname",fullName.getText().toString());

                                    firebaseFirestore.collection("users").document(firebaseAuth.getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        CollectionReference userdataReference= firebaseFirestore.collection("users").document(firebaseAuth.getUid()).collection("User_data");

                                                        ///Maps
                                                        Map<String,Object> wishlistmap=new HashMap<>();
                                                        wishlistmap.put("list_size",(long)0);

                                                        Map<String,Object> ratingsmap=new HashMap<>();
                                                        ratingsmap.put("list_size",(long)0);

                                                        Map<String,Object> cartMap=new HashMap<>();
                                                        cartMap.put("list_size",(long)0);

                                                        Map<String,Object> myAddressesMap=new HashMap<>();
                                                        myAddressesMap.put("list_size",(long)0);

                                                        ///Maps


                                                        final List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("My_wishlist");
                                                        documentNames.add("My_ratings");
                                                        documentNames.add("My_Cart");
                                                        documentNames.add("My_Addresses");

                                                        List<Map<String,Object>> documentfields=new ArrayList<>();
                                                        documentfields.add(wishlistmap);
                                                        documentfields.add(ratingsmap);
                                                        documentfields.add(cartMap);
                                                        documentfields.add(myAddressesMap);

                                                        for(int x=0;x<documentNames.size();x++){

                                                            final int finalX = x;
                                                            userdataReference.document(documentNames.get(x))
                                                                    .set(documentfields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        if(finalX ==documentNames.size()-1) {
                                                                            mainIntent();
                                                                        }
                                                                    }else{
                                                                        signUpbtn.setEnabled(true);
                                                                        signUpbtn.setTextColor(Color.argb(255,255,255,255));
                                                                        String error=task.getException().getMessage();
                                                                        Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                        }


                                                    }else{
                                                        String error=task.getException().getMessage();
                                                        Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }else{
                                    signUpbtn.setEnabled(true);
                                    signUpbtn.setTextColor(Color.argb(255,255,255,255));
                                    String error=task.getException().getMessage();
                                    Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else{
                confirmPassword.setError("Password does not match!",customErrorIcon);
            }
        }else{
            email.setError("Invalid Email",customErrorIcon);
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
