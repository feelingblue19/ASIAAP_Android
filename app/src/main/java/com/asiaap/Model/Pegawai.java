package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pegawai {

    @SerializedName("id_pegawai")
    @Expose
    private String idPegawai;
    @SerializedName("id_cabang")
    @Expose
    private String idCabang;
    @SerializedName("nama_pegawai")
    @Expose
    private String namaPegawai;
    @SerializedName("alamat_pegawai")
    @Expose
    private String alamatPegawai;
    @SerializedName("no_telp_pegawai")
    @Expose
    private String noTelpPegawai;
    @SerializedName("gaji_per_minggu")
    @Expose
    private float gajiPerMinggu;
    @SerializedName("jabatan_pegawai")
    @Expose
    private String jabatanPegawai;
    @SerializedName("username")
    @Expose
    private String username;

    public String getIdPegawai() {
        return idPegawai;
    }

    public void setIdPegawai(String idPegawai) {
        this.idPegawai = idPegawai;
    }

    public String getIdCabang() {
        return idCabang;
    }

    public void setIdCabang(String idCabang) {
        this.idCabang = idCabang;
    }

    public String getNamaPegawai() {
        return namaPegawai;
    }

    public void setNamaPegawai(String namaPegawai) {
        this.namaPegawai = namaPegawai;
    }

    public String getAlamatPegawai() {
        return alamatPegawai;
    }

    public void setAlamatPegawai(String alamatPegawai) {
        this.alamatPegawai = alamatPegawai;
    }

    public String getNoTelpPegawai() {
        return noTelpPegawai;
    }

    public void setNoTelpPegawai(String noTelpPegawai) {
        this.noTelpPegawai = noTelpPegawai;
    }

    public float getGajiPerMinggu() {
        return gajiPerMinggu;
    }

    public void setGajiPerMinggu(float gajiPerMinggu) {
        this.gajiPerMinggu = gajiPerMinggu;
    }

    public String getJabatanPegawai() {
        return jabatanPegawai;
    }

    public void setJabatanPegawai(String jabatanPegawai) {
        this.jabatanPegawai = jabatanPegawai;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return namaPegawai;
    }
}