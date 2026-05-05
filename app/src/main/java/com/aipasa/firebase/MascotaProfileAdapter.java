package com.aipasa.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class MascotaProfileAdapter extends RecyclerView.Adapter<MascotaProfileAdapter.ViewHolder> {

    private Context context;
    private List<DocumentSnapshot> listaMascotas;

    public MascotaProfileAdapter(Context context, List<DocumentSnapshot> listaMascotas) {
        this.context = context;
        this.listaMascotas = listaMascotas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_tarjeta_perfil_mascota, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DocumentSnapshot doc = listaMascotas.get(position);

        String nombre = doc.getString("nombre");
        String tipo = doc.getString("tipo");
        String estado = doc.getString("estado");
        String fotoUrl = doc.getString("fotoUrl");

        holder.txtTitulo.setText(nombre != null ? nombre : "Sin nombre");

        String tipoEstado = (tipo != null ? tipo : "") + " • " + (estado != null ? estado : "");
        holder.txtFecha.setText(tipoEstado);

        if (fotoUrl != null) {
            Glide.with(context)
                    .load(fotoUrl)
                    .into(holder.imgAccion);
        }

        //  BOTÓN EDITAR
        holder.btnEditar.setOnClickListener(v -> {
            Toast.makeText(context, "Editar (luego lo conectamos)", Toast.LENGTH_SHORT).show();
        });

        //  BOTÓN ELIMINAR
        holder.btnEliminar.setOnClickListener(v -> {
            doc.getReference().delete();
            Toast.makeText(context, "Mascota eliminada", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listaMascotas != null ? listaMascotas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAccion;
        TextView txtTitulo, txtFecha;
        Button btnEditar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAccion = itemView.findViewById(R.id.imgAccion);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}