package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Penjualan {

    @SerializedName("no_transaksi")
    @Expose
    private String noTransaksi;
    @SerializedName("tanggal_transaksi")
    @Expose
    private String tanggalTransaksi;
    @SerializedName("subtotal_transaksi")
    @Expose
    private Float subtotalTransaksi;
    @SerializedName("diskon_transaksi")
    @Expose
    private Float diskonTransaksi;
    @SerializedName("total_transaksi")
    @Expose
    private Float totalTransaksi;
    @SerializedName("nama_customer")
    @Expose
    private String namaCustomer;
    @SerializedName("no_telp_customer")
    @Expose
    private String noTelpCustomer;
    @SerializedName("status_transaksi")
    @Expose
    private String statusTransaksi;
    @SerializedName("keterangan_transaksi")
    @Expose
    private String keteranganTransaksi;

    public String getNoTransaksi() {
        return noTransaksi;
    }

    public void setNoTransaksi(String noTransaksi) {
        this.noTransaksi = noTransaksi;
    }

    public String getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(String tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public float getSubtotalTransaksi() {
        return subtotalTransaksi;
    }

    public void setSubtotalTransaksi(float subtotalTransaksi) {
        this.subtotalTransaksi = subtotalTransaksi;
    }

    public float getDiskonTransaksi() {
        return diskonTransaksi;
    }

    public void setDiskonTransaksi(float diskonTransaksi) {
        this.diskonTransaksi = diskonTransaksi;
    }

    public float getTotalTransaksi() {
        return totalTransaksi;
    }

    public void setTotalTransaksi(float totalTransaksi) {
        this.totalTransaksi = totalTransaksi;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    public String getNoTelpCustomer() {
        return noTelpCustomer;
    }

    public void setNoTelpCustomer(String noTelpCustomer) {
        this.noTelpCustomer = noTelpCustomer;
    }

    public String getStatusTransaksi() {
        return statusTransaksi;
    }

    public void setStatusTransaksi(String statusTransaksi) {
        this.statusTransaksi = statusTransaksi;
    }

    public String getKeteranganTransaksi() {
        return keteranganTransaksi;
    }

    public void setKeteranganTransaksi(String keteranganTransaksi) {
        this.keteranganTransaksi = keteranganTransaksi;
    }

}