package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Sparepart implements Serializable {

    @SerializedName("kode_sparepart")
    @Expose
    private String kodeSparepart;

    @SerializedName("penempatan_sparepart")
    @Expose
    private String penempatanSparepart;

    @SerializedName("tipe_sparepart")
    @Expose
    private String tipeSparepart;

    @SerializedName("nama_sparepart")
    @Expose
    private String namaSparepart;

    @SerializedName("harga_jual_sparepart")
    @Expose
    private Double hargaJualSparepart;

    @SerializedName("harga_beli_sparepart")
    @Expose
    private Double hargaBeliSparepart;

    @SerializedName("merk_sparepart")
    @Expose
    private String merkSparepart;

    @SerializedName("stok_sparepart")
    @Expose
    private int stokSparepart;

    @SerializedName("min_stok")
    @Expose
    private int minStok;

    @SerializedName("gambar_sparepart")
    @Expose
    private String gambarSparepart;

    @SerializedName("tipe_kendaraan")
    @Expose
    private List<TipeKendaraan> tipeKendaraan = null;

    public String getKodeSparepart() {
        return kodeSparepart;
    }

    public void setKodeSparepart(String kodeSparepart) {
        this.kodeSparepart = kodeSparepart;
    }

    public String getPenempatanSparepart() {
        return penempatanSparepart;
    }

    public void setPenempatanSparepart(String penempatanSparepart) {
        this.penempatanSparepart = penempatanSparepart;
    }

    public String getTipeSparepart() {
        return tipeSparepart;
    }

    public void setTipeSparepart(String tipeSparepart) {
        this.tipeSparepart = tipeSparepart;
    }

    public String getNamaSparepart() {
        return namaSparepart;
    }

    public void setNamaSparepart(String namaSparepart) {
        this.namaSparepart = namaSparepart;
    }

    public Double getHargaJualSparepart() {
        return hargaJualSparepart;
    }

    public void setHargaJualSparepart(Double hargaJualSparepart) {
        this.hargaJualSparepart = hargaJualSparepart;
    }

    public Double getHargaBeliSparepart() {
        return hargaBeliSparepart;
    }

    public void setHargaBeliSparepart(Double hargaBeliSparepart) {
        this.hargaBeliSparepart = hargaBeliSparepart;
    }

    public String getMerkSparepart() {
        return merkSparepart;
    }

    public void setMerkSparepart(String merkSparepart) {
        this.merkSparepart = merkSparepart;
    }

    public int getStokSparepart() {
        return stokSparepart;
    }

    public void setStokSparepart(int stokSparepart) {
        this.stokSparepart = stokSparepart;
    }

    public int getMinStok() {
        return minStok;
    }

    public void setMinStok(int minStok) {
        this.minStok = minStok;
    }

    public String getGambarSparepart() {
        return gambarSparepart;
    }

    public void setGambarSparepart(String gambarSparepart) {
        this.gambarSparepart = gambarSparepart;
    }

    public List<TipeKendaraan> getTipeKendaraan() {
        return tipeKendaraan;
    }

    public void setTipeKendaraan(List<TipeKendaraan> tipeKendaraan) {
        this.tipeKendaraan = tipeKendaraan;
    }

    @Override
    public String toString() {
        return namaSparepart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sparepart sparepart = (Sparepart) o;
        return Objects.equals(kodeSparepart, sparepart.kodeSparepart);
    }

    @Override
    public int hashCode() {

        return Objects.hash(kodeSparepart);
    }
}
