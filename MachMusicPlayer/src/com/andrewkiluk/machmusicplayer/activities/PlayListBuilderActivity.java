package com.andrewkiluk.machmusicplayer.activities;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.andrewkiluk.machmusicplayer.PlayListBuilderAdapter;
import com.andrewkiluk.machmusicplayer.R;
import com.andrewkiluk.machmusicplayer.models.CurrentData;
import com.andrewkiluk.machmusicplayer.models.LibraryInfo;
import com.andrewkiluk.machmusicplayer.models.PlayerStatus;
import com.andrewkiluk.machmusicplayer.models.SelectionStatus;
import com.andrewkiluk.machmusicplayer.models.Song;

public class PlayListBuilderActivity extends FragmentActivity implements
ActionBar.TabListener{

	private ViewPager viewPager;
	private PlayListBuilderAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Songs", "Artists", "Albums" };
	private int currentTab;

	private Button button_add_selected;
	private Button button_clear_selection;

	public ArrayList<Song> newSongs;
	
	public PlayListBuilderActivity that = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist_builder);

		PlayerStatus.isVisible = true;

		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.tabcolor)));
		bar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.tabcolor)));

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new PlayListBuilderAdapter(getSupportFragmentManager());

		button_add_selected = (Button) findViewById(R.id.button_add_selected);
		button_clear_selection = (Button) findViewById(R.id.button_clear_selection);

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);  
		
		// We use this class to keep track of which songs are selected in which fragments
		SelectionStatus selectionStatusObject = new SelectionStatus();



		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
				currentTab = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		button_add_selected.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(), PlayListActivity.class);

				if (LibraryInfo.newSongs != null){

					for (Song song : LibraryInfo.newSongs){
						CurrentData.currentPlaylist.songs.add(song);
					}
					// Playlist has been modified, reset the shuffle queue
					CurrentData.shuffleReset();
				}
					LibraryInfo.newSongs = new ArrayList<Song>();

				setResult(300, intent);
				// Closing PlayListBuilder
				LibraryInfo.newSongs = new ArrayList<Song>();
				finish();
			}
		});

		button_clear_selection.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SelectionStatus selectionStatusObject = new SelectionStatus(); // This resets the selection arrays
				
				Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":"+viewPager.getCurrentItem());
				if (fragment != null){
					if (fragment.getView() != null){
						fragment.getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					}
				}
				
				LibraryInfo.newSongs = new ArrayList<Song>();
				mAdapter = new PlayListBuilderAdapter(getSupportFragmentManager());
				viewPager.setAdapter(mAdapter);
				actionBar.setSelectedNavigationItem(currentTab);
				
			}
		});
	}
	
	/**
	 * The following two methods are to set up the Settings menu.
	 * */

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.menu_settings:
			Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivityForResult(i, 100);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStop() {
		PlayerStatus.isVisible = false;
		super.onStop();
	}


	@Override
	public void onBackPressed() {
		Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":"+viewPager.getCurrentItem());
		if (fragment != null){
			if (fragment.getView() != null) {
				// Pop the backstack on the ChildManager if there is any. If not, close this activity as normal.
				if (!fragment.getChildFragmentManager().popBackStackImmediate()) {
					finish();
				}
			}
		}
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction arg1) {
		// show respective fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabSelected(Tab arg0, android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		actionBar.setSelectedNavigationItem(arg0.getPosition());
		currentTab = arg0.getPosition();

	}

	@Override
	public void onTabUnselected(Tab arg0, android.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
}