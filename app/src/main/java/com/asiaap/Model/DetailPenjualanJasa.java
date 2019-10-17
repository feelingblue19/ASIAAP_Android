package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class DetailPenjualanJasa {

    @SerializedName("id_penjualan_jasa")
    @Expose
    private String idPenjualanJasa;
    @SerializedName("no_transaksi")
    @Expose
    private String noTransaksi;
    @SerializedName("id_jasa_service")
    @Expose
    private String idJasaService;
    @SerializedName("id_kendaraan")
    @Expose
    private String idKendaraan;
    @SerializedName("jumlah_penjualan_jasa")
    @Expose
    private int jumlahPenjualanJasa;
    @SerializedName("subtotal_jasa")
    @Expose
    private int subtotalJasa;
    @SerializedName("kendaraan")
    @Expose
    private KendaraanCustomer kendaraan;

    public String getIdPenjualanJasa() {
        return idPenjualanJasa;
    }

    public void setIdPenjualanJasa(String idPenjualanJasa) {
        this.idPenjualanJasa = idPenjualanJasa;
    }

    public String getNoTransaksi() {
        return noTransaksi;
    }

    public void setNoTransaksi(String noTransaksi) {
        this.noTransaksi = noTransaksi;
    }

    public String getIdJasaService() {
        return idJasaService;
    }

    public void setIdJasaService(String idJasaService) {
        this.idJasaService = idJasaService;
    }

    public String getIdKendaraan() {
        return idKendaraan;
    }

    public void setIdKendaraan(String idKendaraan) {
        this.idKendaraan = idKendaraan;
    }

    public int getJumlahPenjualanJasa() {
        return jumlahPenjualanJasa;
    }

    public void setJumlahPenjualanJasa(int jumlahPenjualanJasa) {
        this.jumlahPenjualanJasa = jumlahPenjualanJasa;
    }

    public int getSubtotalJasa() {
        return subtotalJasa;
    }

    public void setSubtotalJasa(int subtotalJasa) {
        this.subtotalJasa = subtotalJasa;
    }

    public KendaraanCustomer getKendaraan() {
        return kendaraan;
    }

    public void setKendaraan(KendaraanCustomer kendaraan) {
        this.kendaraan = kendaraan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailPenjualanJasa that = (DetailPenjualanJasa) o;
        return Objects.equals(idJasaService, that.idJasaService) &&
                Objects.equals(kendaraan, that.kendaraan);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idJasaService, kendaraan);
    }
}