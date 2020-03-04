package com.pmo.iderin.models;

public class transaksi_model {
    String idpembeli;
    String idtoko;
    Double jumlah;
    Double total;
    String status;
    String metode_pembayaran;
    long waktu_order;
    long created_at;
    long updated_at;
    long tanggal_bayar;
    String id;

    public transaksi_model() {
    }

    public transaksi_model(String idpembeli, String idtoko, Double jumlah, Double total, String status, String metode_pembayaran, long waktu_order, long created_at, long updated_at, long tanggal_bayar, String id) {
        this.idpembeli = idpembeli;
        this.idtoko = idtoko;
        this.jumlah = jumlah;
        this.total = total;
        this.status = status;
        this.metode_pembayaran = metode_pembayaran;
        this.waktu_order = waktu_order;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.tanggal_bayar = tanggal_bayar;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdpembeli() {
        return idpembeli;
    }

    public void setIdpembeli(String idpembeli) {
        this.idpembeli = idpembeli;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public Double getJumlah() {
        return jumlah;
    }

    public void setJumlah(Double jumlah) {
        this.jumlah = jumlah;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMetode_pembayaran() {
        return metode_pembayaran;
    }

    public void setMetode_pembayaran(String metode_pembayaran) {
        this.metode_pembayaran = metode_pembayaran;
    }

    public long getWaktu_order() {
        return waktu_order;
    }

    public void setWaktu_order(long waktu_order) {
        this.waktu_order = waktu_order;
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

    public long getTanggal_bayar() {
        return tanggal_bayar;
    }

    public void setTanggal_bayar(long tanggal_bayar) {
        this.tanggal_bayar = tanggal_bayar;
    }
}
