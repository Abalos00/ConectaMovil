package com.example.conectamvil;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder> {

    private List<String> listaContactos;

    public ContactosAdapter(List<String> listaContactos) {
        this.listaContactos = listaContactos;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactoViewHolder holder, int position) {
        String nombreContacto = listaContactos.get(position);

        // Configura el texto en el TextView del diseño del elemento
        holder.textViewContacto.setText(nombreContacto);
        holder.textViewContacto.setTextColor(Color.BLACK); // Set text color to black
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public void setListaContactos(List<String> listaFiltrada) {
        listaContactos = listaFiltrada;
    }

    public static class ContactoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewContacto;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Modifica aquí para adaptarte a tu diseño actual
            textViewContacto = itemView.findViewById(R.id.textViewNombre);
        }
    }
}
