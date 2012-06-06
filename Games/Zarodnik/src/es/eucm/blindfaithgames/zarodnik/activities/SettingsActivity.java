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


import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input;
import es.eucm.blindfaithgames.bfgtoolkit.sound.Music;
import es.eucm.blindfaithgames.bfgtoolkit.sound.SubtitleInfo;
import es.eucm.blindfaithgames.bfgtoolkit.sound.TTS;
import es.eucm.blindfaithgames.zarodnik.R;
import es.eucm.blindfaithgames.zarodnik.game.ZarodnikMusicSources;

public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceClickListener {

	private CheckBoxPreference music, tts, transcription, blindInteraction, blindMode;

	// Option names and default values
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = true;
	private static final String OPT_TTS = "tts";
	private static final boolean OPT_TTS_DEF = true;
	private static final String OPT_TRANSCRIPTION = "transcription";
	private static final boolean OPT_TRANSCRIPTION_DEF = true;
	public static final String OPT_BLIND_INTERACTION = "interaction";
	private static final boolean OPT_BLIND_INTERACTION_DEF = true;
	public static final String OPT_BLIND_MODE = "blind_mode";
	private static final boolean OPT_BLIND_MODE_DEF = false;
	public static final String FIRSTRUN = "first";
	public static final boolean FIRSTRUN_DEF = true;
	public static final String FIRSTGAME = "firstg";
	public static final boolean FIRSTGAME_DEF = true;
	
	private TTS textToSpeech;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		// Get the checkbox preference
		music = (CheckBoxPreference) findPreference(OPT_MUSIC);
		music.setOnPreferenceClickListener(this);

		tts = (CheckBoxPreference) findPreference(OPT_TTS);
		tts.setOnPreferenceClickListener(this);

		transcription = (CheckBoxPreference) findPreference(OPT_TRANSCRIPTION);
		transcription.setOnPreferenceClickListener(this);
		
		blindInteraction = (CheckBoxPreference) findPreference(OPT_BLIND_INTERACTION);
		blindInteraction.setOnPreferenceClickListener(this);
		
		blindMode = (CheckBoxPreference) findPreference(OPT_BLIND_MODE);
		blindMode.setOnPreferenceClickListener(this);
		
		// Initialize TTS engine
		textToSpeech = (TTS) getIntent().getParcelableExtra(MainActivity.KEY_TTS);
		textToSpeech.setContext(this);
		textToSpeech.setInitialSpeech(getString(R.string.settings_menu_initial_TTStext) + ", "
				+ findPreference(OPT_MUSIC).toString() + ", "
				+ findPreference(OPT_TTS).toString() + ", "
				+ findPreference(OPT_TRANSCRIPTION).toString() + ", "
				+ findPreference(OPT_BLIND_INTERACTION).toString());
	}

	/**
	 * Turns off TTS engine
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		textToSpeech.stop();
	}

	/** Get the current value of the music option */
	public static boolean getMusic(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
	}

	/** Get the current value of the tts option */
	public static boolean getTTS(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_TTS, OPT_TTS_DEF);
	}

	/** Get the current value of the tts option */
	public static boolean getTranscription(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_TRANSCRIPTION, OPT_TRANSCRIPTION_DEF);
	}
	
	/** Get the current value of the blind interaction option */
	public static boolean getBlindMode(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_BLIND_MODE, OPT_BLIND_MODE_DEF);
	}
	
	/** Get the current value of the blind interaction option */
	public static boolean getBlindInteraction(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_BLIND_INTERACTION, OPT_BLIND_INTERACTION_DEF);
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (OPT_MUSIC.equals(preference.getKey())) {
			if(music.isChecked())
				textToSpeech.speak(findPreference(OPT_MUSIC).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_MUSIC).toString() + " " + getString(R.string.disabled));
		} else if (OPT_TTS.equals(preference.getKey())) {
			if(tts.isChecked())
				textToSpeech.speak(findPreference(OPT_TTS).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_TTS).toString() + " " + getString(R.string.disabled));
			
		}else if (OPT_BLIND_INTERACTION.equals(preference.getKey())){
			if(blindInteraction.isChecked())
				textToSpeech.speak(findPreference(OPT_BLIND_INTERACTION).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_BLIND_INTERACTION).toString() + " " + getString(R.string.disabled));
		}else if (OPT_BLIND_MODE.equals(preference.getKey())){
			if(blindMode.isChecked())
				textToSpeech.speak(findPreference(OPT_BLIND_MODE).toString() + " " + getString(R.string.enabled));
			else
				textToSpeech.speak(findPreference(OPT_BLIND_MODE).toString() + " " + getString(R.string.disabled));
		} else if (OPT_TRANSCRIPTION.equals(preference.getKey())) {
			if(transcription.isChecked()){
				Map<Integer, String> onomatopeias = ZarodnikMusicSources.getMap(this);
				
				SubtitleInfo s = new SubtitleInfo(R.layout.toast_custom, R.id.toast_layout_root,
						R.id.toast_text, 0, 0, Toast.LENGTH_SHORT, Gravity.BOTTOM, onomatopeias);
				
				Music.getInstanceMusic().enableTranscription(this, s);
				textToSpeech.enableTranscription(s);
				
				textToSpeech.speak(findPreference(OPT_TRANSCRIPTION).toString() + " " +  getString(R.string.enabled));
			}else{
				Music.getInstanceMusic().disableTranscription();
				textToSpeech.disableTranscription();
				
				textToSpeech.speak(findPreference(OPT_TRANSCRIPTION).toString() + " " + getString(R.string.disabled));
			}
		}
		return true;
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
