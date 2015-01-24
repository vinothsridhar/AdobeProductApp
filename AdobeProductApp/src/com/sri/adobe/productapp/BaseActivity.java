package com.sri.adobe.productapp;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.sri.adobe.productapp.utils.L;


public class BaseActivity extends SherlockActivity {

	private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
	protected void onStart() {
		L.d(TAG, "Inside onStart()");
		super.onStart();
	}

	@Override
	protected void onResume() {
		L.d(TAG, "Inside onResume()");
		super.onResume();
	}

	@Override
	protected void onPause() {
		L.d(TAG, "Inside onPause()");
		super.onPause();
	}

	@Override
	protected void onStop() {
		L.d(TAG, "Inside onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void finish() {
		L.d(TAG, "Inside finish()");
		super.finish();
	}
}
