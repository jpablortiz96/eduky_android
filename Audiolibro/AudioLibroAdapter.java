package com.juanpablo.eduky.Audiolibro;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.juanpablo.eduky.Adaptador.RecyclerViewAdapter;
import com.juanpablo.eduky.R;

import java.util.ArrayList;

/**
 * Created by PERSONAL on 20/07/2018.
 */

public class AudioLibroAdapter extends RecyclerView.Adapter<AudioLibroAdapter.AudioLibroViewHolder> {

    ArrayList<AudioLibro> audioLibroList;

    public AudioLibroAdapter(){
    }

    public AudioLibroAdapter(ArrayList<AudioLibro> audioLibroList){
        this.audioLibroList = audioLibroList;
    }

    @Override
    public AudioLibroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.card_view_audiolibro, parent, false);

        return new AudioLibroViewHolder(view);

    }

    @Override
    public void onBindViewHolder(AudioLibroViewHolder holder, int position) {

        holder.videoWeb.loadData( audioLibroList.get(position).getUrlAL(), "text/html" , "utf-8" );

    }

    @Override
    public int getItemCount() {
        return audioLibroList.size();
    }

    public class AudioLibroViewHolder extends RecyclerView.ViewHolder{

        WebView videoWeb;

        public AudioLibroViewHolder(View itemView) {
            super(itemView);

            videoWeb = (WebView) itemView.findViewById(R.id.webview_audiolibro);
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient() {

            } );
        }
    }

    public void setFilter(ArrayList<AudioLibro> youtubeVideos){
        audioLibroList =new ArrayList<>();
        audioLibroList.addAll(youtubeVideos);
        notifyDataSetChanged();
    }

}
