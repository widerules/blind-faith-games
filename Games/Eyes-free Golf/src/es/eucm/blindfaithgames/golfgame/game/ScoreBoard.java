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
package es.eucm.blindfaithgames.golfgame.game;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import es.eucm.blindfaithgames.bfgtoolkit.feedback.AnalyticsManager;
import es.eucm.blindfaithgames.bfgtoolkit.general.Entity;
import es.eucm.blindfaithgames.bfgtoolkit.general.GameState;
import es.eucm.blindfaithgames.bfgtoolkit.general.Mask;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input.EventType;
import es.eucm.blindfaithgames.bfgtoolkit.others.RuntimeConfig;
import es.eucm.blindfaithgames.bfgtoolkit.sound.TTS;
import es.eucm.blindfaithgames.golfgame.R;
import es.eucm.blindfaithgames.golfgame.activities.MainActivity;

public class ScoreBoard extends Entity {

	private GolfGameplay game;
	
	private int counter;
	private int record;
	
	private static float fontSize;
	private static float scale;
	private static Typeface font;
	private Paint brush;
	
	public ScoreBoard(int x, int y, int record,Bitmap img, GameState state, List<Mask> mask,
			boolean animated, int frameCount) {
		super(x, y, img, state, mask, null, null, null, false);
		counter = 0;
		this.game = (GolfGameplay) state;
		this.record = record;
		
		font = Typeface.createFromAsset(this.game.getContext().getAssets(), RuntimeConfig.FONT_PATH);  
		scale = this.game.getContext().getResources().getDisplayMetrics().density;
		fontSize =  (this.game.getContext().getResources().getDimensionPixelSize(R.dimen.font_size_menu))/scale;
		
		brush = new Paint();
		brush.setTextSize(fontSize);
		brush.setTypeface(font);
	}

	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		brush.setARGB(255, 51, 51, 51);
		canvas.drawRect(new Rect(this.x ,(int) (this.y) , (int) (this.x + (int) 2 * fontSize), (int) (this.y + fontSize * 2.3)), brush);
		
		if(this.game.isStageMode()){
			brush.setColor(Color.BLUE);
			canvas.drawText(Integer.toString(counter), this.x, (float) (this.y + fontSize * 1.5), brush);
		}
		else{
			brush.setColor(Color.BLUE);
			canvas.drawText(Integer.toString(counter), this.x, this.y + fontSize * 2, brush);
			
			if(record >= 0){
				brush.setColor(Color.RED);
				canvas.drawText(Integer.toString(record), this.x, this.y + fontSize,brush);
			}
		}
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		EventType e = Input.getInput().removeEvent("speakRecord");
		if(e != null){
			speakRecord();
		}
		
		onTouch();
		
	}
	
	private void speakRecord() {
		this.game.getTTS().setQueueMode(TTS.QUEUE_FLUSH);
		if(this.game.isStageMode()){
			this.game.getTTS().speak(this.game.getContext().getResources().getString(R.string.counterSpeech) + " " + counter);
		}
		else{
			this.game.getTTS().speak(this.game.getContext().getResources().getString(R.string.recordSpeech) + " " + record
										+ ". " + this.game.getContext().getResources().getString(R.string.counterSpeech)  + " " + counter);
		}
	}

	private void onTouch() {
		EventType drag = Input.getInput().removeEvent("onDrag");
		if(drag != null){
			double ex = drag.getMotionEventE1().getX();
			double ey = drag.getMotionEventE1().getY();
			if(this.x < ex && this.y < ey && ex < (this.x + (int) 2 * fontSize) && ey < (this.y + fontSize * 2.3)){
				speakRecord();
			}
		}
	}

	public void incrementCounter(){
		counter++;
		if (counter > record) {
			record = counter;
			if(!game.isStageMode())
				save();
			game.getTTS().speak(this.game.getContext().getResources().getString(R.string.newRecordSpeech) + counter);
		} else
			game.getTTS().speak(this.game.getContext().getResources().getString(R.string.scoreboard_free_mode) + counter);
	}
	
	private void save() {
		FileOutputStream fos;
		try { 
			fos = this.game.getContext().openFileOutput(MainActivity.FILENAMEFREEMODE, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(record); 
			AnalyticsManager.getAnalyticsManager(this.game.getContext()).registerAction(GolfGameAnalytics.MISCELLANEOUS, 
					GolfGameAnalytics.GAME_RESULT_FREE, Integer.toString(record), 0);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void resetCounter(){
		game.getTTS().speak(game.getContext().getString(R.string.scoreboard_reset));
		counter = 0;
	}
	
	@Override
	public void onCollision(Entity e) {}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}

	public int getRecord() {
		return record;
	}

	public int getCounter() {
		return counter;
	}
	
	public void incrementCounter(int i) {
		counter += i;
		game.getTTS().speak(game.getContext().getString(R.string.scoreboard_success) + " " + counter);
	}

	public void decrementCounter(int i) {
		counter -= i;
		game.getTTS().speak(game.getContext().getString(R.string.scoreboard_fail) + " " + counter);
	}

	@Override
	public void onRemove() {}

}
