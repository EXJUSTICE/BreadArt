package com.xu.leipasaari;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.graphics.Palette;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;
//bugs to fix, mainly a new button to launch dialogfrag. longclcik doesnt work

//MainActivity handles the generation of the canvas
public class MainActivity extends AppCompatActivity  implements DialogChooseFragment.OnDialogListener{
    private CanvasView Customcanvas;
    private Bitmap bp;
    private int dark;
    private int vib;
    private int vibl;
    private int darkm;
    private int lightm;

    private int allmute;

    private String display_results = "lol";
    private String VIB_POP="VIB_POP";
    private String DARK_POP="DARK_POP";
    private String VIBL_POP ="VIBL_POP";

    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final int blue= getResources().getColor(R.color.Blue);
        final int sand = getResources().getColor(R.color.Sand);
        final int grey =getResources().getColor(R.color.Grey);

        //initialize the Canvas object
        Customcanvas = (CanvasView)findViewById(R.id.signature_canvas);
        Customcanvas.setBrushColor(blue);
        //initialize sharebutton
        Button getButton = (Button)findViewById(R.id.getbutton);
        getButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


              //long click to analyse, maybe do a button?

              bp = Customcanvas.getBitmap();

              Palette.from(bp).generate(new Palette.PaletteAsyncListener() {
                  public void onGenerated(Palette p) {


                        //find the correct colors!
                      //most likely canary
                      Palette.Swatch lightvib = p.getLightVibrantSwatch();
                      //most likely green
                      Palette.Swatch vibrant = p.getVibrantSwatch();
                      //green
                      Palette.Swatch darkvib = p.getMutedSwatch();
                      Palette.Swatch darkmuted=p.getDarkMutedSwatch();
                      Palette.Swatch mutedlight = p.getLightMutedSwatch();
                      //find populations from the swatch
                      if(vibrant != null){
                          //vib should be blue
                          vib = vibrant.getPopulation();
                      }

                      if(darkvib != null){
                          //dark or darkvib should be green
                         dark= darkvib.getPopulation();
                      }

                      if(darkmuted != null){
                          darkm = darkmuted.getPopulation();
                      }

                      if(mutedlight != null){
                          lightm = mutedlight.getPopulation();
                      }

                      if(lightvib != null){
                          //dark or darkvib should be green
                          vibl = lightvib.getPopulation();
                      }

                        allmute = dark+ darkm+lightm;
                      //combine all muted colors

                      //https://chris.banes.me/2014/10/20/palette-v21/

                      FragmentManager fm = getSupportFragmentManager();
                      DialogChooseFragment dialog = new DialogChooseFragment();
                      Bundle bdl = new Bundle();
                      bdl.putInt("VIB_POP", vib);
                      bdl.putInt("DARK_POP", allmute);
                      bdl.putInt("VIBL_POP", vibl);
                      dialog.setArguments(bdl);
                      dialog.show(fm, display_results);
                  }
              });


        }

        });


        ImageButton bluebutton = (ImageButton)findViewById(R.id.bluebutton);
        bluebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        setColor(blue);
            }
        });
        ImageButton yellowbutton =(ImageButton)findViewById(R.id.yellowbutton);
        yellowbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setColor(sand);
            }
        });
        ImageButton greybutton =(ImageButton)findViewById(R.id.greybutton);
        greybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(grey);
            }
        });



        };

    public void setColor(int color) {
        Customcanvas.setBrushColor(color);
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

    //interface method from dialogfragment, called by it and defined here, linked to CanvasView's own cleraCanvas method
    public void clearcanvas(){
        Customcanvas.clearCanvas();
    }
}
