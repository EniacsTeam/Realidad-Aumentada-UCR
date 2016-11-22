package com.eniac.eniacs.realidadaumentadaucr;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Francisco on 11/11/2016.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] rutas_list;
    private final String[] descripcion_list;
    private final String[] duracion_list;

    public CustomListAdapter(Activity context, String[] rutas_list,  String[] descripcion_list, String[] duracion_list ) {
        super(context, R.layout.activity_listview, rutas_list);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.rutas_list=rutas_list;
        this.descripcion_list =descripcion_list;
        this.duracion_list = duracion_list;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_listview, null,true);

        TextView txtRuta = (TextView) rowView.findViewById(R.id.ruta_num);
        TextView txtDescripcion = (TextView) rowView.findViewById(R.id.descripcion);
        TextView txtDuracion = (TextView) rowView.findViewById(R.id.duracion);

        txtRuta.setText(rutas_list[position]);
        if(position == 0)
        {
            txtRuta.setTextColor(Color.parseColor("#8BC34A"));
        }
        if(position == 1)
        {
            txtRuta.setTextColor(Color.parseColor("#800080"));
        }
        if(position == 2)
        {
            txtRuta.setTextColor(Color.parseColor("#2196F3"));
        }
        txtDescripcion.setText(descripcion_list[position]);
        txtDuracion.setText(duracion_list[position]);
        return rowView;

    };
}
