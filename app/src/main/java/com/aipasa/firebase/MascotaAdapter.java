package com.aipasa.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {

    private Context context;
    private List<Mascotaa> lista;

    public MascotaAdapter(Context context, List<Mascotaa> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_mascota, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Mascotaa mascota = lista.get(position);

        holder.txtNombre.setText(mascota.getNombre());
        holder.txtEstado.setText(mascota.getEstado());
        holder.txtInfo.setText(mascota.getInfoAdicional());

        Glide.with(context)
                .load(mascota.getFotoUrl())
                .into(holder.imgMascota);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMascota;
        TextView txtNombre, txtEstado, txtInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMascota = itemView.findViewById(R.id.imgMascota);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            txtInfo = itemView.findViewById(R.id.txtInfo);
        }
    }
}
