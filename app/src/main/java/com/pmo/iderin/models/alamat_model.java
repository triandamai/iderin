package com.pmo.iderin.models;

public class alamat_model {
    String namaalamat,alamatlengkap,lat,lng;

    public alamat_model() {
    }

    public alamat_model(String namaalamat, String alamatlengkap, String lat, String lng) {
        this.namaalamat = namaalamat;
        this.alamatlengkap = alamatlengkap;
        this.lat = lat;
        this.lng = lng;
    }

    public String getNamaalamat() {
        return namaalamat;
    }

    public void setNamaalamat(String namaalamat) {
        this.namaalamat = namaalamat;
    }

    public String getAlamatlengkap() {
        return alamatlengkap;
    }

    public void setAlamatlengkap(String alamatlengkap) {
        this.alamatlengkap = alamatlengkap;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
