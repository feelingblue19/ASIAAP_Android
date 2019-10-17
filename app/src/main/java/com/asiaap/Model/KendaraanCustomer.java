package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class KendaraanCustomer {

    @SerializedName("id_kendaraan")
    @Expose
    private String idKendaraan;
    @SerializedName("id_tipe")
    @Expose
    private String idTipe;
    @SerializedName("no_polisi")
    @Expose
    private String noPolisi;
    @SerializedName("id_pegawai")
    @Expose
    private String idPegawai;

    public KendaraanCustomer(String idTipe, String noPolisi, String idPegawai) {
        this.idTipe = idTipe;
        this.noPolisi = noPolisi;
        this.idPegawai = idPegawai;
    }

    public KendaraanCustomer() {}

    public String getIdKendaraan() {
        return idKendaraan;
    }

    public void setIdKendaraan(String idKendaraan) {
        this.idKendaraan = idKendaraan;
    }

    public String getIdTipe() {
        return idTipe;
    }

    public void setIdTipe(String idTipe) {
        this.idTipe = idTipe;
    }

    public String getNoPolisi() {
        return noPolisi;
    }

    public void setNoPolisi(String noPolisi) {
        this.noPolisi = noPolisi;
    }

    public String getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(String idPegawai) {
        this.idPegawai = idPegawai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KendaraanCustomer that = (KendaraanCustomer) o;
        return Objects.equals(noPolisi, that.noPolisi);
    }

    @Override
    public int hashCode() {

        return Objects.hash(noPolisi);
    }

    @Override
    public String toString() {
        return noPolisi;
    }
}
