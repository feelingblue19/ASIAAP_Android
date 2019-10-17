package com.asiaap.PenjualanSparepart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.asiaap.Model.DetailPenjualanSparepart;
import com.asiaap.Model.DetailPenjualanSparepart;
import com.asiaap.Model.Penjualan;
import com.asiaap.PenjualanSparepart.DetailSparepartAdapter;
import com.asiaap.R;

import java.util.List;

public class DetailSparepartAdapter extends RecyclerView.Adapter<DetailSparepartAdapter.MyViewHolder> {
    private Context mContext;
    private List<DetailPenjualanSparepart> list;
    public ViewGroup vg;
    public myCallBack myCB;
    public TextView tv;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sparepart, jumlah;
        public ImageView overflow;


        public MyViewHolder(View view) {
            super(view);
            sparepart = (TextView) view.findViewById(R.id.sparepart);
            jumlah = (TextView) view.findViewById(R.id.jumlah);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public DetailSparepartAdapter(Context mContext, List<DetailPenjualanSparepart> list, myCallBack myCB) {
        this.mContext = mContext;
        this.list = list;
        this.myCB = myCB;
    }


    @NonNull
    @Override
    public DetailSparepartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_sparepart_card, parent, false);
        vg = parent;
        return new DetailSparepartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailSparepartAdapter.MyViewHolder holder, int position) {
        DetailPenjualanSparepart detail = list.get(position);
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

    private void showPopupMenu(View view, final DetailSparepartAdapter.MyViewHolder holder) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new DetailSparepartAdapter.MyMenuItemClickListener(holder));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        DetailSparepartAdapter.MyViewHolder holder;
        public MyMenuItemClickListener(final DetailSparepartAdapter.MyViewHolder holder) {
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

    private void delete(DetailSparepartAdapter.MyViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(position != RecyclerView.NO_POSITION) {
            list.remove(holder.getAdapterPosition());
            notifyItemRemoved(position);
            notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
        }
    }

    private void edit(DetailSparepartAdapter.MyViewHolder holder, myCallBack callBack) {
        String jumlah = holder.jumlah.getText().toString().substring(9);
        String sparepart = holder.sparepart.getText().toString().substring(12);
        int position = holder.getAdapterPosition();
        Boolean editedSparepart = true;

        myCB.updateET(jumlah, sparepart, position, editedSparepart);



    }
}
