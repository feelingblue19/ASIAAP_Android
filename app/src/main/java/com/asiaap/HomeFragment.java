package com.asiaap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.asiaap.Model.Sparepart;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private SparepartAdapter adapter;
    private List<Sparepart> sparepartList;
    private Spinner spinner;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);



        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        sparepartList = new ArrayList<>();
        adapter = new SparepartAdapter(getContext(), sparepartList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareAlbums();
        spinner = rootView.findViewById(R.id.sort);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.sort_type, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1) {
                    sortHargaTermurah();
                } else if(position == 2) {
                    sortHargaTermahal();
                } else if(position == 3) {
                    sortStokTerendah();
                } else if(position == 4) {
                    sortStokTertinggi();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void prepareAlbums() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final retrofit2.Call<List<Sparepart>> sparepartCall = apiService.getResult();

        sparepartCall.enqueue(new Callback<List<Sparepart>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Sparepart>> call, Response<List<Sparepart>> response) {
                Log.d("response_Error", new Gson().toJson(response));
                if(response.body() != null) {
                    Log.d("response_Error", new Gson().toJson(response.body()));
                    Sparepart a;
                    adapter.notifyDataSetChanged();
                    for(int i = 0; i < response.body().size(); i++) {
                        a = response.body().get(i);
                        sparepartList.add(a);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<Sparepart>> call, Throwable t) {
                Log.e("response_error", t.getMessage());
            }
        });

    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void sortHargaTermurah() {
        Collections.sort(sparepartList, new Comparator<Sparepart>() {
            @Override
            public int compare(Sparepart o1, Sparepart o2) {
                return Double.valueOf(o1.getHargaJualSparepart()).compareTo(Double.valueOf(o2.getHargaJualSparepart()));
            }
        });
    }

    private void sortHargaTermahal() {
        Collections.sort(sparepartList, new Comparator<Sparepart>() {
            @Override
            public int compare(Sparepart o1, Sparepart o2) {
                return Double.valueOf(o2.getHargaJualSparepart()).compareTo(Double.valueOf(o1.getHargaJualSparepart()));
            }
        });
    }

    private void sortStokTerendah() {
        Collections.sort(sparepartList, new Comparator<Sparepart>() {
            @Override
            public int compare(Sparepart o1, Sparepart o2) {
                return Integer.valueOf(o1.getStokSparepart()).compareTo(Integer.valueOf(o2.getStokSparepart()));
            }
        });
    }

    private void sortStokTertinggi() {
        Collections.sort(sparepartList, new Comparator<Sparepart>() {
            @Override
            public int compare(Sparepart o1, Sparepart o2) {
                return Integer.valueOf(o2.getStokSparepart()).compareTo(Integer.valueOf(o1.getStokSparepart()));
            }
        });
    }
}
