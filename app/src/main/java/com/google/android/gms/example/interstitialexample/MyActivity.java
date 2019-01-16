/*
 * Copyright (C) 2013 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.example.interstitialexample;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Main Activity. Inflates main activity xml.
 */
public class MyActivity extends AppCompatActivity {

    private static final long GAME_LENGTH_MILLISECONDS = 3000;

    private InterstitialAd interstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    private CountDownTimer countDownTimer;
    private Button retryButton, interstitialButton, bannerButton, videoButton;
    private boolean gameIsInProgress;
    private long timerMilliseconds;
    private String INTERSTITIAL_ID;
    private String BANNER_ID;
    private String VIDEO_ID;
    private TextView resultText;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        initTest();
    }


    @Override
    public void onResume() {
        // Start or resume the game.
        super.onResume();

    }

    @Override
    public void onPause() {
        // Cancel the timer if the game is paused.
        super.onPause();
    }





    private void initTest(){

        interstitialButton = (Button) findViewById(R.id.test_interstitial);
        bannerButton = findViewById(R.id.test_banner);
        videoButton = findViewById(R.id.test_video);

        interstitialButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.e("AR","am he c");
                initInterstitial();
            }
        });
        bannerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                initBanner();
            }
        });
        videoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                initVideo();
            }
        });

        resultText = (TextView) findViewById(R.id.resultView);
    }

    private void initInterstitial(){
        EditText unitId = (EditText) findViewById(R.id.interstitial_unit);
        INTERSTITIAL_ID = unitId.getText().toString();
        resultText.setVisibility(View.VISIBLE);
        clearResult();
        showResult("Loading Interstitial");
        showResult("Interstitial ID: "+ INTERSTITIAL_ID);
        if(INTERSTITIAL_ID == null || INTERSTITIAL_ID.length()<38){
            showResult("Error occured: INVALID UNIT");
            return;
        }
        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(INTERSTITIAL_ID);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                showToast(getString(R.string.congrat));
                showResult("INTERSTITAL UNIT WORKING FINE");
            }

            @Override
            public void onAdLoaded(){
                showToast("Interstitial Loaded");
                interstitialAd.show();
            }

            @Override
            public void onAdOpened(){

            }
            @Override
            public void onAdFailedToLoad( int errorCode){
                showToast("Failed To Load Ads: Error Code: "+ errorCode);
                showResult("Failed To Load Ad, Error Code: " + errorCode);
                showResult(getErrorText(errorCode));
            }
        });

        //if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        //}

    }

    private void initBanner(){
        EditText unitId = (EditText) findViewById(R.id.banner_unit);
        BANNER_ID = unitId.getText().toString();
        View adContainer = findViewById(R.id.adMobView);
        resultText.setVisibility(View.VISIBLE);
        clearResult();


        showResult("Loading Banner");
        showResult("Banner ID: "+ BANNER_ID);
        if(BANNER_ID == null || BANNER_ID.length()<38){
            showResult("Error occured: INVALID UNIT");
            return;
        }
        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(BANNER_ID);
        ((RelativeLayout)adContainer).addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                showToast("Banner Loaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                showToast("Failed To Load Ads: Error Code: "+ errorCode);
                showResult("Failed To Load Ad, Error Code: " + errorCode);
                showResult(getErrorText(errorCode));
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                showToast("Banner Loaded");
                showResult("Banner Loaded");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                showToast(getString(R.string.congrat));
                showResult("Banner UNIT WORKING FINE");
            }
        });


    }

    private void initVideo(){
        EditText unitId = (EditText) findViewById(R.id.video_unit);
        VIDEO_ID = unitId.getText().toString();
        resultText.setVisibility(View.VISIBLE);
        clearResult();
        showResult("Video ID: "+ VIDEO_ID);
        if(VIDEO_ID == null || VIDEO_ID.length()<38){
            showResult("Error occured: INVALID UNIT");
            return;
        }
        showResult("Loading video ...");
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem reward) {
                showToast("onRewarded! currency: " + reward.getType() + "  amount: " +
                        reward.getAmount());
                // Reward the user.
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                showToast(getString(R.string.congrat));
                showResult("VIDEO UNIT WORKING FINE");
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                showToast("Failed To Load Ads: Error Code: "+ errorCode);
                showResult("Failed To Load Ad, Error Code: " + errorCode);
                showResult(getErrorText(errorCode));
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                mRewardedVideoAd.show();
                showToast("Video Loaded");
                showResult("Video Loaded");
            }

            @Override
            public void onRewardedVideoAdOpened() {
            }

            @Override
            public void onRewardedVideoStarted() {
            }

            @Override
            public void onRewardedVideoCompleted() {
            }

        });
        mRewardedVideoAd.loadAd(VIDEO_ID,
                        new AdRequest.Builder().build());

    }





    private void showResult(String message){
        //String prevMessage = resultText.getText().toString();
        resultText.setVisibility(View.VISIBLE);
        resultText.append("\n" + message);
    }

    private void clearResult(){
        resultText.setText(null);
    }

    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private String getErrorText(int errorCode){
        String textCode = "";
        switch (errorCode){
            case 3:
                textCode = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
                break;
            case 2:
                textCode = "The ad request was unsuccessful due to network connectivity.";
                break;
            case 1:
                textCode = "The ad request was invalid; for instance, the ad unit ID was incorrect.";
                break;
                default:
                    textCode = "Unkown Error";
                    break;
        }
        return textCode;
    }
}
