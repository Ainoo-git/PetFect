package com.aipasa.firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.main.TarjetaFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MascotaAdapterGuardados extends RecyclerView.Adapter<MascotaAdapterGuardados.ViewHolder> {

    private List<DocumentSnapshot> listaMascotas;

    public MascotaAdapterGuardados(List<DocumentSnapshot> listaMascotas) {
        this.listaMascotas = listaMascotas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mascota_guardado, parent, false);

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

        // NOMBRE
        holder.txtNombre.setText(
                nombre != null ? nombre : "Sin nombre"
        );

        // TIPO Y ESTADO
        String tipoEstado = "";

        if (tipo != null && !tipo.isEmpty()) {

            tipoEstado +=
                    tipo.substring(0,1).toUpperCase()
                            + tipo.substring(1);
        }

        if (estado != null && !estado.isEmpty()) {

            if (!tipoEstado.isEmpty()) {
                tipoEstado += " • ";
            }

            tipoEstado +=
                    estado.substring(0,1).toUpperCase()
                            + estado.substring(1);
        }

        holder.txtTipoEstado.setText(tipoEstado);

        // INFO
        String info = "";

        if (edad != null && !edad.isEmpty()) {
            info += "Edad: " + edad;
        }

        if (infoAdicional != null && !infoAdicional.isEmpty()) {

            if (!info.isEmpty()) {
                info += " • ";
            }

            info += infoAdicional;
        }

        holder.txtInfoAdicional.setText(
                info.isEmpty()
                        ? "Sin información adicional"
                        : info
        );

        // FECHA
        if (fechaLong != null) {

            String fechaFormateada =
                    new SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                    ).format(new Date(fechaLong));

            holder.txtFecha.setText(
                    "Publicado: " + fechaFormateada
            );

        } else {

            holder.txtFecha.setText("");
        }

        // IMAGEN
        if (fotoUrl != null && !fotoUrl.isEmpty()) {

            Glide.with(holder.itemView.getContext())
                    .load(fotoUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_search)
                    .centerCrop()
                    .into(holder.imgMascota);

        } else {

            holder.imgMascota.setImageResource(
                    R.drawable.ic_launcher_background
            );
        }

        // FIREBASE
        //Notificaciones
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String uid = auth.getCurrentUser().getUid();

        // COMPROBAR SI ESTÁ GUARDADO
        db.collection("usuarios")
                .document(uid)
                .collection("guardados")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        holder.btnFavorito.setImageResource(
                                R.drawable.marcador
                        );

                        holder.btnFavorito.setTag(true);

                    } else {

                        holder.btnFavorito.setImageResource(
                                R.drawable.marcador_vacio
                        );

                        holder.btnFavorito.setTag(false);
                    }
                });

        // CLICK FAVORITO
        holder.btnFavorito.setOnClickListener(v -> {

            boolean esFavorito =
                    holder.btnFavorito.getTag() != null
                            && (boolean) holder.btnFavorito.getTag();

            if (esFavorito) {

                // ELIMINAR
                db.collection("usuarios")
                        .document(uid)
                        .collection("guardados")
                        .document(id)
                        .delete();

                holder.btnFavorito.setImageResource(
                        R.drawable.marcador_vacio
                );

                holder.btnFavorito.setTag(false);

            } else {

                // GUARDAR
                db.collection("usuarios")
                        .document(uid)
                        .collection("guardados")
                        .document(id)
                        .set(doc.getData());

                holder.btnFavorito.setImageResource(
                        R.drawable.marcador
                );

                holder.btnFavorito.setTag(true);
            }
        });

        // CLICK TARJETA
        holder.itemView.setOnClickListener(v -> {

            if (id != null
                    && !id.isEmpty()
                    && holder.itemView.getContext()
                    instanceof AppCompatActivity) {

                AppCompatActivity activity =
                        (AppCompatActivity)
                                holder.itemView.getContext();

                TarjetaFragment fragment =
                        TarjetaFragment.newInstance(id);

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.content_container,
                                fragment
                        )
                        .addToBackStack(null)
                        .commit();

            } else {

                Toast.makeText(
                        holder.itemView.getContext(),
                        "No se puede abrir la publicación",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return listaMascotas != null
                ? listaMascotas.size()
                : 0;
    }

    public void actualizarLista(
            List<DocumentSnapshot> nuevaLista
    ) {

        this.listaMascotas = nuevaLista;

        notifyDataSetChanged();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgMascota;

        ImageButton btnFavorito;

        TextView txtNombre;
        TextView txtTipoEstado;
        TextView txtInfoAdicional;
        TextView txtFecha;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            imgMascota =
                    itemView.findViewById(R.id.imgMascota);

            btnFavorito =
                    itemView.findViewById(R.id.btnFavorito);

            txtNombre =
                    itemView.findViewById(R.id.txtNombre);

            txtTipoEstado =
                    itemView.findViewById(R.id.txtTipoEstado);

            txtInfoAdicional =
                    itemView.findViewById(R.id.txtInfoAdicional);

            txtFecha =
                    itemView.findViewById(R.id.txtFecha);
        }
    }
}