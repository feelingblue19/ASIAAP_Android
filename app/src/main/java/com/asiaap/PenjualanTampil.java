package com.asiaap;

import android.net.Uri;
import android.os.Bundle;
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

import com.asiaap.Model.Penjualan;
import com.asiaap.PenjualanService.PenjualanServiceInput;
import com.asiaap.PenjualanServiceSparepart.PenjualanServiceSparepart;
import com.asiaap.PenjualanSparepart.PenjulanSparepartInput;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PenjualanTampil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PenjualanTampil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PenjualanTampil extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private PenjualanAdapter adapter;
    private List<Penjualan> penjualanList;
    private List<Penjualan> penjualanListFull;

    SpeedDialView speedDialView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PenjualanTampil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PenjualanTampil.
     */
    // TODO: Rename and change types and number of parameters
    public static PenjualanTampil newInstance(String param1, String param2) {
        PenjualanTampil fragment = new PenjualanTampil();
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
        preparePenjualan();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootview = inflater.inflate(R.layout.fragment_home_customer, container, false);
        speedDialView = (SpeedDialView) rootview.findViewById(R.id.speedDial);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_penjualan);
        penjualanList = new ArrayList<>();
        penjualanListFull = new ArrayList<>();
        adapter = new PenjualanAdapter(getContext(), penjualanList, penjualanListFull);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_service, R.drawable.ic_add)
                        .setLabel(getString(R.string.service))
                        .create()
        );

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_sparepart, R.drawable.ic_add)
                        .setLabel(getString(R.string.sparepart))
                        .create()
        );

        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_service_sparepart, R.drawable.ic_add)
                        .setLabel(getString(R.string.servicesparepart))
                        .create()
        );

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_service:
                        Fragment fragment = new PenjualanServiceInput();
                        displaySelectedFragment(fragment);
                        return false;
                    case R.id.fab_sparepart:
                        Fragment fragment1 = new PenjulanSparepartInput();
                        displaySelectedFragment(fragment1);
                        return false;
                    case R.id.fab_service_sparepart:
                        Fragment fragment2 = new PenjualanServiceSparepart();
                        displaySelectedFragment(fragment2);
                        return false;
                    default:
                        return false;
                }
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
            adapter.setFilter(penjualanListFull);
        } else {
            newText = newText.toLowerCase();
            ArrayList<Penjualan> filteredList = new ArrayList<>();
            for (Penjualan model : penjualanListFull) {
                String id = model.getNoTransaksi().toLowerCase();
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
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
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

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void preparePenjualan() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<List<Penjualan>> call = apiService.getResultPenjualan();

        call.enqueue(new Callback<List<Penjualan>>() {
            @Override
            public void onResponse(Call<List<Penjualan>> call, Response<List<Penjualan>> response) {
                if(response.body() != null) {
                    Penjualan a;
                    for(int i = 0; i < response.body().size(); i++) {
                        a = response.body().get(i);
                        penjualanList.add(a);
                        penjualanListFull .add(a);
                        adapter.notifyDataSetChanged();
                    }

                }
                else {
                    Log.d("masuk", "masuk");
                }
            }

            @Override
            public void onFailure(Call<List<Penjualan>> call, Throwable t) {
                Log.d("masuk", t.getMessage());
            }
        });
    }

}
