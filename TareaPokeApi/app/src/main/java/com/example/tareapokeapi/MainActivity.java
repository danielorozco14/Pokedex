package com.example.tareapokeapi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> pokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokemonList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetPokemons().execute();
    }

    private class GetPokemons extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = "https://pokeapi.co/api/v2/pokemon/?offset=0&limit=300";
            String jsonStr = sh.makeServiceCall(url);
            Log.d(TAG,jsonStr);

             if (jsonStr != null) {
                try {

                    Log.d(TAG,"ENTRANDO AL TRY");
                    //SE OBTIENE EL JSON COMPLETO
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //SE OBTIENE EL NOMBRE DEL ARREGLO
                    JSONArray pokemon = jsonObj.getJSONArray("results");

                    for (int i = 0; i < pokemon.length(); i++) {
                        //SE OBTIENE EL INDICE DEL OBJETO
                        JSONObject c = pokemon.getJSONObject(i);
                        //SE OBTIENE EL ATRIBUTO QUE SE QUIERE AGREGAR A LA LISTA
                        String name = c.getString("name").toUpperCase();
                        Log.d(TAG,name);

                        HashMap<String, String> pokeDex = new HashMap<>();

                        pokeDex.put("name", name);
                        pokemonList.add(pokeDex);
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(TAG, "No se pudo obtener el JSON");
             }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, pokemonList,
                    R.layout.list_item, new String[]{ "name"},
                    new int[]{R.id.name});
            lv.setAdapter(adapter);
        }
    }
}