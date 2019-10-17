package com.asiaap.PenjualanServiceSparepart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.asiaap.Model.DetailPenjualanSparepart;
import com.asiaap.Model.KendaraanCustomer;
import com.asiaap.PenjualanService.myCallBackService;
import com.asiaap.PenjualanSparepart.DetailSparepartAdapter;

import com.asiaap.R;

import java.util.List;

public class DetailSparepartSSAdapter extends RecyclerView.Adapter<DetailSparepartSSAdapter.MyViewHolder> {

    private Context mContext;
    private List<DetailPenjualanSparepart> list;
    public ViewGroup vg;
    public myCallBackService myCB;
    public TextView tv;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sparepart, jumlah, kendaraan;
        public ImageView overflow;


        public MyViewHolder(View view) {
            super(view);
            kendaraan = (TextView) view.findViewById(R.id.kendaraan);
            sparepart = (TextView) view.findViewById(R.id.sparepart);
            jumlah = (TextView) view.findViewById(R.id.jumlah);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public DetailSparepartSSAdapter(Context mContext, List<DetailPenjualanSparepart> list, myCallBackService myCB) {
        this.mContext = mContext;
        this.list = list;
        this.myCB = myCB;
    }


    @NonNull
    @Override
    public DetailSparepartSSAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_sparepartspr_card, parent, false);
        vg = parent;
        return new DetailSparepartSSAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailSparepartSSAdapter.MyViewHolder holder, int position) {
        DetailPenjualanSparepart detail = list.get(position);
        KendaraanCustomer ken = detail.getKendaraan();
        holder.kendaraan.setText("No. Polisi : " + ken.getNoPolisi());
        holder.sparepart.setText("Sparepart : " + detail.getKodeSparepart());
        holder.jumlah.setText("Jumlah : " + detail.getJumlahPenjualanSparepart());

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

    private void showPopupMenu(View view, final DetailSparepartSSAdapter.MyViewHolder holder) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new DetailSparepartSSAdapter.MyMenuItemClickListener(holder));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        DetailSparepartSSAdapter.MyViewHolder holder;
        public MyMenuItemClickListener(final DetailSparepartSSAdapter.MyViewHolder holder) {
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

    private void delete(DetailSparepartSSAdapter.MyViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(position != RecyclerView.NO_POSITION) {
            list.remove(holder.getAdapterPosition());
            notifyItemRemoved(position);
            notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
        }
    }

    private void edit(DetailSparepartSSAdapter.MyViewHolder holder, myCallBackService callBack) {
        String nopol = holder.kendaraan.getText().toString().substring(13);
        String jumlah = holder.jumlah.getText().toString().substring(9);
        String sparepart = holder.sparepart.getText().toString().substring(12);
        int position = holder.getAdapterPosition();
        Boolean editedSparepart = true;

        myCB.updateETSpr(nopol, jumlah, sparepart, position, editedSparepart);



    }
}
