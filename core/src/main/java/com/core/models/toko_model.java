package com.core.models;

public class toko_model {
    public String namatoko, banner_toko, foto_profil, alamat, geo_location, id;

    public toko_model() {
    }

    public toko_model(String namatoko, String banner_toko, String foto_profil, String alamat, String geo_location, String id) {
        this.namatoko = namatoko;
        this.banner_toko = banner_toko;
        this.foto_profil = foto_profil;
        this.alamat = alamat;
        this.geo_location = geo_location;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamatoko() {
        return namatoko;
    }

    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }

    public String getBanner_toko() {
        return banner_toko;
    }

    public void setBanner_toko(String banner_toko) {
        this.banner_toko = banner_toko;
    }

    public String getFoto_profil() {
        return foto_profil;
    }

    public void setFoto_profil(String foto_profil) {
        this.foto_profil = foto_profil;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGeo_location() {
        return geo_location;
    }

    public void setGeo_location(String geo_location) {
        this.geo_location = geo_location;
    }
}
