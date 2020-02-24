package com.pmo.iderin.models;

public class kategori_model{
    String nama,foto;
    long created_at,updated_at;

    public kategori_model() {
    }

    public kategori_model(String nama, String foto, long created_at, long updated_at) {
        this.nama = nama;
        this.foto = foto;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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
