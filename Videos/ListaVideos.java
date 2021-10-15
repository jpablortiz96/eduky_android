package com.juanpablo.eduky.Videos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juanpablo.eduky.Educador.FirebaseReferences;
import com.juanpablo.eduky.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * Created by PERSONAL on 22/04/2018.
 */

public class ListaVideos extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView recyclerViewVideo;
    DatabaseReference mDataBaseReferenceVideo;
    FirebaseDatabase databasevideo;
    private ProgressDialog progressDialog;
    VideoAdapter adapta;
    ArrayList<Video> youtubeVideos = new ArrayList<>();
    private static final String TAG = "PostDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_videos);


        progressDialog = new ProgressDialog(ListaVideos.this);
        progressDialog.setMessage("Cargando videos...");
        progressDialog.show();

        databasevideo = FirebaseDatabase.getInstance();
        mDataBaseReferenceVideo = databasevideo.getReference(FirebaseReferences.VIDEOS)
                .child(FirebaseReferences.CATEGORIAS_VIDEOS);

        recyclerViewVideo = (RecyclerView) findViewById(R.id.recyclerview_videos);
        recyclerViewVideo.setHasFixedSize(true);
        recyclerViewVideo.setLayoutManager( new LinearLayoutManager(this));

        youtubeVideos = new ArrayList<>();
        cargarVideos();

        adapta = new VideoAdapter(youtubeVideos);
        recyclerViewVideo.setAdapter(adapta);

    }

    public void cargarVideos(){

        mDataBaseReferenceVideo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                youtubeVideos.removeAll(youtubeVideos);
                for(DataSnapshot snapshot:
                        dataSnapshot.getChildren()){
                    Video video = snapshot.getValue(Video.class);
                    youtubeVideos.add(video);
                    shuffleArray(youtubeVideos);
                }
                adapta.notifyDataSetChanged();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buscador_video,menu);
        MenuItem item = menu.findItem(R.id.menu_buscador_video);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                cargarVideos();
                recyclerViewVideo.setAdapter(adapta);
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                cargarVideos();
                recyclerViewVideo.setAdapter(adapta);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        try {
            ArrayList<Video> listaVideos = filter(youtubeVideos,newText);
            adapta.setFilter(listaVideos);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private ArrayList<Video> filter(ArrayList<Video> videos, String texto){
        ArrayList<Video> listaFiltrada = new ArrayList<>();

        try {
            texto = texto.toLowerCase();
            for (Video video: videos){
                String videoNom = video.getNombreVideo().toLowerCase();
                if(videoNom.contains(texto)){
                    listaFiltrada.add(video);
                    shuffleArray(listaFiltrada);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return listaFiltrada;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private static ArrayList shuffleArray(ArrayList array)

    {
        int index;
        Object temp;
        Random random = new Random();
        for (int i = array.size() - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array.get(index);
            array.remove(index);
            array.add(temp);
        }
        return array;
    }
}