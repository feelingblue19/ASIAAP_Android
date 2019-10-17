package com.asiaap.Histori;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.asiaap.Model.Histori;
import com.asiaap.Model.Pengadaan;
import com.asiaap.Pengadaan.PengadaanAdapter;
import com.asiaap.Pengadaan.PengadaanInput;
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
 */
public class HistoriTampil extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private RecyclerView recyclerView;
    private HistoriAdapter adapter;
    private List<Histori> historiList;
    private List<Histori> historiListFull;
    private FloatingActionButton tambah;
    private View rootView;

    public HistoriTampil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareHistori();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        if(rootView == null)
            rootView = inflater.inflate(R.layout.fragment_histori_tampil, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_histori);
        tambah = (FloatingActionButton) rootView.findViewById(R.id.fab);
        historiList = new ArrayList<>();
        historiListFull = new ArrayList<>();
        adapter = new HistoriAdapter(getContext(), historiList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HistoriInput();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return rootView;
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
            adapter.setFilter(historiListFull);
        } else {
            newText = newText.toLowerCase();
            ArrayList<Histori> filteredList = new ArrayList<>();
            for (Histori model : historiListFull) {
                String id = model.getIdHistori().toLowerCase();
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

    private void prepareHistori() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final retrofit2.Call<List<Histori>> call = apiService.getHistori();

        call.enqueue(new Callback<List<Histori>>() {
            @Override
            public void onResponse(Call<List<Histori>> call, Response<List<Histori>> response) {
                if(response.body() != null) {
                    Histori a;
                    for(int i = 0; i < response.body().size(); i++) {
                        a = response.body().get(i);
                        historiList.add(a);
                        historiListFull.add(a);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Histori>> call, Throwable t) {

            }
        });
    }

}
