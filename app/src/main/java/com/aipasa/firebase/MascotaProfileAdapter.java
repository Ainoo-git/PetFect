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
import java.util.Locale;

public class MascotaProfileAdapter extends RecyclerView.Adapter<MascotaProfileAdapter.ViewHolder> {

    private Context context;
    private List<DocumentSnapshot> listaMascotas;

    public MascotaProfileAdapter(Context context, List<DocumentSnapshot> listaMascotas) {
        this.context = context;
        this.listaMascotas = listaMascotas;
    }

    @NonNull
    @Override
    public MascotaProfileAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_mascota, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MascotaProfileAdapter.ViewHolder holder,
            int position
    ) {
        DocumentSnapshot doc = listaMascotas.get(position);

        String nombre = doc.getString("nombre");
        String tipo = doc.getString("tipo");
        String estado = doc.getString("estado");
        String infoAdicional = doc.getString("infoAdicional");
        String fotoUrl = doc.getString("fotoUrl");

        holder.txtNombre.setText(
                nombre != null && !nombre.isEmpty()
                        ? nombre
                        : context.getString(R.string.sin_nombre)
        );

        String tipoEstado = crearTipoEstado(tipo, estado);
        holder.txtTipoEstado.setText(tipoEstado);

        holder.txtInfoAdicional.setText(
                infoAdicional != null && !infoAdicional.isEmpty()
                        ? infoAdicional
                        : context.getString(R.string.sin_informacion_adicional)
        );

        if (fotoUrl != null && !fotoUrl.isEmpty()) {
            Glide.with(context)
                    .load(fotoUrl)
                    .into(holder.imgMascota);
        } else {
            holder.imgMascota.setImageResource(R.drawable.logologin);
        }

        holder.itemView.setOnClickListener(v -> {
            int posicionActual = holder.getBindingAdapterPosition();

            if (posicionActual == RecyclerView.NO_POSITION) {
                return;
            }

            DocumentSnapshot documentoActual = listaMascotas.get(posicionActual);

            abrirDialog(v, documentoActual, posicionActual);
        });
    }

    private void abrirDialog(
            View v,
            DocumentSnapshot doc,
            int posicion
    ) {
        String nombre = doc.getString("nombre");
        String tipo = doc.getString("tipo");
        String estado = doc.getString("estado");
        String fotoUrl = doc.getString("fotoUrl");

        String edad = doc.getString("edad");
        String chip = doc.getString("chip");
        String telefono = doc.getString("telefono");
        String info = doc.getString("infoAdicional");

        View dialogView = LayoutInflater.from(v.getContext())
                .inflate(R.layout.fragment_tarjeta_perfil_mascota, null);

        ImageView img = dialogView.findViewById(R.id.imgAccion);
        TextView txtTitulo = dialogView.findViewById(R.id.txtTitulo);
        TextView txtFecha = dialogView.findViewById(R.id.txtFecha);

        TextView txtEdad = dialogView.findViewById(R.id.txtEdad);
        TextView txtChip = dialogView.findViewById(R.id.txtChip);
        TextView txtTelefono = dialogView.findViewById(R.id.txtTelefono);
        TextView txtInfoAdicional = dialogView.findViewById(R.id.txtInfoAdicional);

        Button btnEditar = dialogView.findViewById(R.id.btnEditar);
        Button btnEliminar = dialogView.findViewById(R.id.btnEliminar);

        txtTitulo.setText(
                nombre != null && !nombre.isEmpty()
                        ? nombre
                        : context.getString(R.string.sin_nombre)
        );

        txtFecha.setText(crearTipoEstado(tipo, estado));

        txtEdad.setText(
                context.getString(
                        R.string.edad_label,
                        edad != null && !edad.isEmpty()
                                ? edad
                                : context.getString(R.string.no_especificada)
                )
        );

        txtChip.setText(
                context.getString(
                        R.string.chip_label,
                        chip != null && !chip.isEmpty()
                                ? chip
                                : context.getString(R.string.no_especificado)
                )
        );

        txtTelefono.setText(
                context.getString(
                        R.string.telefono_label,
                        telefono != null && !telefono.isEmpty()
                                ? telefono
                                : context.getString(R.string.no_disponible)
                )
        );

        txtInfoAdicional.setText(
                context.getString(
                        R.string.info_adicional_label,
                        info != null && !info.isEmpty()
                                ? info
                                : context.getString(R.string.sin_informacion)
                )
        );

        if (fotoUrl != null && !fotoUrl.isEmpty()) {
            Glide.with(v.getContext())
                    .load(fotoUrl)
                    .into(img);
        } else {
            img.setImageResource(R.drawable.logologin);
        }

        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(v.getContext())
                .setView(dialogView)
                .create();

        dialog.show();

        btnEditar.setOnClickListener(v2 -> {
            dialog.dismiss();
            abrirEditar(doc);
        });

        btnEliminar.setOnClickListener(v2 -> {
            dialog.dismiss();
            confirmarEliminar(doc, posicion);
        });
    }

    private String crearTipoEstado(String tipo, String estado) {
        String tipoTraducido = traducirTipo(tipo);
        String estadoTraducido = traducirEstado(estado);

        String tipoEstado = "";

        if (!tipoTraducido.isEmpty()) {
            tipoEstado += tipoTraducido;
        }

        if (!estadoTraducido.isEmpty()) {
            if (!tipoEstado.isEmpty()) {
                tipoEstado += " • ";
            }

            tipoEstado += estadoTraducido;
        }

        return tipoEstado;
    }

    private String traducirTipo(String tipo) {
        if (tipo == null) {
            return "";
        }

        switch (tipo.toLowerCase(Locale.ROOT)) {
            case "perro":
                return context.getString(R.string.tipo_perro);
            case "gato":
                return context.getString(R.string.tipo_gato);
            case "otro":
                return context.getString(R.string.tipo_otro);
            default:
                return tipo;
        }
    }

    private String traducirEstado(String estado) {
        if (estado == null) {
            return "";
        }

        switch (estado.toLowerCase(Locale.ROOT)) {
            case "perdido":
                return context.getString(R.string.estado_perdido);
            case "adopcion":
            case "adopción":
                return context.getString(R.string.estado_adopcion);
            case "encontrado":
                return context.getString(R.string.estado_encontrado);
            default:
                return estado;
        }
    }

    @Override
    public int getItemCount() {
        return listaMascotas != null ? listaMascotas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMascota;
        TextView txtNombre;
        TextView txtTipoEstado;
        TextView txtInfoAdicional;
        TextView txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMascota = itemView.findViewById(R.id.imgMascota);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTipoEstado = itemView.findViewById(R.id.txtTipoEstado);
            txtInfoAdicional = itemView.findViewById(R.id.txtInfoAdicional);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }

    private void abrirEditar(DocumentSnapshot doc) {
        android.content.Intent intent =
                new android.content.Intent(
                        context,
                        com.aipasa.main.PublicacionActivity.class
                );

        intent.putExtra("modo", "editar");
        intent.putExtra("idMascota", doc.getId());

        context.startActivity(intent);
    }

    private void confirmarEliminar(
            DocumentSnapshot doc,
            int posicion
    ) {
        new android.app.AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.eliminar_mascota))
                .setMessage(context.getString(R.string.seguro_eliminar_mascota))
                .setPositiveButton(context.getString(R.string.si), (dialog, which) -> {
                    eliminarMascota(doc, posicion);
                })
                .setNegativeButton(context.getString(R.string.no), null)
                .show();
    }

    private void eliminarMascota(
            DocumentSnapshot doc,
            int posicion
    ) {
        doc.getReference()
                .delete()
                .addOnSuccessListener(unused -> {
                    if (posicion >= 0 && posicion < listaMascotas.size()) {
                        listaMascotas.remove(posicion);
                        notifyItemRemoved(posicion);
                        notifyItemRangeChanged(posicion, listaMascotas.size());
                    }

                    Toast.makeText(
                            context,
                            context.getString(R.string.mascota_eliminada),
                            Toast.LENGTH_SHORT
                    ).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                context,
                                "No se pudo eliminar la publicación",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }
}