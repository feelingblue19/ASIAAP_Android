package com.asiaap.Pengadaan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
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

import com.asiaap.Model.DetailPengadaan;
import com.asiaap.Model.Pengadaan;
import com.asiaap.Model.Sparepart;
import com.asiaap.Model.Supplier;
import com.asiaap.Model.TipeKendaraan;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.asiaap.Sparepart.TipeKendaraanAdapter;
import com.asiaap.Supplier.SupplierTampil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PengadaanInput.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PengadaanInput#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PengadaanInput extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, kodePengadaan;
    private String mParam2;
    private EditText jumlah, satuan, harga;
    private Spinner sparepart, supplier;
    private Button tambah, input;

    private RecyclerView recyclerView;
    private DetailPengadaanAdapter adapterDetail;

    private List<Supplier> supplierList;
    private List<Sparepart> sparepartList;
    private List<DetailPengadaan> detailPengadaanList;
    private List<EditText> editTextList = new ArrayList<>();

    private ArrayAdapter<Sparepart> adapterSparepart;
    private ArrayAdapter<Supplier> adapterSupplier;

    private Sparepart selectedSparepart = null;
    private Supplier selectedSupplier = null;

    Boolean edited = false;
    String id = null;

    private OnFragmentInteractionListener mListener;

    public PengadaanInput() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PengadaanInput.
     */
    // TODO: Rename and change types and number of parameters
    public static PengadaanInput newInstance(String param1, String param2) {
        PengadaanInput fragment = new PengadaanInput();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_pengadaan_input, container, false);

        sparepartList = new ArrayList<>();
        detailPengadaanList = new ArrayList<>();
        supplierList = new ArrayList<>();

        jumlah = (EditText) rootview.findViewById(R.id.jumlah);
        satuan = (EditText) rootview.findViewById(R.id.satuan);
        harga = (EditText) rootview.findViewById(R.id.harga);
        sparepart = (Spinner) rootview.findViewById(R.id.spinnerSparepart);
        supplier = (Spinner) rootview.findViewById(R.id.spinnerSupplier);
        tambah = (Button) rootview.findViewById(R.id.tambahSparepart);
        input = (Button) rootview.findViewById(R.id.input);
        harga.setEnabled(false);

        editTextList.add(jumlah);
        editTextList.add(satuan);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_sparepart);

        adapterDetail = new DetailPengadaanAdapter(getContext(), detailPengadaanList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterDetail);

        adapterSparepart = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sparepartList);
        adapterSparepart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sparepart.setAdapter(adapterSparepart);
        loadSparepart();
        sparepart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSparepart = (Sparepart) parent.getSelectedItem();
                harga.setText(selectedSparepart.getHargaBeliSparepart().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterSupplier = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, supplierList);
        adapterSupplier.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplier.setAdapter(adapterSupplier);
        loadSupplier();
        supplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSupplier = (Supplier) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bundle mBundle = getArguments();

        if(mBundle != null) {
            id = mBundle.getString("id");
            loadDataPengadaan(id);
            edited = true;
        }

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDetail(editTextList)) {
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

                if(Integer.parseInt(jumlah.getText().toString()) <= 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Peringatan");
                    alertDialog.setMessage("Jumlah Minimal 1");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }

                String sparepart = selectedSparepart.getKodeSparepart();
                Integer jumlah_sparepart = Integer.parseInt(jumlah.getText().toString());
                String satuan_sparepart = satuan.getText().toString();
                Float harga_sparepart = Float.parseFloat(harga.getText().toString());
                DetailPengadaan detail = new DetailPengadaan(sparepart, jumlah_sparepart, satuan_sparepart, harga_sparepart);
                if(detailPengadaanList.contains(detail)) {
                    Toast.makeText(getContext(), "Data telah terdaftar", Toast.LENGTH_SHORT).show();
                    return;
                }
                detailPengadaanList.add(detail);
                adapterDetail.notifyDataSetChanged();
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData(id);
            }
        });

        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void inputData(final String id_pengadaan) {
        if(checkIsNull()) {
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

        String id_supplier = selectedSupplier.getIdSupplier();
        Float total = 0.0f;

        for(DetailPengadaan det : detailPengadaanList) {
            total += det.getJumlah() * det.getHargaBeli();
        }

        String status = "Belum Dicetak";
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        if(edited == false) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<JsonElement> call = apiService.inputPengadaan(id_supplier, total, status);

            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    JsonObject res = response.body().getAsJsonObject().getAsJsonObject("pengadaan");
                    for(DetailPengadaan det : detailPengadaanList) {
                        det.setIdPengadaan(res.get("id_pengadaan").getAsString());
                    }
                    inputDetail(detailPengadaanList);
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new PengadaanTampil();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        } else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<JsonElement> call = apiService.ubahPengadaan(id_pengadaan, id_supplier, total, status);

            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    for(DetailPengadaan det : detailPengadaanList) {
                        det.setIdPengadaan(id_pengadaan);
                    }
                    inputDetail(detailPengadaanList);
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Edit Gagal", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new PengadaanTampil();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
        }


    }

    private void inputDetail(List<DetailPengadaan> list) {
        String data = new Gson().toJson(list);
        Log.d("data_detail", data);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<DetailPengadaan> call = apiService.inputDetailPengadaan(data);

        call.enqueue(new Callback<DetailPengadaan>() {
            @Override
            public void onResponse(Call<DetailPengadaan> call, Response<DetailPengadaan> response) {
                Log.d("detail", new Gson().toJson(response.body()));
                if(edited == false)
                    Toast.makeText(getContext(), "Input Berhasil", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Edit Berhasil", Toast.LENGTH_SHORT).show();
                Fragment fragment = new PengadaanTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onFailure(Call<DetailPengadaan> call, Throwable t) {
                Log.d("detail", t.getMessage());
                if(edited == false)
                    Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Edit Gagal", Toast.LENGTH_SHORT).show();
                Fragment fragment = new PengadaanTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
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

    private void loadSupplier () {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<Supplier>> call = apiService.getResultSupplier();

        call.enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Call<List<Supplier>> call, Response<List<Supplier>> response) {
                supplierList.addAll(response.body());
                adapterSupplier.notifyDataSetChanged();
                Log.d("supplier", "ini semua");
            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {

            }
        });
    }

    private void loadDataPengadaan(final String id_pengadaan) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<Pengadaan> call = apiService.cariPengadaan(id_pengadaan);

        call.enqueue(new Callback<Pengadaan>() {
            @Override
            public void onResponse(Call<Pengadaan> call, Response<Pengadaan> response) {
                Pengadaan p = response.body();
                Supplier s = p.getSupplier();
                Log.d("supplier", new Gson().toJson(s));

                ArrayAdapter<Supplier> adapSupplier = (ArrayAdapter<Supplier>) supplier.getAdapter();
                Integer supplierPosition = new Integer(adapSupplier.getPosition(s));
                Log.d("supplier", supplierPosition.toString());
                supplier.setSelection(supplierPosition);
                loadDetailPengadaan(id_pengadaan);
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<Pengadaan> call, Throwable t) {
                pd.dismiss();
                Log.d("error_pengadaan", t.getMessage());
            }
        });
    }

    private void loadDetailPengadaan (String id_pengadaan) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<DetailPengadaan>> call = apiService.cariDetailPengadaan(id_pengadaan);

        call.enqueue(new Callback<List<DetailPengadaan>>() {
            @Override
            public void onResponse(Call<List<DetailPengadaan>> call, Response<List<DetailPengadaan>> response) {
                detailPengadaanList.addAll(response.body());
                adapterDetail.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<DetailPengadaan>> call, Throwable t) {

            }
        });


    }

    private Boolean checkIsNull() {
        if(detailPengadaanList.size() == 0)
            return true;

        return false;
    }

    private Boolean checkDetail(List<EditText> editTextList) {
        for (EditText editText : editTextList) {
            if (TextUtils.isEmpty(editText.getText().toString().trim()))
                return true;
        }

        return false;
    }

}
