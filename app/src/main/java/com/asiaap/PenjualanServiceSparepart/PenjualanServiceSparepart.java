package com.asiaap.PenjualanServiceSparepart;


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
import com.asiaap.PenjualanService.DetailServiceAdapter;
import com.asiaap.PenjualanService.KendaraanCustomerAdapter;
import com.asiaap.PenjualanService.myCallBackService;
import com.asiaap.PenjualanSparepart.DetailSparepartAdapter;
import com.asiaap.PenjualanSparepart.myCallBack;
import com.asiaap.PenjualanTampil;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
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
public class PenjualanServiceSparepart extends Fragment {

    public EditText customer, no_telp, nopol, jumlah;
    public Spinner tipe, montir, motor, service, sparepart, motorSpr;
    public Button btnTambahKendaraan, btnTambahService, input, btnTambahSparepart;



    private List<TipeKendaraan> tipeKendaraanList;
    private List<Pegawai> montirList;
    private List<JasaService> jasaServiceList;
    private List<KendaraanCustomer> kendaraanCustomerList;
    private List<DetailPenjualanJasa> penjualanJasaList;
    private List<DetailPenjualanSparepart> penjualanSparepartList;
    private List<Sparepart> sparepartList;

    private ArrayAdapter<TipeKendaraan> tipeKendaraanArrayAdapter;
    private ArrayAdapter<Pegawai> pegawaiArrayAdapter;
    private ArrayAdapter<JasaService> jasaServiceArrayAdapter;
    private ArrayAdapter<KendaraanCustomer> kendaraanCustomerArrayAdapter;
    private ArrayAdapter<KendaraanCustomer> kendaraanCustomerSPRArrayAdapter;
    private ArrayAdapter<DetailPenjualanSparepart> adapterDetailSparepart;
    private ArrayAdapter<Sparepart> adapterSparepart;

    private RecyclerView recyclerView, recyclerViewService, recyclerViewSparepart;
    private KendaraanCustomerAdapter adapterKendaraan;
    private DetailServiceAdapter adapterDetail;
    private DetailSparepartSSAdapter adapterDetailSpr;


    private List<EditText> list;

    private myCallBackService myCallBackService;
    private myCallBack myCallBack;

    TipeKendaraan selectedTipe;
    Pegawai selectedPegawai;
    JasaService selectedService;
    KendaraanCustomer selectedKendaraan;
    KendaraanCustomer selectedKendaraanSPR;
    Sparepart selectedSparepart;

    Boolean editedSrv = false;
    Boolean editedSpr = false;
    Boolean edited = false;
    String id = null;

    int posSpr;
    int posSrv;

    View view;


    public PenjualanServiceSparepart() {
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

                posSrv = position;
                editedSrv = editedService;
            }

            @Override
            public void updateETSpr(String nopol, String jumlah, String sparepart, int position, Boolean editedSparepart) {
                EditText etJumlah = view.findViewById(R.id.jumlah);
                Spinner sMotor = view.findViewById(R.id.motorSpr);
                Spinner sSparepart = view.findViewById(R.id.sparepart);
                editedSpr = editedSparepart;
                Log.d("sparepart", sparepart);

                Sparepart s = new Sparepart();
                s.setKodeSparepart(sparepart);

                KendaraanCustomer k = new KendaraanCustomer();
                k.setNoPolisi(nopol);

                ArrayAdapter<Sparepart> adapSparepart = (ArrayAdapter<Sparepart>) sSparepart.getAdapter();
                Integer sparepartPosition = new Integer(adapSparepart.getPosition(s));
                Log.d("supplier", sparepartPosition.toString());
                sSparepart.setSelection(sparepartPosition);

                ArrayAdapter<KendaraanCustomer> adapKendaraan = (ArrayAdapter<KendaraanCustomer>) sMotor.getAdapter();
                Integer kendaraanPosition = new Integer(adapKendaraan.getPosition(k));
                sMotor.setSelection(kendaraanPosition);

                etJumlah.setText(jumlah);

                posSpr = position;

            }

