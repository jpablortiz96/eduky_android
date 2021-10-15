package com.juanpablo.eduky;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.juanpablo.eduky.Estudiante.FirebaseReferences;
import com.juanpablo.eduky.Estudiante.Usuario;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextInputLayout nombre_login, correo_login, confi_correo;
    private Button btn_resgistrar;
    CallbackManager callbackManager;
    LoginButton loginButton;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 777;
    GoogleApiClient googleApiClient;
    SignInButton btnGoogle;
    ImageButton fb, insta;
    UnderlineSpan underlineSpan;
    TextView txt_politica;
    LinearLayout ll_politica;
    CheckBox cb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        cb = (CheckBox)findViewById(R.id.cb_politica);

        btnGoogle = (SignInButton) findViewById(R.id.btn_google);
        btnGoogle.setSize(SignInButton.SIZE_WIDE);
        btnGoogle.setColorScheme(SignInButton.COLOR_DARK);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setReadPermissions("user_friends");

        ll_politica = (LinearLayout)findViewById(R.id.ll_politica);
        ll_politica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://drive.google.com/file/d/0B7rUj1fgw-rhd1J2MlRGV2I2dzNpUEpZNlFONHhfOXY2X2JZ/view?usp=sharing";
                Intent pol = new Intent(Intent.ACTION_VIEW);
                pol.setData(Uri.parse(url));
                startActivity(pol);
            }
        });

        txt_politica = (TextView)findViewById(R.id.txt_politica);
        SpannableString sp = new SpannableString("He leído y acepto la Política de Privacidad");
        underlineSpan = new UnderlineSpan();
        TextPaint tp = new TextPaint();
        tp.setColor(Color.BLUE);
        sp.setSpan(underlineSpan, 0, sp.length(), 0);
        txt_politica.setText(sp);

        nombre_login = (TextInputLayout) findViewById(R.id.name_login);
        correo_login = (TextInputLayout) findViewById(R.id.email_login);
        confi_correo = (TextInputLayout) findViewById(R.id.cofi_email_login);
        btn_resgistrar = (Button) findViewById(R.id.btn_registro);
        fb = (ImageButton) findViewById(R.id.facebook_logo);
        insta = (ImageButton) findViewById(R.id.instagram_logo);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference estudiantesRef = database.getReference(FirebaseReferences.ESTUDIANTES_REFERENCE);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookId = "fb://page/387149005060259";
                String urlPage = "http://www.facebook.com/mypage";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId )));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/edukyapp/?hl=es-la");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/_u/edukyapp/?hl=es-la")));
                }
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    forwardZoom();
                }else{

                }
            }
        };

        btn_resgistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nom = nombre_login.getEditText().getText().toString();
                final char[] arrayCharNom = nom.toCharArray();
                final String ema = correo_login.getEditText().getText().toString();
                final String confi = confi_correo.getEditText().getText().toString();
                if (!nom.equals("")&&!ema.equals("")&&!confi.equals("")){
                    if(!confi.equals(ema)){
                        Toast.makeText(MainActivity.this, R.string.correoMalo_toast, Toast.LENGTH_LONG).show();
                    }else{
                        if(arrayCharNom.length>6){
                            if(!esEmailCorrecto(ema) && !esEmailCorrecto(confi)){
                                Toast.makeText(MainActivity.this, R.string.correoIncorrecto_toast, Toast.LENGTH_LONG).show();
                            }
                            else{
                                if(cb.isChecked()){
                                    Usuario user = new Usuario(nom,ema);
                                    estudiantesRef.child(FirebaseReferences.USUARIO_REFERENCE).push().setValue(user);
                                    registrar(ema,nom);
                                    iniciar(ema,nom);
                                    Toast.makeText(MainActivity.this, R.string.usuarioRegistrado_toast, Toast.LENGTH_LONG).show();
                                    forwardZoom();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, R.string.politica, Toast.LENGTH_LONG).show();
                                }

                            }
                        }else{
                            Toast.makeText(MainActivity.this, R.string.seisCaracteres_toast, Toast.LENGTH_LONG).show();
                        }
                    }
                }else {
                    Toast.makeText(MainActivity.this, R.string.todosCampos_toast, Toast.LENGTH_LONG).show();
                }
            }
        });


            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException exception) {
                }
            });


            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            handleFacebookAccesToken(loginResult.getAccessToken());
                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException exception) {
                        }
                    });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb.isChecked()){
                    signIn();
                }else{
                    Toast.makeText(MainActivity.this, R.string.politica, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleFacebookAccesToken(AccessToken accessToken) {
        if (cb.isChecked()) {
            AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, R.string.bienvenido, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Sesión", task.getException().getMessage() + "");
                    }
                }
            });
        }else{
            Toast.makeText(MainActivity.this, R.string.politicafb, Toast.LENGTH_LONG).show();
        }

    }

    private void registrar(String email, String nom){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,nom).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, R.string.bienvenido, Toast.LENGTH_SHORT).show();
                    }else {
                        Log.e("Sesión", task.getException().getMessage()+"");
                    }
                }
            });
        }

        private void iniciar(String email, String nom){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,nom);
        }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    protected void onStop(){
        super.onStop();
        if(mAuthStateListener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    protected static boolean esEmailCorrecto(String email) {

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);

        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }

    public void forwardZoom()
    {
        Intent intent = new Intent(this, ListaEducadores.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(googleSignInResult);
        }

    }

    private void handleSignInResult(GoogleSignInResult googleSignInResult) {
        if(googleSignInResult.isSuccess()){
            firebaseAuthWithGoogle(googleSignInResult.getSignInAccount());
        }else {
            Toast.makeText(this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Error en Firebase", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


