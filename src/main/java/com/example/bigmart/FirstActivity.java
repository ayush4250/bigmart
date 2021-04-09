package com.example.bigmart;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirstActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        firebaseAuth=FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser==null){
            Intent registerintent = new Intent(FirstActivity.this,RegisterActivity.class);
            startActivity(registerintent);
            finish();

        }else{

            FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid()).update("Last Seen", FieldValue.serverTimestamp())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent mainintent = new Intent(FirstActivity.this,MainActivity.class);
                                startActivity(mainintent);
                                finish();
                            }else{
                                String error=task.getException().getMessage();
                                Toast.makeText(FirstActivity.this,error,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }

    }
}
