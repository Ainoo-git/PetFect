package com.aipasa.firebase;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.firebase.NotificacionModel;
import com.aipasa.main.TarjetaFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;
import com.google.android.material.appbar.MaterialToolbar;
public class NotificacionesAdapter
        extends RecyclerView.Adapter<
        NotificacionesAdapter.ViewHolder> {

    private Context context;

    private List<NotificacionModel> lista;

    public NotificacionesAdapter(
            Context context,
            List<NotificacionModel> lista
    ) {

        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_notificacion,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        NotificacionModel notif =
                lista.get(position);

        holder.txtNombre.setText(
                notif.getNombreMascota()
        );

        holder.txtTipoEstado.setText(
                notif.getTipo()
        );

        // FECHA
        String fechaFormateada = "";

        if (notif.getFecha() != null) {

            Date fecha =
                    notif.getFecha().toDate();

            fechaFormateada =
                    DateFormat.format(
                            "dd/MM/yyyy HH:mm",
                            fecha
                    ).toString();
        }

        holder.txtFecha.setText(
                fechaFormateada
        );

        // IMAGEN
        Glide.with(context)
                .load(notif.getImagenUrl())
                .placeholder(
                        R.drawable.ic_launcher_background
                )
                .into(holder.imgMascota);

        // COLOR SEGÚN LEÍDO
        if (!notif.isLeido()) {

            holder.cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                            context,
                            R.color.coral_alertasimp_errores
                    )
            );

        } else {

            holder.cardView.setCardBackgroundColor(
                    Color.WHITE
            );
        }

        // CLICK NOTIFICACIÓN
        holder.itemView.setOnClickListener(v -> {

            FirebaseFirestore.getInstance()
                    .collection("notificaciones")
                    .document(notif.getId())
                    .update("leido", true);

            notif.setLeido(true);

            notifyItemChanged(position);

            // ABRIR MASCOTA
//            if (notif.getIdMascota() != null
//                    && !notif.getIdMascota().isEmpty()
//                    && context instanceof AppCompatActivity) {
//
//                AppCompatActivity activity =
//                        (AppCompatActivity) context;
//
//                TarjetaFragment fragment =
//                        TarjetaFragment.newInstance(
//                                notif.getIdMascota()
//                        );
//
//                fragment.show(
//                        activity.getSupportFragmentManager(),
//                        "tarjeta"
//                );
//            }
        });
    }

    @Override
    public int getItemCount() {

        return lista != null
                ? lista.size()
                : 0;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgMascota;

        TextView txtNombre;

        TextView txtTipoEstado;

        TextView txtFecha;

        CardView cardView;

        public ViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            imgMascota =
                    itemView.findViewById(
                            R.id.imgMascota
                    );

            txtNombre =
                    itemView.findViewById(
                            R.id.txtNombre
                    );

            txtTipoEstado =
                    itemView.findViewById(
                            R.id.txtTipoEstado
                    );

            txtFecha =
                    itemView.findViewById(
                            R.id.txtFecha
                    );

            cardView =
                    itemView.findViewById(
                            R.id.cardNotificacion
                    );
        }
    }
}