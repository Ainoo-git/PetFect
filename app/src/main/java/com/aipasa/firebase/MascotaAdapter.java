package com.aipasa.firebase;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
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

        DocumentSnapshot mascota = listaMascotas.get(position);

        String nombre = mascota.getString("nombre");
        String tipo = mascota.getString("tipo");
        String zona = mascota.getString("infoAdicional");
        Long fechaLong = mascota.getLong("fecha");

        // Nombre
        holder.txtNombre.setText(nombre != null ? nombre : "Sin nombre");

        // Tipo
        holder.txtTipo.setText(tipo != null ? tipo : "");

        // Zona
        holder.txtZona.setText(zona != null ? zona : "");

        // Fecha
        if (fechaLong != null) {
            String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(fechaLong));
            holder.txtFecha.setText(fechaFormateada);
        } else {
            holder.txtFecha.setText("");
        }

        // Color según tipo
        if (tipo != null) {
            switch (tipo.toLowerCase()) {
                case "perro":
                    holder.txtTipo.setBackgroundColor(Color.parseColor("#FF9800"));
                    break;
                case "gato":
                    holder.txtTipo.setBackgroundColor(Color.parseColor("#2196F3"));
                    break;
                default:
                    holder.txtTipo.setBackgroundColor(Color.parseColor("#4CAF50"));
                    break;
            }
        } else {
            holder.txtTipo.setBackgroundColor(Color.TRANSPARENT);
        }

        // Imagen (placeholder por ahora)
        holder.imgMascota.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        return listaMascotas != null ? listaMascotas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMascota;
        TextView txtNombre, txtTipo, txtZona, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMascota = itemView.findViewById(R.id.imgMascota);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTipo = itemView.findViewById(R.id.txtTipo);
            txtZona = itemView.findViewById(R.id.txtZona);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}
