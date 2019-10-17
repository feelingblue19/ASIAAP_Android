package com.asiaap.Supplier;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asiaap.HomeAdminFragment;
import com.asiaap.Model.Login;
import com.asiaap.Model.Supplier;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SupplierInput.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SupplierInput#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupplierInput extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditText txtNamaSupplier;
    public EditText txtAlamatSupplier;
    public EditText txtNoTelpSupplier;
    public EditText txtNamaSales;
    public EditText txtNoTelpSales;
    public Button Input;
    private List<EditText> editTextList = new ArrayList<>();

    Boolean edited = false;
    String id = null;

    private OnFragmentInteractionListener mListener;

    public SupplierInput() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SupplierInput.
     */
    // TODO: Rename and change types and number of parameters
    public static SupplierInput newInstance(String param1, String param2) {
        SupplierInput fragment = new SupplierInput();
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
        View rootview =  inflater.inflate(R.layout.fragment_supplier_input, container, false);

        txtNamaSupplier = (EditText) rootview.findViewById(R.id.txtNamaSupplier);
        txtAlamatSupplier = (EditText) rootview.findViewById(R.id.txtAlamatSuppliier);
        txtNoTelpSupplier = (EditText) rootview.findViewById(R.id.txtNoTelpSupplier);
        txtNamaSales= (EditText) rootview.findViewById(R.id.txtNamaSales);
        txtNoTelpSales= (EditText) rootview.findViewById(R.id.txtNoTelpSales);
        Input = (Button) rootview.findViewById(R.id.input);

        editTextList.add(txtNamaSupplier);
        editTextList.add(txtAlamatSupplier);
        editTextList.add(txtNoTelpSupplier);
        editTextList.add(txtNamaSales);
        editTextList.add(txtNoTelpSales);

        Bundle mBundle = getArguments();

        if(mBundle != null) {
            id = mBundle.getString("id_supplier");
            loadData(id);
            edited = true;
        }

        Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    inputData(edited, id);

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

    private void inputData(final Boolean edited, String id_supplier) {

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

        String nama_supplier = txtNamaSupplier.getText().toString();
        String alamat_supplier = txtAlamatSupplier.getText().toString();
        String no_telp_supplier = txtNoTelpSupplier.getText().toString();
        String nama_sales = txtNamaSales.getText().toString();
        String no_telp_sales = txtNoTelpSales.getText().toString();

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        if(edited == false) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            final retrofit2.Call<Supplier> supplierCall = apiService.inputSupplier(nama_supplier, alamat_supplier, no_telp_supplier,
                    nama_sales, no_telp_sales);

            supplierCall.enqueue(new Callback<Supplier>() {
                @Override
                public void onResponse(Call<Supplier> call, Response<Supplier> response) {
                    pd.dismiss();
                    if(response.code() == 200) {
                        Toast.makeText(getContext(), "Input Berhasil", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();

                    Fragment fragment = new SupplierTampil();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }

                @Override
                public void onFailure(Call<Supplier> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Input Gagal", Toast.LENGTH_SHORT).show();
                    Log.e("error", t.getMessage());
                    Log.d("error_supplier", t.getMessage());
                    Fragment fragment = new SupplierTampil();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }


            });
        } else {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            final Call<Supplier> supplierCall = apiService.ubahSupplier(id_supplier, nama_supplier, alamat_supplier, no_telp_supplier,
                                                nama_sales, no_telp_sales);

            supplierCall.enqueue(new Callback<Supplier>() {
                @Override
                public void onResponse(Call<Supplier> call, Response<Supplier> response) {
                    pd.dismiss();
                    if(response.code() == 200) {
                        Toast.makeText(getContext(), "Edit Berhasil", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Edit Gagal", Toast.LENGTH_SHORT).show();
                    }

                    Fragment fragment = new SupplierTampil();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                @Override
                public void onFailure(Call<Supplier> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Edit Gagal", Toast.LENGTH_SHORT).show();

                    Log.d("error_supplier", t.getMessage());
                    Fragment fragment = new SupplierTampil();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }

    private void loadData(String id_supplier) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<Supplier> supplierCall = apiService.cariSupplier(id_supplier);

        supplierCall.enqueue(new Callback<Supplier>() {
            @Override
            public void onResponse(Call<Supplier> call, Response<Supplier> response) {
                Supplier sup = response.body();
                txtNamaSupplier.setText(sup.getNamaSupplier());
                txtAlamatSupplier.setText(sup.getAlamatSupplier());
                txtNoTelpSupplier.setText(sup.getNoTelpSupplier());
                txtNamaSales.setText(sup.getNamaSales());
                txtNoTelpSales.setText(sup.getNoTelpSales());
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<Supplier> call, Throwable t) {

            }
        });
    }

    public Boolean checkIsNull(List<EditText> editTextList) {
        for (EditText editText : editTextList) {
            if (TextUtils.isEmpty(editText.getText().toString().trim()))
                return true;
        }
        return false;
    }
}
