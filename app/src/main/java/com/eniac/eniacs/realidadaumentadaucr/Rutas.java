package com.eniac.eniacs.realidadaumentadaucr;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static android.location.Location.distanceBetween;

/**
 * Esta clase provee metodos que a partir de informacion con respecto a la posicion del usuario devuelve puntos de interes que podrian ser relevantes
 * para el usuario debido a su localizacion y orientacion con respecto al mapa.
 *
 * @author  EniacsTeam
 */
public class Rutas {
    List<Location> listaCoordenadas;
    String[]edificios;
    Map<Integer,Location> mapaEnvio;
    Location localizacion;
    boolean usado;


    /**
     * Constructor de {@code Rutas}.
     * <p>
     * Construye una una lista de {@code Location} compuesta por el edificio y su posicion latitud/longitud dentro del mapa y luego lo asocia a su correspondiente identificador entero.
     */
    public Rutas(){

        mapaEnvio = new LinkedHashMap<Integer,Location>();
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
        double[] elatitud= new double[] {9.93639, 9.9355, 9.93601, 9.93486, 9.93724, 9.93595, 9.93648, 9.93612,
                9.93603, 9.93867, 9.9386, 9.937654,
                9.938080, 9.937046, 9.937932, 9.938255, 9.938637, 9.938880, 9.937950, 9.937640, 9.937178, 9.937408,
                9.936547, 9.936083, 9.937604, 9.937249, 9.936307, 9.935916};
        double[] elonguitud= new double[]{-84.05386, -84.05422, -84.0527, -84.05261, -84.05309, -84.05194, -84.05157,
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
     * @return Map<Integer,Location> Los 3 edificios mas cercanos a la posicion brindada por parametro.
     */
    public Map<Integer,Location> edificiosMasCercanos(Location location){
        Map<Double,Integer> mapaOrdenado = new TreeMap<Double,Integer>();
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
     * Se genera el {@code Location} del edificio que se esta apuntando con el dispositivo.
     *
     * @param  angle Angulo entre el eje "y" del dispositivo y el polo norte magnetico.
     * @return Location Retorna la posicion del edificio apuntado, si no existe ninguno retorna {@code null}.
     */
    public Map<Integer,Location> edificioApuntado(float angle){
        float bearing;
        float heading;
        float arrow_rotation;
        Map<Integer,Location>retLoc=new LinkedHashMap<Integer, Location>();
        boolean isSet=false;
        for (Map.Entry<Integer, Location> entry : mapaEnvio.entrySet()) {
            bearing = localizacion.bearingTo(entry.getValue());    // -180 to 180
            heading = localizacion.getBearing();         // 0 to 360
            arrow_rotation = (360+((bearing + 360) % 360)-heading) % 360;
            if((angle-30)%360 < arrow_rotation && arrow_rotation < (angle+30)%360){
                retLoc.put(entry.getKey(),entry.getValue());
                isSet=true;
                break;
            }
        }
        if(!isSet){
            retLoc.put(-1,new Location("Localizacion invalida"));
        }
        return retLoc;
    }
}
