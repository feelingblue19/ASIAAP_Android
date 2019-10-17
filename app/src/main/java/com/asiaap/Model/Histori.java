package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Histori {

    @SerializedName("id_histori")
    @Expose
    private String idHistori;
    @SerializedName("tanggal_histori")
    @Expose
    private String tanggalHistori;
    @SerializedName("kode_sparepart")
    @Expose
    private String kodeSparepart;
    @SerializedName("jumlah_histori")
    @Expose
    private int jumlahHistori;
    @SerializedName("keterangan_histori")
    @Expose
    private String keteranganHistori;

    public String getIdHistori() {
        return idHistori;
    }

    public void setIdHistori(String idHistori) {
        this.idHistori = idHistori;
    }

    public String getTanggalHistori() {
        return tanggalHistori;
    }

    public void setTanggalHistori(String tanggalHistori) {
        this.tanggalHistori = tanggalHistori;
    }

    public String getKodeSparepart() {
        return kodeSparepart;
    }

    public void setKodeSparepart(String kodeSparepart) {
        this.kodeSparepart = kodeSparepart;
    }

    public int getJumlahHistori() {
        return jumlahHistori;
    }

    public void setJumlahHistori(int jumlahHistori) {
        this.jumlahHistori = jumlahHistori;
    }

    public String getKeteranganHistori() {
        return keteranganHistori;
    }

    public void setKeteranganHistori(String keteranganHistori) {
        this.keteranganHistori = keteranganHistori;
    }

    public Histori() {
    }
}