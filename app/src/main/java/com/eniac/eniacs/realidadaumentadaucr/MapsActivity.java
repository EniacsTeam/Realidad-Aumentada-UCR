package com.eniac.eniacs.realidadaumentadaucr;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.eniac.eniacs.realidadaumentadaucr.R.id.map;
import static com.eniac.eniacs.realidadaumentadaucr.R.id.textView;
import static com.wikitude.architect.CameraPreviewBase.m;


/**
 * Esta clase representa un mapa de Google. Contiene metodos para solicitar y manejar permisos de localizacion, para luego con ellos ayudar
 * al usuario de la aplicacion a moverse mas facilmente en el mundo real a partir de la informacion presentada en el mapa (i.e. puntos de interes).
 *
 * @author  EniacsTeam
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,

        LocationListener, SensorEventListener, NavigationView.OnNavigationItemSelectedListener, TextToSpeech.OnInitListener {


    SearchView searchView;
    SearchManager searchManager;
    DrawerLayout mDrawerLayout;
    private boolean isFirst = true;
    private GoogleMap.OnMarkerClickListener markerListener;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Location mCurrentLocation;
    private Rutas mRuta;
    private final String[] StringPermisos = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int iconVec[] = new int[28];
    private final String wordVec[] = {"derecho", "oficbecas", "biblio", "arqui", "comedor", "inge", "fisicamate", "generales", "biblio", "preescolar",
            "letras", "centinform", "geologia", "economicas", "ecci", "odonto", "medicina", "farmacia", "microbiologia", "biolo", "quimica", "musica",
            "artes", "educa", "bosque", "mariposario", "plaza", "pretil"};//28
    private Paint paint;

    private Marker marcasTodas[] = new Marker[28];
    private Map<Integer, Location> res;
    private int apuntAnterior = -1;
    private boolean correrApuntado = false;
    private JSONObject rutasDetalle[] = new JSONObject[3];
    FloatingActionButton fab;
    Animation cargafab;
    Animation quitafab;



    NavigationView navigationView;
    /*para los sensores*/
    private float[] rotationMatrix;
    private float[] orientationVals;
    private SensorManager mSensorManager;//control de sensores
    private Sensor distanceVector;



    private SlidingUpPanelLayout mLayout;
    private static final String TAG = "MapsActivity";
    List<String> array_list;
    TextView textView ;
    ListView listview;
    ImageButton imageButton;
    private  Marker marcador_actual;
    private int indice_actual = -1;
    private int cantidad_rutas = 0;
    boolean busqueda = false;

    //para navegar prueba
    private int rutaElegida=0;
    private int pasoSgt=0;
    private boolean navegar= false;
    Location pasoSgte = new Location("Paso Sgt");
    String[] resp;
    TextToSpeech tts;
    private LatLng pos_actual;
    ArrayList<ArrayList<Polyline>> rutas;
    boolean flagRutas;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }


    /**
     * Este metodo es usado para inicializar la actividad. Se define la interfaz de usuario, se instancian clases auxiliares, se crea un
     * servicio de solicitud de localizacion, se inicializan referencias a los iconos de los marcadores para los edificios, se obtiene el
     * mapa listo para usarlo, se recuperan las herramientas de interfaz de usuario con las que se quieren interactuar y se inicializan
     * las variables necesarias para manejar el sensor de rotacion del dispositivo.
     *
     * @param savedInstanceState estado guardado de la aplicacion un valor {@code null} indica que la actividad no debe ser recreada a partir de información previa.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mRuta = new Rutas();
        llenarIconVec();
        if (mGoogleApiClient == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }
        //requestPermission();
        createLocationRequest();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        cargafab = AnimationUtils.loadAnimation(this, R.anim.fab_show);
        fab.startAnimation(cargafab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(MapsActivity.this, WikitudeActivity.class));

                quitafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide);
                fab.startAnimation(quitafab);
                fab.setVisibility(View.GONE);
                startActivity(new Intent(MapsActivity.this, WikitudeActivity.class));

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });

        rotationMatrix = new float[9];
        orientationVals = new float[3];
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);//obtenemos el servicio
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        distanceVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP));


        init();            // call init method
        panelListener(); // Call paneListener method
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        //mLayout.setAnchorPoint(0.3f); //Para que solo se vea las 3 rutas y no se expanda completamente el panel
        mLayout.setTouchEnabled(false); //Para que el usuario no pueda deslizar el panel

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if(navegar == true)
                {
                    new AlertDialog.Builder(drawerView.getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.quit)
                            .setMessage(R.string.really_quit)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    navegar_off(1); //salir de modo navegacion
                                }

                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    mDrawerLayout.closeDrawers();

                                }

                            })
                            .show()
                            .setCanceledOnTouchOutside(false);

                }
                if (isFirst) {
                    isFirst = false;
                    configureSearch();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        tts = new TextToSpeech(this, this);
        Locale loc = new Locale ("us", "ESP");
        tts.setLanguage(loc);

        flagRutas=false;
        rutas=new ArrayList<ArrayList<Polyline>>();


    }

    /**
     * Este metodo maneja los {@code Intent} que son retornados al hacer una busqueda y desempeña una funcion especifica dependiendo
     * si es busqueda textual o por sugerencias.
     *
     * @param intent el nuevo {@code Intent} que fue empezado para la actividad.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

            if (!TextUtils.isEmpty(query)) {
                String[] mProjection = {SuggestionProvider.Edificios.COL_ID, SuggestionProvider.Edificios.COL_NOMBRE};
                String mSelectionClause = SuggestionProvider.Edificios.COL_NOMBRE + " LIKE ? ";
                String[] mSelectionArgs = new String[]{"%"+query+"%"};
                Cursor mCursor = getContentResolver().query(
                        SuggestionProvider.CONTENT_URI,
                        mProjection,
                        mSelectionClause,
                        mSelectionArgs,
                        null
                );

                boolean found = false;
                if(mCursor != null) {
                    if(mCursor.getCount() == 1) {
                        if(mCursor.moveToFirst()) {
                            found = true;
                            int result = mCursor.getInt(mCursor.getColumnIndex(SuggestionProvider.Edificios.COL_ID));
                            Toast.makeText(this, "Cursor by: " + Integer.toString(result), Toast.LENGTH_SHORT).show();

                            mDrawerLayout.closeDrawers();
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(marcasTodas[result-1].getPosition()), 250, null);
                            indice_actual = result -1;
                            quitafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide);
                            fab.startAnimation(quitafab);
                            fab.setVisibility(View.GONE);
                            borrarRutas(10);
                            listview.setAdapter(null);
                            textView.setText(marcasTodas[result-1].getTitle());

                            datos(result-1);

                            InputMethodManager inputManager = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                            //mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            busqueda = true;
                            marcador_actual = marcasTodas[result-1];

                        }
                    }
                }
                if (!found) {
                    Toast.makeText(this, query+" no es válido.", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
            //Toast.makeText(this, "Suggestion: "+ uri, Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawers();
            indice_actual = (Integer.parseInt(uri)-1);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(marcasTodas[indice_actual].getPosition()), 250, null);
            quitafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide);
            fab.startAnimation(quitafab);
            fab.setVisibility(View.GONE);
            borrarRutas(10);
            listview.setAdapter(null);
            datos(indice_actual);
            textView.setText(marcasTodas[indice_actual].getTitle());
            marcador_actual = marcasTodas[indice_actual];
            //mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            busqueda = true;



        }
    }

    /**
     * Configura la barra de busqueda con los parametros del {@code SearchManager} respectivo.
     */
    private void configureSearch() {
        // Get the SearchView and set the searchable configuration
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search);
        // Current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    /**
     * Si la instancia de {@code GoogleApiClient} tiene un valor distinto de {@code null} es necesario reconectar la aplicación
     * a los servicios de Google.
     */
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }

    /**
     * Metodo llamado al hacer uso del boton de retorno del dispositivo. Su funcion si el menu esta abierto es cerrarlo,
     * en caso contrario llama el metodo de la superclase.
     *
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Cuando la actividad ya no es visible al usuario detiene la conexión a los servicios de Google.
     */
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * Construye una instancia de {@code GoogleApiClient} el cual provee un punto de entrada a todos los servicios de Google Play.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    /**
     * Permite definir parametro de calidad de servicio para las solicitudes de localizacion.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Manipula el mapa una vez que esta disponible. Se habilitan herramientas como myLocation y el compass.
     * <p>
     * Esta retrollamada es desencadenada cuando el mapa esta listo para ser usado.
     * <p>
     * Si los Google Play services no estan instalados en el dispositivo, se le solicitara al usuario instalarlo dentro del
     * SupportMapFragment. Este metodo solo se desencadenara una vez que el usuario ha instalado los Google Play services
     * y ha retornado a la aplicacion.
     *
     * @param googleMap una instancia no nula de un GoogleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = MapsActivity.this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMinZoomPreference(13);

        for (int i = 0; i < mRuta.edificios.length; ++i) {
            LatLng pos = new LatLng(mRuta.elatitud[i], mRuta.elonguitud[i]);
            marcasTodas[i] = mMap.addMarker(new MarkerOptions().position(pos).alpha(0.3f)
                    .title(mRuta.edificios[i]).icon(BitmapDescriptorFactory.fromResource(iconVec[i])));

        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(navegar != true)
                {
                    //rutaElegida=0;
                    pasoSgt=0;
                    navegar= false;

                    quitafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide);
                    fab.startAnimation(quitafab);
                    fab.setVisibility(View.GONE);
                    textView.setText(marker.getTitle());
                    if(rutas.size() != 0)
                    {
                        borrarRutas(10);
                    }

                    marcador_actual = marker;
                    double lat = marcador_actual.getPosition().latitude;
                    Boolean condicion = false;
                    int cont = -1;
                    while (condicion==false)
                    {
                        ++cont;
                        if(mRuta.elatitud[cont] == lat)
                        {
                            condicion = true;
                        }
                    }
                    indice_actual = cont;
                    datos(indice_actual);
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    marker.showInfoWindow();
                    marcador_actual = marker;
                }
                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {
                if(navegar != true)
                {
                    if(rutas.size() != 0)
                    {
                        borrarRutas(10);
                    }
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    }
                    if(fab.getVisibility() != View.VISIBLE){
                        cargafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show);
                        fab.setVisibility(View.VISIBLE);
                        fab.startAnimation(cargafab);
                    }
                }
            }
        });

    }


    /**
     * Metodo encargado de pedir que se le conceda a la aplicacion ciertos permisos.
     */
    private void requestPermission() {
        //Preguntar por permiso
        ActivityCompat.requestPermissions(this, StringPermisos, 0);
    }


    /**
     * Verifica que tenga los permisos apropiados para acceder a la ubicación de usuario.
     *
     * @param  requestCode  codigo del permiso
     * @param  permissions  los permisos que se solicitan
     * @param  grantResults  indica si permiso es concedido o no
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //se crea bien
                } else {
                    Toast.makeText(this, "Need your location", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }


    /**
     * Llena el vector con los identificadores de cada icono que se utiliza para representar a cada marcador.
     */
    private void llenarIconVec() {
        for (int i = 0; i < 28; ++i) {
            iconVec[i] = getResources().getIdentifier(wordVec[i], "drawable", getPackageName());
        }
    }


    /**
     * Este metodo se encarga de agregar los marcadores respectivos a los 3 edificios mas cercanos a la posicion del usuario.
     */
    private void addMarkers() {
        res = mRuta.edificiosMasCercanos(mCurrentLocation);

        Iterator<Integer> it = res.keySet().iterator();

        int indice;
        while (it.hasNext())
        {   indice = it.next();
            marcasTodas[indice].setAlpha(3);
        }
        correrApuntado = true;
    }

    /**
     * Actualiza posicion de usuario y actualiza vista de usuario.
     * @param  bundle Conjunto de datos proveidos a los clientes por los Google Play services.
     *                Podria ser {@code null} si ningun contenido es brindado por el servicio.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = MapsActivity.this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            mCurrentLocation = mLastLocation;
            if (mLastLocation != null) {
                LatLng posicion = new LatLng(9.937604, -84.050606);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15.7f));
            } else {
                Toast.makeText(this, "Se cayó en el onConnected", Toast.LENGTH_LONG).show();
            }
        }

        startLocationUpdates();
    }


    /**
     * Si se han concedido los permisos de acceso a la localizacion permite iniciar el rastreo de la posición del usuario.
     */
    protected void startLocationUpdates() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = MapsActivity.this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }

    }


    /**
     * Permite re-conectarse a los servicios de google en caso de perder conexion.
     *
     * @param  i la razon de la desconexion.
     */
    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    /**
     * Este metodo es llamado cuando hubo un error conectando el cliente a los servicios de Google.
     *
     * @param  connectionResult  resultado de la conexion
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    /**
     * Actualiza la vista cada vez que se cambia la posicion del usuario. Constantemente actualiza los marcadores que deben ser presentados.
     *
     * @param  location  ubicacion del usuario
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (res != null) {
            Iterator<Integer> it = res.keySet().iterator();
            int indice;
            while (it.hasNext())
            {   indice = it.next();
                marcasTodas[indice].setAlpha(0.3f);
            }
        }
        addMarkers();
        if(navegar) {
            /*Para que la camara siga al usuario*/
            pos_actual = new LatLng(location.getLatitude(),location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(pos_actual)
                    .bearing(location.getBearing())
                    .zoom(18)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            actualizarRuta(current);

            if (mCurrentLocation.distanceTo(pasoSgte) < 15 || pasoSgt==0 ) {

                resp = instruccionesRuta(rutaElegida, pasoSgt);
                if(resp[0]!= "-1") {
                    ++pasoSgt; //hay que ponerlo en cero cuando se haga el borrar rutas
                    pasoSgte.setLatitude(Double.parseDouble(resp[2]));
                    pasoSgte.setLongitude(Double.parseDouble(resp[3]));

                    textView.setText(Html.fromHtml(resp[4]));
                    String aux = textView.getText().toString();
                    textView.setTextSize(15);
                    tts.speak(aux,TextToSpeech.QUEUE_FLUSH, null);
                   // Toast.makeText(MapsActivity.this, resp[4], Toast.LENGTH_SHORT).show();
                }

            }
        }

    }

    /**
     * Este metodo para las actualizaciones de localizacion pues la actividad sera pausada, ya que esta se ira al "background".
     */
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        mSensorManager.unregisterListener(this);
    }


    /**
     * Metodo para detener el rastreo de posicion y de esta manera no incurrir en un gasto de procesamiento y bateria innecesario.
     */
    protected void stopLocationUpdates() {

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }


    /**
     * Metodo para resumir la actividad.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
        mSensorManager.registerListener(this, distanceVector, SensorManager.SENSOR_DELAY_UI);
        if(busqueda == true){
            busqueda = false;
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }else{
            if(fab.getVisibility() != View.VISIBLE){
                cargafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show);
                fab.setVisibility(View.VISIBLE);
                fab.startAnimation(cargafab);
            }
        }

    }

    /**
     * Este metodo es llamado cuando hay un nuevo evento del sensor.
     * <p>
     * Se encarga de constantemente escuchar cambios en la rotacion del dispositivo y revisar si en la direccion apuntada existe
     * un marcador en las cercanias para resaltarlo dentro del mapa.
     *
     * @param event el evento ocurrido
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (correrApuntado) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrix);
            SensorManager.getOrientation(rotationMatrix, orientationVals);
            orientationVals[0] = (float) Math.toDegrees(orientationVals[0]);
            float azimuth = (orientationVals[0] + 360) % 360;

            int indice = mRuta.edificioApuntado(azimuth);

            if (apuntAnterior != -1 && indice != apuntAnterior) {
                marcasTodas[apuntAnterior].setIcon(BitmapDescriptorFactory.fromResource(iconVec[apuntAnterior]));

            }
            if (indice != -1) {
                Bitmap ob = BitmapFactory.decodeResource(this.getResources(), iconVec[indice]);
                Bitmap obm = Bitmap.createBitmap(ob.getWidth(), ob.getHeight(), ob.getConfig());
                Canvas canvas = new Canvas(obm);
                canvas.drawBitmap(ob, 0f, 0f, paint);
                marcasTodas[indice].setIcon(BitmapDescriptorFactory.fromBitmap(obm));
                apuntAnterior = indice;
            }
        }
    }

    /**
     * Metodo llamado cuando la precision del sensor registrado ha cambiado.
     *
     * @param sensor el sensor registrado
     * @param i la nueva precision del sensor
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * Initialization of the textview and SlidingUpPanelLayout
     */
    public void init(){

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        listview = (ListView) findViewById(R.id.soy_lista);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //al presionar un item del listview, me dirige en modo
                                                                                               //navegacion a ruta seleccionada
                rutaElegida=position;
                navegar= true;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                imageButton.setEnabled(false);
                imageButton.setVisibility(View.INVISIBLE);
                borrarRutas(position);
                mMap.getUiSettings().setCompassEnabled(true);
                //Toast.makeText(MapsActivity.this, "posicion " + position, Toast.LENGTH_SHORT).show();
            }
        });
        textView = (TextView) findViewById(R.id.name);
        imageButton = (ImageButton) findViewById(R.id.direction_go);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flagRutas == true)
                {
                    flagRutas = false;
                    List <String[]> result = datosRuta(indice_actual);
                    setListview(result);
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                }

            }
        });
    }

    /**
     *  Metodo para llenar el listview
     *  @param result  lista de arrays donde en su primer array tiene la descripcion de las rutas
     *                 y en el segundo la duracion de cada ruta
     */
    public void setListview(List <String[]> result){

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.

        if(indice_actual == -1)
        {
            CustomListAdapter adapter=new CustomListAdapter(this, rutas_list(3), descripcion_list(),duracion_list());
            listview.setAdapter(adapter);
        }
        else
        {
            CustomListAdapter adapter=new CustomListAdapter(this, rutas_list(cantidad_rutas), result.get(0),result.get(1));
            listview.setAdapter(adapter);
        }

        ColorDrawable grey = new ColorDrawable(this.getResources().getColor(R.color.grey));
        listview.setDivider(grey);
        listview.setDividerHeight(1);



        listview.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        float list_height = listview.getMeasuredHeight() * listview.getCount() + (listview.getCount() * listview.getDividerHeight()) + (listview.getCount()* 10)+ (listview.getCount()*20);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float anchor = (list_height/metrics.heightPixels);
        mLayout.setAnchorPoint(anchor);
        //Toast.makeText(this, "Anchor es " + anchor , Toast.LENGTH_SHORT).show();


    }

    /**
     * this method call setPanelSlidelistener method to listen open and close of slide panel
     */
    public void panelListener(){
        SlidingUpPanelLayout.PanelSlideListener listener = new SlidingUpPanelLayout.PanelSlideListener() {

            // During the transition of expand and collapse onPanelSlide function will be called.
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }

        };
        mLayout.addPanelSlideListener(listener);
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    /**
     * Metodo para llenar el listview con la cantidad de rutas en el json
     *  @param cantidad  indica el numero de rutas
     * @return array list
     */
    public String[] rutas_list(int cantidad){
        String[] array_list = new String[cantidad];
        switch (cantidad){
            case 1:
                array_list[0] = "Ruta 1";
                return array_list;
            case 2:
                array_list[0] = "Ruta 1";
                array_list[1] = "Ruta 2";
                return array_list;
            case 3:
                array_list[0] = "Ruta 1";
                array_list[1] = "Ruta 2";
                array_list[2] = "Ruta 3";
                return array_list;

        }
        return array_list;
    }

    /**
     * Metodo para llenar el listview y que se utiliza en caso de que el json no tenga informacion
     */
    public String[]descripcion_list(){
        String[] array_list = {
                "Ejemplo 1",
                "Ejemplo 2",
                "Ejemplo 3"
        };
        return array_list;
    }

    /**
     * Metodo para llenar el listview y que se utiliza en caso de que el json no tenga informacion
     */
    public String[] duracion_list(){
        String[] array_list = {
                "5 min",
                "7 min",
                "10 min"
        };
        return array_list;
    }


    /**
     * Metodo que es llamado cuando se preciona el boton back del celular, toma una accion dependiendo de lo que se este enseñando en pantalla.
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(navegar == true) //desplegar mensaje si desea salir de modo navegacion
            {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.quit)
                        .setMessage(R.string.really_quit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                navegar_off(1);
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return true;
            }

            if (mLayout != null &&
                    (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                return true;
            }

            else if (mLayout != null &&
                    mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                marcador_actual.hideInfoWindow();
                borrarRutas(10); //borro rutas si salgo del panel
                if(fab.getVisibility() != View.VISIBLE){
                    cargafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show);
                    fab.setVisibility(View.VISIBLE);
                    fab.startAnimation(cargafab);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode,event);
    }


    /**
     * Metodo que toma la latitud-longitud del origen y destino y hace una peticion a Google para que retorne las rutas posibles.
     * @param startL
     * @param endL
     */
    public void getURL(final LatLng startL, LatLng endL){
        String start = startL.latitude+","+startL.longitude;
        String end = endL.latitude+","+endL.longitude;
        String stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin="+start+"&destination="+end+"&alternatives=true&mode=walking&language=es&key=AIzaSyCljYcjcbR69841xYHr5kTcuPfmQ_2qWZE";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stringUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Principal","Dibuja");
                        dibujar(response, startL);
                        flagRutas = true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    /**
     * Metodo que dibujas las distintas posibles rutas a escoger.
     * @param jsonRuta
     * @param start
     */
    public void dibujar(String jsonRuta, LatLng start){
        try {
            JSONObject jsonObject = new JSONObject(jsonRuta);
            // routesArray contains ALL routes
            JSONArray routesArray = jsonObject.getJSONArray("routes");

            ArrayList<Polyline> tempP;
            int longitud = routesArray.length();
            cantidad_rutas = longitud;
            // Grab the first route

            for (int i = 0; i < longitud;i++){
                JSONObject route = routesArray.getJSONObject(i);
                rutasDetalle[i] = route;
                //instruccionesRuta(0);
                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                List<LatLng> polyz= decodePoly(polyline);//decodificación de la polilinea
                String color = "0";
                if(i==0){
                    color = "#8BC34A"; //Verde
                } else if(i==1){
                    color = "#800080"; //Morado
                } else {
                    color = "#2196F3"; //Azul
                }
                tempP = new ArrayList<>();

                //dibujar línea de la entrada a la primer unión con calle
                tempP.add(mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(start.latitude, start.longitude),
                                new LatLng(polyz.get(0).latitude, polyz.get(0).longitude))
                        .width(4).color(Color.parseColor("#7e7e7e")).geodesic(true)));


                for (int j = 0; j < polyz.size() - 1; j++) {
                    LatLng src = polyz.get(j);
                    LatLng dest = polyz.get(j + 1);

                    tempP.add(mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude, dest.longitude))
                            .width(4).color(Color.parseColor(color)).geodesic(true)));
                }
                rutas.add(i,tempP);
            }
            flagRutas=true;
        } catch (Exception e) {

        }
    }

    /**
     * Metodo que decodifica el string encoded y lo convierte en un vector de latitud-longitud para construir las rutas.
     * @param encoded
     * @return
     */
    /* Method to decode polyline points */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    /**
     * Metodo llamado para obtener la informacion sobre las rutas disponibles
     *
     * @param puntoElegido  El indice de la ruta seleccionada por el usuario
     */
    private void datos(int puntoElegido) {
        LatLng inicioRuta = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        LatLng destinoRuta = new LatLng(mRuta.elatitud[puntoElegido], mRuta.elonguitud[puntoElegido]);
        getURL(inicioRuta, destinoRuta);
    }

    /**
     * Toma el dato de distancia y duracion de las rutas dadas por Google.
     * @param puntoElegido
     * @return
     */
    private List < String[] > datosRuta(int puntoElegido) {
        //cantidad_rutas = 2;
        String[] distancias = new String[cantidad_rutas];
        String[] duraciones = new String[cantidad_rutas];

        List < String[] > distanciaDuracion = new ArrayList < String[] > ();
        try {
            for (int i = 0; i < cantidad_rutas; i++) { //cantidadRutas
                JSONObject distanciaJson = rutasDetalle[i].getJSONArray("legs").getJSONObject(0).getJSONObject("distance");
                String distancia = distanciaJson.getString("text");
                distancias[i] = distanciaJson.getString("text");
                JSONObject duracionJson = rutasDetalle[i].getJSONArray("legs").getJSONObject(0).getJSONObject("duration");
                String duracion = duracionJson.getString("text");
                duraciones[i] = duracionJson.getString("text");
                Log.i(TAG, "Distancia: " + distancia + " Duracion: " + duracion);
            }
            distanciaDuracion.add(distancias);
            distanciaDuracion.add(duraciones);
        } catch (Exception e) {

        }
        return distanciaDuracion;
    }

    /**
     * Metodo llamado para obtener la informacion sobre una ruta seleccionada.
     *
     * @param rutaElegida  El indice de la ruta seleccionada por el usuario
     */
    private String[] instruccionesRuta(int rutaElegida, int pasoSolicitado) {
        if (pasoSolicitado == 0) {
            //borrarRutas(rutaElegida);
        }
        String[] detallesPaso = new String[5];
        try {
            JSONArray pasos = rutasDetalle[rutaElegida].getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            JSONObject paso = pasos.getJSONObject(pasoSolicitado);
            String distanciaPaso = paso.getJSONObject("distance").getString("text");
            detallesPaso[0] = distanciaPaso;
            String duracionPaso = paso.getJSONObject("duration").getString("text");
            detallesPaso[1] = duracionPaso;
            String latEndPaso = paso.getJSONObject("end_location").getString("lat");
            detallesPaso[2] = latEndPaso;
            String lonEndPaso = paso.getJSONObject("end_location").getString("lng");
            detallesPaso[3] = lonEndPaso;
            String mensajePaso = paso.getString("html_instructions");
            detallesPaso[4] = mensajePaso;
            //Log.i(TAG, "Distancia Paso: " + distanciaPaso + " Duracion Paso: " + duracionPaso + " Latitud final: " + latEndPaso + " Longitud final: " + lonEndPaso + " Mensaje: " + mensajePaso);



        } catch (Exception e) {
            // rutaElegida=0;
            navegar_off(0);
            detallesPaso[0]="-1";
            //pasoSgt= 0;
        }
        return detallesPaso;
    }

    /**
     * Metodo llamado para borrar rutas no requeridas.
     *
     * @param index  El indice de la ruta seleccionada por el usuario
     */
    private void borrarRutas(int index){
        ArrayList<Polyline> tempPoli = new ArrayList<>();
        for(int i = 0; i < rutas.size();i++){
            if(i==index){
                tempPoli = rutas.get(i);
            }else{
                for (int j = 0 ;j < rutas.get(i).size();j++){
                    rutas.get(i).get(j).setVisible(false);
                }
            }
        }
        rutas.clear();
        if(index!=10){
            rutas.add(tempPoli);
        }
    }


    /**
     * Metodo llamado al cliquear sobre un item dentro del {@code NavigationDrawer}.
     *
     * @param item el item seleccionado
     * @return verdadero para mostrar el item como el item seleccionado
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Wikitude) {
            startActivity(new Intent(MapsActivity.this, WikitudeActivity.class));
        }else if (id == R.id.About){

        }else if (id == R.id.Salir){
            finish();
            System.exit(0);
        }
        return true;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onInit(int status) {

    }

    /**
     * Metodo para borrar ruta a medida que el usuario se mueve
     *
     * @param current es la posicion actual
     */
    public void actualizarRuta(LatLng current) {

        Location inicioRuta= new Location("currentL1");
        inicioRuta.setLatitude(current.latitude);
        inicioRuta.setLongitude(current.longitude);
        try
        {
            LatLng segundo = rutas.get(0).get(0).getPoints().get(1);//falta crear control de null
            Location segundoRuta= new Location("currentL2");
            segundoRuta.setLatitude(segundo.latitude);
            segundoRuta.setLongitude(segundo.longitude);

            if(inicioRuta.distanceTo(segundoRuta)<15 ) {//cambiar 3 por la distancia que deseamos utilizar de cercanía
                rutas.get(0).get(0).setVisible(false);
                rutas.get(0).remove(0);
            }else{
                List<LatLng> pol = rutas.get(0).get(0).getPoints();
                pol.add(0,current);
                pol.remove(1);
                rutas.get(0).get(0).setPoints(pol);
                rutas.get(0).get(0).setVisible(true);
            }
        }
        catch (Exception e)
        {

        }

    }

    /**
     * Metodo para quitar modo navegacion
     *
     * @param cond es para saber si se llamo al terminar la ruta o por el dialogo
     */
    public void navegar_off(int cond){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        navegar = false;
        tts.stop();
        if(cond == 0)
        {
            textView.setText("Ha llegado a su destino");
            tts.speak("Ha llegado a su destino",TextToSpeech.QUEUE_FLUSH, null);
        }
        borrarRutas(10);
        imageButton.setEnabled(true);
        imageButton.setVisibility(View.VISIBLE);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mMap.setMinZoomPreference(13);
        cargafab = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show);
        fab.startAnimation(cargafab);
        fab.setVisibility(View.VISIBLE);
        mMap.getUiSettings().setCompassEnabled(false);
        pasoSgt = 0;
    }
}

