package com.asiaap.PenjualanService;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.asiaap.Model.DetailPenjualanJasa;
import com.asiaap.Model.DetailPenjualanSparepart;
import com.asiaap.Model.JasaService;
import com.asiaap.Model.KendaraanCustomer;
import com.asiaap.Model.Pegawai;
import com.asiaap.Model.Penjualan;
import com.asiaap.Model.Sparepart;
import com.asiaap.Model.TipeKendaraan;
import com.asiaap.PenjualanTampil;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.asiaap.Sparepart.SparepartTampil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PenjualanServiceInput extends Fragment {

    public EditText customer, no_telp, nopol;
    public Spinner tipe, montir, motor, service;
    public Button btnTambahKendaraan, btnTambahService, input;

    private List<TipeKendaraan> tipeKendaraanList;
    private List<Pegawai> montirList;
    private List<JasaService> jasaServiceList;
    private List<KendaraanCustomer> kendaraanCustomerList;
    private List<DetailPenjualanJasa> penjualanJasaList;

    private ArrayAdapter<TipeKendaraan> tipeKendaraanArrayAdapter;
    private ArrayAdapter<Pegawai> pegawaiArrayAdapter;
    private ArrayAdapter<JasaService> jasaServiceArrayAdapter;
    private ArrayAdapter<KendaraanCustomer> kendaraanCustomerArrayAdapter;

    private RecyclerView recyclerView, recyclerViewService;
    private KendaraanCustomerAdapter adapterKendaraan;
    private DetailServiceAdapter adapterDetail;

    private myCallBackService myCallBackService;
    private List<EditText> list;

    TipeKendaraan selectedTipe;
    Pegawai selectedPegawai;
    JasaService selectedService;
    KendaraanCustomer selectedKendaraan;

    Boolean editedSrv = false;
    Boolean edited = false;
    String id = null;

    int pos;

    View view;

    public PenjualanServiceInput() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myCallBackService = new myCallBackService() {
            @Override
            public void loadService(String service, String nopol, int position, Boolean editedService) {
                Log.d("service_bro", service +" "+ nopol);
                Spinner sMotor = view.findViewById(R.id.motor);
                Spinner sService = view.findViewById(R.id.service);

                KendaraanCustomer k = new KendaraanCustomer();
                k.setNoPolisi(nopol);

                JasaService s = new JasaService();
                s.setIdJasaService(service);

                Log.d("service_bro", s.getIdJasaService());

                ArrayAdapter<KendaraanCustomer> adapKendaraan = (ArrayAdapter<KendaraanCustomer>) sMotor.getAdapter();
                Integer kendaraanPosition = new Integer(adapKendaraan.getPosition(k));
                sMotor.setSelection(kendaraanPosition);

                ArrayAdapter<JasaService> adapService = (ArrayAdapter<JasaService>) sService.getAdapter();
                Integer servicePosition = new Integer(adapService.getPosition(s));
                sService.setSelection(servicePosition);

                pos = position;
                editedSrv = editedService;

            }

            @Override
            public void updateETSpr(String nopol, String jumlah, String sparepart, int position, Boolean editedSparepart) {

            }

            @Override
            public void deleteKendaraan(String nopol) {
                KendaraanCustomer k = new KendaraanCustomer();
                k.setNoPolisi(nopol);

//                selectedKendaraan = null;
                kendaraanCustomerList.remove(k);
                kendaraanCustomerArrayAdapter.notifyDataSetChanged();

                int count = 0;

                if(penjualanJasaList.size() != 0) {
                    for (int i = 0; i <= penjualanJasaList.size(); i++) {
                            if (penjualanJasaList.get(i - count).getKendaraan().getNoPolisi().equals(nopol)) {
                                penjualanJasaList.remove(i - count);
                                adapterDetail.notifyItemRemoved(i - count);
                                adapterDetail.notifyItemRangeChanged(i - count, list.size());
                                count++;
                            }

                            if(i == penjualanJasaList.size()-1 && count == 0)
                                break;
                    }
                }
            }


        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tipeKendaraanList = new ArrayList<>();
        jasaServiceList = new ArrayList<>();
        montirList = new ArrayList<>();
        kendaraanCustomerList = new ArrayList<>();
        penjualanJasaList = new ArrayList<>();
        list = new ArrayList<>();

        Bundle mBundle = getArguments();

        if(mBundle != null) {
            id = mBundle.getString("id");
            loadDataPenjualan(id);
            edited = true;
        }

        View rootview = inflater.inflate(R.layout.fragment_penjualan_service_input, container, false);
        view = rootview;

        customer = rootview.findViewById(R.id.customer);
        no_telp = rootview.findViewById(R.id.no_telp);
        nopol = rootview.findViewById(R.id.nopol);
        tipe = rootview.findViewById(R.id.tipe);
        montir = rootview.findViewById(R.id.montir);
        motor = rootview.findViewById(R.id.motor);
        service = rootview.findViewById(R.id.service);
        btnTambahKendaraan = rootview.findViewById(R.id.btnTambahKendaraan);
        btnTambahService = rootview.findViewById(R.id.btnTambahService);
        input = rootview.findViewById(R.id.input);

        list.add(customer);
        list.add(no_telp);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_kendaraan);
        adapterKendaraan = new KendaraanCustomerAdapter(getContext(), kendaraanCustomerList, myCallBackService);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterKendaraan);

        recyclerViewService = (RecyclerView) rootview.findViewById(R.id.recycler_view_service);
        adapterDetail = new DetailServiceAdapter(getContext(), penjualanJasaList, myCallBackService, kendaraanCustomerArrayAdapter);
        RecyclerView.LayoutManager mLayoutManager2 = new GridLayoutManager(getContext(), 1);
        recyclerViewService.setLayoutManager(mLayoutManager2);
        recyclerViewService.setItemAnimator(new DefaultItemAnimator());
        recyclerViewService.setAdapter(adapterDetail);

        tipeKendaraanArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tipeKendaraanList);
        tipeKendaraanArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipe.setAdapter(tipeKendaraanArrayAdapter);
        loadTipeKendaraan();
        tipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTipe = (TipeKendaraan) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kendaraanCustomerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, kendaraanCustomerList);
        kendaraanCustomerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        motor.setAdapter(kendaraanCustomerArrayAdapter);
        motor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedKendaraan = (KendaraanCustomer) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pegawaiArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, montirList);
        pegawaiArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        montir.setAdapter(pegawaiArrayAdapter);
        loadPegawai();
        montir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPegawai = (Pegawai) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jasaServiceArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, jasaServiceList);
        jasaServiceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        service.setAdapter(jasaServiceArrayAdapter);
        loadJasaService();
        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedService = (JasaService) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnTambahKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNull2()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Peringatan");
                    alertDialog.setMessage("Data Tidak Boleh Kosong!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }

                String tipe = selectedTipe.getIdTipe();
                String no_pol = nopol.getText().toString();
                String montir = selectedPegawai.getIdPegawai();
                KendaraanCustomer kendaraanCustomer = new KendaraanCustomer(tipe, no_pol, montir);
                if(kendaraanCustomerList.contains(kendaraanCustomer)) {
                    Toast.makeText(getContext(), "Data telah terdaftar", Toast.LENGTH_SHORT).show();
                    return;
                }
                adapterKendaraan.notifyDataSetChanged();
                kendaraanCustomerList.add(kendaraanCustomer);
                kendaraanCustomerArrayAdapter.notifyDataSetChanged();

            }
        });

        btnTambahService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkNull3()){
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Peringatan");
                    if(editedSrv == false)
                        alertDialog.setMessage("Data Tidak Boleh Kosong!");
                    else
                        alertDialog.setMessage("Data Tidak Boleh Kosong, Edit Gagal!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    editedSrv = false;
                    return;
                }
                String jasa_service = selectedService.getIdJasaService();
                int jumlah = 1;

                DetailPenjualanJasa detailPenjualanJasa = new DetailPenjualanJasa();
                detailPenjualanJasa.setKendaraan(selectedKendaraan);
                detailPenjualanJasa.setIdJasaService(jasa_service);
                detailPenjualanJasa.setJumlahPenjualanJasa(jumlah);
                if(editedSrv == false) {
                    if(penjualanJasaList.contains(detailPenjualanJasa)) {
                        Toast.makeText(getContext(), "Data telah terdaftar", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    penjualanJasaList.add(detailPenjualanJasa);
                    adapterDetail.notifyDataSetChanged();
                } else {
                    Log.d("service_bro", "edited");
                    DetailPenjualanJasa detail = penjualanJasaList.get(pos);

                    List<DetailPenjualanJasa> tempDet = new ArrayList<>();

                    for(DetailPenjualanJasa det : penjualanJasaList) {
                        if(!det.equals(detail))
                            tempDet.add(det);
                    }

                    if(tempDet.contains(detailPenjualanJasa)) {
                        Toast.makeText(getContext(), "Data telah terdaftar, Edit Gagal", Toast.LENGTH_SHORT).show();
                        editedSrv = false;
                        return;
                    }


                    detail.setKendaraan(detailPenjualanJasa.getKendaraan());
                    detail.setIdJasaService(detailPenjualanJasa.getIdJasaService());
                    detail.setJumlahPenjualanJasa(detailPenjualanJasa.getJumlahPenjualanJasa());
                    adapterDetail.notifyDataSetChanged();
                    editedSrv = false;
                }
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPenjualan();
            }
        });



        return rootview;
    }

    public void loadTipeKendaraan() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<TipeKendaraan>> call = apiService.getTipeKendaraan();

        call.enqueue(new Callback<List<TipeKendaraan>>() {
            @Override
            public void onResponse(Call<List<TipeKendaraan>> call, Response<List<TipeKendaraan>> response) {
                tipeKendaraanList.addAll(response.body());
                tipeKendaraanArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<TipeKendaraan>> call, Throwable t) {

            }
        });

    }

    public void loadJasaService() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<JasaService>> call = apiService.getResultService();

        call.enqueue(new Callback<List<JasaService>>() {
            @Override
            public void onResponse(Call<List<JasaService>> call, Response<List<JasaService>> response) {
                jasaServiceList.addAll(response.body());
                jasaServiceArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<JasaService>> call, Throwable t) {

            }
        });
    }

    public void loadPegawai() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<Pegawai>> call = apiService.getResultPegawai();

        call.enqueue(new Callback<List<Pegawai>>() {
            @Override
            public void onResponse(Call<List<Pegawai>> call, Response<List<Pegawai>> response) {
                Log.d("pegawai", new Gson().toJson(response.body()));
                if(response.body() != null) {
                    montirList.addAll(response.body());
                    pegawaiArrayAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<List<Pegawai>> call, Throwable t) {

            }
        });


    }

    public void inputPenjualan() {

        if(checkNull()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Peringatan");
            alertDialog.setMessage("Data Tidak Boleh Kosong!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        if(checkMotor()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Peringatan");
            alertDialog.setMessage("Terdapat Motor yang Tidak diservice!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        String nama_customer = customer.getText().toString();
        String no_telp_customer = no_telp.getText().toString();

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        if(edited == false) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<JsonElement> call = apiService.inputPenjualan(nama_customer, no_telp_customer, "SV", "Proses", "Belum Lunas");

            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    if(response.code()==200) {
                        JsonObject res = response.body().getAsJsonObject().getAsJsonObject("penjualan");
                        for(DetailPenjualanJasa det : penjualanJasaList) {
                            det.setNoTransaksi(res.get("no_transaksi").getAsString());
                        }
                        inputKendaraan(penjualanJasaList, pd);
                    }
                    else
                        Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<Penjualan> call = apiService.ubahPenjualan(id, nama_customer, no_telp_customer, "SV", "Proses", "Belum Lunas");
            call.enqueue(new Callback<Penjualan>() {
                @Override
                public void onResponse(Call<Penjualan> call, Response<Penjualan> response) {
                    if(response.code() == 200) {
                        for(DetailPenjualanJasa det : penjualanJasaList) {
                            det.setNoTransaksi(id);
                        }
                        inputKendaraan(penjualanJasaList, pd);
                        pd.dismiss();
                    }
                    else
                        Toast.makeText(getContext(), "Edit Gagal", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<Penjualan> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Edit Gagal", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    public void inputDetail(List<DetailPenjualanJasa> list, final ProgressDialog pd) {
        String data = new Gson().toJson(list);
        Log.d("det_spr", data);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<DetailPenjualanJasa> call = apiService.inputDetailService(data);

        Log.d("penjualan_bro", data);

        call.enqueue(new Callback<DetailPenjualanJasa>() {
            @Override
            public void onResponse(Call<DetailPenjualanJasa> call, Response<DetailPenjualanJasa> response) {
                if(response.code() == 200 && edited == false)
                    Toast.makeText(getContext(), "Input Berhasil", Toast.LENGTH_SHORT).show();
                else if(response.code() == 200 && edited == true)
                    Toast.makeText(getContext(), "Edit Berhasil", Toast.LENGTH_SHORT).show();
                else
                    Log.d("penjualan_bro", new Gson().toJson(response));

                pd.dismiss();
                Fragment fragment = new PenjualanTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onFailure(Call<DetailPenjualanJasa> call, Throwable t) {
                pd.dismiss();

                Log.d("penjualan_bro", t.getMessage());
                Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();

                Fragment fragment = new PenjualanTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    public void inputKendaraan(final List<DetailPenjualanJasa> list, final ProgressDialog pd) {
        String data = new Gson().toJson(kendaraanCustomerList);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<JsonElement> call = apiService.inputKendaraan(data);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonArray res = response.body().getAsJsonObject().getAsJsonArray("kendaraan_customer");
                String resString = new Gson().toJson(res);
                Gson gson = new Gson();
                Type type = new TypeToken<List<KendaraanCustomer>>(){}.getType();
                List<KendaraanCustomer> kenList = gson.fromJson(resString, type);

                for(DetailPenjualanJasa det : list) {
                    for (KendaraanCustomer ken : kenList) {
                        if(det.getKendaraan().getNoPolisi().equals(ken.getNoPolisi()))
                            det.setIdKendaraan(ken.getIdKendaraan());
                    }
                }

                inputDetail(list, pd);

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });

    }

    public void loadKendaraan(final ProgressDialog pd) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<KendaraanCustomer>> call = apiService.cariKendaraan(id);

        call.enqueue(new Callback<List<KendaraanCustomer>>() {
            @Override
            public void onResponse(Call<List<KendaraanCustomer>> call, Response<List<KendaraanCustomer>> response) {
                kendaraanCustomerList.addAll(response.body());
                Log.d("penjualan_bro", new Gson().toJson(kendaraanCustomerList));
                adapterKendaraan.notifyDataSetChanged();
                kendaraanCustomerArrayAdapter.notifyDataSetChanged();
                pd.dismiss();


            }

            @Override
            public void onFailure(Call<List<KendaraanCustomer>> call, Throwable t) {
                pd.dismiss();
            }
        });
    }



    public void loadDataPenjualan(final String no_transaksi) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<Penjualan> call = apiService.cariPenjualan(no_transaksi);

        call.enqueue(new Callback<Penjualan>() {
            @Override
            public void onResponse(Call<Penjualan> call, Response<Penjualan> response) {
                Penjualan p = response.body();
                Log.d("cek_jenis", new Gson().toJson(response.body()));
                customer.setText(p.getNamaCustomer());
                no_telp.setText(p.getNoTelpCustomer());
                loadDetailPenjualan(no_transaksi);
                loadKendaraan(pd);
            }

            @Override
            public void onFailure(Call<Penjualan> call, Throwable t) {
                Log.d("error_pengadaan", t.getMessage());
                pd.dismiss();
            }
        });

    }

    private void loadDetailPenjualan (String no_transaksi) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<DetailPenjualanJasa>> call = apiService.cariDetailService(no_transaksi);

        call.enqueue(new Callback<List<DetailPenjualanJasa>>() {
            @Override
            public void onResponse(Call<List<DetailPenjualanJasa>> call, Response<List<DetailPenjualanJasa>> response) {
                penjualanJasaList.addAll(response.body());
                adapterDetail.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<DetailPenjualanJasa>> call, Throwable t) {

            }
        });
    }

    public Boolean checkNull() {
        for (EditText et : list) {
            if(TextUtils.isEmpty(et.getText().toString().trim()))
                return true;
        }

        if(penjualanJasaList.size() == 0)
            return true;

        if(kendaraanCustomerList.size() == 0)
            return true;

        return false;
    }

    private Boolean checkNull2() {
        if(TextUtils.isEmpty(nopol.getText().toString().trim()))
            return true;

        return false;
    }

    private Boolean checkNull3() {
        if(kendaraanCustomerList.size() == 0)
            return true;


        return false;
    }

    private Boolean checkMotor() {
        ArrayList<String> nopol = new ArrayList<>();
        ArrayList<String> detail = new ArrayList<>();

        for(KendaraanCustomer ken : kendaraanCustomerList) {
            nopol.add(ken.getNoPolisi());
        }

        for(DetailPenjualanJasa det : penjualanJasaList) {
            detail.add(det.getKendaraan().getNoPolisi());
        }

        int cek = 0;

        for(String n : nopol) {
            if(!detail.contains(n))
                cek++;

        }

        if(cek > 0)
            return true;
        else
            return false;
    }



}
