package com.pmo.iderin.models;

public class barang_model {
    String id, nama,
			idkategori,
			 idtoko,
			 deskripsi,
			 stokasli,
			 stoksementara,
            foto;
    double harga;

    public barang_model() {
    }

    public barang_model(String id, String nama, String idkategori, String idtoko, String deskripsi, String stokasli, String stoksementara, String foto, double harga) {
        this.id = id;
        this.nama = nama;
        this.idkategori = idkategori;
        this.idtoko = idtoko;
        this.deskripsi = deskripsi;
        this.stokasli = stokasli;
        this.stoksementara = stoksementara;
        this.foto = foto;
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(String idkategori) {
        this.idkategori = idkategori;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getStokasli() {
        return stokasli;
    }

    public void setStokasli(String stokasli) {
        this.stokasli = stokasli;
    }

    public String getStoksementara() {
        return stoksementara;
    }

    public void setStoksementara(String stoksementara) {
        this.stoksementara = stoksementara;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
