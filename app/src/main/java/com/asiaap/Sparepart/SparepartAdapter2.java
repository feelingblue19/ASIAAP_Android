package com.asiaap.Sparepart;

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

import com.asiaap.Model.Sparepart;
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

public class SparepartAdapter2 extends RecyclerView.Adapter<SparepartAdapter2.MyViewHolder> {
    private Context mContext;
    private List<Sparepart> sparepartList;
    private List<Sparepart> sparepartListFull;


    public void setFilter(List<Sparepart> sparepartArrayList) {
        sparepartList.clear();
        sparepartList.addAll(sparepartArrayList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kode_sparepart, penempatan_sparepart, nama_sparepart, merk_sparepart, tipe_sparepart,
                        harga_beli_sparepart, harga_jual_sparepart, stok_sparepart, min_stok;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            kode_sparepart = (TextView) view.findViewById(R.id.kode_sparepart);
            penempatan_sparepart = (TextView) view.findViewById(R.id.penempatan_sparepart);
            nama_sparepart = (TextView) view.findViewById(R.id.nama_sparepart);
            merk_sparepart = (TextView) view.findViewById(R.id.merk_sparepart);
            tipe_sparepart = (TextView) view.findViewById(R.id.tipe_sparepart);
            harga_beli_sparepart = (TextView) view.findViewById(R.id.harga_beli_sparepart);
            harga_jual_sparepart = (TextView) view.findViewById(R.id.harga_jual_sparepart);
            stok_sparepart = (TextView) view.findViewById(R.id.stok_sparepart);;
            min_stok = (TextView) view.findViewById(R.id.min_stok);;
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }
    }

    public SparepartAdapter2(Context mContext, List<Sparepart> sparepartList, List<Sparepart> sparepartListFull) {
        this.mContext = mContext;
        this.sparepartList = sparepartList;
        this.sparepartListFull = sparepartListFull;
    }

    @NonNull
    @Override
    public SparepartAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sparepart_card2, parent, false);

        return new SparepartAdapter2.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SparepartAdapter2.MyViewHolder holder, int position) {
        Sparepart sparepart = sparepartList.get(position);
        holder.kode_sparepart.setText("Kode : " + sparepart.getKodeSparepart());
        holder.penempatan_sparepart.setText("Penempatan : " + sparepart.getPenempatanSparepart());
        holder.nama_sparepart.setText("Nama : " + sparepart.getNamaSparepart());
        holder.merk_sparepart.setText("Merk : " + sparepart.getMerkSparepart());
        holder.tipe_sparepart.setText("Tipe : " + sparepart.getTipeSparepart());
        holder.harga_beli_sparepart.setText("Harga Beli : Rp " + sparepart.getHargaBeliSparepart());
        holder.harga_jual_sparepart.setText("Harga Jual : Rp " + sparepart.getHargaJualSparepart());
        holder.stok_sparepart.setText("Stok : " + sparepart.getStokSparepart());
        holder.min_stok.setText("Min. Stok : " + sparepart.getMinStok());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sparepartList.size();
    }

    private void showPopupMenu(View view, final SparepartAdapter2.MyViewHolder holder) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new SparepartAdapter2.MyMenuItemClickListener(holder));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        SparepartAdapter2.MyViewHolder holder;

        public MyMenuItemClickListener(final SparepartAdapter2.MyViewHolder holder) {
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

    private void delete(final SparepartAdapter2.MyViewHolder holder) {
        new AlertDialog.Builder(mContext)
                .setTitle("Hapus")
                .setMessage("Apakah Anda Yakin?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        int position = holder.getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            sparepartList.remove(holder.getAdapterPosition());
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(holder.getAdapterPosition(), sparepartList.size());
                        }

                        String id = holder.kode_sparepart.getText().toString();

                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        final Call<Sparepart> sparepartCall = apiService.hapusSparepart(id.substring(7));

                        sparepartCall.enqueue(new Callback<Sparepart>() {
                            @Override
                            public void onResponse(Call<Sparepart> call, Response<Sparepart> response) {
                                if(response.code() == 200) {
                                    Toast.makeText(mContext, "Delete Berhasil", Toast.LENGTH_SHORT).show();
                                    sparepartListFull.clear();
                                    sparepartListFull.addAll(sparepartList);
                                } else
                                    Toast.makeText(mContext, "Delete Gagal", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<Sparepart> call, Throwable t) {

                            }
                        });
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    private void edit (SparepartAdapter2.MyViewHolder holder) {
        String id = holder.kode_sparepart.getText().toString();

        Bundle mBundle = new Bundle();
        mBundle.putString("id", id.substring(7));

        Fragment fragment = new SparepartInput();
        fragment.setArguments(mBundle);
        FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
