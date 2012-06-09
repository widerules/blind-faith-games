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

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
import es.eucm.blindfaithgames.bfgtoolkit.story.SceneManager;
import es.eucm.blindfaithgames.bfgtoolkit.story.ScenesReader;
import es.eucm.blindfaithgames.bfgtoolkit.story.Text;
import es.eucm.blindfaithgames.sotp.R;

public class Gameplay extends GameState {
	
	private Text text;
	private Paint brush;

	private SceneManager scm;
	
	private static final int textoffSetX = 0;
	private static final int textoffSetY = 40;
	
	private int stepsPerWord = 1;

	public Gameplay(View v, TTS textToSpeech, Context c, Game game) {
		super(v,c,textToSpeech, game);

		float fontSize;
		Typeface font;
		fontSize =  (this.getContext().getResources().getDimension(R.dimen.font_size_intro)/GameState.scale);
		font = Typeface.createFromAsset(this.getContext().getAssets(),RuntimeConfig.GAME_FONT_PATH);
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setColor(this.getContext().getResources().getColor(R.color.green0));
		if(font != null)
			brush.setTypeface(font);
		
		float whiteSpaceSize = (this.getContext().getResources().getDimension(R.dimen.white_space_size)/GameState.scale);
		text = new Text(textoffSetX, textoffSetY, null, this, null, null, 
				null, null, false, brush, stepsPerWord, "", fontSize, whiteSpaceSize);
		this.addEntity(text);
		
		InputStream is = this.context.getResources().openRawResource(R.raw.game_script);
		ScenesReader scenesReader = new ScenesReader();
		scm = scenesReader.loadAdventure(is);
		
		this.getTTS().setQueueMode(TTS.QUEUE_FLUSH);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		super.onDraw(canvas);
		
		Paint localBrush = new Paint(brush);
		localBrush.setColor(this.v.getResources().getColor(R.color.red2));
		localBrush.setStrokeWidth(1);
		scm.drawButtons(canvas,this.getView().getResources().getString(R.string.optionButton),localBrush);
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

		scm.manageTTS(this.getTTS());
		
		boolean finished = scm.manageSceneManager(text,this.getTTS());
		if(finished){
			this.stop();
		}
	}
}
