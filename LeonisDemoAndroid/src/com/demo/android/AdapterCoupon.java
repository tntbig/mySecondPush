package com.demo.android;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.leonisand.leonis.Image.ImageListener;
import co.leonisand.offers.OffersCoupon;
import co.leonisand.offers.OffersKit.OffersListener;
import co.leonisand.offers.OffersTemplate;

import com.tutecentral.navigationdrawer.R;

public class AdapterCoupon extends BaseAdapter {
	public ArrayList<OffersCoupon> mList;
	private Context mContext;
	private ItemCoupon itemCoupon;
	private MainActivity mActivity ;

	public AdapterCoupon(ArrayList<OffersCoupon> pList, Context pContext , MainActivity pActivity) {
		// TODO Auto-generated constructor stub
		mList = pList;
		mContext = pContext;
		mActivity = pActivity;
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
	public View getView(int position, final View convertView, ViewGroup parent) {
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
		offerscoupon.template(new OffersListener() {
			public void onDone(Map<String, Object> map) {
				if (map.get("template") != null) {
					@SuppressWarnings("unchecked")
					Map<String, Object> mapTemp = (Map<String, Object>) ((OffersTemplate) map
							.get("template")).getValues().get("background");
					convertView.setBackgroundColor(Color
							.parseColor((String) mapTemp.get("color")));
				}
			}

			public void onFail(Integer s) {
				mActivity.alert("onFail", s.toString());
			}
		});
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
