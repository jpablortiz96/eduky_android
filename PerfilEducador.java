package com.juanpablo.eduky;

import android.*;
import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PerfilEducador extends AppCompatActivity {

    private String nombre, ocupacion, materias, descripcion, imagen;
    private String edad, latitud, longitud, celular, correo;
    private Double lat, lon;
    private String correoAuth;
    private InterstitialAd mInterstitialAd;

    private Toolbar toolbar;

    private ImageView imageEducador;
    private TextView txtProfesion, txtEdad, txtDescripcion, txtAsignatura, txtCorreo, txtCelular, txtDireccion;

    private Button btnLlamar, btnChat;

    FirebaseAuth.AuthStateListener mAuthStateListener;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 124;
    int hasAccessCallPhone;

    private void accessPermission(){
        hasAccessCallPhone = ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE);

        if(hasAccessCallPhone != PackageManager.PERMISSION_GRANTED ){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)){

            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_educador);
        accessPermission();

        MobileAds.initialize(this,"ca-app-pub-3909964761084078~7936098913");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("a-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        nombre = getIntent().getStringExtra("eduNombre");
        materias = getIntent().getStringExtra("eduMate");
        ocupacion = getIntent().getStringExtra("eduProfe");
        descripcion = getIntent().getStringExtra("eduDescri");
        imagen = getIntent().getStringExtra("eduImagen");
        edad = getIntent().getStringExtra("eduEdad");
        latitud = getIntent().getStringExtra("eduLat");
        longitud = getIntent().getStringExtra("eduLon");
        celular = getIntent().getStringExtra("eduCel");
        correo = getIntent().getStringExtra("eduCorreo");

        lat = Double.parseDouble(latitud);
        lon = Double.parseDouble(longitud);

        imageEducador = (ImageView) findViewById(R.id.imagen_perfil);
        txtProfesion = (TextView) findViewById(R.id.txtProfesion_educador);
        txtEdad = (TextView) findViewById(R.id.txtEdad);
        txtDescripcion = (TextView) findViewById(R.id.txtdescripcion);
        txtAsignatura = (TextView) findViewById(R.id.txtAsignatura);
        txtCorreo = (TextView) findViewById(R.id.txtCorreo);
        txtCelular = (TextView) findViewById(R.id.txtCelular);
        txtDireccion = (TextView) findViewById(R.id.txtDireccion);
        btnChat = (Button) findViewById(R.id.btn_chat);
        btnLlamar = (Button) findViewById(R.id.btn_llamar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nombre);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    correoAuth = user.getEmail();
                } else {

                }
            }
        };

        Glide.with(PerfilEducador.this)
                .load(imagen)
                .asBitmap()
                .into(new BitmapImageViewTarget(imageEducador) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(PerfilEducador.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageEducador.setImageDrawable(circularBitmapDrawable);
                    }
                });

        txtProfesion.setText(ocupacion);
        txtEdad.setText(edad);
        txtAsignatura.setText(materias);
        txtDescripcion.setText(descripcion);
        txtCorreo.setText(correo);
        txtCelular.setText(celular);
        setLocation();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                styleSnackbar(view);
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirWhatsApp(celular);
            }
        });

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog();
            }
        });

    }

    public void setLocation() {
        if (lat != 0.0 && lon != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(lat, lon, 1);
                if (!list.isEmpty()) {
                    Address address = list.get(0);
                    txtDireccion.setText(address.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void sendEmail() {
        String[] TO = {correo}; //aquí pon tu correo

        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Eduky - Inquietud de usuario");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Escribe aquí lo que quieras preguntarle al educador " + nombre);

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    public void styleSnackbar(View view) {
        Snackbar snackbar = Snackbar.make(view, R.string.enviar_correo, Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.parseColor("#0277BD"))
                .setAction(R.string.si, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEmail();
                    }
                });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        View snackBarView = snackbar.getView();

        snackBarView.setBackgroundColor(Color.parseColor("#0277BD"));

        TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.parseColor("#FFFFFF"));

        snackbar.show();
    }

    private void abrirWhatsApp(String telefono) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
        intent.putExtra("jid", PhoneNumberUtils.stripSeparators("57" + telefono) + "@s.whatsapp.net");
        startActivity(intent);

    }

    public void onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilEducador.this);
        builder.setMessage(getString(R.string.desea_llamar) + nombre + "?")
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+celular));
                        if (ActivityCompat.checkSelfPermission(PerfilEducador.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }
}


