package com.core.models;

public class profil_model {
    String uid,nama,username,nohp,alamat,foto,jenis_kelamin,level;
    long created_at,updated_at;

    public profil_model(){

    }

    public profil_model(String uid, String nama, String username, String nohp, String alamat, String foto, String jenis_kelamin, String level, long created_at, long updated_at) {
        this.uid = uid;
        this.nama = nama;
        this.username = username;
        this.nohp = nohp;
        this.alamat = alamat;
        this.foto = foto;
        this.jenis_kelamin = jenis_kelamin;
        this.level = level;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }
}
