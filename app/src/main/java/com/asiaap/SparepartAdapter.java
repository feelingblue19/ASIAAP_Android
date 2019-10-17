package com.asiaap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiaap.Model.Sparepart;
import com.bumptech.glide.Glide;

import java.util.List;

public class SparepartAdapter extends RecyclerView.Adapter<SparepartAdapter.MyViewHolder> {

    private Context mContext;
    private List<Sparepart> sparepartList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, stok, harga;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            stok = (TextView) view.findViewById(R.id.stok);
            harga = (TextView) view.findViewById(R.id.harga);
        }
    }

    public SparepartAdapter(Context mContext, List<Sparepart> sparepartList) {
        this.mContext = mContext;
        this.sparepartList = sparepartList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sparepart_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Sparepart sparepart = sparepartList.get(position);
        holder.nama.setText(sparepart.getNamaSparepart());
        holder.harga.setText("Harga: Rp "+sparepart.getHargaJualSparepart());
        holder.stok.setText("Stok: "+sparepart.getStokSparepart());

        // loading album cover using Glide library
        Log.d("gambar", sparepart.getGambarSparepart());
        Glide.with(mContext).load(sparepart.getGambarSparepart()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return sparepartList.size();
    }
}
