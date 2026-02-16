package com.aipasa.firebase;

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
                .inflate(R.layout.item_mascota, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DocumentSnapshot mascota = listaMascotas.get(position);

        String nombre = mascota.getString("nombre");
        String estado = mascota.getString("estado");
        String info = mascota.getString("infoAdicional");

        holder.txtNombre.setText(nombre != null ? nombre : "Sin nombre");
        holder.txtEstado.setText(estado != null ? estado : "");
        holder.txtInfo.setText(info != null ? info : "");

        // donde cogera la imagen
        holder.imgMascota.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        return listaMascotas.size();
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
