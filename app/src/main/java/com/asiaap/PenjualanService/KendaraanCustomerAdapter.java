package com.asiaap.PenjualanService;

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

import com.asiaap.Model.KendaraanCustomer;
import com.asiaap.PenjualanService.KendaraanCustomerAdapter;
import com.asiaap.R;

import java.util.List;

public class KendaraanCustomerAdapter extends RecyclerView.Adapter<KendaraanCustomerAdapter.MyViewHolder> {

    private Context mContext;
    private List<KendaraanCustomer> list;
    public myCallBackService myCB;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tipe, nopol, montir;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            tipe = (TextView) view.findViewById(R.id.tipe);
            nopol = (TextView) view.findViewById(R.id.nopol);
            montir = (TextView) view.findViewById(R.id.montir);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public KendaraanCustomerAdapter(Context mContext, List<KendaraanCustomer> list, myCallBackService myCB) {
        this.mContext = mContext;
        this.list = list;
        this.myCB = myCB;
    }

    @NonNull
    @Override
    public KendaraanCustomerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kendaraan_card, parent, false);

        return new KendaraanCustomerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final KendaraanCustomerAdapter.MyViewHolder holder, int position) {
        KendaraanCustomer ken = list.get(position);
        holder.tipe.setText("Motor : " + ken.getIdTipe());
        holder.nopol.setText("No. Polisi : " + ken.getNoPolisi());
        holder.montir.setText("Montir : " + ken.getIdPegawai());



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

    private void showPopupMenu(View view, final KendaraanCustomerAdapter.MyViewHolder holder) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu2, popup.getMenu());
        popup.setOnMenuItemClickListener(new KendaraanCustomerAdapter.MyMenuItemClickListener(holder));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        KendaraanCustomerAdapter.MyViewHolder holder;
        public MyMenuItemClickListener(final KendaraanCustomerAdapter.MyViewHolder holder) {
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

    private void delete(KendaraanCustomerAdapter.MyViewHolder holder) {
        int position = holder.getAdapterPosition();
        if(position != RecyclerView.NO_POSITION) {
            list.remove(holder.getAdapterPosition());
            notifyItemRemoved(position);
            notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
        }

        String nopol = holder.nopol.getText().toString().substring(13);
        myCB.deleteKendaraan(nopol);

    }
}
