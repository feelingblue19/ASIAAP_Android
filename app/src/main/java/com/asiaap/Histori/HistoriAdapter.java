package com.asiaap.Histori;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.asiaap.Model.Histori;
import com.asiaap.Model.Pengadaan;
import com.asiaap.Pengadaan.PengadaanAdapter;
import com.asiaap.Pengadaan.PengadaanInput;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoriAdapter extends RecyclerView.Adapter<HistoriAdapter.MyViewHolder> {

    private Context mContext;
    private List<Histori> list;

    public void setFilter(List<Histori> historiList) {
        list.clear();
        list.addAll(historiList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id_histori, tanggal, kode_sparepart, jumlah, keterangan;

        public MyViewHolder(View view) {
            super(view);
            id_histori = (TextView) view.findViewById(R.id.id_histori);
            tanggal = (TextView) view.findViewById(R.id.tanggal);
            kode_sparepart = (TextView) view.findViewById(R.id.kode_sparepart);
            jumlah = (TextView) view.findViewById(R.id.jumlah);
            keterangan = (TextView) view.findViewById(R.id.keterangan);
        }
    }

    public HistoriAdapter(Context mContext, List<Histori> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoriAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.histori_card, parent, false);

        return new HistoriAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoriAdapter.MyViewHolder holder, int position) {
        Histori histori = list.get(position);
        holder.id_histori.setText("ID Histori : " + histori.getIdHistori());
        holder.tanggal.setText("Tanggal : " + histori.getTanggalHistori());
        holder.kode_sparepart.setText("Kode Sparepart : " + histori.getKodeSparepart());
        holder.jumlah.setText("Jumlah : " + histori.getJumlahHistori());
        holder.keterangan.setText("Keterangan : " + histori.getKeteranganHistori());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
