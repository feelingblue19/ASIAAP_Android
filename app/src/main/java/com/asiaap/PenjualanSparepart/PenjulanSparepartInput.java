package com.asiaap.PenjualanSparepart;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.BoringLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.asiaap.Model.DetailPengadaan;
import com.asiaap.Model.DetailPenjualanSparepart;
import com.asiaap.Model.Pengadaan;
import com.asiaap.Model.Penjualan;
import com.asiaap.Model.Sparepart;
import com.asiaap.Model.Supplier;
import com.asiaap.Pengadaan.DetailPengadaanAdapter;
import com.asiaap.PenjualanTampil;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PenjulanSparepartInput extends Fragment {

    public EditText customer, no_telp, jumlah;
    public Spinner sparepart;
    public Button tambahSparepart, input;

    private RecyclerView recyclerView;
    private DetailSparepartAdapter adapterDetail;
    private List<DetailPenjualanSparepart> penjualanSparepartList;
    private List<Sparepart> sparepartList;
    private List<EditText> list;

    private ArrayAdapter<DetailPenjualanSparepart> adapterDetailSparepart;
    private ArrayAdapter<Sparepart> adapterSparepart;

    private myCallBack myCallBack;
    View view;

    Boolean edited = false;
    String id = null;
    Boolean editedSpr = false;
    int pos;

    private Sparepart selectedSparepart = null;

    public PenjulanSparepartInput() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myCallBack =  new myCallBack() {
            @Override
            public void updateET(String jumlah, String kode, int position, Boolean editedSparepart) {
                EditText etJumlah = view.findViewById(R.id.jumlah);
                Spinner sSparepart = view.findViewById(R.id.sparepart);
                editedSpr = editedSparepart;
                Log.d("sparepart", kode);

                Sparepart s = new Sparepart();
                s.setKodeSparepart(kode);


                ArrayAdapter<Sparepart> adapSparepart = (ArrayAdapter<Sparepart>) sSparepart.getAdapter();
                Integer sparepartPosition = new Integer(adapSparepart.getPosition(s));
                Log.d("supplier", sparepartPosition.toString());
                sSparepart.setSelection(sparepartPosition);

                etJumlah.setText(jumlah);

                pos = position;

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle mBundle = getArguments();

        if(mBundle != null) {
            id = mBundle.getString("id");
            loadDataPenjualan(id);
            edited = true;
        }

        sparepartList = new ArrayList<>();
        penjualanSparepartList = new ArrayList<>();
        list = new ArrayList<>();

        View rootview = inflater.inflate(R.layout.fragment_penjulan_sparepart_input, container, false);
        view = rootview;

        customer = rootview.findViewById(R.id.customer);
        no_telp = rootview.findViewById(R.id.no_telp);
        jumlah = rootview.findViewById(R.id.jumlah);
        sparepart = rootview.findViewById(R.id.sparepart);
        tambahSparepart = rootview.findViewById(R.id.btnTambahSparepart);
        input = rootview.findViewById(R.id.input);

        list.add(customer);
        list.add(no_telp);


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

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_detailspr);
        adapterDetail = new DetailSparepartAdapter(getContext(), penjualanSparepartList, myCallBack);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterDetail);

        tambahSparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkNull2()){
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
                DetailPenjualanSparepart detail = new DetailPenjualanSparepart(sparepart, jumlah_sparepart);
                if(editedSpr == false) {
                    if(penjualanSparepartList.contains(detail)) {
                        Toast.makeText(getContext(), "Data telah terdaftar", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    penjualanSparepartList.add(detail);
                    adapterDetail.notifyDataSetChanged();
                } else {
                    DetailPenjualanSparepart getDetail = penjualanSparepartList.get(pos);

                    ArrayList<DetailPenjualanSparepart> tempDet = new ArrayList<>();

                    for(DetailPenjualanSparepart det : penjualanSparepartList) {
                        if(!det.equals(getDetail))
                            tempDet.add(det);
                    }

                    Log.d("tempDet", new Gson().toJson(tempDet));

                    if(tempDet.contains(detail)) {
                        Toast.makeText(getContext(), "Data telah terdaftar, Edit Gagal!", Toast.LENGTH_SHORT).show();
                        editedSpr = false;
                        return;
                    }

                    getDetail.setKodeSparepart(detail.getKodeSparepart());
                    getDetail.setJumlahPenjualanSparepart(detail.getJumlahPenjualanSparepart());
                    adapterDetail.notifyDataSetChanged();
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

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        String nama_customer = customer.getText().toString();
        String no_telp_customer = no_telp.getText().toString();

        if(edited == false) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<JsonElement> call = apiService.inputPenjualan(nama_customer, no_telp_customer, "SP", "Selesai", "Belum Lunas");

            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    if(response.code()==200) {
                        JsonObject res = response.body().getAsJsonObject().getAsJsonObject("penjualan");
                        for(DetailPenjualanSparepart det : penjualanSparepartList) {
                            det.setNoTransaksi(res.get("no_transaksi").getAsString());
                        }
                        inputDetail(penjualanSparepartList, pd);
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
            final Call<Penjualan> call = apiService.ubahPenjualan(id, nama_customer, no_telp_customer, "SP", "Selesai", "Belum Lunas");
            call.enqueue(new Callback<Penjualan>() {
                @Override
                public void onResponse(Call<Penjualan> call, Response<Penjualan> response) {
                    if(response.code() == 200) {
                        for(DetailPenjualanSparepart det : penjualanSparepartList) {
                            det.setNoTransaksi(id);
                        }
                        inputDetail(penjualanSparepartList, pd);
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

    public void inputDetail(List<DetailPenjualanSparepart> list, final ProgressDialog pd) {
        String data = new Gson().toJson(list);
        Log.d("det_spr", data);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<DetailPenjualanSparepart> call = apiService.inputDetailSparepart(data);

        Log.d("penjualan_bro", data);

        call.enqueue(new Callback<DetailPenjualanSparepart>() {
            @Override
            public void onResponse(Call<DetailPenjualanSparepart> call, Response<DetailPenjualanSparepart> response) {
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
            public void onFailure(Call<DetailPenjualanSparepart> call, Throwable t) {
                pd.dismiss();
                Fragment fragment = new PenjualanTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                Log.d("penjualan_bro", t.getMessage());
                Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();
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
                loadDetailPenjualan(no_transaksi, pd);
            }

            @Override
            public void onFailure(Call<Penjualan> call, Throwable t) {
                Log.d("error_pengadaan", t.getMessage());
            }
        });

    }

    private void loadDetailPenjualan (String no_transaksi, final ProgressDialog pd) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<DetailPenjualanSparepart>> call = apiService.cariDetailSparepart(no_transaksi);

        call.enqueue(new Callback<List<DetailPenjualanSparepart>>() {
            @Override
            public void onResponse(Call<List<DetailPenjualanSparepart>> call, Response<List<DetailPenjualanSparepart>> response) {
                penjualanSparepartList.addAll(response.body());
                adapterDetail.notifyDataSetChanged();
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<List<DetailPenjualanSparepart>> call, Throwable t) {
                pd.dismiss();
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

        return false;
    }

    public Boolean checkNull2 () {
        if(TextUtils.isEmpty(jumlah.getText().toString().trim()))
            return true;

        return false;
    }

    public Boolean checkStok() {
        if(Integer.parseInt(jumlah.getText().toString()) > selectedSparepart.getStokSparepart())
            return true;

        return false;
    }

}

