package com.juanpablo.eduky;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.juanpablo.eduky.Adaptador.Adapter;
import com.juanpablo.eduky.Audiolibro.ListaAudioLibros;
import com.juanpablo.eduky.Educador.Educador;
import com.juanpablo.eduky.Educador.FirebaseReferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juanpablo.eduky.Libro.ListaLibros;
import com.juanpablo.eduky.Videos.ListaVideos;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;



import java.util.ArrayList;
import java.util.Random;

public class ListaEducadores extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView mRecyclerView;
    ArrayList<Educador> educadores;
    Adapter adapta;
    DatabaseReference mDataBaseReference;
    LinearLayout info;
    private static final String TAG = "PostDetailActivity";
    FloatingActionButton btnBook, btnVideo, btnAudioLibro;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_educadores);

        MobileAds.initialize(this, "ca-app-pub-3909964761084078~7936098913");

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btnBook = (FloatingActionButton) findViewById(R.id.btnBook);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaEducadores.this, ListaLibros.class);
                startActivity(intent);
            }
        });

        btnVideo = (FloatingActionButton) findViewById(R.id.btnVideo);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaEducadores.this, ListaVideos.class);
                startActivity(intent);
            }
        });

        btnAudioLibro = (FloatingActionButton) findViewById(R.id.btnAudioLibro);
        btnAudioLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaEducadores.this, ListaAudioLibros.class);
                startActivity(intent);
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        info = (LinearLayout) findViewById(R.id.layout_lista);

        educadores = new ArrayList<>();
        adapta = new Adapter(educadores, this);

        mDataBaseReference = FirebaseDatabase.getInstance().getReference(FirebaseReferences.PROFESORES)
                .child(FirebaseReferences.PROFESORES_INFO);


    }

    public void cargarProfe(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                educadores.removeAll(educadores);
                for(DataSnapshot snapshot:
                        dataSnapshot.getChildren()){
                    Educador educa = snapshot.getValue(Educador.class);
                    educadores.add(educa);
                }
                adapta.notifyDataSetChanged();
                btnBook.setVisibility(View.INVISIBLE);
                btnVideo.setVisibility(View.INVISIBLE);
                btnAudioLibro.setVisibility(View.INVISIBLE);
                mAdView.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDataBaseReference.addValueEventListener(postListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        salirApp();
    }

    public void salirApp(){
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buscador,menu);
        MenuItem item = menu.findItem(R.id.buscador);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                cargarProfe();
                mRecyclerView.setAdapter(adapta);
                info.setVisibility(View.INVISIBLE);
                btnBook.setVisibility(View.INVISIBLE);
                btnVideo.setVisibility(View.INVISIBLE);
                btnAudioLibro.setVisibility(View.INVISIBLE);
                mAdView.setVisibility(View.INVISIBLE);
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                educadores.removeAll(educadores);
                adapta.setFilter(educadores);
                info.setVisibility(View.VISIBLE);
                btnBook.setVisibility(View.VISIBLE);
                btnVideo.setVisibility(View.VISIBLE);
                btnAudioLibro.setVisibility(View.VISIBLE);
                mAdView.setVisibility(View.VISIBLE);
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
            ArrayList<Educador> listaFilstrada = filter(educadores,newText);
            adapta.setFilter(listaFilstrada);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private ArrayList<Educador> filter(ArrayList<Educador> educadores, String texto){
        ArrayList<Educador> listaFiltrada = new ArrayList<>();

        try {
            texto = texto.toLowerCase();
            for (Educador educador: educadores){
                String eduNom = educador.getNomPro().toLowerCase();
                String eduApellido = educador.getApePro().toLowerCase();
                String eduCorreo = educador.getCorreoPro().toLowerCase();
                String eduDescri = educador.getDescriPro().toLowerCase();
                String eduAsig = educador.getAsigPro().toLowerCase();
                String eduOcupa = educador.getOcupaPro().toLowerCase();

                if(eduNom.contains(texto)||eduApellido.contains(texto)||eduCorreo.contains(texto)||
                        eduDescri.contains(texto)||eduAsig.contains(texto)||eduOcupa.contains(texto)){
                    listaFiltrada.add(educador);
                    btnBook.setVisibility(View.INVISIBLE);
                    btnVideo.setVisibility(View.INVISIBLE);
                    btnAudioLibro.setVisibility(View.INVISIBLE);
                    mAdView.setVisibility(View.INVISIBLE);
                    shuffleArray(listaFiltrada);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return listaFiltrada;
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
