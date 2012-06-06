/*******************************************************************************
 * Blind Faith Games is a research project of the e-UCM
 *           research group, developed by Gloria Pozuelo and Javier Álvarez, 
 *           under supervision by Baltasar Fernández-Manjón and Javier Torrente.
 *    
 *     Copyright 2011-2012 e-UCM research group.
 *   
 *      e-UCM is a research group of the Department of Software Engineering
 *           and Artificial Intelligence at the Complutense University of Madrid
 *           (School of Computer Science).
 *   
 *           C Profesor Jose Garcia Santesmases sn,
 *           28040 Madrid (Madrid), Spain.
 *   
 *           For more info please visit:  <http://blind-faith-games.e-ucm.es> or
 *           <http://www.e-ucm.es>
 *   
 *   ****************************************************************************
 * 	  This file is part of ZARODNIK GAME, developed in the Blind Faith Games project.
 *  
 *       ZARODNIK GAME, is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *   
 *       ZARODNIK GAME is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *   
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with Adventure.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.blindfaithgames.zarodnik.activities;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;
import es.eucm.blindfaithgames.bfgtoolkit.general.DrawablePanel;
import es.eucm.blindfaithgames.bfgtoolkit.general.Game;
import es.eucm.blindfaithgames.bfgtoolkit.general.GameState;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input;
import es.eucm.blindfaithgames.bfgtoolkit.input.XMLKeyboard;
import es.eucm.blindfaithgames.bfgtoolkit.sound.Music;
import es.eucm.blindfaithgames.bfgtoolkit.sound.Sound3DManager;
import es.eucm.blindfaithgames.bfgtoolkit.sound.SubtitleInfo;
import es.eucm.blindfaithgames.bfgtoolkit.sound.TTS;
import es.eucm.blindfaithgames.zarodnik.R;
import es.eucm.blindfaithgames.zarodnik.game.ZarodnikGameOver;
import es.eucm.blindfaithgames.zarodnik.game.ZarodnikGameplay;
import es.eucm.blindfaithgames.zarodnik.game.ZarodnikIntro;
import es.eucm.blindfaithgames.zarodnik.game.ZarodnikMusicSources;
import es.eucm.blindfaithgames.zarodnik.game.ZarodnikTutorial;
import es.eucm.blindfaithgames.zarodnik.game.ZarodnikTutorial.TutorialID;

public class ZarodnikGameActivity extends Activity {
	private TTS textToSpeech;
	private Game game;

	// Keyboard configuration
	private XMLKeyboard keyboard;
	
	private DrawablePanel zarodnikView;

	/*---------------------------------------------------------------------
	 *  LIFECYCLE METHODS
	 ----------------------------------------------------------------------*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);

		if (SettingsActivity.getTranscription(this)) {

			Map<Integer, String> onomatopeias = ZarodnikMusicSources.getMap(this);

			SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom,
					R.id.toast_layout_root, R.id.toast_text, 0, 0,
					Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);

			textToSpeech.enableTranscription(s);
			Music.getInstanceMusic().enableTranscription(this, s);
		} else {
			textToSpeech.disableTranscription();
			Music.getInstanceMusic().disableTranscription();
		}

		keyboard = Input.getKeyboard();

		zarodnikView = new ZarodnikGamePanel(this);
		setContentView(zarodnikView);
		
		if(game != null)
			game.delete();
		try{
			if(savedInstanceState != null){
				loadGame(zarodnikView, savedInstanceState);
			}else{
				createGame(zarodnikView, checkFirstGame());
			}			

		}
		catch (Exception e){
			this.finish();
		}
		if (SettingsActivity.getBlindMode(this))
			game.setDisabled(true);
	}

	private void loadGame(DrawablePanel zarodnikView, Bundle savedInstanceState) {
		ArrayList<Integer> order = savedInstanceState.getIntegerArrayList("Game.order");
		createGame(zarodnikView,order.size() == 8);
		game.onRestoreInstance(savedInstanceState);
	}

	private void createGame(DrawablePanel zarodnikView, boolean isfirstGame) {
		ArrayList<Integer> order;
		ArrayList<GameState> gameStates;
		if(isfirstGame){
			order = new ArrayList<Integer>();
			order.add(0);
			order.add(1);
			order.add(2);
			order.add(3);
			order.add(4);
			order.add(5);
			order.add(6);
			order.add(7);
			
			gameStates = new ArrayList<GameState>();
			gameStates.add(new ZarodnikIntro(zarodnikView, textToSpeech,this, game));	
			gameStates.add(new ZarodnikTutorial(zarodnikView,textToSpeech, this, TutorialID.TUTORIAL5, game));		
			gameStates.add(new ZarodnikTutorial(zarodnikView,textToSpeech, this, TutorialID.TUTORIAL4, game));		
			gameStates.add(new ZarodnikTutorial(zarodnikView,textToSpeech, this, TutorialID.TUTORIAL7, game));
			gameStates.add(new ZarodnikTutorial(zarodnikView,textToSpeech, this, TutorialID.TUTORIAL8, game));		
			gameStates.add(new ZarodnikTutorial(zarodnikView,textToSpeech, this, TutorialID.TUTORIAL0, game));	
			gameStates.add(new ZarodnikGameplay(zarodnikView,textToSpeech, this, game));
			gameStates.add(new ZarodnikGameOver(zarodnikView,textToSpeech, this, game));
			
		}else{
			order = new ArrayList<Integer>();
			order.add(0);
			order.add(1);
			order.add(2);
			
			gameStates = new ArrayList<GameState>();
			gameStates.add(new ZarodnikIntro(zarodnikView, textToSpeech,this, game));
			gameStates.add(new ZarodnikGameplay(zarodnikView,textToSpeech, this, game));
			gameStates.add(new ZarodnikGameOver(zarodnikView,textToSpeech, this, game));
		}
		
		game = new Game();
		game.initialize(gameStates, order);
	}

	private boolean checkFirstGame() {
		boolean isFirstGame = PreferenceManager.getDefaultSharedPreferences(this)
				.getBoolean(SettingsActivity.FIRSTGAME, SettingsActivity.FIRSTGAME_DEF);
		
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putBoolean(SettingsActivity.FIRSTGAME, false);
		editor.commit();
		
		return isFirstGame;
	}

	/**
	 * Called after your activity has been stopped, prior to it being started
	 * again. Always followed by onStart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	/**
	 * Called when the activity is becoming visible to the user. Followed by
	 * onResume() if the activity comes to the foreground, or onStop() if it
	 * becomes hidden.
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * Called when the activity will start interacting with the user. At this
	 * point your activity is at the top of the activity stack, with user input
	 * going to it. Always followed by onPause().
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * Called when the system is about to start resuming a previous activity.
	 * This is typically used to commit unsaved changes to persistent data, stop
	 * animations and other things that may be consuming CPU, etc.
	 * Implementations of this method must be very quick because the next
	 * activity will not be resumed until this method returns. Followed by
	 * either onResume() if the activity returns back to the front, or onStop()
	 * if it becomes invisible to the user.
	 */
	@Override
	protected void onPause() {
		super.onPause();
    	textToSpeech.stop();
    	Sound3DManager.getSoundManager(this).stopAllSources();
	}
	
	/**
	 * Called when the activity is no longer visible to the user, because
	 * another activity has been resumed and is covering this one. This may
	 * happen either because a new activity is being started, an existing one is
	 * being brought in front of this one, or this one is being destroyed.
	 * Followed by either onRestart() if this activity is coming back to
	 * interact with the user, or onDestroy() if this activity is going away.
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * The final call you receive before your activity is destroyed. This can
	 * happen either because the activity is finishing (someone called finish()
	 * on it, or because the system is temporarily destroying this instance of
	 * the activity to save space. You can distinguish between these two
	 * scenarios with the isFinishing() method.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		textToSpeech.stop();
		Sound3DManager.getSoundManager(this).stopAllSources();
		Music.getInstanceMusic().stopAllResources();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		game.onSaveInstance(outState);
		super.onSaveInstanceState(outState);
	}

	/*---------------------------------------------------------------------
	 *  GESTURE DETECTOR
	 ----------------------------------------------------------------------*/
	class MyGestureDetector extends SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			Input.getInput().addEvent("onDoubleTap", MotionEvent.obtain(e),
					null, -1, -1);
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			Input.getInput().addEvent("onDown", MotionEvent.obtain(e), null,
					-1, -1);
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			Input.getInput().addEvent("onLongPress", e, null, -1, -1);
		}
	}

	/*---------------------------------------------------------------------
	 *  FIN GESTURE DETECTOR
	 ----------------------------------------------------------------------*/

	/*---------------------------------------------------------------------
	 *  DRAWABLE PANEL
	 ----------------------------------------------------------------------*/

	class ZarodnikGamePanel extends DrawablePanel {

		private GestureDetector mGestureDetector;

		private boolean dragging;

		public ZarodnikGamePanel(Context context) {
			super(context);
			mGestureDetector = new GestureDetector(new MyGestureDetector());
		}

		@Override
		public void onInitalize() {
			game.onInit();
		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			game.onDraw(canvas);
		}

		@Override
		public void onUpdate() {
			game.onUpdate();
			if (game.isEndGame())
				((Activity) this.getContext()).finish();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			onDrag(event);
			return mGestureDetector.onTouchEvent(event);
		}

		private void onDrag(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				dragging = true;
				Input.getInput().addEvent("onDrag", MotionEvent.obtain(event),
						null, -1, -1);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				dragging = false;
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (dragging) {
					Input.getInput().addEvent("onDrag",
							MotionEvent.obtain(event), null, -1, -1);
				}
			}
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			boolean found = manageCustomConfigurationKeys(keyCode, event);
			if (!found) {
				manageDefaultConfigurationKeys(keyCode, event);
			}
			return true;
		}

		private void manageDefaultConfigurationKeys(int keyCode, KeyEvent event) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				finish();
				break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				Input.getInput().addEvent("onVolDown", event, -1, -1);
				break;
			case KeyEvent.KEYCODE_VOLUME_UP:
				Input.getInput().addEvent("onVolUp", event, -1, -1);
				break;
			}
		}

		private boolean manageCustomConfigurationKeys(int keyCode, KeyEvent e) {
			int i = 0;
			boolean found = false;
			if (e.getAction() == KeyEvent.ACTION_DOWN) {
				while (!found && i < keyboard.getNum()) {
					found = keyboard.getAction(keyCode) != null;
					i++;
				}
				if (found) {
					if (keyboard.getAction(keyCode).equals(
							KeyConfActivity.ACTION_RECORD)) {
						Input.getInput().addEvent(
								KeyConfActivity.ACTION_RECORD, e, -1, -1);
					}
					if (keyboard.getAction(keyCode).equals(
							KeyConfActivity.ACTION_REPEAT)) {
						textToSpeech.repeatSpeak();
					}
				}
			}
			return found;
		}
	}
}
