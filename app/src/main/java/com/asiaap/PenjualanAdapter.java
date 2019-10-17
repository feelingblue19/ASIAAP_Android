package com.asiaap;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.asiaap.Model.Penjualan;
import com.asiaap.Model.Sparepart;
import com.asiaap.PenjualanService.PenjualanServiceInput;
import com.asiaap.PenjualanServiceSparepart.PenjualanServiceSparepart;
import com.asiaap.PenjualanSparepart.PenjulanSparepartInput;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenjualanAdapter extends RecyclerView.Adapter<PenjualanAdapter.MyViewHolder> {
    private Context mContext;
    private List<Penjualan> penjualanList;
    private List<Penjualan> penjualanListFull;


    public void setFilter(List<Penjualan> penjualanArrayList) {
        penjualanList.clear();
        penjualanList.addAll(penjualanArrayList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView no_transaksi, tanggal, subtotal, status;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            no_transaksi = (TextView) view.findViewById(R.id.no_transaksi);
            tanggal = (TextView) view.findViewById(R.id.tanggal);
            subtotal = (TextView) view.findViewById(R.id.subtotal);
            status = (TextView) view.findViewById(R.id.status);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public PenjualanAdapter(Context mContext, List<Penjualan> penjualanList, List<Penjualan> penjualanListFull) {
        this.mContext = mContext;
        this.penjualanList = penjualanList;
        this.penjualanListFull = penjualanListFull;
    }

    @NonNull
    @Override
    public PenjualanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.penjualan_card, parent, false);

        return new PenjualanAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PenjualanAdapter.MyViewHolder holder, int position) {
        Penjualan penjualan = penjualanList.get(position);
        holder.no_transaksi.setText("No Transaksi : " + penjualan.getNoTransaksi());
        holder.tanggal.setText("Tanggal : " + penjualan.getTanggalTransaksi());
        holder.subtotal.setText("Subtotal : Rp " + penjualan.getSubtotalTransaksi());
        holder.status.setText("Status : " + penjualan.getStatusTransaksi());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return penjualanList.size();
    }

    private void showPopupMenu(View view, final PenjualanAdapter.MyViewHolder holder) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PenjualanAdapter.MyMenuItemClickListener(holder));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        PenjualanAdapter.MyViewHolder holder;

        public MyMenuItemClickListener(final PenjualanAdapter.MyViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.edit:
                    edit(holder);
                    return true;
                case R.id.delete:
                    delete(holder);
                    return true;
                default:
            }
            return false;
        }
    }

    private void delete(final PenjualanAdapter.MyViewHolder holder) {
        new AlertDialog.Builder(mContext)
                .setTitle("Hapus")
                .setMessage("Apakah Anda Yakin?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        String status = new String(holder.status.getText().toString().substring(9));

                        if(status.equals("Selesai")){
                            Toast.makeText(mContext, "Transaksi tidak dapat dihapus karena sudah selesai", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String id = holder.no_transaksi.getText().toString().substring(15);
                        Log.d("id_penjualan", id);


                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        final Call<Penjualan> sparepartCall = apiService.hapusPenjualan(id);

                        sparepartCall.enqueue(new Callback<Penjualan>() {
                            @Override
                            public void onResponse(Call<Penjualan> call, Response<Penjualan> response) {
                                if(response.code() == 200)
                                {
                                    int position = holder.getAdapterPosition();
                                    if(position != RecyclerView.NO_POSITION) {
                                        penjualanList.remove(holder.getAdapterPosition());
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(holder.getAdapterPosition(), penjualanList.size());
                                    }
                                    penjualanListFull.clear();
                                    penjualanListFull.addAll(penjualanList);
                                    Toast.makeText(mContext, "Delete Berhasil", Toast.LENGTH_SHORT).show();
                                }

                                else
                                {
                                    Log.d("log_penjualan", new Gson().toJson(response));
                                    Toast.makeText(mContext, "Delete Gagal", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Call<Penjualan> call, Throwable t) {

                            }
                        });
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    private void edit (PenjualanAdapter.MyViewHolder holder) {
        String id = holder.no_transaksi.getText().toString().substring(15);

        String jenis_transaksi = new String(id.substring(0, 2));

        String jenis = new String(holder.status.getText().toString().substring(9));

        if(jenis.equals("Selesai")) {
            Toast.makeText(mContext, "Transaksi Sudah Selesai!", Toast.LENGTH_SHORT).show();
            return;
        }



        Fragment fragment = null;

        Log.d("cek_jenis", id);


        if(jenis_transaksi.equals("SP"))
            fragment = new PenjulanSparepartInput();
        else if(jenis_transaksi.equals("SV"))
            fragment = new PenjualanServiceInput();
        else if(jenis_transaksi.equals("SS"))
            fragment = new PenjualanServiceSparepart();

        Bundle mBundle = new Bundle();
        mBundle.putString("id", id);

        fragment.setArguments(mBundle);
        FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
