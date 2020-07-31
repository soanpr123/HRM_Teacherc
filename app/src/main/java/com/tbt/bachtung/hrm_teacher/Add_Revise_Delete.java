package com.tbt.bachtung.hrm_teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class Add_Revise_Delete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__revise__delete);

        Button add = (Button) findViewById(R.id.btnadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(Add_Revise_Delete.this, Add_Teacher.class);
                startActivity(add);
            }
        });
        Button updateteacher = (Button) findViewById(R.id.btnupdate);
        updateteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update = new Intent(Add_Revise_Delete.this, Listview_teacher.class);
                startActivity(update);
            }
        });
    }
}