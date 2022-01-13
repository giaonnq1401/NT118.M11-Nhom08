package com.example.magoapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magoapp.data.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class dangki extends AppCompatActivity implements View.OnClickListener {

    private TextView login;
    private EditText editdate, regUsername, regEmail, regPwd, pwdConfirm;
    private Button regBtn;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    String joinDate;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);

        mAuth = FirebaseAuth.getInstance();

        editdate = (EditText) findViewById(R.id.bd_signup);
        regUsername = (EditText) findViewById(R.id.username_signup);
        regEmail = (EditText) findViewById(R.id.email_signup);
        regPwd = (EditText) findViewById(R.id.password_signup);
        pwdConfirm = (EditText) findViewById(R.id.cf_password_signup);
        regBtn = (Button) findViewById(R.id.btn_signup);
        login = (TextView) findViewById(R.id.login);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        joinDate = df.format(c);
//        Toast.makeText(dangki.this, "Today is " + joinDate, Toast.LENGTH_SHORT).show();

        regBtn.setOnClickListener(this);
        login.setOnClickListener(this);
        editdate.setOnClickListener(this);
    }

    private void chonngay(){
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //i: năm, i1:tháng, i2: ngày
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                editdate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_signup:
                registerUser();
                break;
            case R.id.bd_signup:
                chonngay();
                break;
            case R.id.login:
                startActivity(new Intent(this, dangnhap.class));
        }
    }

    private void registerUser() {
        String username = regUsername.getText().toString().trim();
        String email = regEmail.getText().toString().trim();
        String password = regPwd.getText().toString().trim();
        String passConfirm = pwdConfirm.getText().toString().trim();
        String birthday = editdate.getText().toString().trim();
        String mImageUrl = "";
        String zodiac = "zodiac";
        String hobbies = "hobbies";
        String quotes = "quotes";

        //Kiểm tra dữ liệu nhập vào khi đăng ký tài khoản
        if (username.isEmpty()){
            regUsername.setError("Username is required!");
            regUsername.requestFocus();
            return;
        }

        if (email.isEmpty()){
            regEmail.setError("Email is required!");
            regEmail.requestFocus();
            return;
        }

        if (birthday.isEmpty()){
            editdate.setError("Date of birth is required!");
            editdate.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            regEmail.setError("Please provide valid email!");
            regEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            regPwd.setError("Password is required!");
            regPwd.requestFocus();
            return;
        }

        if (password.length() < 8){
            regPwd.setError("Min password length should be 8 characters!");
            regPwd.requestFocus();
            return;
        }

        if (!passConfirm.equals(password)){
            pwdConfirm.setError("Password is not matched!");
            pwdConfirm.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Users user = new Users(username, email, birthday, mImageUrl, zodiac, hobbies, quotes);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(dangki.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(dangki.this, dangnhap.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(dangki.this, "Failed to register! Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(dangki.this, "Failed to register! Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}