package com.asiaap.Supplier;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.asiaap.HomeAdminFragment;
import com.asiaap.Model.Sparepart;
import com.asiaap.Model.Supplier;
import com.asiaap.R;
import com.asiaap.REST.ApiClient;
import com.asiaap.REST.ApiInterface;
import com.asiaap.SparepartAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.MyViewHolder>{
    private Context mContext;
    private List<Supplier> supplierList;
    private List<Supplier> supplierListFull;


    public void setFilter(List<Supplier> supplierArrayList) {
        supplierList.clear();
        supplierList.addAll(supplierArrayList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id_supplier, nama_supplier, alamat_supplier, no_telp_supplier,
                        nama_sales, no_telp_sales;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            id_supplier = (TextView) view.findViewById(R.id.id_supplier);
            nama_supplier = (TextView) view.findViewById(R.id.nama_supplier);
            alamat_supplier = (TextView) view.findViewById(R.id.alamat_supplier);
            no_telp_supplier = (TextView) view.findViewById(R.id.no_telp_supplier);
            nama_sales = (TextView) view.findViewById(R.id.nama_sales);
            no_telp_sales = (TextView) view.findViewById(R.id.no_telp_sales);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }
    }

    public SupplierAdapter(Context mContext, List<Supplier> supplierList, List<Supplier> supplierListFull) {
        this.mContext = mContext;
        this.supplierList = supplierList;
        this.supplierListFull = supplierListFull;
    }

    @NonNull
    @Override
    public SupplierAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.supplier_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SupplierAdapter.MyViewHolder holder, int position) {
        Supplier supplier = supplierList.get(position);
        holder.id_supplier.setText("ID : " + supplier.getIdSupplier());
        holder.nama_supplier.setText("Nama : " + supplier.getNamaSupplier());
        holder.no_telp_supplier.setText("No. Telp : " + supplier.getNoTelpSupplier());
        holder.alamat_supplier.setText("Alamat : " + supplier.getAlamatSupplier());
        holder.nama_sales.setText("Nama Sales : " + supplier.getNamaSales());
        holder.no_telp_sales.setText("No. Telp Sales : " + supplier.getNoTelpSales());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return supplierList.size();
    }

    private void showPopupMenu(View view, final SupplierAdapter.MyViewHolder holder) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(holder));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {


        SupplierAdapter.MyViewHolder holder;
        public MyMenuItemClickListener(final SupplierAdapter.MyViewHolder holder) {
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

    private void delete(final SupplierAdapter.MyViewHolder holder) {
        new AlertDialog.Builder(mContext)
                .setTitle("Hapus")
                .setMessage("Apakah Anda Yakin?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        int position = holder.getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            supplierList.remove(holder.getAdapterPosition());
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(holder.getAdapterPosition(), supplierList.size());
                        }

                        String id = holder.id_supplier.getText().toString();

                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        final Call<Supplier> supplierCall = apiService.hapusSupplier(id.substring(5));

                        supplierCall.enqueue(new Callback<Supplier>() {
                            @Override
                            public void onResponse(Call<Supplier> call, Response<Supplier> response) {
                                if(response.code() == 200) {
                                    Toast.makeText(mContext, "Delete Berhasil", Toast.LENGTH_SHORT).show();
                                    supplierListFull.clear();
                                    supplierListFull.addAll(supplierList);
                                }

                                else
                                    Toast.makeText(mContext, "Delete Gagal", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<Supplier> call, Throwable t) {

                            }
                        });
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void edit (SupplierAdapter.MyViewHolder holder) {
        String id_supplier = holder.id_supplier.getText().toString();

        Bundle mBundle = new Bundle();
        mBundle.putString("id_supplier", id_supplier.substring(5));

        Fragment fragment = new SupplierInput();
        fragment.setArguments(mBundle);
        FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }


}
