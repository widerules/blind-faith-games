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
 * 	  This file is part of EYES-FREE GOLF, developed in the Blind Faith Games project.
 *  
 *        EYES-FREE GOLF, is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *   
 *       EYES-FREE GOLF is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *   
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with Adventure.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.blindfaithgames.golfgame.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import es.eucm.blindfaithgames.bfgtoolkit.feedback.AnalyticsManager;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input;
import es.eucm.blindfaithgames.bfgtoolkit.sound.TTS;
import es.eucm.blindfaithgames.golfgame.R;
import es.eucm.blindfaithgames.golfgame.game.GolfGameAnalytics;

public class InstructionsActivity extends Activity implements OnTouchListener{

	private TTS textToSpeech;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		if(!SettingsActivity.getBlindMode(this)){
			setTheme(android.R.style.Theme_Dialog);
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentScreen();
		}else{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.empty);
		}
		
		Intent i = getIntent();
		
		int type = i.getIntExtra(MainActivity.KEY_TYPE_INSTRUCTIONS, 0);
		
		String speech;
		
		if(type == 0)
			speech = getString(R.string.instructions_controls_label) + " " + getString(R.string.instructions_controls_text);
		else
			speech = getString(R.string.instructions_general_label) + " " + getString(R.string.instructions_general_text);

		// This initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(speech);
	}
	
	private void setContentScreen() {
		View v;
		
		Intent i = getIntent();
		
		int type = i.getIntExtra(MainActivity.KEY_TYPE_INSTRUCTIONS, 0);

		//scale = this.getResources().getDisplayMetrics().density;
		//fontSize =  (this.getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		//font = Typeface.createFromAsset(getAssets(), RuntimeConfig.FONT_PATH);
		
		if(type == 0){
			setContentView(R.layout.instructions_controls);
			AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.INSTRUCTIONS_CONTROLS_ACTIVITY);
			//t = (TextView) findViewById(R.id.instructions_controls_content);
			v = findViewById(R.id.control_root);
		}else{
			setContentView(R.layout.instructions_general);
			AnalyticsManager.getAnalyticsManager(this).registerPage(GolfGameAnalytics.INSTRUCTIONS_GENERAL_ACTIVITY);
			//t = (TextView) findViewById(R.id.instructions_general_content);
			v = findViewById(R.id.general_root);
		}
		
		v.setOnTouchListener(this);
		
		//t.setTextSize(fontSize);
		//t.setTypeface(font);
	}

	/**
	 *  Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		 super.onDestroy();
		 textToSpeech.stop();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		this.finish();
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Integer key = Input.getKeyboard().getKeyByAction(KeyConfActivity.ACTION_REPEAT);
		if(key != null){
			if (keyCode == key) {
				textToSpeech.repeatSpeak();
				return true;
			} 
		}
		return super.onKeyDown(keyCode, event);
	}
}
