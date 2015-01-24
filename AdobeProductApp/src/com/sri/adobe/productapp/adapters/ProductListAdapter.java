package com.sri.adobe.productapp.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Space;

import com.sri.adobe.productapp.R;
import com.sri.adobe.productapp.json.Product;
import com.sri.adobe.productapp.views.ColoredRatingBar;
import com.sri.adobe.productapp.views.TextAwesome;

public class ProductListAdapter extends BaseAdapter {

	private ArrayList<Product> productList = new ArrayList<Product>();
	private ArrayList<Product> filteredproductList = new ArrayList<Product>();
	private static LayoutInflater inflater;
	private Filter productFilter;
	private String currentFilter;

	public ProductListAdapter(ArrayList<Product> productList, Context cxt) {
		this.productList = productList;
		this.filteredproductList = productList;
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

		Product product = filteredproductList.get(position);
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
		return filteredproductList.size();
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
	
	public Filter getFilter() {
		if (productFilter == null) {
			productFilter = new ProductFilter();
		}
		
		return productFilter;
	}

	private class ProductFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence filterText) {

			if (filterText != null) {
				currentFilter = filterText.toString().toLowerCase();
			} else {
				currentFilter = "";
				filterText = "";
			}

			FilterResults result = new FilterResults();
			ArrayList<Product> filteredList = new ArrayList<Product>();
			int length = productList.size();
			
			if (currentFilter.length() == 0) {
				filteredList.addAll(productList);
			} else {
				for (int i=0; i<length; i++) {
					if (productList.get(i).getName().toLowerCase().contains(currentFilter)) {
						filteredList.add(productList.get(i));
					}
				}	
			}

			result.count = filteredList.size();
			result.values = filteredList;
			return result;
		}

		@Override
		protected void publishResults(CharSequence filterText, FilterResults result) {
			filteredproductList = (ArrayList<Product>) result.values;
			notifyDataSetChanged();
		}
	}

}
