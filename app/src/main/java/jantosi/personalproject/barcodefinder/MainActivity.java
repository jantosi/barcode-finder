package jantosi.personalproject.barcodefinder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    private ArrayList<String> watchedCodes = new ArrayList<>();
    private SharedPreferences sharedPref;
    private BarcodeListItemAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restoreFields(savedInstanceState);

        sharedPref = this.getSharedPreferences(
                getString(R.string.watchedbarcodes_preference_file_key), Context.MODE_PRIVATE);

        //todo removeme debug
        watchedCodes.add("5904825069301");
        sharedPref.edit().putInt("5904825069301", 0).apply();
        //


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add behaviour to FAB

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context alertDialogContext = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final View textInputModalView = LayoutInflater.from(alertDialogContext).inflate(R.layout.barcodenumber_modal, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(alertDialogContext)
                        .setView(textInputModalView)
                        .setCancelable(true)
                        .setPositiveButton(R.string.add_barcode, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) textInputModalView
                                        .findViewById(R.id.barcodenumber_modal_prompt_text_input);
                                String text = editText.getText().toString();
                                watchedCodes.add(text);
                                sharedPref.edit().putInt(text, 0).apply();
                            }
                        })
                        .setNegativeButton(R.string.add_barcode_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create();

                alertDialog.show();
            }
        });

        // Add behaviour to ListView

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new BarcodeListItemAdapter<>(watchedCodes, this, sharedPref);
        listView.setAdapter(adapter);

        askForNecessaryPermissions(this);
    }

    private void askForNecessaryPermissions(Activity thisActivity) {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("watchedCodes", watchedCodes);
        super.onSaveInstanceState(outState);
    }

    private void restoreFields(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        if (savedInstanceState.containsKey("watchedCodes")) {
            watchedCodes = savedInstanceState.getStringArrayList("watchedCodes");
        }
    }

    public void openBarcodeReaderActivity(View view) {
        Intent intent = new Intent(this, SimpleScannerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        adapter.notifyDataSetChanged();
    }
}
