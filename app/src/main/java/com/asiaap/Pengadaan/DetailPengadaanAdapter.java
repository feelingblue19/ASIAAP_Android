package com.asiaap.Pengadaan;

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

import com.asiaap.Model.DetailPengadaan;
import com.asiaap.Model.Pengadaan;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPengadaanAdapter extends RecyclerView.Adapter<DetailPengadaanAdapter.MyViewHolder>{

    private Context mContext;
    private List<DetailPengadaan> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sparepart, jumlah, satuan, harga;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            sparepart = (TextView) view.findViewById(R.id.sparepart);
            jumlah = (TextView) view.findViewById(R.id.jumlah);
            satuan = (TextView) view.findViewById(R.id.satuan);
            harga = (TextView) view.findViewById(R.id.harga);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public DetailPengadaanAdapter(Context mContext, List<DetailPengadaan> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public DetailPengadaanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_pengadaan_card, parent, false);

        return new DetailPengadaanAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailPengadaanAdapter.MyViewHolder holder, int position) {
        DetailPengadaan detail = list.get(position);
        holder.sparepart.setText("Sparepart : " + detail.getKodeSparepart());
        holder.jumlah.setText("Jumlah : " + detail.getJumlah());
        holder.satuan.setText("Satuan : " + detail.getSatuan());
        holder.harga.setText("Harga : Rp " + detail.getHargaBeli());


        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void showPopupMenu(View view, final DetailPengadaanAdapter.MyViewHolder holder) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu2, popup.getMenu());
        popup.setOnMenuItemClickListener(new DetailPengadaanAdapter.MyMenuItemClickListener(holder));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        DetailPengadaanAdapter.MyViewHolder holder;
        public MyMenuItemClickListener(final DetailPengadaanAdapter.MyViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete:
                    delete(holder);
                    return true;
                default:
            }
            return false;
        }
    }

    private void delete(DetailPengadaanAdapter.MyViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(position != RecyclerView.NO_POSITION) {
            list.remove(holder.getAdapterPosition());
            notifyItemRemoved(position);
            notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
        }
    }
}
