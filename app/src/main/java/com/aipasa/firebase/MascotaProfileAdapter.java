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
        String fotoUrl = doc.getString("fotoUrl");

        holder.txtNombre.setText(nombre != null ? nombre : "Sin nombre");
        holder.txtTipoEstado.setText((tipo != null ? tipo : "") + " • " + (estado != null ? estado : ""));
        holder.txtInfoAdicional.setText(infoAdicional != null ? infoAdicional : "");

        if (fotoUrl != null) {
            Glide.with(context).load(fotoUrl).into(holder.imgMascota);
        }

        // Al pulsar la tarjetita abre el dialog grande
        holder.itemView.setOnClickListener(v -> abrirDialog(v, doc));
    }

    private void abrirDialog(View v, DocumentSnapshot doc) {

        String nombre = doc.getString("nombre");
        String tipo = doc.getString("tipo");
        String estado = doc.getString("estado");
        String fotoUrl = doc.getString("fotoUrl");
        String tipoEstado = (tipo != null ? tipo : "") + " • " + (estado != null ? estado : "");

        View dialogView = LayoutInflater.from(v.getContext())
                .inflate(R.layout.fragment_tarjeta_perfil_mascota, null);

        ImageView img = dialogView.findViewById(R.id.imgAccion);
        TextView txtTitulo = dialogView.findViewById(R.id.txtTitulo);
        TextView txtFecha = dialogView.findViewById(R.id.txtFecha);
        TextView txtDescripcion = dialogView.findViewById(R.id.txtDescripcion);
        Button btnEditar = dialogView.findViewById(R.id.btnEditar);
        Button btnEliminar = dialogView.findViewById(R.id.btnEliminar);

        txtTitulo.setText(nombre != null ? nombre : "Sin nombre");
        txtFecha.setText(tipoEstado);
        txtDescripcion.setText(
                "Edad: " + doc.getString("edad") +
                        "\nChip: " + doc.getString("chip") +
                        "\nTeléfono: " + doc.getString("telefono") +
                        "\nInfo: " + doc.getString("infoAdicional")
        );

        if (fotoUrl != null) {
            Glide.with(v.getContext()).load(fotoUrl).into(img);
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
            confirmarEliminar(doc);
        });
    }

    @Override
    public int getItemCount() {
        return listaMascotas != null ? listaMascotas.size() : 0;
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

    private void abrirEditar(DocumentSnapshot doc) {
        android.content.Intent intent =
                new android.content.Intent(context, com.aipasa.main.PublicacionActivity.class);
        intent.putExtra("modo", "editar");
        intent.putExtra("idMascota", doc.getId());
        context.startActivity(intent);
    }

    private void confirmarEliminar(DocumentSnapshot doc) {
        new android.app.AlertDialog.Builder(context)
                .setTitle("Eliminar mascota")
                .setMessage("¿Seguro que quieres eliminarla?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    doc.getReference().delete();
                    Toast.makeText(context, "Mascota eliminada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}