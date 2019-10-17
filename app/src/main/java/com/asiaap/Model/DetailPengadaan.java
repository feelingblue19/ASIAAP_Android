package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailPengadaan {

    @SerializedName("id_detail_pengadaan")
    @Expose
    private String idDetailPengadaan;
    @SerializedName("id_pengadaan")
    @Expose
    private String idPengadaan;
    @SerializedName("kode_sparepart")
    @Expose
    private String kodeSparepart;
    @SerializedName("jumlah")
    @Expose
    private int jumlah;
    @SerializedName("harga_beli")
    @Expose
    private Float hargaBeli;
    @SerializedName("subtotal_pengadaan")
    @Expose
    private Float subtotalPengadaan;
    @SerializedName("satuan")
    @Expose
    private String satuan;

    public DetailPengadaan(String kodeSparepart, int jumlah, String satuan, Float hargaBeli) {
        this.kodeSparepart = kodeSparepart;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.hargaBeli = hargaBeli;
    }

    public String getIdDetailPengadaan() {
        return idDetailPengadaan;
    }

    public void setIdDetailPengadaan(String idDetailPengadaan) {
        this.idDetailPengadaan = idDetailPengadaan;
    }

    public String getIdPengadaan() {
        return idPengadaan;
    }

    public void setIdPengadaan(String idPengadaan) {
        this.idPengadaan = idPengadaan;
    }

    public String getKodeSparepart() {
        return kodeSparepart;
    }

    public void setKodeSparepart(String kodeSparepart) {
        this.kodeSparepart = kodeSparepart;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public Float getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(Float hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public Float getSubtotalPengadaan() {
        return subtotalPengadaan;
    }

    public void setSubtotalPengadaan(Float subtotalPengadaan) {
        this.subtotalPengadaan = subtotalPengadaan;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DetailPengadaan) {
            return ((DetailPengadaan) obj).getKodeSparepart().equals(this.getKodeSparepart());
        }
        return false;
    }
}
