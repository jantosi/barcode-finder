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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import jantosi.personalproject.barcodefinder.model.BarcodeToFind;
import jantosi.personalproject.barcodefinder.model.MatchMode;
import jantosi.personalproject.barcodefinder.model.viewmapper.MatchModeRadioButtonMapper;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    private SharedPreferences sharedPref;
    private BarcodeListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = this.getSharedPreferences(
                getString(R.string.watchedbarcodes_preference_file_key), Context.MODE_PRIVATE);

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
                        .setTitle(R.string.barcodenumber_modal_prompt_title)
                        .setView(textInputModalView)
                        .setCancelable(true)
                        .setPositiveButton(R.string.add_barcode, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) textInputModalView
                                        .findViewById(R.id.barcodenumber_modal_prompt_text_input);
                                String text = editText.getText().toString();

                                RadioGroup matchModeRadios = (RadioGroup) textInputModalView.findViewById(R.id.barcodeMatchModes);
                                MatchMode matchMode = MatchModeRadioButtonMapper.fromId(matchModeRadios.getCheckedRadioButtonId());
                                BarcodeToFind.save(new BarcodeToFind(text, matchMode));

                                List<BarcodeToFind> all = BarcodeToFind.find(BarcodeToFind.class, "text NOT NULL");
                                adapter.setList(all);
                                adapter.notifyDataSetChanged();
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
        List<BarcodeToFind> all = BarcodeToFind.find(BarcodeToFind.class, "text NOT NULL");
        adapter = new BarcodeListItemAdapter(all, this);
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

    public void openBarcodeReaderActivity(View view) {
        Intent intent = new Intent(this, SimpleScannerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        List<BarcodeToFind> all = BarcodeToFind.find(BarcodeToFind.class, "text NOT NULL");
        adapter.setList(all);
        adapter.notifyDataSetChanged();
    }
}
