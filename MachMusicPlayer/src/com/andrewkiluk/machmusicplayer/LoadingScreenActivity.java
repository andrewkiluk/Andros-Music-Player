package com.andrewkiluk.machmusicplayer;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class LoadingScreenActivity extends Activity {

	private SharedPreferences sharedPrefs;
	private String oldsongsListJson;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);


		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		CurrentData init = new CurrentData(); // Used to initialize static fields in CurrentData
		
		// Load old shuffle status
		String shuffleHistory = sharedPrefs.getString("shuffleHistory", "NULL");
		Type dequeType = new TypeToken<ArrayDeque<Song>>() {}.getType();
		Gson gson = new Gson();
		if(!shuffleHistory.equals("NULL") ){
			CurrentData.shuffleHistory = gson.fromJson(shuffleHistory, dequeType);
		}
		String shuffleQueue = sharedPrefs.getString("shuffleQueue", "NULL");
		if(!shuffleQueue.equals("NULL") ){
			CurrentData.shuffleQueue = gson.fromJson(shuffleQueue, Playlist.class);
		}
		CurrentData.shuffleHistoryPosition = sharedPrefs.getInt("shuffleHistoryPosition", 0);

		// Load old songsList
		oldsongsListJson = sharedPrefs.getString("songsList", "NULL");
		if (oldsongsListJson != "NULL"){

			gson = new Gson();
			Type listType = new TypeToken<ArrayList<Song>>() {}.getType();
			LibraryFiller libFill = new LibraryFiller(getApplicationContext());
			LibraryInfo initializer = new LibraryInfo();
			LibraryInfo.songsList = gson.fromJson(oldsongsListJson, listType);
			libFill.buildLibraryFromSongsList();
			Log.d("Library", "Old library Loaded");

			// Check if there is a stored playlist; if so, load it, else initialize a new one.
			String oldPlaylistJson = sharedPrefs.getString("currentPlaylist", "NULL");
			String oldSongJson = sharedPrefs.getString("currentSong", "NULL");
			Log.d("test", oldSongJson);

			CurrentData.currentSong = null;

			if (oldPlaylistJson != "NULL"){
				CurrentData.currentPlaylist = gson.fromJson(oldPlaylistJson, Playlist.class);
				if(oldSongJson != "NULL"){
					CurrentData.currentSong = gson.fromJson(oldSongJson, Song.class);
				}
				try{
					if(CurrentData.currentPlaylist.songs.size() == 0){
						PlayerStatus.playlistReset = true;
						CurrentData.currentSong = null;
					}
				}catch(NullPointerException e){
					CurrentData.currentPlaylist = new Playlist();
					PlayerStatus.playlistReset = true;
					CurrentData.currentSong = null;
				}
			}
			else{
				CurrentData.currentPlaylist = new Playlist();
				PlayerStatus.playlistReset = true;
				CurrentData.currentSong = null;
			}

			// test to make sure it's a valid playlist, else initialize to an empty one
			try{
				CurrentData.currentPlaylist.songs.get(0);
			}
			catch(Exception e){
				CurrentData.currentPlaylist = new Playlist();
				PlayerStatus.playlistReset = true;
				CurrentData.currentSong = null;
			}

			// Load the list of stored playlists

			String oldPlaylistsJson = sharedPrefs.getString("playlists", "NULL");

			if (oldPlaylistsJson != "NULL"){
				Type playlistType = new TypeToken<ArrayList<Playlist>>() {}.getType();
				LibraryInfo.playlists = gson.fromJson(oldPlaylistsJson, playlistType);
			}
			else{
				LibraryInfo.playlists = new ArrayList<Playlist>();
			}
			try{
				LibraryInfo.playlists.isEmpty(); // This is to test whether we have a valid list of playlists
			}catch(Exception e){
				LibraryInfo.playlists = new ArrayList<Playlist>();
			}

			Intent i = new Intent(LoadingScreenActivity.this, MusicPlayerActivity.class);
			startActivity(i);
			finish();


		}
		else{

			setContentView(R.layout.loading_screen);
			new PrefetchData().execute();

		}






	}

	/**
	 * Async Task to make http call
	 */
	private class PrefetchData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			// Load the last saved state.


			//		 Check for a previous songsList, else create one

			Log.d("Library", "No Library Loaded");
			LibraryFiller libFillAll = new LibraryFiller(getApplicationContext());
			libFillAll.loadLibrary();
			Gson gson = new Gson();
			String songsListJson = gson.toJson(LibraryInfo.songsList);
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putString("songsList", songsListJson);
			editor.commit();

			// Look up the directory to search for music from the preferences activity
			//		Log.d("Library", "No Library Loaded");
			//		String library_location = sharedPrefs.getString("library_location", "NULL");
			//		LibraryFiller libFill = new LibraryFiller(library_location);
			//		if(libFill.loadLibrary() == -1){
			//			Toast.makeText(getApplicationContext(), "Invalid Library folder, could not load music.",
			//					Toast.LENGTH_LONG).show();
			//		}

			// Check if there is a stored playlist; if so, load it, else initialize a new one.
			String oldPlaylistJson = sharedPrefs.getString("currentPlaylist", "NULL");

			if (oldPlaylistJson != "NULL"){
				CurrentData.currentPlaylist = gson.fromJson(oldPlaylistJson, Playlist.class);
				Log.d("Library", "Playlist Loaded");
			}
			else{
				CurrentData.currentPlaylist = new Playlist();
			}





			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Close this activity and launch MusicPlayerActivity
			Intent i = new Intent(LoadingScreenActivity.this, MusicPlayerActivity.class);
			startActivity(i);
			finish();
		}



		//		@Override
		//		protected void onCreate(Bundle savedInstanceState) {
		//			super.onCreate(savedInstanceState);
		//			setContentView(R.layout.loading_screen);
		//
		//			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		//
		//			new Handler().postDelayed(new Runnable() {
		//
		//				/*
		//				 * Showing splash screen with a timer. This will be useful when you
		//				 * want to show case your app logo / company
		//				 */
		//
		//				@Override
		//				public void run() {
		//					// This method will be executed once the timer is over
		//					// Start your app main activity
		//					Intent i = new Intent(LoadingScreenActivity.this, MusicPlayerActivity.class);
		//					startActivity(i);
		//
		//					// close this activity
		//					finish();
		//				}
		//			}, 3000);
		//		}
	}
}