            @Override
            public void deleteKendaraan(String nopol) {
                KendaraanCustomer k = new KendaraanCustomer();
                k.setNoPolisi(nopol);
//                selectedKendaraan = null;
                kendaraanCustomerList.remove(k);
                kendaraanCustomerArrayAdapter.notifyDataSetChanged();

                int count = 0;
                int count2 = 0;

                if(penjualanJasaList.size() != 0) {
                    for (int i = 0; i <= penjualanJasaList.size(); i++) {
                        if (penjualanJasaList.get(i-count).getKendaraan().getNoPolisi().equals(nopol)) {
                            penjualanJasaList.remove(i-count);
                            adapterDetail.notifyItemRemoved(i-count);
                            adapterDetail.notifyItemRangeChanged(i-count, penjualanJasaList.size());
                            count++;
                        }

                        if(i == penjualanJasaList.size()-1 && count == 0)
                            break;
                    }
                }

                if(penjualanSparepartList.size() != 0) {
                    for (int i = 0; i <= penjualanSparepartList.size(); i++) {
                        if (penjualanSparepartList.get(i-count2).getKendaraan().getNoPolisi().equals(nopol)) {
                            penjualanSparepartList.remove(i-count2);
                            adapterDetailSpr.notifyItemRemoved(i-count2);
                            adapterDetailSpr.notifyItemRangeChanged(i-count2, penjualanSparepartList.size());
                            count2++;
                        }

                        if(i == penjualanSparepartList.size()-1 && count2 == 0)
                            break;
                    }
                }

            }
        };

//        myCallBack = new myCallBack() {
//            @Override
//            public void updateET(String jumlah, String kode, int position, Boolean editedSparepart) {
//                EditText etJumlah = view.findViewById(R.id.jumlah);
//                Spinner sSparepart = view.findViewById(R.id.sparepart);
//                editedSpr = editedSparepart;
//                Log.d("sparepart", kode);
//
//                Sparepart s = new Sparepart();
//                s.setKodeSparepart(kode);
//
//
//                ArrayAdapter<Sparepart> adapSparepart = (ArrayAdapter<Sparepart>) sSparepart.getAdapter();
//                Integer sparepartPosition = new Integer(adapSparepart.getPosition(s));
//                Log.d("supplier", sparepartPosition.toString());
//                sSparepart.setSelection(sparepartPosition);
//
//                etJumlah.setText(jumlah);
//
//                posSpr = position;
//            }
//        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tipeKendaraanList = new ArrayList<>();
        jasaServiceList = new ArrayList<>();
        montirList = new ArrayList<>();
        kendaraanCustomerList = new ArrayList<>();
        penjualanJasaList = new ArrayList<>();
        sparepartList = new ArrayList<>();
        penjualanSparepartList = new ArrayList<>();
        list = new ArrayList<>();

        Bundle mBundle = getArguments();

        if(mBundle != null) {
            id = mBundle.getString("id");
            loadDataPenjualan(id);
            edited = true;
        }

        View rootview = inflater.inflate(R.layout.fragment_penjualan_service_sparepart, container, false);
        view = rootview;

        customer = rootview.findViewById(R.id.customer);
        no_telp = rootview.findViewById(R.id.no_telp);
        nopol = rootview.findViewById(R.id.nopol);
        tipe = rootview.findViewById(R.id.tipe);
        montir = rootview.findViewById(R.id.montir);
        motor = rootview.findViewById(R.id.motor);
        service = rootview.findViewById(R.id.service);
        motorSpr = rootview.findViewById(R.id.motorSpr);
        btnTambahKendaraan = rootview.findViewById(R.id.btnTambahKendaraan);
        btnTambahService = rootview.findViewById(R.id.btnTambahService);
        input = rootview.findViewById(R.id.input);
        sparepart = rootview.findViewById(R.id.sparepart);
        jumlah = rootview.findViewById(R.id.jumlah);
        btnTambahSparepart = rootview.findViewById(R.id.btnTambahSparepart);

        list.add(customer);
        list.add(no_telp);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_kendaraan);
        adapterKendaraan = new KendaraanCustomerAdapter(getContext(), kendaraanCustomerList, myCallBackService);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterKendaraan);

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

//        kendaraanCustomerSPRArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, kendaraanCustomerList);
//        kendaraanCustomerSPRArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        motorSpr.setAdapter(kendaraanCustomerArrayAdapter);
        motorSpr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedKendaraanSPR = (KendaraanCustomer) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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


        adapterSparepart = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sparepartList);
        adapterSparepart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sparepart.setAdapter(adapterSparepart);
        loadSparepart();
        sparepart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSparepart = (Sparepart) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerViewSparepart = (RecyclerView) rootview.findViewById(R.id.recycler_view_detailspr);
        adapterDetailSpr = new DetailSparepartSSAdapter(getContext(), penjualanSparepartList, myCallBackService);
        RecyclerView.LayoutManager mLayoutManager3 = new GridLayoutManager(getContext(), 1);
        recyclerViewSparepart.setLayoutManager(mLayoutManager3);
        recyclerViewSparepart.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSparepart.setAdapter(adapterDetailSpr);

        btnTambahKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkNullKendaraan()) {
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
                if(checkNullSrv()) {
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
                    DetailPenjualanJasa detail = penjualanJasaList.get(posSrv);

                    List<DetailPenjualanJasa> tempDet = new ArrayList<>();

                    for(DetailPenjualanJasa det : penjualanJasaList) {
                        if(!det.equals(detail))
                            tempDet.add(det);
                    }

                    if(tempDet.contains(detailPenjualanJasa)){
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

        btnTambahSparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNullSpr()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Peringatan");
                    if(editedSpr == false)
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
                    editedSpr = false;
                    return;
                }

                if(checkStok()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Peringatan");
                    if(editedSpr == false)
                        alertDialog.setMessage("Stok tidak cukup!");
                    else
                        alertDialog.setMessage("Stok tidak cukup, Edit Gagal!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    editedSpr = false;
                    return;
                }

                if(Integer.parseInt(jumlah.getText().toString()) <= 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Peringatan");
                    if(editedSpr == false)
                        alertDialog.setMessage("Jumlah Minimal 1");
                    else
                        alertDialog.setMessage("Jumlah Minimal 1, Edit Gagal");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    editedSpr = false;
                    return;
                }

                String sparepart = selectedSparepart.getKodeSparepart();
                Integer jumlah_sparepart = Integer.parseInt(jumlah.getText().toString());
                DetailPenjualanSparepart detail = new DetailPenjualanSparepart();
                detail.setKodeSparepart(sparepart);
                detail.setJumlahPenjualanSparepart(jumlah_sparepart);
                detail.setKendaraan(selectedKendaraanSPR);

                if(editedSpr == false) {
                    if(penjualanSparepartList.contains(detail)){
                        Toast.makeText(getContext(), "Data telah terdaftar", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    penjualanSparepartList.add(detail);
                    adapterDetailSpr.notifyDataSetChanged();
                } else {
                    DetailPenjualanSparepart getDetail = penjualanSparepartList.get(posSpr);

                    ArrayList<DetailPenjualanSparepart> tempDet = new ArrayList<>();

                    for(DetailPenjualanSparepart det : penjualanSparepartList) {
                        if(!det.equals(getDetail))
                            tempDet.add(det);
                    }

                    Log.d("tempDet", new Gson().toJson(tempDet));

                    if(tempDet.contains(detail)){
                        Toast.makeText(getContext(), "Data telah terdaftar, Edit Gagal", Toast.LENGTH_SHORT).show();
                        editedSpr = false;
                        return;
                    }

                    getDetail.setKendaraan(detail.getKendaraan());
                    getDetail.setKodeSparepart(detail.getKodeSparepart());
                    getDetail.setJumlahPenjualanSparepart(detail.getJumlahPenjualanSparepart());
                    adapterDetailSpr.notifyDataSetChanged();
                    editedSpr = false;
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
            alertDialog.setMessage("Terdapat Motor yang Tidak diservice atau Tidak Memiliki Sparepart!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        String nama_customer = customer.getText().toString();
        String no_telp_customer = no_telp.getText().toString();

        if(edited == false) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<JsonElement> call = apiService.inputPenjualan(nama_customer, no_telp_customer, "SS", "Proses", "Belum Lunas");

            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    if(response.code()==200) {
                        JsonObject res = response.body().getAsJsonObject().getAsJsonObject("penjualan");
                        for(DetailPenjualanJasa det : penjualanJasaList) {
                            det.setNoTransaksi(res.get("no_transaksi").getAsString());
                        }

                        for(DetailPenjualanSparepart det : penjualanSparepartList) {
                            det.setNoTransaksi(res.get("no_transaksi").getAsString());
                        }
                        inputKendaraan(penjualanJasaList, penjualanSparepartList, pd);
//                        inputDetailSparepart(penjualanSparepartList, pd);
                    }
                    else
                        Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                }
            });
        }
        else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<Penjualan> call = apiService.ubahPenjualan(id, nama_customer, no_telp_customer, "SS", "Proses", "Belum Lunas");
            call.enqueue(new Callback<Penjualan>() {
                @Override
                public void onResponse(Call<Penjualan> call, Response<Penjualan> response) {
                    if(response.code() == 200) {
                        for(DetailPenjualanJasa det : penjualanJasaList) {
                            det.setNoTransaksi(id);
                        }

                        for(DetailPenjualanSparepart det : penjualanSparepartList) {
                            det.setNoTransaksi(id);
                        }

                        inputKendaraan(penjualanJasaList, penjualanSparepartList, pd);

                    }
                    else
                        Toast.makeText(getContext(), "Edit Gagal", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<Penjualan> call, Throwable t) {

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
                pd.dismiss();
                if(response.code() == 200 && edited == false)
                    Toast.makeText(getContext(), "Input Berhasil", Toast.LENGTH_SHORT).show();
                else if(response.code() == 200 && edited == true)
                    Toast.makeText(getContext(), "Edit Berhasil", Toast.LENGTH_SHORT).show();
                else
                    Log.d("penjualan_bro", new Gson().toJson(response));

                Fragment fragment = new PenjualanTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

            @Override
            public void onFailure(Call<DetailPenjualanJasa> call, Throwable t) {
                pd.dismiss();
//
                Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();

                Fragment fragment = new PenjualanTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    public void inputKendaraan(final List<DetailPenjualanJasa> list, final List<DetailPenjualanSparepart> listSpr, final ProgressDialog pd) {
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

                for(DetailPenjualanSparepart det : listSpr) {
                    for (KendaraanCustomer ken : kenList) {
                        if(det.getKendaraan().getNoPolisi().equals(ken.getNoPolisi()))
                            det.setIdKendaraan(ken.getIdKendaraan());
                    }
                }
                inputDetailSparepart(listSpr);
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
                pd.dismiss();
                kendaraanCustomerList.addAll(response.body());
                Log.d("penjualan_bro", new Gson().toJson(kendaraanCustomerList));
                adapterKendaraan.notifyDataSetChanged();
                kendaraanCustomerArrayAdapter.notifyDataSetChanged();


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
                loadDetailPenjualanSpr(no_transaksi);
                loadKendaraan(pd);
            }

            @Override
            public void onFailure(Call<Penjualan> call, Throwable t) {
                Log.d("error_pengadaan", t.getMessage());
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

    private void loadSparepart () {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<Sparepart>> call = apiService.getResult();

        call.enqueue(new Callback<List<Sparepart>>() {
            @Override
            public void onResponse(Call<List<Sparepart>> call, Response<List<Sparepart>> response) {
                sparepartList.addAll(response.body());
                adapterSparepart.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Sparepart>> call, Throwable t) {

            }
        });
    }

    private void loadDetailPenjualanSpr (String no_transaksi) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<DetailPenjualanSparepart>> call = apiService.cariDetailSparepart(no_transaksi);

        call.enqueue(new Callback<List<DetailPenjualanSparepart>>() {
            @Override
            public void onResponse(Call<List<DetailPenjualanSparepart>> call, Response<List<DetailPenjualanSparepart>> response) {
                penjualanSparepartList.addAll(response.body());
                adapterDetailSpr.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<DetailPenjualanSparepart>> call, Throwable t) {

            }
        });


    }

    public void inputDetailSparepart(List<DetailPenjualanSparepart> list) {
        String data = new Gson().toJson(list);
        Log.d("det_spr", data);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<DetailPenjualanSparepart> call = apiService.inputDetailSparepart(data);

        Log.d("penjualan_bro", data);

        call.enqueue(new Callback<DetailPenjualanSparepart>() {
            @Override
            public void onResponse(Call<DetailPenjualanSparepart> call, Response<DetailPenjualanSparepart> response) {
//                pd.dismiss();
//                if(response.code() == 200 && edited == false)
//                    Toast.makeText(getContext(), "Input Berhasil", Toast.LENGTH_SHORT).show();
//                else if(response.code() == 200 && edited == true)
//                    Toast.makeText(getContext(), "Edit Berhasil", Toast.LENGTH_SHORT).show();
//                else
//                    Log.d("penjualan_bro", new Gson().toJson(response));
//
//                Fragment fragment = new PenjualanTampil();
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.frame, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }

            @Override
            public void onFailure(Call<DetailPenjualanSparepart> call, Throwable t) {
//                pd.dismiss();
//
//                Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();
//
//                Fragment fragment = new PenjualanTampil();
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.frame, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });

    }

    public Boolean checkNull() {
        for (EditText et : list) {
            if(TextUtils.isEmpty(et.getText().toString().trim()))
                return true;
        }

        if(penjualanSparepartList.size() == 0)
            return true;

        if(penjualanJasaList.size() == 0)
            return true;

        if(kendaraanCustomerList.size() == 0)
            return true;

        return false;
    }

    public Boolean checkNullSpr () {
        if(kendaraanCustomerList.size() == 0)
            return true;

        if(TextUtils.isEmpty(jumlah.getText().toString().trim()))
            return true;

        return false;
    }

    public Boolean checkStok() {
        if(Integer.parseInt(jumlah.getText().toString()) > selectedSparepart.getStokSparepart())
            return true;

        return false;
    }

    private Boolean checkNullKendaraan() {
        if(TextUtils.isEmpty(nopol.getText().toString().trim()))
            return true;

        return false;
    }

    private Boolean checkNullSrv() {
        if(kendaraanCustomerList.size() == 0)
            return true;

        return false;
    }

    private Boolean checkMotor() {
        ArrayList<String> nopol = new ArrayList<>();
        ArrayList<String> detail = new ArrayList<>();
        ArrayList<String> detailSpr = new ArrayList<>();

        for(KendaraanCustomer ken : kendaraanCustomerList) {
            nopol.add(ken.getNoPolisi());
        }

        for(DetailPenjualanJasa det : penjualanJasaList) {
            detail.add(det.getKendaraan().getNoPolisi());
        }

        for(DetailPenjualanSparepart det : penjualanSparepartList) {
            detailSpr.add(det.getKendaraan().getNoPolisi());
        }

        int cek = 0;

        for(String n : nopol) {
            if(!detail.contains(n))
                cek++;

        }

        if(cek > 0)
            return true;

        int cek1 = 0;

        for(String n : nopol) {
            if(!detailSpr.contains(n))
                cek1++;
        }

        if(cek1 > 0)
            return true;

        return false;



    }

}
