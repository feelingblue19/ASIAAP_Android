package com.asiaap.Sparepart;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.PathUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.asiaap.Model.Sparepart;
import com.asiaap.Model.Supplier;
import com.asiaap.Model.TipeKendaraan;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.asiaap.Supplier.SupplierTampil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SparepartInput.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SparepartInput#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SparepartInput extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Spinner letak, ruang, kecocokan;
    public EditText nama, merk, tipe, harga_jual, harga_beli, stok, minstok;
    public Button tambahGambar, tambahKecocokan, input;
    public ImageView gambar;
    private List<TipeKendaraan> responseList, kecocokanList;
    private ArrayAdapter<TipeKendaraan> adapter3;
    private TipeKendaraan selected = null;
    private String ruangInput, letakInput, imagePath = null;
    private List<EditText> editTextList = new ArrayList<>();

    Boolean edited = false;
    String id = null;


    private RecyclerView recyclerView;
    private TipeKendaraanAdapter adapterTipe;

    private OnFragmentInteractionListener mListener;

    public SparepartInput() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SparepartInput.
     */
    // TODO: Rename and change types and number of parameters
    public static SparepartInput newInstance(String param1, String param2) {
        SparepartInput fragment = new SparepartInput();
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
        responseList = new ArrayList<>();
        kecocokanList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_sparepart_input, container, false);

        Bundle mBundle = getArguments();

        if(mBundle != null) {
            id = mBundle.getString("id");
            loadData(id);
            edited = true;
        }

        letak = (Spinner) rootview.findViewById(R.id.spinnerLetak);
        ruang = (Spinner) rootview.findViewById(R.id.spinnerRuang);
        nama  = (EditText) rootview.findViewById(R.id.txtNamaSparepart);
        merk  = (EditText) rootview.findViewById(R.id.txtMerkSparepart);
        tipe = (EditText) rootview.findViewById(R.id.txtTipeSparepart);
        harga_beli = (EditText) rootview.findViewById(R.id.txtHargaBeliSparepart);
        harga_jual = (EditText) rootview.findViewById(R.id.txtHargaJualSparepart);
        stok = (EditText) rootview.findViewById(R.id.txtStokSparepart);
        minstok = (EditText) rootview.findViewById(R.id.txtMinStokSparepart);
        kecocokan = (Spinner) rootview.findViewById(R.id.spinnerTipe);
        tambahKecocokan = (Button) rootview.findViewById(R.id.btnTambahTipe);
        tambahGambar = (Button) rootview.findViewById(R.id.btnGambarSparepart);
        gambar = (ImageView) rootview.findViewById(R.id.ivGambarSparepart);
        input = (Button) rootview.findViewById(R.id.input);

        editTextList.add(nama);
        editTextList.add(merk);
        editTextList.add(tipe);
        editTextList.add(harga_beli);
        editTextList.add(harga_jual);
        editTextList.add(stok);
        editTextList.add(minstok);


        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_tipe);

        adapterTipe = new TipeKendaraanAdapter(getContext(), kecocokanList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterTipe);

        List<String> letakList = new ArrayList<String>();
        letakList.add("DPN");
        letakList.add("TGH");
        letakList.add("BLK");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, letakList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        letak.setAdapter(adapter);
        letak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                letakInput = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> ruangList = new ArrayList<String>();
        ruangList.add("KACA");
        ruangList.add("DUS");
        ruangList.add("BAN");
        ruangList.add("KAYU");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ruangList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ruang.setAdapter(adapter2);
        ruang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ruangInput = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, responseList);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kecocokan.setAdapter(adapter3);
        loadTipe();
        kecocokan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = (TipeKendaraan)parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tambahKecocokan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kecocokanList.contains(selected)) {
                    Toast.makeText(getContext(), "Data telah terdaftar", Toast.LENGTH_SHORT).show();
                    return;
                }

                kecocokanList.add(selected);
                adapterTipe.notifyDataSetChanged();
            }
        });

        tambahGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
                } else {
                    startGallery();
                }
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

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getApplicationContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                imagePath = getPathFromURI(returnUri);
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gambar.setImageBitmap(bitmapImage);
            }
        }
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

    private void loadTipe() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<TipeKendaraan>> call = apiService.getTipeKendaraan();

        call.enqueue(new Callback<List<TipeKendaraan>>() {
            @Override
            public void onResponse(Call<List<TipeKendaraan>> call, Response<List<TipeKendaraan>> response) {
                responseList.addAll(response.body());
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<TipeKendaraan>> call, Throwable t) {

            }
        });
    }

    private void inputData(String id_sparepart) {

        if(checkIsNull(editTextList)) {
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

        String penempatan_sparepart = letakInput + "-" + ruangInput + "-";
        String nama_sparepart = nama.getText().toString();
        String merk_sparepart = merk.getText().toString();
        String tipe_sparepart = tipe.getText().toString();
        String harga_jual_sparepart = harga_jual.getText().toString();
        String harga_beli_sparepart = harga_beli.getText().toString();
        String stok_sparepart = stok.getText().toString();
        String min_stok = minstok.getText().toString();
        List<String> tipeList = new ArrayList<>();
        for(int i = 0; i < kecocokanList.size(); i++) {
            tipeList.add(kecocokanList.get(i).getIdTipe());
        }

        if(cekHarga(Float.parseFloat(harga_jual_sparepart), Float.parseFloat(harga_beli_sparepart))) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Peringatan");
            alertDialog.setMessage("Harga Jual tidak Boleh Kurang Dari Harga Beli!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        File file = null;
        MultipartBody.Part body = null;
        if(imagePath != null) {
            file = new File(imagePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("gambar_sparepart", file.getName(), requestBody);
        }

        String id_tipe = new Gson().toJson(tipeList);

        RequestBody penempatan = RequestBody.create(MediaType.parse("text/plain"), penempatan_sparepart);
        RequestBody nama = RequestBody.create(MediaType.parse("text/plain"), nama_sparepart);
        RequestBody merk = RequestBody.create(MediaType.parse("text/plain"), merk_sparepart);
        RequestBody tipe = RequestBody.create(MediaType.parse("text/plain"), tipe_sparepart);
        RequestBody harga_jual = RequestBody.create(MediaType.parse("text/plain"), harga_jual_sparepart);
        RequestBody harga_beli = RequestBody.create(MediaType.parse("text/plain"), harga_beli_sparepart);
        RequestBody stok = RequestBody.create(MediaType.parse("text/plain"), stok_sparepart);
        RequestBody minStok = RequestBody.create(MediaType.parse("text/plain"), min_stok);
        RequestBody kendaraan = RequestBody.create(MediaType.parse("text/plain"), id_tipe);

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        if (edited == false) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<RequestBody> call = apiService.inputSparepart(nama, penempatan, merk, tipe, harga_beli, harga_jual, stok, minStok, body, kendaraan);

            call.enqueue(new Callback<RequestBody>() {
                @Override
                public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                    pd.dismiss();
                    if(response.code() == 200)
                        Toast.makeText(getContext(), "Input Berhasil!", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getContext(), "Input gagal", Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.d("gagal", jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Fragment fragment = new SparepartTampil();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                @Override
                public void onFailure(Call<RequestBody> call, Throwable t) {
                    pd.dismiss();
                    Log.d("input_sparepart", t.getMessage());

                    if(t.getMessage().equals("Failed to invoke public okhttp3.RequestBody() with no args")) {
                        Toast.makeText(getContext(), "Input Berhasil!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Input Gagal!", Toast.LENGTH_SHORT).show();
                    }
                    Fragment fragment = new SparepartTampil();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
        } else {
            Call<RequestBody> call = null;
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            if(imagePath != null) {
                call = apiService.ubahSparepart(id_sparepart, nama, penempatan, merk, tipe, harga_beli, harga_jual, stok, minStok, body, kendaraan);
            } else
                call = apiService.ubahSparepart2(id_sparepart, nama_sparepart, penempatan_sparepart, merk_sparepart, tipe_sparepart, harga_beli_sparepart, harga_jual_sparepart, stok_sparepart, min_stok, id_tipe);

            call.enqueue(new Callback<RequestBody>() {
                @Override
                public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                    pd.dismiss();
                    if(response.code() == 200)
                        Toast.makeText(getContext(), "Edit Berhasil!", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getContext(), "Edit gagal", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Log.d("gagal", jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Fragment fragment = new SparepartTampil();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                @Override
                public void onFailure(Call<RequestBody> call, Throwable t) {
                    pd.dismiss();
                    Log.d("input_sparepart", t.getMessage());

                    if(t.getMessage().equals("Failed to invoke public okhttp3.RequestBody() with no args")) {
                        Toast.makeText(getContext(), "Edit Berhasil!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Edit Gagal!", Toast.LENGTH_SHORT).show();
                    }
                    Fragment fragment = new SparepartTampil();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
        }


    }

    private void loadData(String id_sparepart) {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<Sparepart> call = apiService.cariSparepart(id_sparepart);

        call.enqueue(new Callback<Sparepart>() {
            @Override
            public void onResponse(Call<Sparepart> call, Response<Sparepart> response) {
                pd.dismiss();
                Sparepart spr = response.body();
                Log.d("sparepart", new Gson().toJson(response.body()));

                String penempatan = spr.getPenempatanSparepart();
                String ruang1;
                if(penempatan.length() == 11) {
                    ruang1 = penempatan.substring(4, 8);
                }
                else
                {
                    ruang1 = penempatan.substring(4, 7);
                }

                ArrayAdapter adapLetak = (ArrayAdapter) letak.getAdapter();
                int letakPosition = adapLetak.getPosition(penempatan.substring(0, 3));
                letak.setSelection(letakPosition);

                ArrayAdapter adapRuang = (ArrayAdapter) ruang.getAdapter();
                int ruangPosition = adapRuang.getPosition(ruang1);
                ruang.setSelection(ruangPosition);

                nama.setText(spr.getNamaSparepart());
                merk.setText(spr.getMerkSparepart());
                tipe.setText(spr.getTipeSparepart());
                harga_beli.setText(spr.getHargaBeliSparepart().toString());
                harga_jual.setText(spr.getHargaJualSparepart().toString());
                stok.setText(String.valueOf(spr.getStokSparepart()));
                minstok.setText(String.valueOf(spr.getMinStok()));
                kecocokanList.addAll(spr.getTipeKendaraan());
                adapterTipe.notifyDataSetChanged();
                Glide.with(getContext()).load(spr.getGambarSparepart()).into(gambar);
            }

            @Override
            public void onFailure(Call<Sparepart> call, Throwable t) {

            }
        });
    }

    public Boolean checkIsNull(List<EditText> editTextList) {
        for (EditText editText : editTextList) {
            if (TextUtils.isEmpty(editText.getText().toString().trim()))
                return true;
        }

        if(kecocokanList.size() == 0)
            return true;

        if(gambar.getDrawable() == null)
            return true;

        return false;
    }

    public Boolean cekHarga(float harga_jual, float harga_beli) {
        if(harga_beli > harga_jual)
            return true;

        return false;
    }
}
