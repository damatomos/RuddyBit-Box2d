package com.gaosworld.ruddybit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gaosworld.ruddybit.ads.AdHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AndroidLauncher extends AndroidApplication implements AdHandler {

	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	private static final String TAG = "AndroidLauncher";
	protected AdView adView;
	protected InterstitialAd interstitialAd;

	@SuppressLint("HandlerLeak")
	Handler handlerBanner = new Handler() {
		@Override
		public void handleMessage(@NonNull Message msg) {
			switch (msg.what) {
				case SHOW_ADS:
					adView.setVisibility(View.VISIBLE); break;
				case HIDE_ADS:
					adView.setVisibility(View.GONE); break;
			}
		}
	};

	@SuppressLint("HandlerLeak")
	Handler handlerInterstitial = new Handler() {
		@Override
		public void handleMessage(@NonNull Message msg) {
			switch (msg.what) {
				case SHOW_ADS:
					if ( interstitialAd.isLoaded() ) {
						interstitialAd.show();
					} else {
						Log.i(TAG, "Intertitial Failed...");
					}
					break;
				case HIDE_ADS:
					break;
			}
		}
	};

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		interstitialAd = new InterstitialAd(this);
		//interstitialAd.setAdUnitId("ca-app-pub-9828953737739239/6921573013");
		interstitialAd.loadAd(new AdRequest.Builder().build());

		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				// Load the next interstitial.
				interstitialAd.loadAd(new AdRequest.Builder().build());
			}

		});

		RelativeLayout layout = new RelativeLayout(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;

		View gameView = initializeForView(new RuddyBitGame(this), config);
		layout.addView(gameView);

		adView = new AdView(this);
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.i(TAG, "Ad Loaded...");
			}
		});

		adView.setAdSize(AdSize.BANNER);
		//adView.setAdUnitId("ca-app-pub-9828953737739239/6590153105");
		AdRequest.Builder builder = new AdRequest.Builder();

		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
		);

		layout.addView(adView, adParams);
		adView.loadAd(builder.build());

		setContentView(layout);
	}

	@Override
	public void showAds(boolean show) {
		//handlerBanner.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}

	@Override
	public void showInterstitial(boolean show) {
		//handlerInterstitial.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
}
