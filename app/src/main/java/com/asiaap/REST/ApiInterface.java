package com.asiaap.REST;

import com.asiaap.Model.DetailPengadaan;
import com.asiaap.Model.DetailPenjualanJasa;
import com.asiaap.Model.DetailPenjualanSparepart;
import com.asiaap.Model.Histori;
import com.asiaap.Model.JasaService;
import com.asiaap.Model.KendaraanCustomer;
import com.asiaap.Model.Login;
import com.asiaap.Model.Pegawai;
import com.asiaap.Model.Pengadaan;
import com.asiaap.Model.Penjualan;
import com.asiaap.Model.Sparepart;
import com.asiaap.Model.Supplier;
import com.asiaap.Model.TipeKendaraan;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface ApiInterface {

    @POST("auth/login")
    @FormUrlEncoded
    Call<Login> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("auth/logout")
    Call<ResponseBody> logout();

    /******************* CRUDS Supplier ****************************/

    @GET("supplier/tampil")
    Call<List<Supplier>> getResultSupplier();

    @POST("supplier/tambah")
    @FormUrlEncoded
    Call<Supplier> inputSupplier(
            @Field("nama_supplier") String nama_supplier,
            @Field("alamat_supplier") String alamat_supplier,
            @Field("no_telp_supplier") String no_telp_supplier,
            @Field("nama_sales") String nama_sales,
            @Field("no_telp_sales") String no_telp_sales
    );

    @DELETE("supplier/hapus/{id}")
    Call<Supplier> hapusSupplier(@Path("id") String id);

    @GET("supplier/cari/{id}")
    Call<Supplier> cariSupplier(@Path("id") String id);

    @PUT("supplier/ubah/{id}")
    @FormUrlEncoded
    Call<Supplier> ubahSupplier(
            @Path("id") String id,
            @Field("nama_supplier") String nama_supplier,
            @Field("alamat_supplier") String alamat_supplier,
            @Field("no_telp_supplier") String no_telp_supplier,
            @Field("nama_sales") String nama_sales,
            @Field("no_telp_sales") String no_telp_sales
    );

    /*************************************************************************/

    /*************************** CRUDS SPAREPART *****************************/

    @GET("sparepart/tampil")
    Call<List<Sparepart>> getResult();

    @POST("sparepart/tambah")
    @Multipart
    Call<RequestBody> inputSparepart(
            @Part("nama_sparepart") RequestBody nama_sparepart,
            @Part("penempatan_sparepart") RequestBody penempatan_sparepart,
            @Part("merk_sparepart") RequestBody merk_sparepart,
            @Part("tipe_sparepart") RequestBody tipe_sparepart,
            @Part("harga_beli_sparepart") RequestBody harga_beli_sparepart,
            @Part("harga_jual_sparepart") RequestBody harga_jual_sparepart,
            @Part("stok_sparepart") RequestBody stok_sparepart,
            @Part("min_stok") RequestBody min_stok,
            @Part MultipartBody.Part gambar_sparepart,
            @Part("id_tipe") RequestBody id_tipe
    );

    @DELETE("sparepart/hapus/{id}")
    Call<Sparepart> hapusSparepart(@Path("id") String id);

    @GET("sparepart/cari/{id}")
    Call<Sparepart> cariSparepart(@Path("id") String id);

    @POST("sparepart/ubah/{id}")
    @Multipart
    Call<RequestBody> ubahSparepart(
            @Path("id") String id,
            @Part("nama_sparepart") RequestBody nama_sparepart,
            @Part("penempatan_sparepart") RequestBody penempatan_sparepart,
            @Part("merk_sparepart") RequestBody merk_sparepart,
            @Part("tipe_sparepart") RequestBody tipe_sparepart,
            @Part("harga_beli_sparepart") RequestBody harga_beli_sparepart,
            @Part("harga_jual_sparepart") RequestBody harga_jual_sparepart,
            @Part("stok_sparepart") RequestBody stok_sparepart,
            @Part("min_stok") RequestBody min_stok,
            @Part MultipartBody.Part gambar_sparepart,
            @Part("id_tipe") RequestBody id_tipe
    );

    @POST("sparepart/ubah/{id}")
    @FormUrlEncoded
    Call<RequestBody> ubahSparepart2(
            @Path("id") String id,
            @Field("nama_sparepart") String nama_sparepart,
            @Field("penempatan_sparepart") String penempatan_sparepart,
            @Field("merk_sparepart") String merk_sparepart,
            @Field("tipe_sparepart") String tipe_sparepart,
            @Field("harga_beli_sparepart") String harga_beli_sparepart,
            @Field("harga_jual_sparepart") String harga_jual_sparepart,
            @Field("stok_sparepart") String stok_sparepart,
            @Field("min_stok") String min_stok,
            @Field("id_tipe") String id_tipe
    );



    /*************************************************************************/

    @GET("tipekendaraan/tampil")
    Call<List<TipeKendaraan>> getTipeKendaraan();

    /*************************** CRUDS PENGADAAN *****************************/

    @GET("pengadaan/tampil")
    Call<List<Pengadaan>> getResultPengadaan();

    @POST("pengadaan/tambah")
    @FormUrlEncoded
    Call<JsonElement> inputPengadaan(
            @Field("id_supplier") String id_supplier,
            @Field("total_pengadaan") Float total_pengadaan,
            @Field("status") String status
    );

    @DELETE("pengadaan/hapus/{id}")
    Call<Pengadaan> hapusPengadaan(@Path("id") String id);

    @GET("pengadaan/cari/{id}")
    Call<Pengadaan> cariPengadaan(@Path("id") String id);

    @PUT("pengadaan/ubah/{id}")
    @FormUrlEncoded
    Call<JsonElement> ubahPengadaan(
            @Path("id") String id,
            @Field("id_supplier") String id_supplier,
            @Field("total_pengadaan") Float total_pengadaan,
            @Field("status") String status

    );

    /*************************** CRUDS DETAIL PENGADAAN *****************************/

    @POST("detailpengadaan/tambah")
    @FormUrlEncoded
    Call<DetailPengadaan> inputDetailPengadaan(
            @Field("data") String data
    );

    @GET("detailpengadaan/cari/{id}")
    Call<List<DetailPengadaan>> cariDetailPengadaan(@Path("id") String id);


    /*************************** HISTORI ***********************/

    @POST("histori/tambah")
    @FormUrlEncoded
    Call<Histori> inputHistori(
            @Field("data") String data
    );

    @GET("histori/tampil")
    Call<List<Histori>> getHistori();

    /************************** PENJUALAN *****************************/

    @GET("penjualan/tampil")
    Call<List<Penjualan>> getResultPenjualan();

    @POST("penjualan/tambah")
    @FormUrlEncoded
    Call<JsonElement> inputPenjualan(
            @Field("nama_customer") String nama_customer,
            @Field("no_telp_customer") String no_telp_customer,
            @Field("jenis_transaksi") String jenis_transaksi,
            @Field("status_transaksi") String status_transaksi,
            @Field("keterangan_transaksi") String keterangan_transaksi

    );

    @GET("penjualan/cari/{id}")
    Call<Penjualan> cariPenjualan(@Path("id") String id);

    @DELETE("penjualan/hapus/{id}")
    Call<Penjualan> hapusPenjualan(@Path("id") String id);

    @PUT("penjualan/ubah/{id}")
    @FormUrlEncoded
    Call<Penjualan> ubahPenjualan(
            @Path("id") String id,
            @Field("nama_customer") String nama_customer,
            @Field("no_telp_customer") String no_telp_customer,
            @Field("jenis_transaksi") String jenis_transaksi,
            @Field("status_transaksi") String status_transaksi,
            @Field("keterangan_transaksi") String keterangan_transaksi
    );

    /************************ DETAIL PENJUALAN SPAREPART ********************/

    @POST("penjualansparepart/tambah")
    @FormUrlEncoded
    Call<DetailPenjualanSparepart> inputDetailSparepart(
            @Field("data") String data
    );

    @GET("penjualansparepart/cari/{id}")
    Call<List<DetailPenjualanSparepart>> cariDetailSparepart(@Path("id") String id);

    /********************************** JASA SERVICE ****************************/

    @GET("jasaservice/tampil")
    Call<List<JasaService>> getResultService();

    /*********************************** PEGAWAI *********************************/

    @GET("pegawai/tampil_montir")
    Call<List<Pegawai>> getResultPegawai();

    /********************************* DETAIL PENJUALAN JASA ********************/

    @POST("penjualanjasa/tambah")
    @FormUrlEncoded
    Call<DetailPenjualanJasa> inputDetailService(
            @Field("data") String data
    );

    @GET("penjualanjasa/cari/{id}")
    Call<List<DetailPenjualanJasa>> cariDetailService(@Path("id") String id);

    /****************************** KENDARAAN CUSTOMER ****************************/
    @POST("kendaraancustomer/tambah")
    @FormUrlEncoded
    Call<JsonElement> inputKendaraan(
            @Field("data") String data
    );

    @GET("kendaraancustomer/tampil")
    Call<List<KendaraanCustomer>> getKendaraan();

    @GET("kendaraancustomer/cari_kendaraan/{id}")
    Call<List<KendaraanCustomer>> cariKendaraan(
            @Path("id") String id
    );





}
