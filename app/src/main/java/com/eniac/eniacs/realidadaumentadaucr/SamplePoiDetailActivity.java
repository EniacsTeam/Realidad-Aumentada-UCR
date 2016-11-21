package com.eniac.eniacs.realidadaumentadaucr;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Esta clase representa el menu usado para desplegar la informacion en los marcadores de Wikitude.
 *
 * @author  EniacsTeam
 */
public class SamplePoiDetailActivity extends Activity {

    public static final String EXTRAS_KEY_POI_ID = "id";
    public static final String EXTRAS_KEY_POI_TITILE = "title";
    public static final String EXTRAS_KEY_POI_DESCR = "description";

    public static final String[] contacto = {"Teléfono:", "Correo:", "Facebook:",  "Web:", "Museo+UCR:"};

    private String tel;
    private String web;
    private String correo;
    private String face;
    private String museo;

    ImageButton facebook;
    ImageButton numTel;
    ImageButton email;
    ImageButton webPage;
    ImageButton museumPage;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * @param pm  El {@link PackageManager}. Puede encontrar esta clase a traves de {@link
     *            Context!getPackageManager()}.
     * @param url El URL completo de la pagina o perfil de Facebook.
     * @return Un {@code Intent} que abrira el perfil o pagina de Facebook.
     */
    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            int versionCode = pm.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            } else { //older versions of fb app
                String pageId = url.substring(25);
                uri = Uri.parse("fb://page/" + pageId);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }


    /**
     * Este metodo es llamado cuando la actividad esta iniciando y se encarga de cargar toda la informacion correspondiente.
     *
     * @param savedInstanceState estado guardado de la aplicacion un valor {@code null} indica que la actividad no debe ser recreada a partir de información previa.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sample_poidetail);

        webPage = (ImageButton) findViewById(R.id.imageButton);
        numTel = (ImageButton) findViewById(R.id.imageButton2);
        email = (ImageButton) findViewById(R.id.imageButton3);
        facebook = (ImageButton) findViewById(R.id.imageButton4);
        museumPage = (ImageButton) findViewById(R.id.imageButton5);

        String descript = getIntent().getExtras().getString(EXTRAS_KEY_POI_DESCR);

        int index;
        int endIndex;
        for (int i = 0; i < 5; i++) {
            index = descript.indexOf(contacto[i],0);
            if (index != -1) {
                switch (contacto[i]) {
                    case "Teléfono:":
                        tel = descript.substring(index + 16, index + 20) + descript.substring(index + 21, index + 25);
                        numTel.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:" + tel));
                                startActivity(callIntent);
                            }

                        });
                        break;
                    case "Correo:":
                        endIndex = descript.indexOf('\n', index);
                        correo = descript.substring(index + 8, endIndex);
                        email.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL, new String[]{correo});
                                try {
                                    startActivity(Intent.createChooser(i, "Enviar correo..."));
                                } catch (ActivityNotFoundException ex) {
                                    Toast.makeText(SamplePoiDetailActivity.this, "No existen clientes de correo instalados.", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                        break;
                    case "Facebook:":
                        endIndex = descript.indexOf('\n', index);
                        face = descript.substring(index + 10, endIndex);
                        facebook.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                startActivity(newFacebookIntent(getPackageManager(), face));
                            }

                        });
                        break;
                    case "Web:":
                        web = descript.substring(index + 5);
                        webPage.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                Uri uri = Uri.parse(web); // missing 'http://' will cause crashed
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }

                        });
                        break;
                    case "Museo+UCR:":
                        endIndex = descript.indexOf('\n',index);
                        museo = descript.substring(index+11,endIndex);
                        museumPage.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                Uri uri = Uri.parse(museo); // missing 'http://' will cause crashed
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }

                        });
                        break;
                }
            } else {
                switch (contacto[i]) {
                    case "Teléfono:":
                        tel = null;
                        numTel.setAlpha(0.3f);
                        break;
                    case "Correo:":
                        correo = null;
                        email.setAlpha(0.3f);
                        break;
                    case "Facebook:":
                        face = null;
                        facebook.setAlpha(0.3f);
                        break;
                    case "Web:":
                        web = null;
                        webPage.setAlpha(0.3f);
                        break;
                    case "Museo+UCR:":
                        museo = null;
                        museumPage.setAlpha(0.3f);
                        break;
                }
            }
        }

        //((TextView)findViewById(R.id.poi_id)).setText(  getIntent().getExtras().getString(EXTRAS_KEY_POI_ID) );
        ((TextView) findViewById(R.id.poi_title)).setText(getIntent().getExtras().getString(EXTRAS_KEY_POI_TITILE));
        ((TextView) findViewById(R.id.poi_description)).setText(descript);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SamplePoiDetail Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
