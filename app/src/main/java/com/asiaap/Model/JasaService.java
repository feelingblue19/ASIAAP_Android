package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class JasaService {

    @SerializedName("id_jasa_service")
    @Expose
    private String idJasaService;
    @SerializedName("nama_jasa_service")
    @Expose
    private String namaJasaService;
    @SerializedName("harga_jasa_service")
    @Expose
    private float hargaJasaService;

    public JasaService() {}

    public String getIdJasaService() {
        return idJasaService;
    }

    public void setIdJasaService(String idJasaService) {
        this.idJasaService = idJasaService;
    }

    public String getNamaJasaService() {
        return namaJasaService;
    }

    public void setNamaJasaService(String namaJasaService) {
        this.namaJasaService = namaJasaService;
    }

    public float getHargaJasaService() {
        return hargaJasaService;
    }

    public void setHargaJasaService(float hargaJasaService) {
        this.hargaJasaService = hargaJasaService;
    }

    @Override
    public String toString() {
        return namaJasaService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JasaService that = (JasaService) o;
        return Objects.equals(idJasaService, that.idJasaService);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idJasaService);
    }
}