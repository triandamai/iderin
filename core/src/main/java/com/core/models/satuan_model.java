package com.core.models;

public class satuan_model {
    String idsatuan;
    String deskripsi;
    String nama;
    long created_at;
    long updated_at;

    public satuan_model() {
    }

    public satuan_model(String idsatuan, String deskripsi, String nama, long created_at, long updated_at) {
        this.idsatuan = idsatuan;
        this.deskripsi = deskripsi;
        this.nama = nama;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getIdsatuan() {
        return idsatuan;
    }

    public void setIdsatuan(String idsatuan) {
        this.idsatuan = idsatuan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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
