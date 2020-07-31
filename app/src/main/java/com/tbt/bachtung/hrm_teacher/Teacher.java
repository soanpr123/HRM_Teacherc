package com.tbt.bachtung.hrm_teacher;

public class Teacher {
    private String magiangvien;
    private String Ten;
    private String Khoa;
    private String Bomon;
    private String Diachi;
    private String Gioitinh;
    private String Hocvi;
    private String NgaySinh;
    private String urlImage;

    public Teacher(String ten, String khoa, String bomon, String diachi, String gioitinh, String hocvi, String ngaySinh,String Magiangvien,String imgUrl) {
        Ten = ten;
        Khoa = khoa;
        Bomon = bomon;
        Diachi = diachi;
        Gioitinh = gioitinh;
        Hocvi = hocvi;
        NgaySinh = ngaySinh;
        magiangvien=Magiangvien;
        urlImage=imgUrl;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getKhoa() {
        return Khoa;
    }

    public void setKhoa(String khoa) {
        Khoa = khoa;
    }

    public String getBomon() {
        return Bomon;
    }

    public void setBomon(String bomon) {
        Bomon = bomon;
    }


    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String diachi) {
        Diachi = diachi;
    }

    public String getGioitinh() {
        return Gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        Gioitinh = gioitinh;
    }

    public String getHocvi() {
        return Hocvi;
    }

    public void setHocvi(String hocvi) {
        Hocvi = hocvi;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getMagiangvien() {
        return magiangvien;
    }

    public void setMagiangvien(String magiangvien) {
        this.magiangvien = magiangvien;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
