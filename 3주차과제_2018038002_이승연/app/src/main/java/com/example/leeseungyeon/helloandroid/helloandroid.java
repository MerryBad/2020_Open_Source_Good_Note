package com.example.leeseungyeon.helloandroid;

/* **********************************************
 * 프로그램명 : helloandroid.java
 * 작성자 : 2018038002 이승연
 * 작성일 : 2020.04.12
 *프로그램 설명 : Hello Android 프로그램
 ************************************************/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ImageView;
import android.widget.Toast;

public class helloandroid extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText et;
        Button btn1;
        Button btn2;
        RadioButton radiobtn1;
        RadioButton radiobtn2;
        final ImageView imgView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_android);
        setTitle("좀 그럴듯한 앱");

        et=(EditText)findViewById((R.id.edittext));
        btn1=(Button)findViewById(R.id.button1);
        btn2=(Button)findViewById(R.id.button2);
        radiobtn1=(RadioButton)findViewById(R.id.radiobutton1);
        radiobtn2=(RadioButton)findViewById(R.id.radiobutton2);
        imgView=(ImageView)findViewById(R.id.imageview1);


        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String str=et.getText().toString();
                Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=et.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);
            }
        });

        radiobtn1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                imgView.setImageResource(R.drawable.android9pie);
            }
        });

        radiobtn2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                imgView.setImageResource(R.drawable.android8oreo);
            }
        });
    }
}