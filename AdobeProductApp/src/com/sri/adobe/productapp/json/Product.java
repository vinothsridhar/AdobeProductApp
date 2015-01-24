package com.sri.adobe.productapp.json;

import org.json.JSONException;
import org.json.JSONObject;

public class Product implements Comparable<Product> {

	private String name;
	private String type;
	private String url;
	private String image;
	private String rating;
	private String last_updated;
	private boolean isInAppPurchase;
	private String description;
	private static ProductSort sortMethod = ProductSort.NONE;

	public enum ProductSort {
		NONE, INAPP_PURCHASE, RATING;
	};

	public Product(JSONObject jobj) {
		this.name = jobj.optString(JSONKeys.NAME);
		this.type = jobj.optString(JSONKeys.TYPE);
		this.url = jobj.optString(JSONKeys.URL);
		this.image = jobj.optString(JSONKeys.IMAGE);
		this.rating = jobj.optString(JSONKeys.RATING);
		this.last_updated = jobj.optString(JSONKeys.LAST_UPDATED);
		this.isInAppPurchase = jobj.optString(JSONKeys.INAPP_PURCHASE).equals("YES") ? true : false;
		this.description = jobj.optString(JSONKeys.DESCRIPTION);
	}

	public Product(String jstring) throws JSONException {
		this(new JSONObject(jstring));
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public String getImage() {
		return image;
	}

	public String getRating() {
		return rating;
	}

	public String getLastUpdated() {
		return last_updated;
	}

	public boolean isInappPurchase() {
		return isInAppPurchase;
	}

	public String getDescription() {
		return description;
	}

	public static void setSort(ProductSort sort) {
		sortMethod = sort;
	}

	@Override
	public int compareTo(Product another) {
		if (sortMethod == ProductSort.RATING) {
			if (Float.parseFloat(this.getRating()) > Float.parseFloat(another.getRating())) {
				return -1;
			}
			if (Float.parseFloat(this.getRating()) < Float.parseFloat(another.getRating())) {
				return 1;
			}

			return 0;
		} else {
			if (this.isInappPurchase()) {
				return -1;
			}
			if (!this.isInappPurchase()) {
				return 1;
			}

			return 0;
		}
	}

}
