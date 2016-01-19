package com.thefallen.copreg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
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
import android.view.animation.AccelerateDecelerateInterpolator;
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

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import pl.droidsonroids.gif.GifImageView;

// This as per the name is the Main Activity.Everything that the user does or sees is handled in this file.
// Functions : onCreate,setLayoutParams,setTypeface,splashAnimate, getCard, onBackPressed, registerOnClick, fabClick
// callApi, onSuccess, errorSnack, fabPop, registerPopIn, registerPopOut, tickReveal.

public class MainActivity extends AppCompatActivity {

    //Declarations of views buttons contexts, etc.
    GifImageView gifImageView;
    GifImageView onSuccessGIF;
    FrameLayout splashScreen;
    Context mContext;
    ViewGroup[] member;
    EditText teamname_frontside;
    EditText teamname_shadow;
    EditText teamname_backside;
    Button submit_button;
    TextView registerHint;
    TextView memberHint;
    Boolean requestInQueue = false;
    int numOnScreenMembers = 0;
    Boolean backPressedTwice = false;
    int Overshoot;
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
        onSuccessGIF = (GifImageView) findViewById(R.id.tick);
        splashScreen = (FrameLayout) findViewById(R.id.splashScreen);
        submit_button = (Button) findViewById(R.id.register);
        registerHint = (TextView) findViewById(R.id.registertwo);
        memberHint = (TextView) findViewById(R.id.addmember);

        member=new ViewGroup[3];
        member[0] = (ViewGroup) findViewById(R.id.card1);
        member[1] = (ViewGroup) findViewById(R.id.card2);
        member[2] = (ViewGroup) findViewById(R.id.card3);

        setTypeface();

        submit_button.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/roboto/Roboto-Thin.ttf"));

        setLayoutParams();
        splashAnimate();
        fabPop();
        fabClick();
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
        gifLayoutParams.topMargin = DisplayHelper.getHeight(mContext) / 2 - 256;
        gifImageView.setLayoutParams(gifLayoutParams);

        //on success GIF parameters
        ViewGroup.LayoutParams mRevealLayoutParams = onSuccessGIF.getLayoutParams();
        mRevealLayoutParams.height  = (int)(DisplayHelper.getHeight(mContext)*0.56);
        onSuccessGIF.setLayoutParams(mRevealLayoutParams);

        //Cards that contain the information of a team member
        member[0].setTranslationY(DisplayHelper.getHeight(mContext));
        member[1].setTranslationY(DisplayHelper.getHeight(mContext));
        member[2].setTranslationY(DisplayHelper.getHeight(mContext));

        //Submit Button
        submit_button.setTranslationY(DisplayHelper.getHeight(mContext));
        registerHint.setTranslationY(DisplayHelper.getHeight(mContext));
        memberHint.setTranslationY((int) (DisplayHelper.getHeight(mContext) * 0.4) + DisplayHelper.dpToPx(50, mContext));

