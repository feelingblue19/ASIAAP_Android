package com.asiaap.Sparepart;

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

import com.asiaap.Model.Sparepart;
import com.asiaap.Model.TipeKendaraan;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.asiaap.SparepartAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TipeKendaraanAdapter extends RecyclerView.Adapter<TipeKendaraanAdapter.MyViewHolder>{

    private Context mContext;
    private List<TipeKendaraan> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_tipe;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            nama_tipe = (TextView) view.findViewById(R.id.nama_tipe);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }
    }

    public TipeKendaraanAdapter(Context mContext, List<TipeKendaraan> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public TipeKendaraanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tipekendaraan_card, parent, false);

        return new TipeKendaraanAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TipeKendaraanAdapter.MyViewHolder holder, int position) {
        TipeKendaraan tipeKendaraan = list.get(position);
        holder.nama_tipe.setText("Tipe Kendaraan : " + tipeKendaraan.getNamaTipe());

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

    private void showPopupMenu(View view, final TipeKendaraanAdapter.MyViewHolder holder) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu2, popup.getMenu());
        popup.setOnMenuItemClickListener(new TipeKendaraanAdapter.MyMenuItemClickListener(holder));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        TipeKendaraanAdapter.MyViewHolder holder;

        public MyMenuItemClickListener(final TipeKendaraanAdapter.MyViewHolder holder) {
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

    private void delete(TipeKendaraanAdapter.MyViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(position != RecyclerView.NO_POSITION) {
            list.remove(holder.getAdapterPosition());
            notifyItemRemoved(position);
            notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
        }

    }
}
