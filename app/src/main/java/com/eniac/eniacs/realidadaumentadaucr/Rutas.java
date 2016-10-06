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
 * Created by johan on 3/10/2016.
 */



public class Rutas {
    List<Location> listaCoordenadas;
    String[]edificios;
    Map<Integer,Location> mapaEnvio;
    Location localizacion;
    boolean usado;

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
         double[] elatitud= {9.93639, 9.9355, 9.93601, 9.93486, 9.93724, 9.93595, 9.93648, 9.93612,
                9.93603, 9.93867, 9.9386, 9.937654,
                9.938080, 9.937046, 9.937932, 9.938255, 9.938637, 9.938880, 9.937950, 9.937640, 9.937178, 9.937408,
                9.936547, 9.936083, 9.937604, 9.937249, 9.936307, 9.935916};
        double[] elonguitud= {-84.05386, -84.05422, -84.0527, -84.05261, -84.05309, -84.05194, -84.05157,
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
        //usado = true;
        return  mapaEnvio;
        //Map angleBearing = getArrow(location,mapaEnvio,angle);
        //coordenadasResult.add(3,angleBearing);
        //mapaEnvio.put();
        // return coordenadasResult;
    }


    public Location edificioApuntado(double angle){
        float bearing;
        //double arrow_rotation;
        //float heading;
        Location retLoc=null;
        for(int i = 0; i<mapaEnvio.size(); i++){
            bearing = localizacion.bearingTo(mapaEnvio.get(i));    // -180 to 180
            bearing +=bearing+180;
            //heading = localizacion.getBearing();         // 0 to 360
            // *** Code to calculate where the arrow should point ***
            //arrow_rotation = (360+((bearing + 360) % 360)-heading) % 360;
            if((angle-10)%360 < bearing && bearing < (angle+10)%360){
                retLoc=new Location("apunta");
                retLoc=mapaEnvio.get(i);
                i=i+3;
            }
        }
        return retLoc;
    }
/*
    float bearing;
    Location startingLocation=new Location("starting point");
    Location endingLocation=new Location("ending point");
    startingLocation.setLatitude(9.831949);
    startingLocation.setLongitude(-84.210125);
    endingLocation.setLatitude(9.837795);
    endingLocation.setLongitude(-84.210043);
    double dist = startingLocation.distanceTo(endingLocation);
    bearing = startingLocation.bearingTo(endingLocation);    // -180 to 180
    float heading = startingLocation.getBearing();         // 0 to 360
    // *** Code to calculate where the arrow should point ***
    float arrow_rotation = (360+((bearing + 360) % 360)-heading) % 360;
    //mTextView.setText(mTextView.getText()+"norte_\ndistancia:"+ dist+"\n"+"bearing: (-180 180): "+bearing+"\n"+"heading (0 360): "+heading+"\n"+"arrow: "+arrow_rotation+"\n");
    //mTextView.setText(mTextView.getText()+"\n"+"\n");
*/
}
