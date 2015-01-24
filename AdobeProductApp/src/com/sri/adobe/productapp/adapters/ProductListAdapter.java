package com.sri.adobe.productapp.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sri.adobe.productapp.R;
import com.sri.adobe.productapp.imageloader.SriImageLoader;
import com.sri.adobe.productapp.json.Product;
import com.sri.adobe.productapp.utils.L;
import com.sri.adobe.productapp.views.ColoredRatingBar;
import com.sri.adobe.productapp.views.TextAwesome;

public class ProductListAdapter extends BaseAdapter {

	private ArrayList<Product> productList = new ArrayList<Product>();
	private static LayoutInflater inflater;

	public ProductListAdapter(ArrayList<Product> productList, Context cxt) {
		this.productList = productList;
		inflater = (LayoutInflater) cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProductViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_product, null);
			holder = new ProductViewHolder();
			holder.productName = (TextAwesome) convertView.findViewById(R.id.product_name);
			holder.productRating = (ColoredRatingBar) convertView.findViewById(R.id.product_rating);
			holder.productRatingText = (TextAwesome) convertView.findViewById(R.id.product_rating_text);
			holder.productInapp = (TextAwesome) convertView.findViewById(R.id.product_inapppurchase);
			convertView.setTag(holder);
		} else {
			holder = (ProductViewHolder) convertView.getTag();
		}

		Product product = productList.get(position);
		holder.productName.setText(product.getName());
		holder.productRating.setRating(Float.parseFloat(product.getRating()));
		holder.productRatingText.setText(product.getRating());
		holder.productInapp.setVisibility(View.INVISIBLE);
		if (product.isInappPurchase()) {
			holder.productInapp.setText(String.format(inflater.getContext().getString(R.string.inapp_text), "YES"));
		} else {
			holder.productInapp.setText(String.format(inflater.getContext().getString(R.string.inapp_text), "NO"));
		}

		holder.productInapp.setVisibility(View.VISIBLE);


		return convertView;
	}

	@Override
	public int getCount() {
		return productList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	static class ProductViewHolder {
		private TextAwesome productName;
		private ColoredRatingBar productRating;
		private TextAwesome productInapp;
		private TextAwesome productRatingText;
	}

}
