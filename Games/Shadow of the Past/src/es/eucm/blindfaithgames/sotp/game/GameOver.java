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
 * 	  This file is part of SHADOW OF THE PAST, developed in the Blind Faith Games project.
 *  
 *       SHADOW OF THE PAST is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *   
 *       SHADOW OF THE PAST is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *   
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with Adventure.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.blindfaithgames.sotp.game;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.view.View;
import es.eucm.blindfaithgames.bfgtoolkit.general.Game;
import es.eucm.blindfaithgames.bfgtoolkit.general.GameState;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input.EventType;
import es.eucm.blindfaithgames.bfgtoolkit.others.RuntimeConfig;
import es.eucm.blindfaithgames.bfgtoolkit.sound.TTS;
import es.eucm.blindfaithgames.bfgtoolkit.sound.VolumeManager;
import es.eucm.blindfaithgames.bfgtoolkit.story.Text;
import es.eucm.blindfaithgames.sotp.R;

public class GameOver extends GameState {
	
	private Text text;
	
	private int stepsPerWord = RuntimeConfig.TEXT_SPEED;
	
	public GameOver(View v, TTS textToSpeech, Context c, Game game) {
		super(v,c,textToSpeech, game);
		
		int textoffSetX;
		int textoffSetY;
		float fontSize;
		Typeface font;
		Paint brush;
		
		fontSize =  (this.getContext().getResources().getDimension(R.dimen.font_size_game_over)/GameState.scale);
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.GAME_FONT_PATH);
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setColor(this.getContext().getResources().getColor(R.color.green0));
		if(font != null)
			brush.setTypeface(font);
	
		float whiteSpaceSize = (this.getContext().getResources().getDimension(R.dimen.white_space_size)/GameState.scale);
		textoffSetX = SCREEN_WIDTH  / 4;
		textoffSetY = SCREEN_HEIGHT / 2;
		text = new Text(textoffSetX, textoffSetY, null, this, null, null, 
				null, null, false, brush, stepsPerWord, this.getContext().getString(R.string.game_over_text), fontSize, whiteSpaceSize);
		this.addEntity(text);
		
	}
	
	@Override
	public void onInit() {
		super.onInit();
		getTTS().speak(this.context.getString(R.string.game_over_tts));
	}
	
	public void onUpdate() {
		super.onUpdate();
		EventType e = Input.getInput().removeEvent("onVolUp");
		if (e != null){
			VolumeManager.adjustStreamVolume(this.context, AudioManager.ADJUST_RAISE);
		}else{
			e = Input.getInput().removeEvent("onVolDown");
			if (e != null)
				VolumeManager.adjustStreamVolume(this.context, AudioManager.ADJUST_LOWER);
		}
		
		e = Input.getInput().removeEvent("onDoubleTap");
		if(e != null && text.isFinished()){
			this.stop();
		}
		else{
			if(e != null){
				text.setStepsPerWord(1);
			}
		}
	}
}
