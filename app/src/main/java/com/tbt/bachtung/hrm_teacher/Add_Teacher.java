package com.tbt.bachtung.hrm_teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.UUID;

import static android.widget.Toast.*;

public class Add_Teacher extends AppCompatActivity {
    private Uri filePath;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Button btnChupanh, btnInsert, btnCancle, btnChonanh;
    ImageView imgHinh;
    String urlImage = "";
    EditText hoten, khoa, gioitinh, ngaysinh, diachi, bomon, hocvi, magiangvien;
    int REQUEST_CODE_IMAGE = 71;
    StorageReference storageReference;
    private StorageTask mUploadTask;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__teacher);


        // Create a storage reference from our app
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://teacherhrm-1b66b.appspot.com");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        SaveIMG();

btnChupanh.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }
});
        btnChonanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
            }
        });

        Add();
        btnInsert.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(Add_Teacher.this,
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
                            }else {
                                Toast.makeText(Add_Teacher.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        Cancle();
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Teacher.this, Add_Revise_Delete.class);
                startActivity(intent);
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
                        imgHinh.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                    imgHinh.setImageBitmap(selectedImage);
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

    private void SaveIMG() {
        btnChupanh = (Button) findViewById(R.id.btnchupanh);
        btnChonanh = (Button) findViewById(R.id.btnchonanh);
        imgHinh = (ImageView) findViewById(R.id.imglibrary);
    }

    private void Add() {
        magiangvien = (EditText) findViewById(R.id.edtmagv);
        hoten = (EditText) findViewById(R.id.edthoten);
        khoa = (EditText) findViewById(R.id.edttkhoa);
        gioitinh = (EditText) findViewById(R.id.edtgioitinh);
        ngaysinh = (EditText) findViewById(R.id.edtngaysinh);
        diachi = (EditText) findViewById(R.id.edtdiachi);
        bomon = (EditText) findViewById(R.id.edtbomon);
        hocvi = (EditText) findViewById(R.id.edthocvi);
        btnInsert = (Button) findViewById(R.id.btnactionadd);

    }

    private void Cancle() {
        btnCancle = (Button) findViewById(R.id.actioncancle);
    }


}