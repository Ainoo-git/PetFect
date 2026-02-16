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

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {

    private List<DocumentSnapshot> listaMascotas;

    public MascotaAdapter(List<DocumentSnapshot> listaMascotas) {
        this.listaMascotas = listaMascotas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_perdidos, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DocumentSnapshot mascota = listaMascotas.get(position);

        String nombre = mascota.getString("nombre");
        String tipo = mascota.getString("tipo");
        String zona = mascota.getString("infoAdicional");
        String fecha = mascota.getString("fechaTexto"); // si guardas fecha como texto

        holder.txtNombre.setText(nombre != null ? nombre : "Sin nombre");
        holder.txtTipo.setText(tipo != null ? tipo : "");
        holder.txtZona.setText(zona != null ? zona : "");
        holder.txtFecha.setText(fecha != null ? fecha : "");

        // Cambiar color según tipo
        if (tipo != null) {
            switch (tipo) {
                case "Perro":
                    holder.txtTipo.setBackgroundColor(Color.parseColor("#FF9800"));
                    break;
                case "Gato":
                    holder.txtTipo.setBackgroundColor(Color.parseColor("#2196F3"));
                    break;
                default:
                    holder.txtTipo.setBackgroundColor(Color.parseColor("#4CAF50"));
                    break;
            }
        }

        // Imagen (de momento placeholder)
        holder.imgMascota.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        return listaMascotas.size();
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