        //Team name Display and edit field
        ViewGroup.LayoutParams teamParams = teamname_backside.getLayoutParams();
        teamParams.height = (int) (DisplayHelper.getHeight(mContext) * 0.4);
        teamname_backside.setLayoutParams(teamParams);
        teamname_frontside.setLayoutParams(teamParams);
        teamname_shadow.setLayoutParams(teamParams);

    }

    //Sets typeface for the team name EditText
    public void setTypeface(){
        teamname_frontside = (EditText)findViewById(R.id.teamname_frontside);
        teamname_shadow = (EditText)findViewById(R.id.teamname_shadow);
        teamname_backside = (EditText)findViewById(R.id.teamname_backside);

        //Relays the text info to the front side and shadow
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

        Typeface typeFace_front_side = Typeface.createFromAsset(getAssets(), "fonts/typogami/Typogami-Frontside.otf");
        Typeface typeFace_shadow= Typeface.createFromAsset(getAssets(), "fonts/typogami/Typogami-Shadow.otf");
        Typeface typeFace_back_side = Typeface.createFromAsset(getAssets(), "fonts/typogami/Typogami-Backside.otf");
        teamname_frontside.setTypeface(typeFace_front_side);
        teamname_shadow.setTypeface(typeFace_shadow);
        teamname_backside.setTypeface(typeFace_back_side);
    }

    //GIF animate
    public void splashAnimate() {
        int splashDelay = 2800;
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
                .translationY((int) (DisplayHelper.getHeight(mContext) * 0.4) + DisplayHelper.dpToPx(50 + mem_id * 50, mContext))
                .setInterpolator(new AnticipateOvershootInterpolator(1));
    }

    //Removes the last added card on pressing back button, and exits the app on pressing back twice
    @Override
    public void onBackPressed(){
        if(numOnScreenMembers>0){
            numOnScreenMembers--;
            member[numOnScreenMembers].animate()
                    .translationY(DisplayHelper.getHeight(mContext))
                    .setInterpolator(new AnticipateInterpolator(1))
                    .setDuration(500);
            ((TextView) member[numOnScreenMembers].getChildAt(0)).setText("");
            ((TextView) member[numOnScreenMembers].getChildAt(1)).setText("");
            if(numOnScreenMembers==0){
                memberHint.animate()
                        .alpha(1.0f)
                        .setDuration(500)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .start();
            }
            if(numOnScreenMembers<2)
                registerPopOut();
        }
        else {
            if (backPressedTwice)
                super.onBackPressed();

            backPressedTwice = true;

            Toast.makeText(mContext,"Press back again to exit",Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    backPressedTwice = false;
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
                ArrayList<MemberData> memberData = new ArrayList<>();
                for (int i = 0; i < numOnScreenMembers; i++) {
                    memberData.add(new MemberData(((EditText) member[i].getChildAt(0)).getText().toString(), ((EditText) member[i].getChildAt(1)).getText().toString()));
                }
                if (VerifyData(teamname_backside.getText().toString(), memberData)) {
                    callApi(teamname_backside.getText().toString(), memberData);
                }
            }
        });
    }

    //Governs action taken on the fab being clicked
    public void fabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Hides keyboard when Fab is pressed
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(teamname_backside.getWindowToken(), 0);

                if(numOnScreenMembers==0){
                    memberHint.animate()
                            .alpha(0.0f)
                            .setDuration(500)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .start();
                    getCard(member[0], 0);
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
    public boolean VerifyData(String teamName, ArrayList<MemberData> memberData)
    {
        if (teamName.equals("") || teamName.charAt(0) == '#') {
            errorSnack(R.string.error_team_name);
            return false;
        }
        String name, entry_no;
        for (int i = 0; i < memberData.size(); i++)
        {
            name = memberData.get(i).getName();
            entry_no = memberData.get(i).getEntry_no();
            if (name.equals("") || !name.matches("[A-z .]+"))
            {
                errorSnack(R.string.error_name);
                return false;
            }

            if (entry_no.length() != 11
                    ||
                    !entry_no.substring(0, 4).matches("[0-9]+")
                    ||
                    !entry_no.substring(4, 6).matches("[A-z]+")
                    ||
                    !entry_no.substring(6, 7).matches("^[a-zA-Z0-9]*$")
                    ||
                    !entry_no.substring(7, 11).matches("[0-9]+")
                    )
            {
                errorSnack(R.string.error_entry);

                return false;
            } else if (!checkDep(entry_no.substring(4, 7)))
            {
                errorSnack(R.string.error_entry);

                return false;
            } else if (!checkYear(entry_no.substring(0, 4)))
            {
                errorSnack(R.string.error_entry);

                return false;
            }


        }
        return true;
    }

    //Validates the department code in the entry number

    boolean checkDep(String mid3)
    {
        mid3 = mid3.toLowerCase();

        if(mid3.substring(2).matches("[0-9]"))
        {
            if (mid3.contentEquals("bb1")
                    || mid3.contentEquals("ch1")
                    || mid3.contentEquals("cs1")
                    || mid3.contentEquals("ce1")
                    || mid3.contentEquals("ee1")
                    || mid3.contentEquals("ee3")
                    || mid3.contentEquals("mt1")
                    || mid3.contentEquals("me1")
                    || mid3.contentEquals("me2")
                    || mid3.contentEquals("ph1")
                    || mid3.contentEquals("tt1")
                    || mid3.contentEquals("bb5")
                    || mid3.contentEquals("ch7")
                    || mid3.contentEquals("cs5")
                    || mid3.contentEquals("mt6")
                    )
            {
                return true;
            }
        }
        return false;
    }

    //Validates the Year in the entry number

    boolean checkYear(String first4)
    {
        int year = Integer.parseInt(first4);

        return year <= 2014 && year >= 2000;

    }


    //Calls the API
    public void callApi(final String teamName, final ArrayList<MemberData> memberData)
    {
            String url = getResources().getString(R.string.url);

            // Request a string response
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            requestInQueue = false;
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
                            requestInQueue = false;
                            // Error handling
                            volleyError.printStackTrace();

                            int statusCode=200;
                            try{
                                statusCode=volleyError.networkResponse.statusCode;
                            }catch (Exception e ){
                                e.printStackTrace();
                            }
                            if(volleyError instanceof NoConnectionError) {
                                errorSnack(R.string.error_noInternet);
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
                            for (int i = 0; i < memberData.size(); i++)
                            {
                                params.put("name" + (i + 1), memberData.get(i).getName());
                                params.put("entry" + (i + 1), memberData.get(i).getEntry_no());
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
        if (!requestInQueue) {
            requestInQueue = true;
                Volley.newRequestQueue(this).add(stringRequest);
            }
    }

    //Dispalys Success message
    public void onSuccess(){
        tickReveal();
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
        registerHint.animate()
                .translationY(DisplayHelper.getHeight(mContext) - registerHint.getHeight() - DisplayHelper.dpToPx(120, mContext))
                .setDuration(500)
                .setStartDelay(400)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        registerHint.animate()
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


    //on success GIF circular reveal animation
    public void tickReveal(){

        // for API < 21 Android ViewAnimationsUtils circular reveal is not supported
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            final SupportAnimator tickAnimator =
                    ViewAnimationUtils.createCircularReveal(onSuccessGIF, onSuccessGIF.getWidth(), DisplayHelper.getHeight(mContext), 0, onSuccessGIF.getHeight());
            tickAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            tickAnimator.setDuration(800);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SupportAnimator animator_reverse = tickAnimator.reverse();
                    animator_reverse.addListener(new SupportAnimator.AnimatorListener() {

                        @Override
                        public void onAnimationEnd() {
                            onSuccessGIF.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationStart() {
                        }

                        @Override
                        public void onAnimationCancel() {
                        }

                        @Override
                        public void onAnimationRepeat() {
                        }
                    });
                    animator_reverse.start();
                }
            }, 4000);

            onSuccessGIF.setVisibility(View.VISIBLE);
                tickAnimator.start();

        } else {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(onSuccessGIF, onSuccessGIF.getWidth() / 2, onSuccessGIF.getHeight(), 0, DisplayHelper.getHeight(mContext));
            onSuccessGIF.setVisibility(View.VISIBLE);
                anim.start();

            anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Animator anim = android.view.ViewAnimationUtils.createCircularReveal(onSuccessGIF, onSuccessGIF.getWidth() / 2, onSuccessGIF.getHeight(), DisplayHelper.getHeight(mContext), 0);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                onSuccessGIF.setVisibility(View.INVISIBLE);
                            }
                        });
                        anim.setStartDelay(3000);
                        anim.start();
                }});
            }
        }
}
