package com.aipasa.firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.main.TarjetaFragment;
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
        String id = doc.getId();
        Long fechaLong = doc.getLong("fecha");

        holder.txtNombre.setText(
                nombre != null && !nombre.isEmpty()
                        ? nombre
                        : holder.itemView.getContext().getString(R.string.sin_nombre)
        );

        String tipoTraducido = traducirTipo(holder, tipo);
        String estadoTraducido = traducirEstado(holder, estado);

        String tipoEstado = "";

        if (!tipoTraducido.isEmpty()) {
            tipoEstado += tipoTraducido;
        }

        if (!estadoTraducido.isEmpty()) {
            if (!tipoEstado.isEmpty()) tipoEstado += " • ";
            tipoEstado += estadoTraducido;
        }

        holder.txtTipoEstado.setText(tipoEstado);

        String info = "";

        if (edad != null && !edad.isEmpty()) {
            info += holder.itemView.getContext().getString(R.string.edad_label, edad);
        }

        if (infoAdicional != null && !infoAdicional.isEmpty()) {
            if (!info.isEmpty()) info += " • ";
            info += infoAdicional;
        }

        holder.txtInfoAdicional.setText(
                info.isEmpty()
                        ? holder.itemView.getContext().getString(R.string.sin_informacion_adicional)
                        : info
        );

        if (fechaLong != null) {
            String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(fechaLong));

            holder.txtFecha.setText(
                    holder.itemView.getContext().getString(R.string.publicado_label, fechaFormateada)
            );
        } else {
            holder.txtFecha.setText("");
        }

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

        holder.itemView.setOnClickListener(v -> {
            if (id != null && !id.isEmpty() && holder.itemView.getContext() instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) holder.itemView.getContext();
                TarjetaFragment fragment = TarjetaFragment.newInstance(id);

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_container, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(
                        holder.itemView.getContext(),
                        holder.itemView.getContext().getString(R.string.no_se_puede_abrir_publicacion),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private String traducirTipo(@NonNull ViewHolder holder, String tipo) {
        if (tipo == null) return "";

        switch (tipo.toLowerCase(Locale.ROOT)) {
            case "perro":
                return holder.itemView.getContext().getString(R.string.tipo_perro);
            case "gato":
                return holder.itemView.getContext().getString(R.string.tipo_gato);
            case "otro":
                return holder.itemView.getContext().getString(R.string.tipo_otro);
            default:
                return tipo;
        }
    }

    private String traducirEstado(@NonNull ViewHolder holder, String estado) {
        if (estado == null) return "";

        switch (estado.toLowerCase(Locale.ROOT)) {
            case "perdido":
                return holder.itemView.getContext().getString(R.string.estado_perdido);
            case "adopcion":
            case "adopción":
                return holder.itemView.getContext().getString(R.string.estado_adopcion);
            case "encontrado":
                return holder.itemView.getContext().getString(R.string.estado_encontrado);
            default:
                return estado;
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