package com.tbt.bachtung.hrm_teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class Update_Teacher extends AppCompatActivity {

    ArrayAdapter<String> adapter_update;
    String TAG = "FIREBASE";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Button btnSave_update, btnupdate, btncancle, btnlibary, btndelete, btnChonAnh_update;
    EditText hoten, khoa, gioitinh, ngaysinh, diachi, bomon, hocvi, magiangvien;
    ImageView imgHinh_update;
    int REQUEST_CODE_IMAGE = 71;
    int REQUEST_CODE_IMAGE_CAPTRUE = 72;
    private Uri filePath;
    StorageReference storageRef = storage.getReferenceFromUrl("gs://teacherhrm-1b66b.appspot.com");
    private String tens = "";
    private String khoas = "";
    private String bomons = "";
    private String diachis = "";
    private String gioitinhs = "";
    private String hocvis = "";
    private String ngaysinhs = "";
    private String magiangviens = "";
    private String urlImage = "";
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__teacher);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        SaveIMG_update();
        btnSave_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        Display();
        getContactdetail();
        Update();
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    final StorageReference ref
                            = storageReference
                            .child(
                                    "images/"
                                            + UUID.randomUUID().toString() + ".jpg");
                    ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri dowloadUrl = task.getResult();
                                Toast.makeText(Update_Teacher.this,
                                        "upload complete",
                                        Toast.LENGTH_SHORT)
                                        .show();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("GIANGVIEN");
                                String maGv = magiangvien.getText().toString();
                                String hoTen = hoten.getText().toString();
                                String Khoa = khoa.getText().toString();
                                String gioiTinh = gioitinh.getText().toString();
                                String boMon = bomon.getText().toString();
                                String ngaySinh = ngaysinh.getText().toString();
                                String hocVi = hocvi.getText().toString();
                                String diaChi = diachi.getText().toString();
                                myRef.child(maGv).child("HoTen").setValue(hoTen);
                                myRef.child(maGv).child("Khoa").setValue(Khoa);
                                myRef.child(maGv).child("GioiTinh").setValue(gioiTinh);
                                myRef.child(maGv).child("BoMon").setValue(boMon);
                                myRef.child(maGv).child("NgaySinh").setValue(ngaySinh);
                                myRef.child(maGv).child("HocVi").setValue(hocVi);
                                myRef.child(maGv).child("DiaChi").setValue(diaChi);
                                myRef.child(maGv).child("imgUrl").setValue(dowloadUrl.toString());
                                finish();
                            } else {
                                Toast.makeText(Update_Teacher.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        ChooseIMG();
        btnlibary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });

        Cancle();
        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update_Teacher.this, Listview_teacher.class);
                startActivity(intent);
            }
        });
        btndelete = (Button) findViewById(R.id.actiondelete_update);
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    filePath = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        imgHinh_update.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    imgHinh_update.setImageBitmap(selectedImage);
                   filePath=getImageUri(this,selectedImage);
                }
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void SaveIMG_update() {
        btnSave_update = (Button) findViewById(R.id.btnchupanh_update);
        imgHinh_update = (ImageView) findViewById(R.id.imglibrary_update);
    }

    private void Update() {
        btnupdate = (Button) findViewById(R.id.btnactionadd_update);

    }

    private void Cancle() {
        btncancle = (Button) findViewById(R.id.actioncancle_update);
    }

    private void ChooseIMG() {
        btnlibary = (Button) findViewById(R.id.btnchonanh_update);
    }

    private void getContactdetail() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            tens = bundle.getString("ten");
            khoas = bundle.getString("khoa");
            bomons = bundle.getString("bomon");
            diachis = bundle.getString("diachi");
            gioitinhs = bundle.getString("gioitinh");
            hocvis = bundle.getString("hocvi");
            ngaysinhs = bundle.getString("ngaysinh");
            magiangviens = bundle.getString("magiangvien");
            urlImage = bundle.getString("url");
            magiangvien.setText(magiangviens);
            hoten.setText(tens);
            bomon.setText(bomons);
            diachi.setText(diachis);
            ngaysinh.setText(ngaysinhs);
            khoa.setText(khoas);
            hocvi.setText(hocvis);
            gioitinh.setText(gioitinhs);
            Picasso.with(this).load(urlImage)
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.no_image)
                    .into(imgHinh_update);
        }
    }

    private void Display() {
        magiangvien = findViewById(R.id.edtmagv_update);
        hoten = findViewById(R.id.edthoten_update);
        gioitinh = findViewById(R.id.edtgioitinh_update);
        hocvi = findViewById(R.id.edthocvi_update);
        ngaysinh = findViewById(R.id.edtngaysinh_update);
        khoa = findViewById(R.id.edtkhoa_update);
        bomon = findViewById(R.id.edttbomon_update);
        diachi = findViewById(R.id.edtdiachi_update);
    }

    private void Delete(View v) {

        String key = magiangvien.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("GIANGVIEN");
        myRef.child(key).removeValue();
        finish();
    }
}