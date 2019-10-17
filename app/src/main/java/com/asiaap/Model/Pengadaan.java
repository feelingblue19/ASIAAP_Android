package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pengadaan {

    @SerializedName("id_pengadaan")
    @Expose
    private String idPengadaan;
    @SerializedName("id_supplier")
    @Expose
    private String idSupplier;
    @SerializedName("tanggal_pengadaan")
    @Expose
    private String tanggalPengadaan;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total_pengadaan")
    @Expose
    private Float totalPengadaan;
    @SerializedName("supplier")
    @Expose
    private Supplier supplier;


    public String getIdPengadaan() {
        return idPengadaan;
    }

    public void setIdPengadaan(String idPengadaan) {
        this.idPengadaan = idPengadaan;
    }

    public String getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(String idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getTanggalPengadaan() {
        return tanggalPengadaan;
    }

    public void setTanggalPengadaan(String tanggalPengadaan) {
        this.tanggalPengadaan = tanggalPengadaan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getTotalPengadaan() {
        return totalPengadaan;
    }

    public void setTotalPengadaan(Float totalPengadaan) {
        this.totalPengadaan = totalPengadaan;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}