package com.demo.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.leonisand.leonis.Image.ImageListener;
import co.leonisand.offers.OffersCoupon;

import com.tutecentral.navigationdrawer.R;

public class AdapterCoupon extends BaseAdapter {
	public ArrayList<OffersCoupon> mList;
	private Context mContext;
	private ItemCoupon itemCoupon;

	public AdapterCoupon(ArrayList<OffersCoupon> pList, Context pContext) {
		// TODO Auto-generated constructor stub
		mList = pList;
		mContext = pContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			view = inflater.inflate(R.layout.layout_item_coupon, parent, false);
			itemCoupon = new ItemCoupon();
			itemCoupon.title = (TextView) view.findViewById(R.id.textView1);
			itemCoupon.description = (TextView) view
					.findViewById(R.id.textView2);
			itemCoupon.icon = (ImageView) view.findViewById(R.id.imageView1);
			view.setTag(itemCoupon);

		} else {
			itemCoupon = (ItemCoupon) view.getTag();

		}

		OffersCoupon offerscoupon = (OffersCoupon) mList.get(position);
		String s;
		if (offerscoupon.getRead()) {
			s = "既読";
		} else {
			s = "未読";
		}
		itemCoupon.title.setText("[" + s + "]"
				+ offerscoupon.getAvailableFrom().toString() + " "
				+ offerscoupon.getTitle());
		offerscoupon.thumbnailImageBitmap(
				itemCoupon.icon,
				imageListener);
		return view;
	}
	ImageListener imageListener = new ImageListener() {
		public void onDone(View view, Bitmap bitmap) {
			((ImageView) view).setImageBitmap(bitmap);
		}
	};
	private static class ItemCoupon {
		TextView description, title;
		ImageView icon;

	}

}
