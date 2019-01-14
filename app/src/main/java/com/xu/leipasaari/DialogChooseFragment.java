package com.xu.leipasaari;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Omistaja on 20/07/2016.
 */
public class DialogChooseFragment extends DialogFragment {
    //since we cant call activity methods from fragment directly,
    //we can use an interface


    public interface OnDialogListener{
        void clearcanvas();
    }
    private String VIB_POP="VIB_POP";
    private String DARK_POP="DARK_POP";
    private String VIBL_POP ="VIBL_POP";

    private int vib;
    private int dark;
    private int vibl;
    private String concatResult;

    //we will use onCreateView because muuch simpler

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){

        //Fetch the int values for population, these arguments are set in MainActivity
        vib = getArguments().getInt("VIB_POP");
        dark =getArguments().getInt("DARK_POP");
        vibl = getArguments().getInt("VIBL_POP");

        //turn the variables into trings
        String vibrant = String.valueOf(vib);
        String darkvibrant = String.valueOf(dark);
        String lightvibrant = String.valueOf(vibl);


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogfragment,null);
        //set the dialog textboxes to show the population.
        //create a share button too;
        TextView yellowpop =(TextView)v.findViewById(R.id.yellowpop);
        yellowpop.setText(lightvibrant);

        TextView greenpop = (TextView)v.findViewById(R.id.greenpop);
        greenpop.setText(darkvibrant);
        TextView bluepop = (TextView)v.findViewById(R.id.bluepop);
        bluepop.setText(vibrant);

        Button sharebutton = (Button)v.findViewById(R.id.sharebutton);
        sharebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                concatResult = "Yellow: " +vibl + " Blue: "+vib + " Grey: " + dark;
                //Send email only with results using SENDTO, with results loaded via concat
                Intent send = new Intent(android.content.Intent.ACTION_SENDTO, Uri.fromParts("mailto","dxjustice@gmail.com", null));
                send.putExtra(Intent.EXTRA_SUBJECT,"Recipe for Leipasaari");
                send.putExtra(Intent.EXTRA_TEXT, concatResult);
                try{
                    startActivity(Intent.createChooser(send,"Send your drawing"));
                }catch(android.content.ActivityNotFoundException ex){
                    Toast.makeText(getActivity(), "There are no email clients installed..",Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }

        });


        //clearcanvas button
        Button returnbutton = (Button)v.findViewById(R.id.returnbutton);
        returnbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               OnDialogListener activity = (OnDialogListener)getActivity();
                activity.clearcanvas();

                dismiss();
            }
        });
        return v;

    }
}
