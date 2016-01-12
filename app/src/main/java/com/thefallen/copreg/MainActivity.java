package com.thefallen.copreg;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    GifImageView gifImageView;
    FrameLayout splashScreen;
    Context mContext;
    ViewGroup[] member;
    EditText teamname_frontside;
    EditText teamname_shadoow;
    EditText teamname_backside;
    Button register;
    int numOnScreenMembers=0;
    Boolean backpressedtwice=false;
    private FloatingActionButton fab ;
    int Overshoot ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        Overshoot = DisplayHelper.dpToPx(50,mContext);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        //Views
        gifImageView = (GifImageView) findViewById(R.id.logo);
        splashScreen = (FrameLayout) findViewById(R.id.splashScreen);
        register = (Button) findViewById(R.id.register);

        member=new ViewGroup[3];
        member[0] = (ViewGroup) findViewById(R.id.card1);
        member[1] = (ViewGroup) findViewById(R.id.card2);
        member[2] = (ViewGroup) findViewById(R.id.card3);

        setTypeface();

        Typeface typeFace_frontside= Typeface.createFromAsset(getAssets(), "fonts/Moldover-THICK.otf");
        register.setTypeface(typeFace_frontside);
        setLayoutParams();
        splashAnimate();
        fabPop();
        fabclick();
        registerOnClick();
    }

    //Layout Parameters for splash, GIF and screen
    public void setLayoutParams() {
//        fab.setY((int) (DisplayHelper.getHeight(mContext) * 0.4 - DisplayHelper.dpToPx(27, mContext)));
        ViewGroup.MarginLayoutParams fabParams = (ViewGroup.MarginLayoutParams)fab.getLayoutParams();
        fabParams.bottomMargin = ((int) (DisplayHelper.getHeight(mContext) * 0.165)); //+ DisplayHelper.dpToPx(36, mContext)));
        fab.setLayoutParams(fabParams);
        splashScreen.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (DisplayHelper.getHeight(mContext) * 1.4 + Overshoot)));
        splashScreen.setTranslationY(-Overshoot);
        FrameLayout.LayoutParams gifLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        gifLayoutParams.topMargin = DisplayHelper.getHeight(mContext) / 2;
        gifImageView.setLayoutParams(gifLayoutParams);
        member[0].setTranslationY(DisplayHelper.getHeight(mContext));
        member[1].setTranslationY(DisplayHelper.getHeight(mContext));
        member[2].setTranslationY(DisplayHelper.getHeight(mContext));
        register.setTranslationY(DisplayHelper.getHeight(mContext));
        ViewGroup.LayoutParams teamParams = teamname_backside.getLayoutParams();
        teamParams.height = (int) (DisplayHelper.getHeight(mContext) * 0.4);
        teamname_backside.setLayoutParams(teamParams);
        teamname_frontside.setLayoutParams(teamParams);
        teamname_shadoow.setLayoutParams(teamParams);

    }

    public void setTypeface(){
        teamname_frontside = (EditText)findViewById(R.id.teamname_frontside);
        teamname_shadoow = (EditText)findViewById(R.id.teamname_shadow);
        teamname_backside = (EditText)findViewById(R.id.teamname_backside);

        //listener
        teamname_backside.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start < s.length())
                    if (s.charAt(start) == KeyEvent.KEYCODE_ENTER || s.charAt(start) == '\n') {
                        String moddedString = teamname_backside.getText().toString().substring(0, start) + teamname_backside.getText().toString().substring(start + count);
                        teamname_backside.setText(moddedString);
                        return;
                    }
                teamname_frontside.setText(s);
                teamname_shadoow.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Typeface typeFace_frontside= Typeface.createFromAsset(getAssets(), "fonts/typogami/Typogami-Frontside.otf");
        Typeface typeFace_shadow= Typeface.createFromAsset(getAssets(), "fonts/typogami/Typogami-Shadow.otf");
        Typeface typeFace_backside= Typeface.createFromAsset(getAssets(), "fonts/typogami/Typogami-Backside.otf");
        teamname_frontside.setTypeface(typeFace_frontside);
        teamname_shadoow.setTypeface(typeFace_shadow);
        teamname_backside.setTypeface(typeFace_backside);
    }

    //GIF animate
    public void splashAnimate() {
        int splashDelay = 2400;
        int splashDuration = 1000;
        // Start the animation
        //animate down
        splashScreen.animate()
                .setDuration(splashDuration)
                .setStartDelay(splashDelay)
                .translationY(-DisplayHelper.getHeight(mContext)-Overshoot)
                .setInterpolator(new AnticipateOvershootInterpolator(1));
    }
    public void getCard(View member, int mem)
    {
        int cardDuration = 700;
        member.animate()
                .setDuration(cardDuration)
                .translationY((int)(DisplayHelper.getHeight(mContext)*0.4)+DisplayHelper.dpToPx(27+mem*50,mContext))
                .setInterpolator(new AnticipateOvershootInterpolator(1));
    }

    @Override
    public void onBackPressed(){
        if(numOnScreenMembers>0){
            member[numOnScreenMembers-1].animate()
                    .translationY(DisplayHelper.getHeight(mContext))
                    .setInterpolator(new AnticipateInterpolator(1))
                    .setDuration(500);
            ((TextView)member[numOnScreenMembers-1].getChildAt(0)).setText("");
            ((TextView) member[numOnScreenMembers - 1].getChildAt(1)).setText("");
            numOnScreenMembers--;
            if(numOnScreenMembers<2)
                registerPopOut();
        }
        else {
            if(backpressedtwice)
            super.onBackPressed();
            backpressedtwice=true;
            Toast.makeText(mContext,"Press back again to exit",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    backpressedtwice=false;
                }
            }, 1000);


        }
    }

    public void registerOnClick()
    {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> dataName = new ArrayList<>();
                ArrayList<String> dataEntryNo = new ArrayList<>();
                for(int i=0;i<numOnScreenMembers;i++)
                {
                    dataName.add(((EditText)member[i].getChildAt(0)).getText().toString());
                    dataEntryNo.add(((EditText)member[i].getChildAt(1)).getText().toString());
                }
                if(VerifyData(dataName,dataEntryNo))
                {
                    callApi(teamname_backside.getText().toString(),dataName,dataEntryNo);
                }
            }
        });
    }
    public void fabclick(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(teamname_backside.getWindowToken(), 0);

                if(numOnScreenMembers==0){
                    getCard(member[0],0);
                    numOnScreenMembers++;
                }
                else if(numOnScreenMembers==1){
                    getCard(member[1],1);
                    numOnScreenMembers++;
                    registerPopIn();
                }
                else if(numOnScreenMembers==2){
                    getCard(member[2],2);
                    numOnScreenMembers++;
                }
                else if(numOnScreenMembers>2){
                    final Snackbar snackBar=Snackbar.make(register, "Max three members allowed", Snackbar.LENGTH_LONG);
                    snackBar.setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction("HIDE", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackBar.dismiss();
                                }
                            });
                            snackBar.show();
        }
            }
        });
    }

    public boolean VerifyData(ArrayList<String> dataName, ArrayList<String> dataEntryNo)
    {

        for (int i=0;i<dataName.size();i++)
        {
            if(dataName.get(i).equals("")||!dataName.get(i).matches("[A-z ]+"))
            {
                errorSnack(R.string.error_name);
                return false;
            }

            if(dataEntryNo.get(i).length()!=11
                    ||
                    !dataEntryNo.get(i).substring(0,4).matches("[0-9]+")
                    ||
                    !dataEntryNo.get(i).substring(4,6).matches("[A-z]+")
                    ||
                    !dataEntryNo.get(i).substring(6, 7).matches("^[a-zA-Z0-9]*$")
                    ||
                    !dataEntryNo.get(i).substring(7,11).matches("[0-9]+")
                    )
            {
                errorSnack(R.string.error_entry);

                return false;
            }
        }
        return true;
    }

    public void callApi(final String teamName, final ArrayList<String> dataName, final ArrayList<String> dataEntryNo)
    {
            String url = getResources().getString(R.string.url);

            // Request a string response
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getInt("RESPONSE_SUCCESS")==1){
                                    onSuccess();
                                }
                                else{
                                    final Snackbar snackBar=Snackbar.make(register,jsonObject.getString("RESPONSE_MESSAGE"), Snackbar.LENGTH_LONG);
                                    snackBar.setActionTextColor(getResources().getColor(R.color.colorAccent))
                                            .setAction("HIDE", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    snackBar.dismiss();
                                                }
                                            });
                                    snackBar.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // Result handling
                            System.out.println(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                            // Error handling
                            volleyError.printStackTrace();

                            int statusCode=200;
                            try{
                                statusCode=volleyError.networkResponse.statusCode;
                            }catch (Exception e ){
                                e.printStackTrace();
                            }
                            if(volleyError instanceof NoConnectionError) {
//                                errorSnack(R.string.error_noInternet);
                                onSuccess();
                            }

                            else if(statusCode==500 ) {

                                errorSnack(R.string.error_serverError);
                            }
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<>();
                            params.put("teamname", teamName);
                            for(int i=0;i<dataName.size();i++)
                            {
                                params.put("name"+(i+1),dataName.get(i));
                                params.put("entry"+(i+1),dataEntryNo.get(i));
                            }
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("Content-Type","application/x-www-form-urlencoded");
                            return params;
                        }
                    };

            // Add the request to the queue
            Volley.newRequestQueue(this).add(stringRequest);
    }

    public void onSuccess(){
        int numOnScreenMembersTemp=numOnScreenMembers;
        for(int i=0;i<numOnScreenMembersTemp;i++)onBackPressed();
        teamname_backside.setText(R.string.teamnameStub);
        final Snackbar snackBar = Snackbar.make(member[0], getResources().getString(R.string.successMessage), Snackbar.LENGTH_LONG);

        snackBar.setActionTextColor(getResources().getColor(R.color.colorAccent))
                .setAction("HIDE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackBar.dismiss();
                    }
                });
        snackBar.show();
    }

    public void errorSnack(int id){
        final Snackbar snackBar = Snackbar.make(member[0], getResources().getString(id), Snackbar.LENGTH_LONG);

        snackBar.setActionTextColor(getResources().getColor(R.color.colorAccent))
                .setAction("HIDE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackBar.dismiss();
                    }
                });
        snackBar.show();
    }

    public void fabPop()
    {
        fab.setScaleX(0);
        fab.setScaleY(0);
//        fab.setY(fab.getY()-(int)(DisplayHelper.getHeight(mContext)*0.1));
        fab.animate()
                .scaleXBy(1)
                .scaleYBy(1)
                .setStartDelay(3400)
                .setInterpolator(new OvershootInterpolator(3));
    }
    public void registerPopIn()
    {
        register.animate()
                .translationY(DisplayHelper.getHeight(mContext) -register.getHeight()-DisplayHelper.dpToPx(55,mContext))
                .setDuration(500)
                .setStartDelay(400)
                .setInterpolator(new OvershootInterpolator());
    }

    public void registerPopOut()
    {
        register.animate()
                .translationY(DisplayHelper.getHeight(mContext))
                .setDuration(500)
                .setInterpolator(new OvershootInterpolator());
    }


}
