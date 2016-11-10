package com.eniac.eniacs.realidadaumentadaucr;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Esta clase provee metodos que a partir de informacion con respecto a la posicion del usuario devuelve puntos de interes que podrian ser relevantes
 * para el usuario debido a su localizacion y orientacion con respecto al mapa.
 *
 * @author  EniacsTeam
 */
public class Rutas {
    private final List<Location> listaCoordenadas;
    final String[]edificios;
    double[] elatitud;
    double[] elonguitud;
    Map<Integer,Location> mapaEnvio;
    private Location localizacion;
    boolean usado;
    String respuesta;

    /**
     * Constructor de {@code Rutas}.
     * <p>
     * Construye una una lista de {@code Location} compuesta por el edificio y su posicion latitud/longitud dentro del mapa y luego lo asocia a su correspondiente identificador entero.
     */
    public Rutas(){

        mapaEnvio = new LinkedHashMap<>();
        localizacion = new Location("current");
        edificios = new String[]
                {"Facultad de Derecho","Oficina de Becas y Atención Socioeconómica",
                "Biblioteca Luis Demetrio Tinoco","Escuela de Arquitectura","Comedor universitario","Facultad de Ingeniería",
                "Escuela de Física y Matemáticas","Escuela de Estudios Generales","Biblioteca Carlos Monge",
                "Sección de Educación Preescolar","Facultad de Letras","Centro de Informática","Escuela Centroamericana de Geología",
                "Facultad de Ciencias Económicas","Escuela de Computación e Informática","Facultad de Odontología","Facultad de Medicina",
                "Facultad de Farmacia","Facultad de Microbiología","Escuela de Biología","Escuela de Química",
                "Escuela de Artes Musicales","Escuela de Bellas Artes","Facultad de Educación","Bosque Leonel Oviedo",
                "Mariposario","Plaza 24 de abril","El Pretil"};
        elatitud= new double[] {9.93639, 9.9355, 9.93601, 9.93486, 9.93724, 9.93595, 9.93648, 9.93612,
                9.93603, 9.93867, 9.9386, 9.937654,
                9.938080, 9.937046, 9.937932, 9.938255, 9.938637, 9.938880, 9.937950, 9.937640, 9.937178, 9.937408,
                9.936547, 9.936083, 9.937604, 9.937249, 9.936307, 9.935916};
        elonguitud= new double[]{-84.05386, -84.05422, -84.0527, -84.05261, -84.05309, -84.05194, -84.05157,
                -84.05047, -84.05105, -84.0536,
                -84.05286, -84.052356, -84.052452, -84.051656, -84.051992, -84.051683, -84.050404, -84.049967, -84.049292,
                -84.049444, -84.048932,-84.048154, -84.048270, -84.048764, -84.050606, -84.050314, -84.050855, -84.050618 };
        listaCoordenadas = new ArrayList<>();
        for(int i = 0;i<elatitud.length;i++){
            Location loc = new Location(edificios[i]);
            loc.setLatitude(elatitud[i]);
            loc.setLongitude(elonguitud[i]);
            listaCoordenadas.add(loc);
        }
    }

    /**
     * Se genera un {@code Map} con los 3 edificios mas cercanos, este contiene un integer con la posicion general del edificio en los vectores de la
     * aplicacion y su location.
     *
     * @param  location  Posicion actual del usuario dentro del mapa.
     * @return Los 3 edificios mas cercanos a la posicion brindada por parametro.
     */
    public Map<Integer,Location> edificiosMasCercanos(Location location){
        Map<Double,Integer> mapaOrdenado = new TreeMap<>();
        mapaEnvio.clear();
        for(int i =0;i<listaCoordenadas.size();i++){
            mapaOrdenado.put((double)location.distanceTo(listaCoordenadas.get(i)), i);
        }

        for(Map.Entry<Double,Integer> entry : mapaOrdenado.entrySet()) {
            if (mapaEnvio.size()<3){
                mapaEnvio.put(entry.getValue(),listaCoordenadas.get(entry.getValue()));
            }else{
                break;
            }
        }
        localizacion=location;
        return  mapaEnvio;
    }

    /**
     * Se genera el identificador del edificio que se esta apuntando con el dispositivo.
     *
     * @param  angle Angulo entre el eje "y" del dispositivo y el polo norte magnetico.
     * @return int el identificador del edificio apuntado, si no existe ninguno retorna {@code null}.
     */
    public int edificioApuntado(float angle){
        float bearing;
        float heading;
        float arrow_rotation;
        int retLoc = -1;
        boolean found = false;

        Iterator it = mapaEnvio.entrySet().iterator();
        while (it.hasNext() && !found) {
            Map.Entry<Integer, Location> pair = (Map.Entry<Integer, Location>) it.next();
            bearing = localizacion.bearingTo(pair.getValue());    // -180 to 180
            heading = localizacion.getBearing();         // 0 to 360
            arrow_rotation = (360+((bearing + 360) % 360)-heading) % 360;
            if((angle-30)%360 < arrow_rotation && arrow_rotation < (angle+30)%360){
                retLoc=pair.getKey();
                found=true;
            }
        }
        return retLoc;
    }

    public void getPolyline(LatLng start, LatLng end){
        System.out.println("1entré");
        //List<LatLng> polyz=new ArrayList<>();
        String startLocation = String.valueOf(start.latitude) +","+String.valueOf(start.longitude);
        String endLocation = String.valueOf(end.latitude) +","+String.valueOf(end.longitude);
        String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + startLocation + "&destination=" + endLocation + "&sensor=false";
        //StringBuilder response = new StringBuilder();
        getURL(stringUrl);
        /*try {
            StringRequest respuesta = getURL(stringUrl);

            String jsonOutput = response.toString();

            JSONObject jsonObject = new JSONObject(jsonOutput);

            // routesArray contains ALL routes
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            // Grab the first route
            JSONObject route = routesArray.getJSONObject(0);

            JSONObject poly = route.getJSONObject("overview_polyline");
            String polyline = poly.getString("points");
            System.out.println(polyline);
            polyz = decodePoly(polyline);
            System.out.println("4saliendo");
            System.out.println("5"+polyz.toString());
        } catch (Exception e) {
            System.out.println("6 error");
        }

        return polyz;*/
    }

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

    public void getURL(String stringUrl){
        // Instantiate the RequestQueue.
        String url ="http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&sensor=false";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stringUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        leerRespuesta(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                leerRespuesta(error.toString());
            }
        });
        // Add the request to the RequestQueue.
    }

    public void leerRespuesta(String texto){
        System.out.println("he leído "+texto);
        respuesta = texto;
    }
}




