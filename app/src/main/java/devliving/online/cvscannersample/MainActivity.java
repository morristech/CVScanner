package devliving.online.cvscannersample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import devliving.online.cvscanner.DocumentScannerActivity;

public class MainActivity extends AppCompatActivity {

    final int REQ_SCAN = 11;

    RecyclerView list;
    ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (RecyclerView) findViewById(R.id.image_list);

        list.setLayoutManager(new LinearLayoutManager(this));
        //contentView.setHasFixedSize(true);

        mAdapter = new ImageAdapter();
        list.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Are you scanning a Passport?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(MainActivity.this, DocumentScannerActivity.class);
                                i.putExtra(DocumentScannerActivity.IsScanningPassport, true);
                                startActivityForResult(i, REQ_SCAN);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(MainActivity.this, DocumentScannerActivity.class);
                                i.putExtra(DocumentScannerActivity.IsScanningPassport, false);
                                startActivityForResult(i, REQ_SCAN);
                            }
                        }).show();
            }
        });
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
        if (id == R.id.action_step_by_step) {
            new AlertDialog.Builder(this)
                    .setTitle("Choose one")
                    .setItems(new String[]{"Document (ID, license etc.)" ,
                            "Passport - HoughLines algorithm",
                            "Passport - MRZ based algorithm",
                        "Passport - MRZ based retrial"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, StepByStepTestActivity.class);
                            intent.putExtra(StepByStepTestActivity.EXTRA_SCAN_TYPE, StepByStepTestActivity.CVCommand.values()[which]);
                            startActivity(intent);
                        }
                    })
                    .setCancelable(true)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SCAN && resultCode == RESULT_OK){
            String path = (data != null && data.getExtras() != null)? data.getStringExtra(DocumentScannerActivity.ImagePath):null;
            if(path != null){
                mAdapter.add(path);
            }
        }
    }
}
