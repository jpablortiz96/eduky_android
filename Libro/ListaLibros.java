package com.juanpablo.eduky.Libro;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.juanpablo.eduky.Adaptador.RecyclerViewAdapter;
import com.juanpablo.eduky.Educador.FirebaseReferences;
import com.juanpablo.eduky.R;
import com.juanpablo.eduky.Videos.ListaVideos;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by PERSONAL on 31/03/2018.
 */

public class ListaLibros extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView myrv;
    ArrayList<Book> libros;
    RecyclerViewAdapter adapta;
    DatabaseReference mDataBaseReference;
    private ProgressDialog progressDialog;
    FirebaseDatabase database;
    private static final String TAG = "PostDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_libros);



        progressDialog = new ProgressDialog(ListaLibros.this);
        progressDialog.setMessage("Cargando libros...");
        progressDialog.show();

        database = FirebaseDatabase.getInstance();
        mDataBaseReference = database.getReference(FirebaseReferences.LIBROS)
                .child(FirebaseReferences.CATEGORIAS);

        myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        myrv.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        myrv.setLayoutManager(layoutManager);

        libros = new ArrayList<>();
        adapta = new RecyclerViewAdapter(libros, this);

        cargarLibros();
        myrv.setAdapter(adapta);

    }

    public void cargarLibros(){

        mDataBaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                libros.removeAll(libros);
                for(DataSnapshot snapshot:
                        dataSnapshot.getChildren()){
                    Book book = snapshot.getValue(Book.class);
                    libros.add(book);
                    shuffleArray(libros);
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
        inflater.inflate(R.menu.menu_buscador_book,menu);
        MenuItem item = menu.findItem(R.id.buscador_book);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                cargarLibros();
                myrv.setAdapter(adapta);
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                cargarLibros();
                myrv.setAdapter(adapta);
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
            ArrayList<Book> listaLibros = filter(libros,newText);
            adapta.setFilter(listaLibros);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private ArrayList<Book> filter(ArrayList<Book> libros, String texto){
        ArrayList<Book> listaFiltrada = new ArrayList<>();

        try {
            texto = texto.toLowerCase();
            for (Book book: libros){
                String bookNom = book.getNombreLibro().toLowerCase();
                if(bookNom.contains(texto)){
                    listaFiltrada.add(book);
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