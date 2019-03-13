package com.nazir.uberandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nazir.uberandroid.Common.Common;
import com.nazir.uberandroid.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity{

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    //Widgets
    Button btnSignIn, btnRegister;
    RelativeLayout rootLayout; //used for SnackBar


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Before setContextView
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                            .setDefaultFontPath("fonts/Arkhip_font.ttf")
                                            .setFontAttrId(R.attr.fontPath)
                                            .build());
        setContentView(R.layout.activity_main);

        //Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference(Common.DRIVER_INFO_TBL);

        //Widgets
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnRegister = findViewById(R.id.btn_register);
        rootLayout = findViewById(R.id.root_layout);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRegisterDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showLoginDialog();
            }
        });

    }



    private void showRegisterDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        final View register_layout = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edt_email);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edt_password);
        final MaterialEditText edtName = register_layout.findViewById(R.id.edt_name);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edt_phone);

        dialog.setView(register_layout);

        dialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                if (TextUtils.isEmpty(edtEmail.getText().toString())){

                    Snackbar.make(rootLayout, "Please enter your email address", Snackbar.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(edtPassword.getText().toString())){

                    Snackbar.make(rootLayout, "Please enter your password", Snackbar.LENGTH_SHORT).show();

                }else if (edtPassword.getText().toString().length() < 6){

                    Snackbar.make(rootLayout, "password must 6 characters", Snackbar.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(edtName.getText().toString())){

                    Snackbar.make(rootLayout, "Please enter your name", Snackbar.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(edtPhone.getText().toString())){

                    Snackbar.make(rootLayout, "Please enter your phone number", Snackbar.LENGTH_SHORT).show();

                }




                //Register new User
                registerNewUser(edtEmail.getText().toString(),
                        edtPassword.getText().toString(),
                        edtName.getText().toString(),
                        edtPhone.getText().toString(),
                        register_layout );





            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        dialog.show();

    }

    private void registerNewUser(final String email, final String password, final String name, final String phone, View registerLayout){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //save user to db
                        User user = new User();
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setName(name);
                        user.setPhone(phone);

                        //use current user UID as the key
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Snackbar.make(rootLayout, "Registered Successfully", Snackbar.LENGTH_SHORT).show();


                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Snackbar.make(rootLayout, "Failed to register"+ e.getMessage(), Snackbar.LENGTH_SHORT).show();

                            }
                        });



                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Snackbar.make(rootLayout, "Failed to register"+ e.getMessage(), Snackbar.LENGTH_SHORT).show();

            }
        });

    }

    private void showLoginDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please Enter Email and Password to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        final View login_layout = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edt_email);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edt_password);


        dialog.setView(login_layout);

        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                //disable sign in button when processing
                btnSignIn.setEnabled(false);


                if (TextUtils.isEmpty(edtEmail.getText().toString())){

                    Snackbar.make(rootLayout, "Please enter your email address", Snackbar.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(edtPassword.getText().toString())){

                    Snackbar.make(rootLayout, "Please enter your password", Snackbar.LENGTH_SHORT).show();

                }else if (edtPassword.getText().toString().length() < 6) {

                    Snackbar.make(rootLayout, "password must 6 characters", Snackbar.LENGTH_SHORT).show();

                }

                final android.app.AlertDialog loadingDialog = new SpotsDialog(MainActivity.this);
                loadingDialog.show();

                //Login through firebase
                auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                loadingDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, Welcome.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        Snackbar.make(rootLayout, "Failed to Sign in"+e.getMessage(), Snackbar.LENGTH_SHORT).show();

                        btnSignIn.setEnabled(true);
                    }
                });






            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        dialog.show();


    }

}
