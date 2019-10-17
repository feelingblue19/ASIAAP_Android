package com.asiaap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import com.asiaap.Model.Login;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginCustomerService.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginCustomerService#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginCustomerService extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public EditText txtUsername;
    public EditText txtPassword;
    public Button btnLogin;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LoginAdmin.OnFragmentInteractionListener mListener;

    private SharedPreferences sp;
    private final String name = "myShared";
    public final int mode = Activity.MODE_PRIVATE;

    public LoginCustomerService() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginAdmin.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginAdmin newInstance(String param1, String param2) {
        LoginAdmin fragment = new LoginAdmin();
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


        sp = this.getActivity().getSharedPreferences(name, mode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.fragment_login_customer, container, false);
        txtUsername= (EditText) rootView.findViewById(R.id.username);
        txtPassword = (EditText) rootView.findViewById(R.id.password);
        btnLogin = (Button) rootView.findViewById(R.id.login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        return rootView;
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

    public void Login() {

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

        sp = getActivity().getSharedPreferences(name, mode);
        String jabatan = new String(sp.getString("jabatan", "Salah"));
        if(jabatan.equals("Admin") || jabatan.equals("Customer Service")) {
            Toast.makeText(getContext(), "Anda sudah Login, silakan Logout terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.show();

        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        final retrofit2.Call<Login> pegawaiCall = apiService.login(username, password);

        pegawaiCall.enqueue(new Callback<Login>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                pd.dismiss();
                if(response.code() == 200) {
                    String jabatan = new String(response.body().getJabatan());
                    String token = response.body().getToken();

                    if(!jabatan.equals("Customer Service")){
                        Toast.makeText(getContext(), "Hanya Login Untuk Customer Service", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("jabatan", jabatan);
                    editor.putString("token", token);
                    editor.apply();

                    Toast.makeText(getContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();

                    Fragment fragment = new HomeCS();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
                else {
                    Toast.makeText(getContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
                Log.e("error", t.getStackTrace().toString());
            }
        });

    }

    public Boolean checkNull() {
        if(TextUtils.isEmpty(txtUsername.getText().toString().trim()) || TextUtils.isEmpty(txtPassword.getText().toString().trim()) )
            return true;
        return false;
    }
}
