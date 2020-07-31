package com.tbt.bachtung.hrm_teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;



import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    FirebaseStorage storage = FirebaseStorage.getInstance();

    String TAG ="FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button viewteacher = (Button) findViewById(R.id.btnxem);
        viewteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listview = new Intent(MainActivity.this, Listview_teacher.class);
                startActivity(listview);
            }
        });
        Button HRM = (Button) findViewById(R.id.btnquanli);
        HRM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ql = new Intent(MainActivity.this, Add_Revise_Delete.class);
                startActivity(ql);
            }
        });
        Button exit = (Button) findViewById(R.id.btnexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        
    }




}