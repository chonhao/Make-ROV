package com.rov.pcms.make_rov;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;


public class MultiMotorAllocationActivity extends ActionBarActivity {

    public static final int TO_EIDT_RESULT_CODE = 0x1001;
    public static final String FILE_NAME = "file-name";
    public static SliderLayout sliderShow;
    private static Button setTemplateProfiles;

    private static int position;

    private String TAG = "File writing";
    private File fileList2[];
    private Context _context = MultiMotorAllocationActivity.this;
    private File extFile;
    private String absPath;
    private String prefrenceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_motor_allocation);
//-----------------------UI init-----------------------------------------------
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        toolbar.setBackground(getResources().getDrawable(R.color.motor_act_primaryColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTemplateProfiles = (Button)findViewById(R.id.setMotorProfileBtn);

//        com.melnykov.fab.FloatingActionButton motorFab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.motorFab);
        sliderShow = (SliderLayout) findViewById(R.id.slider);

//--------------------Additional setup----------------------------------------
        TextSliderView textSliderViewDelphinus = new TextSliderView(this);
        textSliderViewDelphinus.description("Delphinus (2nd generation)")
                      .image(R.drawable.delphinus);
        TextSliderView textSliderViewCtenophora = new TextSliderView(this);
        textSliderViewCtenophora.description("Ctenophora (3rd generation)")
                       .image(R.drawable.ctenophora);
        TextSliderView textSliderViewEagleRay = new TextSliderView(this);
        textSliderViewEagleRay.description("Eagle Ray (1st generation)")
                .image(R.drawable.eagle_ray);
        sliderShow.addSlider(textSliderViewEagleRay);
        sliderShow.addSlider(textSliderViewDelphinus);
        sliderShow.addSlider(textSliderViewCtenophora);
        sliderShow.stopAutoCycle();
        sliderShow.setDuration(0);

        prefrenceName = "Motor_Allocation";
        try {
            fileList2 = _context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);
            extFile = fileList2[1];
        }catch (Exception e){
            Toast.makeText(this,"Can't find any External Storage. Files will be written to Internal Storage",Toast.LENGTH_LONG).show();
            extFile = Environment.getExternalStorageDirectory();
        }catch (NoSuchMethodError noSuchMethodError){
            Toast.makeText(this,"Devices under Android Kitkat do not support External Storage. Files will be written to Internal Storage",Toast.LENGTH_LONG).show();
            extFile = Environment.getExternalStorageDirectory();
        }
        absPath = extFile.getAbsolutePath();
//--------------------Component listeners-------------------------------------
        setTemplateProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = sliderShow.getCurrentPosition();
                switch (position){
                    case 0:
                        loadDefaults(getString(R.string.eagleRay));
                        break;
                    case 1:
                        loadDefaults(getString(R.string.delphinus));
                        break;
                    default:
                        loadDefaults(getString(R.string.ctenophora));
                        break;
                }

            }
        });

    }

    private void loadDefaults(String defaultName){
        File file = new File(absPath,prefrenceName+".txt");
        FileOutputStream f = null;
        PrintWriter pw = null;
        try {
            f = new FileOutputStream(file);
            pw = new PrintWriter(f);
            if(defaultName.equals(getString(R.string.eagleRay))){

            }else if(defaultName.equals(getString(R.string.delphinus))){

            }else if(defaultName.equals(getString(R.string.ctenophora))){

            }
            pw.flush();
            pw.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "File exported to "+file.getPath(),Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi_motor_allocation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.addMotorTemplate:
                Toast.makeText(MultiMotorAllocationActivity.this,Integer.toString(sliderShow.getCurrentPosition()),Toast.LENGTH_LONG).show();

                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        ,LinearLayout.LayoutParams.MATCH_PARENT);
                layout.setMargins(16,0,16,0);
                final EditText adEditText = new EditText(MultiMotorAllocationActivity.this);
                adEditText.setLayoutParams(layout);
                adEditText.setHint("You Must Enter The File Name Here");
                adEditText.setText("New_Pofile");
                adEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        adEditText.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager inputMethodManager= (InputMethodManager) MultiMotorAllocationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.showSoftInput(adEditText, InputMethodManager.SHOW_IMPLICIT);
                            }
                        });
                    }
                });
                adEditText.requestFocus();
                new AlertDialog.Builder(MultiMotorAllocationActivity.this)
                        .setTitle("New profile name")
                        .setView(adEditText)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String fileName = adEditText.getText().toString().trim();
                                if (fileName.isEmpty()||fileName.length()==0||fileName.equals("")|| TextUtils.isEmpty(fileName)) {
                                }else{
                                    Bundle bundle = new Bundle();
                                    bundle.putString(FILE_NAME, fileName);
                                    Intent intent = new Intent(MultiMotorAllocationActivity.this, EditMotorAllocationProfiles.class);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, TO_EIDT_RESULT_CODE);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
