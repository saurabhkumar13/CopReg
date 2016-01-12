package com.thefallen.copreg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
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

    //Declarations of views buttons contexts, etc.
    GifImageView gifImageView;
    FrameLayout splashScreen;
    Context mContext;
    ViewGroup[] member;
    EditText teamname_frontside;
    EditText teamname_shadow;
    EditText teamname_backside;
    Button submit_button;
    TextView registertwo;
    int numOnScreenMembers=0;
    Boolean backpressedtwice=false;
    int Overshoot ;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        Overshoot = DisplayHelper.dpToPx(50,mContext);

        //Connecting objects to layouts in xml

        fab = (FloatingActionButton) findViewById(R.id.fab);

        gifImageView = (GifImageView) findViewById(R.id.logo);
        splashScreen = (FrameLayout) findViewById(R.id.splashScreen);
        submit_button = (Button) findViewById(R.id.register);
        registertwo=(TextView)findViewById(R.id.registertwo);

        member=new ViewGroup[3];
        member[0] = (ViewGroup) findViewById(R.id.card1);
        member[1] = (ViewGroup) findViewById(R.id.card2);
        member[2] = (ViewGroup) findViewById(R.id.card3);

        setTypeface();

        Typeface typeFace_frontside= Typeface.createFromAsset(getAssets(), "fonts/Moldover-THICK.otf");
        submit_button.setTypeface(typeFace_frontside);

        setLayoutParams();
        splashAnimate();
        fabPop();
        fabclick();
        registerOnClick();

    }

    //This function sets size and position parameters for the different views and screens.
    public void setLayoutParams() {

        //Floating Action Button
        ViewGroup.MarginLayoutParams fabParams = (ViewGroup.MarginLayoutParams)fab.getLayoutParams();
        fabParams.bottomMargin = ((int) (DisplayHelper.getHeight(mContext) * 0.165));
        fab.setLayoutParams(fabParams);

        //Splash Screen
        splashScreen.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (DisplayHelper.getHeight(mContext) * 1.4 + Overshoot)));
        splashScreen.setTranslationY(-Overshoot);

        //Gif that shows the name of the app CopReg
        FrameLayout.LayoutParams gifLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        gifLayoutParams.topMargin = DisplayHelper.getHeight(mContext) / 2;
        gifImageView.setLayoutParams(gifLayoutParams);

        //Cards that contain the information of a team member
        member[0].setTranslationY(DisplayHelper.getHeight(mContext));
        member[1].setTranslationY(DisplayHelper.getHeight(mContext));
        member[2].setTranslationY(DisplayHelper.getHeight(mContext));

        //Submit Button
        submit_button.setTranslationY(DisplayHelper.getHeight(mContext));
        registertwo.setTranslationY(DisplayHelper.getHeight(mContext));

        //Team name Display and edit field
        ViewGroup.LayoutParams teamParams = teamname_backside.getLayoutParams();
        teamParams.height = (int) (DisplayHelper.getHeight(mContext) * 0.4);
        teamname_backside.setLayoutParams(teamParams);
        teamname_frontside.setLayoutParams(teamParams);
        teamname_shadow.setLayoutParams(teamParams);

    }

    //Sets typeface for the submit button
    public void setTypeface(){
        teamname_frontside = (EditText)findViewById(R.id.teamname_frontside);
        teamname_shadow = (EditText)findViewById(R.id.teamname_shadow);
        teamname_backside = (EditText)findViewById(R.id.teamname_backside);

        //Relays the text info to the other two fields.
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
                        teamname_frontside.setText(moddedString);
                        teamname_shadow.setText(moddedString);
                        return;
                    }
                teamname_frontside.setText(s);
                teamname_shadow.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Typeface typeFace_frontside= Typeface.createFromAsset(getAssets(), "fonts/typogami/Typogami-Frontside.otf");
        Typeface typeFace_shadow= Typeface.createFromAsset(getAssets(), "fonts/typogami/Typogami-Shadow.otf");
        Typeface typeFace_backside= Typeface.createFromAsset(getAssets(), "fonts/typogami/Typogami-Backside.otf");
        teamname_frontside.setTypeface(typeFace_frontside);
        teamname_shadow.setTypeface(typeFace_shadow);
        teamname_backside.setTypeface(typeFace_backside);
    }

    //GIF animate
    public void splashAnimate() {
        int splashDelay = 2400;
        int splashDuration = 1000;
        // Start the animation
        splashScreen.animate()
                .setDuration(splashDuration)
                .setStartDelay(splashDelay)
                .translationY(-DisplayHelper.getHeight(mContext)-Overshoot)
                .setInterpolator(new AnticipateOvershootInterpolator(1));
    }

    //Animates and positions the cards on the screen
    public void getCard(View member, int mem_id)
    {
        int cardDuration = 700;
        member.animate()
                .setDuration(cardDuration)
                .translationY((int)(DisplayHelper.getHeight(mContext)*0.4)+DisplayHelper.dpToPx(27+mem_id*50,mContext))
                .setInterpolator(new AnticipateOvershootInterpolator(1));
    }

    //Removes the last added card on pressing back button, and exits the app on pressing back twice
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

    //Checks validity and calls the api calling function
    public void registerOnClick()
    {
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> dataName = new ArrayList<>();
                ArrayList<String> dataEntryNo = new ArrayList<>();
                for (int i = 0; i < numOnScreenMembers; i++) {
                    dataName.add(((EditText) member[i].getChildAt(0)).getText().toString());
                    dataEntryNo.add(((EditText) member[i].getChildAt(1)).getText().toString());
                }
                if (VerifyData(dataName, dataEntryNo)) {
                    callApi(teamname_backside.getText().toString(), dataName, dataEntryNo);
                }
            }
        });
    }

    //Governs action taken on the fab being clicked
    public void fabclick(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Hides keyboard when Fab is pressed
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(teamname_backside.getWindowToken(), 0);

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
                    final Snackbar snackBar=Snackbar.make(submit_button, "Max three members allowed", Snackbar.LENGTH_LONG);
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

    //Checks for validity of the entry number
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
                    !dataEntryNo.get(i).substring(6,7).matches("^[a-zA-Z0-9]*$")
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


    //Calls the API
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
                                    final Snackbar snackBar=Snackbar.make(submit_button,jsonObject.getString("RESPONSE_MESSAGE"), Snackbar.LENGTH_LONG);
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

    //Dispalys Success message
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

    //Display error in the snack bar
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

    //Entry for fab in the screen
    public void fabPop()
    {
        fab.setScaleX(0);
        fab.setScaleY(0);
        fab.animate()
                .scaleXBy(1)
                .scaleYBy(1)
                .setStartDelay(3400)
                .setInterpolator(new OvershootInterpolator(3));
    }

    //Button displayed in the screen
    public void registerPopIn()
    {
        submit_button.animate()
                .translationY(DisplayHelper.getHeight(mContext) - DisplayHelper.dpToPx(85, mContext))
                .setDuration(500)
                .setStartDelay(400)
                .setInterpolator(new OvershootInterpolator());
        registertwo.animate()
                .translationY(DisplayHelper.getHeight(mContext) - registertwo.getHeight() - DisplayHelper.dpToPx(120, mContext))
                .setDuration(500)
                .setStartDelay(400)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        registertwo.animate()
                                .translationY(DisplayHelper.getHeight(mContext))
                                .setStartDelay(4000)
                                .setDuration(500)
                                .setInterpolator(new AnticipateInterpolator());
                    }
                });
    }

    //Removes the display of the register button
    public void registerPopOut()
    {
        submit_button.animate()
                .translationY(DisplayHelper.getHeight(mContext))
                .setDuration(500)
                .setInterpolator(new OvershootInterpolator());
    }


}
