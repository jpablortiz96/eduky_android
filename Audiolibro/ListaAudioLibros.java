package com.juanpablo.eduky.Audiolibro;

import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juanpablo.eduky.Educador.FirebaseReferences;
import com.juanpablo.eduky.Libro.ListaLibros;
import com.juanpablo.eduky.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by PERSONAL on 20/07/2018.
 */

public class ListaAudioLibros extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView recyclerViewAudioLibro;
    DatabaseReference mDataBaseReferenceVideo;
    FirebaseDatabase databasevideo;
    private ProgressDialog progressDialog;
    AudioLibroAdapter adapta;
    ArrayList<AudioLibro> youtubeAudioLibros = new ArrayList<>();
    private static final String TAG = "PostDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_audiolibros);


        progressDialog = new ProgressDialog(ListaAudioLibros.this);
        progressDialog.setMessage("Cargando audiolibros...");
        progressDialog.show();

        databasevideo = FirebaseDatabase.getInstance();
        mDataBaseReferenceVideo = databasevideo.getReference(FirebaseReferences.AUDIOLIBROS)
                .child(FirebaseReferences.TIPO_AUDIOLIBROS);

        recyclerViewAudioLibro = (RecyclerView) findViewById(R.id.recyclerview_audiolibros);
        recyclerViewAudioLibro.setHasFixedSize(true);
        recyclerViewAudioLibro.setLayoutManager( new LinearLayoutManager(this));

        youtubeAudioLibros = new ArrayList<>();
        cargarVideos();

        adapta = new AudioLibroAdapter(youtubeAudioLibros);
        recyclerViewAudioLibro.setAdapter(adapta);


    }

    public void cargarVideos(){

        mDataBaseReferenceVideo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                youtubeAudioLibros.removeAll(youtubeAudioLibros);
                for(DataSnapshot snapshot:
                        dataSnapshot.getChildren()){
                    AudioLibro audiolibro = snapshot.getValue(AudioLibro.class);
                    youtubeAudioLibros.add(audiolibro);
                    shuffleArray(youtubeAudioLibros);
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
        inflater.inflate(R.menu.menu_buscador_audiolibro,menu);
        MenuItem item = menu.findItem(R.id.menu_buscador_audiolibro);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                cargarVideos();
                recyclerViewAudioLibro.setAdapter(adapta);
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                cargarVideos();
                recyclerViewAudioLibro.setAdapter(adapta);
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
            ArrayList<AudioLibro> listaVideos = filter(youtubeAudioLibros,newText);
            adapta.setFilter(listaVideos);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private ArrayList<AudioLibro> filter(ArrayList<AudioLibro> audiolibros, String texto){
        ArrayList<AudioLibro> listaFiltrada = new ArrayList<>();

        try {
            texto = texto.toLowerCase();
            for (AudioLibro audioLibro: audiolibros){
                String videoNom = audioLibro.getNombreAL().toLowerCase();
                if(videoNom.contains(texto)){
                    listaFiltrada.add(audioLibro);
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
