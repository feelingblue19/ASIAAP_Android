package com.asiaap.PenjualanService;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.asiaap.Model.DetailPenjualanJasa;
import com.asiaap.Model.KendaraanCustomer;
import com.asiaap.PenjualanService.DetailServiceAdapter;
import com.asiaap.PenjualanSparepart.myCallBack;
import com.asiaap.R;

import java.util.List;

public class DetailServiceAdapter extends RecyclerView.Adapter<DetailServiceAdapter.MyViewHolder> {
    private Context mContext;
    private List<DetailPenjualanJasa> list;
    public ViewGroup vg;
    public myCallBackService myCB;
    public TextView tv;
    private ArrayAdapter<KendaraanCustomer> kendaraanCustomerArrayAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kendaraan, service;
        public ImageView overflow;


        public MyViewHolder(View view) {
            super(view);
            kendaraan = (TextView) view.findViewById(R.id.kendaraan);
            service = (TextView) view.findViewById(R.id.service);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public DetailServiceAdapter(Context mContext, List<DetailPenjualanJasa> list, myCallBackService myCB, ArrayAdapter<KendaraanCustomer> kendaraanCustomerArrayAdapter) {
        this.mContext = mContext;
        this.list = list;
        this.myCB = myCB;
        this.kendaraanCustomerArrayAdapter = kendaraanCustomerArrayAdapter;
    }


    @NonNull
    @Override
    public DetailServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_service_card, parent, false);
        vg = parent;
        return new DetailServiceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailServiceAdapter.MyViewHolder holder, int position) {
        DetailPenjualanJasa detail = list.get(position);
        KendaraanCustomer ken = detail.getKendaraan();
        holder.kendaraan.setText("No. Polisi : " + ken.getNoPolisi());
        holder.service.setText("Service : " + detail.getIdJasaService());

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

    private void showPopupMenu(View view, final DetailServiceAdapter.MyViewHolder holder) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new DetailServiceAdapter.MyMenuItemClickListener(holder));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        DetailServiceAdapter.MyViewHolder holder;
        public MyMenuItemClickListener(final DetailServiceAdapter.MyViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete:
                    delete(holder);
                    return true;
                case R.id.edit:
                    edit(holder, myCB);
                    return true;
                default:
            }
            return false;
        }
    }

    private void delete(DetailServiceAdapter.MyViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(position != RecyclerView.NO_POSITION) {
            list.remove(holder.getAdapterPosition());
            notifyItemRemoved(position);
            notifyItemRangeChanged(holder.getAdapterPosition(), list.size());

        }
    }

    private void edit(DetailServiceAdapter.MyViewHolder holder, myCallBackService callBack) {
        String service = holder.service.getText().toString().substring(10);
        String kendaraan = holder.kendaraan.getText().toString().substring(13);
        int position = holder.getAdapterPosition();
        Boolean editedService = true;

        myCB.loadService(service, kendaraan, position, editedService);
    }

    public void hapusSelected(int position) {
        Log.d("hapus_cek", "cek");
        if(position != RecyclerView.NO_POSITION) {
            Log.d("hapus_cek", "cek");
//            list.remove(position);
//            notifyItemRemoved(position);
//            Integer i = new Integer(list.size());
//            notifyItemRangeChanged(position, list.size());
//            Log.d("sisa_list", i.toString());
        }
    }
}
