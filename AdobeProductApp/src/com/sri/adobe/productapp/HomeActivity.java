package com.sri.adobe.productapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.sri.adobe.productapp.adapters.ProductListAdapter;
import com.sri.adobe.productapp.api.AsyncConnection;
import com.sri.adobe.productapp.api.ConnectionListener;
import com.sri.adobe.productapp.api.ServerCodes;
import com.sri.adobe.productapp.json.JSONKeys;
import com.sri.adobe.productapp.json.Product;
import com.sri.adobe.productapp.json.Product.ProductSort;
import com.sri.adobe.productapp.utils.L;
import com.sri.adobe.productapp.utils.ServerUtils;
import com.sri.adobe.productapp.views.ButtonAwesome;
import com.sri.adobe.productapp.views.TextAwesome;

public class HomeActivity extends BaseActivity {

	private ListView productListView;
	private ProgressBar progressBar;
	private ArrayList<Product> productList;
	private LinearLayout errorLayout;
	private TextAwesome networkError;
	private ButtonAwesome refreshButton;
	private ProductListAdapter productListAdapter;

	@Override
	protected void onResume() {
		super.onResume();
		productList = AdobeProductApp.getProductList();
		if (productList == null) {
			callApi();
		} else {
			showProductList();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUi();
		initComponents();
	}

	private void initUi() {
		productListView = (ListView) findViewById(R.id.product_listView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		networkError = (TextAwesome) findViewById(R.id.error_text);
		errorLayout = (LinearLayout) findViewById(R.id.error_layout);
		refreshButton = (ButtonAwesome) findViewById(R.id.tryagain);
		refreshButton.setText(String.format(getString(R.string.refresh_text), "Try again"));
	}

	private void initComponents() {
		refreshButton.setOnClickListener(refreshClickListener);
		productListView.setOnItemClickListener(productItemClickListener);
	}

	private OnClickListener refreshClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			callApi();
		}
	};

	private void callApi() {
		progressBar.setVisibility(View.VISIBLE);
		productListView.setVisibility(View.GONE);
		errorLayout.setVisibility(View.GONE);

		AsyncConnection productListConnection = new AsyncConnection(AsyncConnection.METHOD_GET, ServerUtils.PRODUCTLIST_URL, null, null, new ConnectionListener() {

			@Override
			public void OnSuccess(String result) {
				L.d("product list call success");
				try {
					JSONArray jArray = new JSONArray(result);
					productList = new ArrayList<Product>();
					for (int i=0; i<jArray.length(); i++) {
						JSONObject jobj = jArray.getJSONObject(i);
						if (!jobj.optString(JSONKeys.NAME).equals("")) {
							productList.add(new Product(jobj));
						}
					}
					AdobeProductApp.setProductList(productList);
					showProductList();
				} catch (JSONException e) {
					networkError.setText(String.format(getString(R.string.network_error), "Unable to process the request."));
				}
			}

			@Override
			public void OnFailure(String error, int status) {
				networkError.setText(String.format(getString(R.string.network_error), error));
				showNetworkError();
			}

			@Override
			public void OnConnectionError(int errorCode) {
				networkError.setText(String.format(getString(R.string.network_error), ServerCodes.getErrorMsg(errorCode)));
				showNetworkError();
			}
		});
		productListConnection.execute();
	}

	private void showNetworkError() {
		errorLayout.setVisibility(View.VISIBLE);
		productListView.setVisibility(View.GONE);
		progressBar.setVisibility(View.GONE);
	}

	private void showProductList() {
		errorLayout.setVisibility(View.GONE);
		productListView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);

		if (productList != null) {
			productListAdapter = new ProductListAdapter(productList, this);
			productListView.setAdapter(productListAdapter);
			productListAdapter.notifyDataSetChanged();
		}
	}

	private OnItemClickListener productItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent i = new Intent(HomeActivity.this, ProductActivity.class);
			i.putExtra(AdobeProductApp.PRODUCT_POSITION, position);
			startActivity(i);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = new MenuInflater(this);
		menuInflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_sort_by_inapp:
			if (productList == null) {
				return false;
			}

			Product.setSort(ProductSort.INAPP_PURCHASE);
			Collections.sort(productList);
			if (productListAdapter != null) {
				productListAdapter.notifyDataSetChanged();
			}
			break;

		case R.id.action_sort_by_rating:
			if (productList == null) {
				return false;
			}

			Product.setSort(ProductSort.RATING);
			Collections.sort(productList);
			if (productListAdapter != null) {
				productListAdapter.notifyDataSetChanged();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
