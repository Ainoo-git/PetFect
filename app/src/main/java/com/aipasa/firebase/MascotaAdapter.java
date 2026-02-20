package com.aipasa.firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {

    private List<DocumentSnapshot> listaMascotas;

    public MascotaAdapter(List<DocumentSnapshot> listaMascotas) {
        this.listaMascotas = listaMascotas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mascota, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DocumentSnapshot doc = listaMascotas.get(position);

        String nombre = doc.getString("nombre");
        String tipo = doc.getString("tipo");
        String estado = doc.getString("estado");
        String infoAdicional = doc.getString("infoAdicional");
        String edad = doc.getString("edad");
        String fotoUrl = doc.getString("fotoUrl");
        Long fechaLong = doc.getLong("fecha");

        // Nombre
        holder.txtNombre.setText(nombre != null ? nombre : "Sin nombre");

        // Tipo y Estado
        String tipoEstado = "";
        if (tipo != null) {
            tipoEstado += tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
        }
        if (estado != null) {
            if (!tipoEstado.isEmpty()) tipoEstado += " • ";
            tipoEstado += estado.substring(0, 1).toUpperCase() + estado.substring(1);
        }
        holder.txtTipoEstado.setText(tipoEstado);

        // Información adicional
        String info = "";
        if (edad != null && !edad.isEmpty()) {
            info += "Edad: " + edad;
        }
        if (infoAdicional != null && !infoAdicional.isEmpty()) {
            if (!info.isEmpty()) info += " • ";
            info += infoAdicional;
        }
        holder.txtInfoAdicional.setText(info.isEmpty() ? "Sin información adicional" : info);

        // Fecha
        if (fechaLong != null) {
            String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(fechaLong));
            holder.txtFecha.setText("Publicado: " + fechaFormateada);
        }

        // Imagen
        if (fotoUrl != null && !fotoUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(fotoUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_search)
                    .centerCrop()
                    .into(holder.imgMascota);
        } else {
            holder.imgMascota.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return listaMascotas != null ? listaMascotas.size() : 0;
    }

    public void actualizarLista(List<DocumentSnapshot> nuevaLista) {
        this.listaMascotas = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMascota;
        TextView txtNombre, txtTipoEstado, txtInfoAdicional, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMascota = itemView.findViewById(R.id.imgMascota);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTipoEstado = itemView.findViewById(R.id.txtTipoEstado);
            txtInfoAdicional = itemView.findViewById(R.id.txtInfoAdicional);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}