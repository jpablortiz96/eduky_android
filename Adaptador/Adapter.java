package com.juanpablo.eduky.Adaptador;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.juanpablo.eduky.Educador.Educador;
import com.juanpablo.eduky.PerfilEducador;
import com.juanpablo.eduky.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by PERSONAL on 30/12/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.EducadoresViewHolder>{

    ArrayList<Educador> educadores;
    Activity activity;
    String urlImagen;

    public Adapter(ArrayList<Educador> educadores, Activity activity) {
        this.educadores = educadores;
        this.activity = activity;
    }

    public static class EducadoresViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, profesion, materias, direccion;
        ImageView imagen;
        LinearLayout layout;

        public EducadoresViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView) itemView.findViewById(R.id.nombre_educador);
            profesion = (TextView) itemView.findViewById(R.id.profesion_educador);
            materias = (TextView) itemView.findViewById(R.id.asignaturas_educador);
            direccion = (TextView) itemView.findViewById(R.id.direccion_educador);
            imagen = (ImageView) itemView.findViewById(R.id.imagen_educador);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_info);
        }
    }

    @Override
    public EducadoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);
        EducadoresViewHolder educadoresViewHolder = new EducadoresViewHolder(v);
        return educadoresViewHolder;
    }

    @Override
    public void onBindViewHolder(final EducadoresViewHolder holder, int position) {
        final Educador educador = educadores.get(position);
        holder.nombre.setText(educador.getNomPro()+" "+educador.getApePro());
        holder.profesion.setText(educador.getOcupaPro());
        holder.materias.setText(educador.getAsigPro());
        Double lat = Double.parseDouble(educador.getLatitudPro());
        Double lon = Double.parseDouble(educador.getLongitudPro());
        if (lat != 0.0 && lon != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(lat, lon, 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    holder.direccion.setText(address.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        urlImagen=educador.getImagenPro();
        Glide.with(activity)
                .load(urlImagen)
                .asBitmap()
                .into(new BitmapImageViewTarget(holder.imagen){
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.imagen.setImageDrawable(circularBitmapDrawable);
                    }
                });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(activity, PerfilEducador.class);
                intento.putExtra("eduNombre",educador.getNomPro()+" "+educador.getApePro());
                intento.putExtra("eduProfe",educador.getOcupaPro());
                intento.putExtra("eduMate",educador.getAsigPro());
                intento.putExtra("eduCorreo",educador.getCorreoPro());
                intento.putExtra("eduCel",educador.getCelPro());
                intento.putExtra("eduDescri",educador.getDescriPro());
                intento.putExtra("eduEdad",educador.getEdadPro());
                intento.putExtra("eduImagen",educador.getImagenPro());
                intento.putExtra("eduLat",educador.getLatitudPro());
                intento.putExtra("eduLon",educador.getLongitudPro());
                activity.startActivity(intento);
                activity.overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
            }
        });

    }

    @Override
    public int getItemCount() {
        return educadores.size();
    }

    public void setFilter(ArrayList<Educador> listaNota){
        educadores=new ArrayList<>();
        educadores.addAll(listaNota);
        notifyDataSetChanged();
    }

}
