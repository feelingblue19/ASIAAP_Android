package com.asiaap.PenjualanService;

public interface myCallBackService {

    public void loadService(String service, String nopol, int position, Boolean editedService);

    public void updateETSpr(String nopol, String jumlah, String sparepart, int position, Boolean editedSparepart);

    public void deleteKendaraan(String nopol);
}
