package com.asiaap;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.asiaap.Histori.HistoriTampil;
import com.asiaap.Model.Sparepart;
import com.asiaap.Model.TipeKendaraan;
import com.asiaap.Pengadaan.PengadaanInput;
import com.asiaap.Pengadaan.PengadaanTampil;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.asiaap.Sparepart.SparepartTampil;
import com.asiaap.Supplier.SupplierTampil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeAdminFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeAdminFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ImageButton btnSparepart;
    public ImageButton btnSupplier;
    public ImageButton btnPengadaan;
    public ImageButton btnHistori;
    public Button btnLogout;
    private ArrayList<Sparepart> spareparts = new ArrayList<>();

    private SharedPreferences sp;
    private final String name = "myShared";
    public final int mode = Activity.MODE_PRIVATE;

    private OnFragmentInteractionListener mListener;

    public HomeAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeAdminFragment newInstance(String param1, String param2) {
        HomeAdminFragment fragment = new HomeAdminFragment();
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
        sp = getActivity().getSharedPreferences(name, mode);
        notifikasiSpareparts();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_home_admin, container, false);
        btnPengadaan = (ImageButton) rootview.findViewById(R.id.btnPengadaan);
        btnSparepart= (ImageButton) rootview.findViewById(R.id.btnSparepart);
        btnSupplier = (ImageButton) rootview.findViewById(R.id.btnSupplier);
        btnHistori = (ImageButton) rootview.findViewById(R.id.btnHistori);
        btnLogout = (Button) rootview.findViewById(R.id.btnLogout);

        btnSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SupplierTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnSparepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SparepartTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnPengadaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PengadaanTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnHistori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HistoriTampil();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                final Call<ResponseBody> call = apiService.logout();

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(getContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("jabatan", "salah");
                        editor.putString("token", "salah");
                        editor.apply();
                        Fragment fragment = new HomeFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
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

    private void notifikasiSpareparts() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final retrofit2.Call<List<Sparepart>> sparepartCall = apiService.getResult();

        sparepartCall.enqueue(new Callback<List<Sparepart>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Sparepart>> call, Response<List<Sparepart>> response) {
                if(response.body() != null) {
                    Sparepart a;
                    for(int i = 0; i < response.body().size(); i++) {
                        a = response.body().get(i);
                        spareparts.add(a);
                    }
                    notifKurangDariMinStok(spareparts);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<Sparepart>> call, Throwable t) {
                Log.e("response", t.getMessage());
            }
        });
    }

    private void notifKurangDariMinStok(ArrayList<Sparepart> spareparts) {
        for(int i=0; i<spareparts.size(); i++){
            Sparepart sparepart = spareparts.get(i);
            if(sparepart.getStokSparepart() < sparepart.getMinStok())
                makeNotifikasi(sparepart);
        }
    }

    private void makeNotifikasi(Sparepart sparepart) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TEST";
            String description = "OKE";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "1")
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setContentTitle("Stok Sparepart Kurang Dari Minimal Stok")
                .setContentText("Harap Segera Melakukan Restock Sparepart")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(getContext());

        notificationManager1.notify(0, builder.build());
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
}
