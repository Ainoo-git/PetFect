package com.aipasa.firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aipasa.R;
import com.aipasa.data_Room.Mascota;

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {

    private List<Mascota> lista;

    public MascotaAdapter(List<Mascota> lista) {
        this.lista = lista;
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

        Mascota mascota = lista.get(position);

        holder.txtNombre.setText(mascota.getNombre());
        holder.txtEstado.setText(mascota.getEstado());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtEstado = itemView.findViewById(R.id.txtEstado);
        }
    }
}
