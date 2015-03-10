package com.rov.pcms.make_rov;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.melnykov.fab.FloatingActionButton;


public class setup_MultiMotorAllocationActivity extends ActionBarActivity {

    public static final int TO_EIDT_RESULT_CODE = 0x1001;
    public static final String FILE_NAME = "file-name";
    public static SliderLayout sliderShow;
    private static Button setTemplateProfiles;
    private static FloatingActionButton backFab;

    private static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup__multi_motor_allocation);
//-----------------------UI init-----------------------------------------------
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        toolbar.setBackground(getResources().getDrawable(R.color.motor_act_primaryColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTemplateProfiles = (Button)findViewById(R.id.setMotorProfileBtn);
        backFab = (FloatingActionButton)findViewById(R.id.previousFab);
//        com.melnykov.fab.FloatingActionButton motorFab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.motorFab);
        sliderShow = (SliderLayout) findViewById(R.id.slider);

//--------------------Additional setup----------------------------------------
        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView.description("Default")
                .image(R.drawable.startup_screen);
        TextSliderView textSliderView2 = new TextSliderView(this);
        textSliderView2.description("Hihi")
                .image(R.drawable.ic_launcher);
        sliderShow.addSlider(textSliderView);
        sliderShow.addSlider(textSliderView2);


        sliderShow.stopAutoCycle();
        sliderShow.setDuration(0);
//--------------------Component listeners-------------------------------------

        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_to_right_close,R.anim.right_to_left_close);
            }
        });

        setTemplateProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = sliderShow.getCurrentPosition();
                Toast.makeText(setup_MultiMotorAllocationActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
                switch (position){
                    case 0:

                        break;
                    case 1:

                        break;
                    default:

                        break;
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right_close,R.anim.right_to_left_close);
        moveTaskToBack(false);
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
                Toast.makeText(setup_MultiMotorAllocationActivity.this,Integer.toString(sliderShow.getCurrentPosition()),Toast.LENGTH_LONG).show();

                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        ,LinearLayout.LayoutParams.MATCH_PARENT);
                layout.setMargins(16,0,16,0);
                final EditText adEditText = new EditText(setup_MultiMotorAllocationActivity.this);
                adEditText.setLayoutParams(layout);
                adEditText.setHint("You Must Enter The File Name Here");
                adEditText.setText("New_Pofile");
                adEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        adEditText.post(new Runnable() {
                            @Override
                            public void run() {
                                InputMethodManager inputMethodManager= (InputMethodManager) setup_MultiMotorAllocationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.showSoftInput(adEditText, InputMethodManager.SHOW_IMPLICIT);
                            }
                        });
                    }
                });
                adEditText.requestFocus();
                new AlertDialog.Builder(setup_MultiMotorAllocationActivity.this)
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
                                    Intent intent = new Intent(setup_MultiMotorAllocationActivity.this, EditMotorAllocationProfiles.class);
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
