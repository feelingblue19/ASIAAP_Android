package com.asiaap.Pengadaan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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

import com.asiaap.Model.Pengadaan;
import com.asiaap.Model.Supplier;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.asiaap.Supplier.SupplierAdapter;
import com.asiaap.Supplier.SupplierInput;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PengadaanAdapter extends RecyclerView.Adapter<PengadaanAdapter.MyViewHolder> {
    private Context mContext;
    private List<Pengadaan> list;
    private List<Pengadaan> listfull;

    public void setFilter(List<Pengadaan> pengadaanList) {
        list.clear();
        list.addAll(pengadaanList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView no_pengadaan, tanggal, supplier, total, status;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            no_pengadaan = (TextView) view.findViewById(R.id.no_pengadaan);
            tanggal = (TextView) view.findViewById(R.id.tanggal);
            supplier = (TextView) view.findViewById(R.id.supplier);
            total = (TextView) view.findViewById(R.id.total);
            status = (TextView) view.findViewById(R.id.status);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public PengadaanAdapter(Context mContext, List<Pengadaan> list, List<Pengadaan> listfull) {
        this.mContext = mContext;
        this.list = list;
        this.listfull = listfull;
    }

    @NonNull
    @Override
    public PengadaanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pengadaan_card, parent, false);

        return new PengadaanAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PengadaanAdapter.MyViewHolder holder, int position) {
        Pengadaan pengadaan = list.get(position);
        holder.no_pengadaan.setText("No. Pengadaan : " + pengadaan.getIdPengadaan());
        holder.tanggal.setText("Tanggal : " + pengadaan.getTanggalPengadaan());
        holder.supplier.setText("Supplier : " + pengadaan.getIdSupplier());
        holder.total.setText("Total : Rp " + pengadaan.getTotalPengadaan());
        holder.status.setText("Status: " + pengadaan.getStatus());


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

    private void showPopupMenu(View view, final PengadaanAdapter.MyViewHolder holder) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PengadaanAdapter.MyMenuItemClickListener(holder));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        PengadaanAdapter.MyViewHolder holder;
        public MyMenuItemClickListener(final PengadaanAdapter.MyViewHolder holder) {
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

    private void delete(final PengadaanAdapter.MyViewHolder holder) {
        new AlertDialog.Builder(mContext)
                .setTitle("Hapus")
                .setMessage("Apakah Anda Yakin?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String id = holder.no_pengadaan.getText().toString();
                        String status = new String(holder.status.getText().toString().substring(8));
                        if(!status.equals("Belum Dicetak")) {
                            Toast.makeText(mContext, "Pengadaan Sudah Selesai", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        final Call<Pengadaan> call = apiService.hapusPengadaan(id.substring(16));

                        call.enqueue(new Callback<Pengadaan>() {
                            @Override
                            public void onResponse(Call<Pengadaan> call, Response<Pengadaan> response) {
                                if(response.code() == 200) {
                                    int position = holder.getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        list.remove(holder.getAdapterPosition());
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
                                        Toast.makeText(mContext, "Delete Berhasil", Toast.LENGTH_SHORT).show();
                                    }
                                    listfull.clear();
                                    listfull.addAll(list);
                                } else
                                    Toast.makeText(mContext, "Delete Gagal", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<Pengadaan> call, Throwable t) {
                                Toast.makeText(mContext, "Delete Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    private void edit (PengadaanAdapter.MyViewHolder holder) {
        String id_supplier = holder.no_pengadaan.getText().toString();
        String status = new String(holder.status.getText().toString().substring(8));

        if(!status.equals("Belum Dicetak")) {
            Toast.makeText(mContext, "Pengadaan Sudah Selesai", Toast.LENGTH_SHORT).show();
            return;
        }



        Bundle mBundle = new Bundle();
        mBundle.putString("id", id_supplier.substring(16));

        Fragment fragment = new PengadaanInput();
        fragment.setArguments(mBundle);
        FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}
