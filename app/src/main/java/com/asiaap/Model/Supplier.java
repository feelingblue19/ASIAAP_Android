package com.asiaap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Supplier implements Serializable {

    public List<Supplier> result;

    @SerializedName("id_supplier")
    @Expose
    private String idSupplier;
    @SerializedName("nama_supplier")
    @Expose
    private String namaSupplier;
    @SerializedName("alamat_supplier")
    @Expose
    private String alamatSupplier;
    @SerializedName("no_telp_supplier")
    @Expose
    private String noTelpSupplier;
    @SerializedName("nama_sales")
    @Expose
    private String namaSales;
    @SerializedName("no_telp_sales")
    @Expose
    private String noTelpSales;

    public String getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(String idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getNamaSupplier() {
        return namaSupplier;
    }

    public void setNamaSupplier(String namaSupplier) {
        this.namaSupplier = namaSupplier;
    }

    public String getAlamatSupplier() {
        return alamatSupplier;
    }

    public void setAlamatSupplier(String alamatSupplier) {
        this.alamatSupplier = alamatSupplier;
    }

    public String getNoTelpSupplier() {
        return noTelpSupplier;
    }

    public void setNoTelpSupplier(String noTelpSupplier) {
        this.noTelpSupplier = noTelpSupplier;
    }

    public String getNamaSales() {
        return namaSales;
    }

    public void setNamaSales(String namaSales) {
        this.namaSales = namaSales;
    }

    public String getNoTelpSales() {
        return noTelpSales;
    }

    public void setNoTelpSales(String noTelpSales) {
        this.noTelpSales = noTelpSales;
    }

    public List<Supplier> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return namaSupplier;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if(obj == null) return false;
//        if(!(obj instanceof Supplier))
//            return false;
//        if(obj == this)
//            return true;
//        return this.getIdSupplier() == ((Supplier) obj).getIdSupplier();
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(idSupplier);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(idSupplier, supplier.idSupplier);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idSupplier);
    }
}
