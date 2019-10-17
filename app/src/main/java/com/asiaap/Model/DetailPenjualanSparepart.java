package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class DetailPenjualanSparepart {

    @SerializedName("id_penjualan_sparepart")
    @Expose
    private String idPenjualanSparepart;
    @SerializedName("no_transaksi")
    @Expose
    private String noTransaksi;
    @SerializedName("kode_sparepart")
    @Expose
    private String kodeSparepart;
    @SerializedName("jumlah_penjualan_sparepart")
    @Expose
    private int jumlahPenjualanSparepart;
    @SerializedName("subtotal_sparepart")
    @Expose
    private Float subtotalSparepart;
    @SerializedName("kendaraan")
    @Expose
    private KendaraanCustomer kendaraan;
    @SerializedName("id_kendaraan")
    @Expose
    private String idKendaraan;

    public DetailPenjualanSparepart() {
    }

    public String getIdPenjualanSparepart() {
        return idPenjualanSparepart;
    }

    public void setIdPenjualanSparepart(String idPenjualanSparepart) {
        this.idPenjualanSparepart = idPenjualanSparepart;
    }

    public String getNoTransaksi() {
        return noTransaksi;
    }

    public void setNoTransaksi(String noTransaksi) {
        this.noTransaksi = noTransaksi;
    }

    public String getKodeSparepart() {
        return kodeSparepart;
    }

    public void setKodeSparepart(String kodeSparepart) {
        this.kodeSparepart = kodeSparepart;
    }

    public int getJumlahPenjualanSparepart() {
        return jumlahPenjualanSparepart;
    }

    public void setJumlahPenjualanSparepart(int jumlahPenjualanSparepart) {
        this.jumlahPenjualanSparepart = jumlahPenjualanSparepart;
    }

    public float getSubtotalSparepart() {
        return subtotalSparepart;
    }

    public void setSubtotalSparepart(float subtotalSparepart) {
        this.subtotalSparepart = subtotalSparepart;
    }

    public DetailPenjualanSparepart(String kodeSparepart, int jumlahPenjualanSparepart) {
        this.kodeSparepart = kodeSparepart;
        this.jumlahPenjualanSparepart = jumlahPenjualanSparepart;
    }

    public KendaraanCustomer getKendaraan() {
        return kendaraan;
    }

    public void setKendaraan(KendaraanCustomer kendaraan) {
        this.kendaraan = kendaraan;
    }

    public String getIdKendaraan() {
        return idKendaraan;
    }

    public void setIdKendaraan(String idKendaraan) {
        this.idKendaraan = idKendaraan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailPenjualanSparepart that = (DetailPenjualanSparepart) o;
        return Objects.equals(kodeSparepart, that.kodeSparepart) &&
                Objects.equals(kendaraan, that.kendaraan);
    }

    @Override
    public int hashCode() {

        return Objects.hash(kodeSparepart, kendaraan);
    }
}