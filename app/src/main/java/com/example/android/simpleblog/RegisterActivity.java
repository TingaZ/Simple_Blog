package com.example.android.simpleblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText mName, mEmail, mPassword;
    private Button mSignUp;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mDialog = new ProgressDialog(this);

        mName = (EditText) findViewById(R.id.et_name);
        mEmail= (EditText) findViewById(R.id.et_email);
        mPassword = (EditText) findViewById(R.id.et_password);
        mSignUp = (Button) findViewById(R.id.button_register);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRegister();

            }
        });



    }

    private void startRegister() {
        final String name = mName.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && ! TextUtils.isEmpty(password)){

            mDialog.setMessage("Signing Up ...");
            mDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        //get the unique Id from database
                        String user_id = mAuth.getCurrentUser().getUid();

                        //Create a Child that will store users(user_id), which is called
                        // {@link Users} on our root directory
                        DatabaseReference current_loggedIn_user_id = mDatabase.child(user_id);

                        //create another child that will store name and image of the logged in user and
                        // assign it to the name Field
                        current_loggedIn_user_id.child("name").setValue(name);
                        current_loggedIn_user_id.child("image").setValue("default");

                        mDialog.dismiss();

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.getMessage();
                }
            });

        }

    }

    public void setNextSignIn(){

        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}
