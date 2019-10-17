package com.asiaap.Histori;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.asiaap.Model.DetailPenjualanSparepart;
import com.asiaap.Model.Histori;
import com.asiaap.Model.Sparepart;
import com.asiaap.PenjualanTampil;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoriInput.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoriInput#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoriInput extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner sparepart;
    private EditText jumlah;
    private Button input;

    private List<Sparepart> sparepartList;
    private ArrayAdapter<Sparepart> adapterSparepart;
    private Sparepart selectedSparepart = null;

    public HistoriInput() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoriInput.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoriInput newInstance(String param1, String param2) {
        HistoriInput fragment = new HistoriInput();
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
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_histori_input, container, false);
        sparepart = rootview.findViewById(R.id.sparepart);
        jumlah = rootview.findViewById(R.id.jumlah);
        input = rootview.findViewById(R.id.btnInput);
        sparepartList = new ArrayList<>();

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

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
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

     private void inputData() {

         if(checkIsNull(jumlah)) {
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

         final ProgressDialog pd = new ProgressDialog(getContext());
         pd.setMessage("Loading...");
         pd.show();

        List<Histori> list = new ArrayList<>();

        String sparepart = selectedSparepart.getKodeSparepart();
        String jml = jumlah.getText().toString();
        String keterangan = "Masuk";

        Histori his = new Histori();
        his.setKodeSparepart(sparepart);
        his.setJumlahHistori(Integer.parseInt(jml));
        his.setKeteranganHistori(keterangan);

        list.add(his);

        String data = new Gson().toJson(list);

         ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
         final Call<Histori> call = apiService.inputHistori(data);

         call.enqueue(new Callback<Histori>() {
             @Override
             public void onResponse(Call<Histori> call, Response<Histori> response) {
                 pd.dismiss();
                 if(response.isSuccessful()) {
                     Toast.makeText(getContext(), "Input Berhasil", Toast.LENGTH_SHORT).show();
                 } else
                     Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();

                 Fragment fragment = new HistoriTampil();
                 FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.frame, fragment);
                 fragmentTransaction.addToBackStack(null);
                 fragmentTransaction.commit();
             }

             @Override
             public void onFailure(Call<Histori> call, Throwable t) {
                 pd.dismiss();

                 Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();
                 Fragment fragment = new PenjualanTampil();
                 FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.frame, fragment);
                 fragmentTransaction.addToBackStack(null);
                 fragmentTransaction.commit();

             }
         });
     }

     private Boolean checkIsNull(EditText jumlah) {
         if (TextUtils.isEmpty(jumlah.getText().toString().trim()))
             return true;

         return false;
     }
}
