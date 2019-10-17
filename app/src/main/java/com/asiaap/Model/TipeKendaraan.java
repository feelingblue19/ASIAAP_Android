package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TipeKendaraan {

    @SerializedName("id_tipe")
    @Expose
    private String idTipe;

    @SerializedName("id_merk")
    @Expose
    private String idMerk;

    @SerializedName("nama_tipe")
    @Expose
    private String namaTipe;

    public String getIdTipe() {
        return idTipe;
    }

    public void setIdTipe(String idTipe) {
        this.idTipe = idTipe;
    }

    public String getIdMerk() {
        return idMerk;
    }

    public void setIdMerk(String idMerk) {
        this.idMerk = idMerk;
    }

    public String getNamaTipe() {
        return namaTipe;
    }

    public void setNamaTipe(String namaTipe) {
        this.namaTipe = namaTipe;
    }

    @Override
    public String toString() {
        return namaTipe;
    }
}