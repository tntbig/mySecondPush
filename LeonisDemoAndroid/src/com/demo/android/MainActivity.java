package com.demo.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import co.leonisand.offers.OffersKit;
import co.leonisand.platform.Leonis;

import com.tutecentral.navigationdrawer.R;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	CustomDrawerAdapter adapter;

	List<DrawerItem> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		// SDKの初期化
		// Leonisの初期化
		// UID生成をSDKにまかせるとき
		Leonis.getInstance().init(getApplicationContext(), true);
		/*
		 * // UIDをアプリから指定するとき Leonis.getInstance().init(getApplicationContext(),
		 * "testuser", true);
		 */
		Leonis.getInstance().setRequestTimeoutInterval(10);
		System.out.println(Leonis.getInstance().uid());

		Leonis.getInstance().setEndUserExtension("target_list",
				"pointcard_number", "1234567890", new Leonis.LeonisListener() {
					public void onDone(Map<String, Object> result) {
						System.out.println("extension ok");
					}

					public void onFail(Integer result) {
						System.out.println("extension ng");
					}
				});

		JSONObject jsonobject = new JSONObject();
		try {
			JSONArray jsonarray = new JSONArray();
			jsonarray.put("key1");
			jsonarray.put("key2");
			jsonarray.put("key3");
			jsonobject.put("resouce1", jsonarray);
			jsonobject.put("resouce2", "key1");
		} catch (JSONException jsonexception) {
			jsonexception.printStackTrace();
		}
		Leonis.getInstance().addEndUserExtensions(jsonobject,
				new Leonis.LeonisListener() {
					public void onDone(Map<String, Object> map) {
					}

					public void onFail(Integer s) {
					}
				});

		// OffersKitの初期化
		OffersKit.getInstance().init(getApplicationContext());

		OffersKit.getInstance().setRequestTimeoutInterval(10);

		Bundle bundle1 = new Bundle();
		bundle1.putString("test", "TESTID");
		bundle1.putString("info", "INFOUSERID");
		OffersKit.getInstance().externalServiceRegistrations(bundle1, null);

		// Initializing
		dataList = new ArrayList<DrawerItem>();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Add Drawer Item to dataList
		dataList.add(new DrawerItem(true)); // adding a spinner to the list

		dataList.add(new DrawerItem("機能")); // adding a header to the list
		dataList.add(new DrawerItem("クーポン一覧", R.drawable.ic_action_email));
		dataList.add(new DrawerItem("レコメンド一覧", R.drawable.ic_action_good));
		dataList.add(new DrawerItem("スタンプラリー", R.drawable.ic_action_good));

		dataList.add(new DrawerItem("キャンペーンコード入力", R.drawable.ic_action_gamepad));
		dataList.add(new DrawerItem("プッシュ通知履歴", R.drawable.ic_action_labels));

		dataList.add(new DrawerItem("アカウント"));// adding a header to the list
		dataList.add(new DrawerItem("社員番号変更", R.drawable.ic_action_search));
		dataList.add(new DrawerItem("グループ変更", R.drawable.ic_action_cloud));
		dataList.add(new DrawerItem("端末情報", R.drawable.ic_action_camera));

		dataList.add(new DrawerItem("その他"));// adding a header to the list
		dataList.add(new DrawerItem("OFFERs特設サイト", R.drawable.ic_action_search));

		adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,
				dataList);

		mDrawerList.setAdapter(adapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {

			if (dataList.get(0).isSpinner()
					& dataList.get(1).getTitle() != null) {
				SelectItem(2);
			} else if (dataList.get(0).getTitle() != null) {
				SelectItem(1);
			} else {
				SelectItem(0);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void SelectItem(int possition) {

		Fragment fragment = null;
		Bundle args = new Bundle();
		switch (possition) {
		case 2:
			fragment = new CouponsFragment();
			break;
		
		default:
			fragment = new CouponsFragment();
			break;
		}

		fragment.setArguments(args);
		FragmentManager frgManager = getSupportFragmentManager();
		frgManager.beginTransaction().replace(R.id.content_frame, fragment)
				.commit();

		mDrawerList.setItemChecked(possition, true);
		setTitle(dataList.get(possition).getItemName());
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return false;
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (dataList.get(position).getTitle() == null) {
				SelectItem(position);
			}

		}
	}

	public DialogFragment alert(String title, String message) {

		Bundle args = new Bundle();
		args.putString(CommonDialogFragment.FIELD_TITLE, title);
		args.putString(CommonDialogFragment.FIELD_MESSAGE, message);
		args.putString(CommonDialogFragment.FIELD_LABEL_POSITIVE,
				getString(android.R.string.ok));
		CommonDialogFragment dialogFragment = new CommonDialogFragment() {

		};
		dialogFragment.setArguments(args);
		dialogFragment.show(getSupportFragmentManager(), "alert");

		return dialogFragment;
	}
	public class CommonDialogFragment extends DialogFragment {

		public static final String FIELD_LAYOUT = "layout";
		public static final String FIELD_TITLE = "title";
		public static final String FIELD_MESSAGE = "message";
		public static final String FIELD_LIST_ITEMS = "list_items";
		public static final String FIELD_LIST_ITEMS_STRING = "list_items_string";
		public static final String FIELD_LABEL_POSITIVE = "label_positive";
		public static final String FIELD_LABEL_NEGATIVE = "label_negative";
		public static final String FIELD_LABEL_NEUTRAL = "label_neutral";

		private DialogInterface.OnShowListener mListenerShow;
		private DialogInterface.OnClickListener mListenerNegativeClick;
		private DialogInterface.OnClickListener mListenerPositiveClick;
		private DialogInterface.OnClickListener mListenerNeutralClick;

		private View mView;

		private AlertDialog mAlertDialog;

		/** ダイアログの共通タグ */
		private static final String TAG = "exclusive_dialog";

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			Bundle args = getArguments();

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			// dialog title
			if (args.containsKey(FIELD_TITLE)) {
				builder.setTitle(args.getString(FIELD_TITLE));
			}

			// dialog message
			if (args.containsKey(FIELD_MESSAGE)) {
				builder.setMessage(args.getString(FIELD_MESSAGE));
			}

			// dialog customize content view
			if (args.containsKey(FIELD_LAYOUT)) {
				builder.setView(mView);
			}

			// positive button title and click listener
			if (args.containsKey(FIELD_LABEL_POSITIVE)) {
				builder.setPositiveButton(args.getString(FIELD_LABEL_POSITIVE), mListenerPositiveClick);
			}

			// negative button title and click listener
			if (args.containsKey(FIELD_LABEL_NEGATIVE)) {
				builder.setNegativeButton(args.getString(FIELD_LABEL_NEGATIVE), mListenerNegativeClick);
			}

			// neutral button title and click listener
			if (args.containsKey(FIELD_LABEL_NEUTRAL)) {
				builder.setNeutralButton(args.getString(FIELD_LABEL_NEUTRAL), mListenerNeutralClick);
			}

			// make dialog
			mAlertDialog = builder.create();

			// show listener
			if (mListenerShow != null) {
				mAlertDialog.setOnShowListener(mListenerShow);
			}

			return mAlertDialog;
		}

		public void setView(View view){
			mView = view;
		}
		public void setShowListener(DialogInterface.OnShowListener listener){
			mListenerShow = listener;
		}
		public void setPotitiveClickListener(DialogInterface.OnClickListener listener){
			mListenerPositiveClick = listener;
		}
		public void setNegativeClickListener(DialogInterface.OnClickListener listener){
			mListenerNegativeClick = listener;
		}
		public void setNeutralClickListener(DialogInterface.OnClickListener listener){
			mListenerNeutralClick = listener;
		}

	}
	public DialogFragment viewConfirm(String title, View view, String positive_title, DialogInterface.OnClickListener positive_listener, String negative_title, DialogInterface.OnClickListener negative_listener, OnShowListener show_listener) {

		Bundle args = new Bundle();
		args.putString(CommonDialogFragment.FIELD_TITLE, title);
		args.putInt(CommonDialogFragment.FIELD_LAYOUT, view.getId());
		args.putString(CommonDialogFragment.FIELD_LABEL_POSITIVE, positive_title);
		args.putString(CommonDialogFragment.FIELD_LABEL_NEGATIVE, negative_title);


		CommonDialogFragment dialogFragment = new CommonDialogFragment();
		dialogFragment.setView(view);

		dialogFragment.setPotitiveClickListener(positive_listener);
		dialogFragment.setNegativeClickListener(negative_listener);

		dialogFragment.setShowListener(show_listener);

		dialogFragment.setArguments(args);
		dialogFragment.show(getSupportFragmentManager(), "viewConfirm");

		return dialogFragment;
	}
}
