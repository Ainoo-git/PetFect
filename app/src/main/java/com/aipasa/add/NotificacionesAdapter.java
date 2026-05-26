package com.aipasa.add;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.firebase.NotificacionModel;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NotificacionesAdapter extends
        RecyclerView.Adapter<NotificacionesAdapter.ViewHolder> {

    private Context context;
    private List<NotificacionModel> lista;

    public NotificacionesAdapter(Context context,
                                 List<NotificacionModel> lista) {

        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_mascota,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        NotificacionModel notif =
                lista.get(position);

        holder.txtNombre.setText(
                notif.getNombreMascota());

        holder.txtTipoEstado.setText(
                notif.getTipo());

        holder.txtFecha.setText(
                DateFormat.format(
                        "dd/MM/yyyy HH:mm",
                        notif.getFecha()
                )
        );

        // 🖼️ Imagen
        Glide.with(context)
                .load(notif.getImagenUrl())
                .into(holder.imgMascota);

        // 🔥 COLOR SEGÚN LEÍDO
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

        // 👁️ MARCAR COMO LEÍDO
        holder.itemView.setOnClickListener(v -> {

            FirebaseFirestore.getInstance()
                    .collection("notificaciones")
                    .document(notif.getId())
                    .update("leido", true);

            notif.setLeido(true);

            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgMascota;

        TextView txtNombre,
                txtTipoEstado,
                txtFecha;

        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMascota =
                    itemView.findViewById(R.id.imgMascota);

            txtNombre =
                    itemView.findViewById(R.id.txtNombre);

            txtTipoEstado =
                    itemView.findViewById(R.id.txtTipoEstado);

            txtFecha =
                    itemView.findViewById(R.id.txtFecha);

            cardView =
                    itemView.findViewById(
                            R.id.recyclerNotificaciones
                    );
        }
    }
}