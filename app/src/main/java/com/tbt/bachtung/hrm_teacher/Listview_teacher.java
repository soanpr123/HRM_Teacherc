package com.tbt.bachtung.hrm_teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Listview_teacher extends AppCompatActivity implements Clicklistener {
    private RecyclerView recyclerView;

    Teacher_Adapter adapterteacher;
    String TAG = "FIREBASE";
    ArrayList<Teacher> _teacher;
    ProgressBar progresbar;
    String ten = "";
    String khoa = "";
    String bomon = "";
    String diachi = "";
    String gioitinh = "";
    String hocvi = "";
    String ngaysinh = "";
    String magiangvien = "";
    String imgAvt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_teacher);
        recyclerView = findViewById(R.id.listviewteacher);
        progresbar = findViewById(R.id.load_data);
        _teacher = new ArrayList<>();
        adapterteacher = new Teacher_Adapter(this, _teacher, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterteacher);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("GIANGVIEN");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                _teacher.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data == null) {
                        progresbar.setVisibility(View.VISIBLE);
                    } else {
                        progresbar.setVisibility(View.GONE);
                        bomon = data.child("BoMon").getValue().toString();
                        ten = data.child("HoTen").getValue().toString();
                        khoa = data.child("Khoa").getValue().toString();
                        diachi = data.child("DiaChi").getValue().toString();
                        gioitinh = data.child("GioiTinh").getValue().toString();
                        hocvi = data.child("HocVi").getValue().toString();
                        ngaysinh = data.child("NgaySinh").getValue().toString();
                        imgAvt = data.child("imgUrl").getValue().toString();
                        magiangvien = data.getKey();
                        _teacher.add(new Teacher(ten, khoa, bomon, diachi, gioitinh, hocvi, ngaysinh, magiangvien,imgAvt));
                        adapterteacher.notifyDataSetChanged();
                        Log.d("datasss", data.toString());
                    }


//                    adapterteacher.add(key + "\n" + value);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());

            }
        });

    }

    @Override
    public void onClick(int position) {
        Teacher teacher = _teacher.get(position);
        Intent intent = new Intent(this, Update_Teacher.class);
        Bundle bundle = new Bundle();
        bundle.putString("ten", teacher.getTen());
        bundle.putString("khoa", teacher.getKhoa());
        bundle.putString("bomon", teacher.getBomon());
        bundle.putString("diachi", teacher.getDiachi());
        bundle.putString("gioitinh", teacher.getGioitinh());
        bundle.putString("hocvi", teacher.getHocvi());
        bundle.putString("ngaysinh", teacher.getNgaySinh());
        bundle.putString("magiangvien", teacher.getMagiangvien());
        bundle.putString("url", teacher.getUrlImage());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(int position) {
        Teacher teacher = _teacher.get(position);
        final String key = teacher.getMagiangvien();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa giảng viên");
        builder.setMessage("Bạn có chắc muốn xóa");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("GIANGVIEN");
                myRef.child(key).removeValue();
                _teacher.remove(i);
                adapterteacher.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapterteacher.notifyDataSetChanged();
            }
        });
        builder.show();
        return true;
    }
}