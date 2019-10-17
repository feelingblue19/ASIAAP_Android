package com.asiaap.Pengadaan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.asiaap.Model.Pengadaan;
import com.asiaap.Model.Supplier;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.asiaap.Supplier.SupplierAdapter;
import com.asiaap.Supplier.SupplierInput;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PengadaanTampil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PengadaanTampil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PengadaanTampil extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private PengadaanAdapter adapter;
    private List<Pengadaan> pengadaanList;
    private List<Pengadaan> pengadaanListFull;
    private FloatingActionButton tambah;

    private OnFragmentInteractionListener mListener;

    public PengadaanTampil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PengadaanTampil.
     */
    // TODO: Rename and change types and number of parameters
    public static PengadaanTampil newInstance(String param1, String param2) {
        PengadaanTampil fragment = new PengadaanTampil();
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
        preparePengadaan();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_pengadaan_tampil, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_pengadaan);
        pengadaanList = new ArrayList<>();
        pengadaanListFull = new ArrayList<>();
        adapter = new PengadaanAdapter(getContext(), pengadaanList, pengadaanListFull);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        tambah = (FloatingActionButton) rootView.findViewById(R.id.fab);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PengadaanInput();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            adapter.setFilter(pengadaanListFull);
        } else {
            newText = newText.toLowerCase();
            ArrayList<Pengadaan> filteredList = new ArrayList<>();
            for (Pengadaan model : pengadaanListFull) {
                String id = model.getIdPengadaan().toLowerCase();
                if ((id.contains(newText))) {
                    filteredList.add(model);
                }
            }
            adapter.setFilter(filteredList);
        }
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
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

    private void preparePengadaan() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final retrofit2.Call<List<Pengadaan>> call = apiService.getResultPengadaan();

        call.enqueue(new Callback<List<Pengadaan>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Pengadaan>> call, Response<List<Pengadaan>> response) {

                if(response.isSuccessful()) {

                    Pengadaan a;
                    for(int i = 0; i < response.body().size(); i++) {
                        a = response.body().get(i);
                        pengadaanList.add(a);
                        pengadaanListFull.add(a);
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    try {
                        Log.d("response_sup", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override
            public void onFailure(retrofit2.Call<List<Pengadaan>> call, Throwable t) {
                Log.e("response_sup", t.getMessage());
            }
        });
    }
}
