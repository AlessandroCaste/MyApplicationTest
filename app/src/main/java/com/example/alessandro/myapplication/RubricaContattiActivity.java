package com.example.alessandro.myapplication;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.alessandro.myapplication.dummy.DummyContent;

import static android.Manifest.permission.CALL_PHONE;


// Nota bene: per far funzionare i fragments devo sia implementare queste due classi che inserire i due metodi di cui sotto
public class RubricaContattiActivity extends AppCompatActivity implements ButtonsFragment.OnFragmentInteractionListener, ContactsFragment.OnListFragmentInteractionListener {

    private static final int EDIT_MODE = 1;
    private static final int EDIT_MODE2 = 2;
    private static final String VALORE_SALVATO = "VALORE_SALVATO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubrica_contatti);

        //Nel caso l'applicazione fosse già stata avviata..
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(VALORE_SALVATO)) {
                TextView labelText = findViewById(R.id.idText);
                labelText.setText(savedInstanceState.getString(VALORE_SALVATO));
            }
        }

    }

    //Questo è di fatto un duplicato! Se esiste un restorepoint, verrà restorato già dentro onCreate. Questo è un metodo alternativo.
    //Nel caso specifico, abbiamo un doppio restore (quello di onRestore fa overwrite su quello di onCreate)
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Non devo mai controllare
        if (savedInstanceState.containsKey(VALORE_SALVATO)) {
            TextView labelText = findViewById(R.id.idText);
            labelText.setText(savedInstanceState.getString(VALORE_SALVATO));
        }
    }

    @Override
    protected void onResume() {
        Log.d(RubricaContattiActivity.class.getName(),"Sono nell'onResume!");

        View addButton = findViewById(R.id.addContactButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddContactActivity.class);
                startActivity(i);
                i.putExtra("varA","Parametro Passato");
            }
        });

     View simulazioneEdit = findViewById(R.id.button1);
        simulazioneEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {

               //Passo ad un'altra activity
                Intent i = new Intent(getApplicationContext(), AddContactActivity.class);
                i.putExtra("idContatto",25);
                startActivityForResult(i, EDIT_MODE);


                /*
                Queste operazioni se voglio sostituire il frammento
                La funzione .addToBackStack() mi permette l'undo usando 'indietro'. Senza esco subito dall'activity
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listaContatti, new ContactsFragment())
                    .commit();

                 //Queste operazioni se voglio rimuovere il frammento
                 getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.listaContatti))
                    .addToBackStack(null)
                    .commit();

                 getActivity() -> mi ritorna l'activity in cui si trova il fragment
                 */
            }
        });


     View simulazioneEdit2 = findViewById(R.id.button2);
        simulazioneEdit2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view) {
                Intent i = new Intent(getApplicationContext(), EditContactActivity.class);
                i.putExtra("idContatto",2);

                startActivityForResult(i, EDIT_MODE2);
            }
        });

        //Devo utilizzare i due elementi fragmentManager e FragmentTransaction
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.listaContatti,new ContactsFragment())
                .commit();

        //Funzionalità chiamata del bottone 3
        View simulazioneCall = findViewById(R.id.simulazioneCall);
        simulazioneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                /* Questo codice apre l'app dial!
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:0612312312"));
                //Se non trova un'app collegata al nostro ACTION_DIAL getPackageManager tornerà false
                if(i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                } */

                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:06123122312"));
                //Verifico che siano garantiti i permessi di chiamata
                if(i.resolveActivity(getPackageManager()) != null) {
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(i);

                    } else {
                        requestPermissions(new String[]{CALL_PHONE}, 1);
                    }
                }

            }
        });

        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Ho premuto il pulsante di sinistra
        if(requestCode == EDIT_MODE) {
            if(resultCode == RESULT_OK) {
                // Abbiamo accesso a tutte le informazioni del nostro text
                TextView labelText = findViewById(R.id.idText);
                labelText.setText(data.getExtras().get("nome") + " " + data.getExtras().get("cognome"));
            } else if (resultCode == RESULT_CANCELED) {

            }

        }
        //Ho premuto il pulsante di destra
        else if (requestCode == EDIT_MODE2) {
            if(resultCode == RESULT_OK) {
                TextView labelText = findViewById(R.id.idText);
                labelText.setText(data.getExtras().get("nome") + " " + data.getExtras().get("cognome") +"\nClickando il bottone 2");
            } else if (resultCode == RESULT_CANCELED) {

            }
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        TextView labelText = findViewById(R.id.idText);
        //Personalizzo l'onSaveInstanceState aggiungendoci come informazione quella del campo di testo
        outState.putString(VALORE_SALVATO,labelText.getText().toString() +" SALVATO! ");

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onListFragmentInteraction(DummyContent.DummyItem uri){
        //you can leave it empty
    }



}
