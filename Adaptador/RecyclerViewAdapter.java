package com.juanpablo.eduky.Adaptador;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.juanpablo.eduky.Libro.Book;
import com.juanpablo.eduky.Libro.WebViewLibro;
import com.juanpablo.eduky.R;

import java.util.ArrayList;

/**
 * Created by PERSONAL on 31/03/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<Book> mData;
    Activity activity;
    String urlImagen;


    public RecyclerViewAdapter(ArrayList<Book> mData, Activity activity) {
        this.mData = mData;
        this.activity = activity;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView book_title;
        ImageView img_book;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            book_title = (TextView) itemView.findViewById(R.id.book_title_id) ;
            img_book = (ImageView) itemView.findViewById(R.id.book_img_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(activity);
        view = mInflater.inflate(R.layout.card_view_ebook,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Book book = mData.get(position);
        holder.book_title.setText(book.getNombreLibro());
        urlImagen=book.getImagenLibro();
        Glide.with(activity)
                .load(urlImagen)
                .asBitmap()
                .into(new BitmapImageViewTarget(holder.img_book){
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                        circularBitmapDrawable.setCircular(false);
                        holder.img_book.setImageDrawable(circularBitmapDrawable);
                    }
                });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity,WebViewLibro.class);

                // passing data to the book activity
                intent.putExtra("url",book.getUrlLibro());
                // start the activity
                activity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void setFilter(ArrayList<Book> books){
        mData=new ArrayList<>();
        mData.addAll(books);
        notifyDataSetChanged();
    }


}