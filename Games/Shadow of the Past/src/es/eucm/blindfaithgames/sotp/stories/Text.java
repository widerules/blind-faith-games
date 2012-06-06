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
package es.eucm.blindfaithgames.sotp.stories;

import java.util.List;
import java.util.StringTokenizer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import es.eucm.blindfaithgames.bfgtoolkit.general.Entity;
import es.eucm.blindfaithgames.bfgtoolkit.general.GameState;
import es.eucm.blindfaithgames.bfgtoolkit.general.Mask;
import es.eucm.blindfaithgames.bfgtoolkit.graphics.SpriteMap;
import es.eucm.blindfaithgames.bfgtoolkit.others.RuntimeConfig;
import es.eucm.blindfaithgames.sotp.R;

public class Text extends Entity {

	public static final String SEPARATOR = " // ";
	private float fontSize;
	private Typeface font;
	private Paint brush;
	
	private String text;
	private int nextWord;
	private int steps;
	private int nTokens;
	
	private int stepsPerWord = RuntimeConfig.TEXT_SPEED;
	private float WHITE_SPACE_SIZE;
	
	public Text(int x, int y, Bitmap img, GameState gameState, List<Mask> mask,
			SpriteMap animations, String soundName, Point soundOffset,
			boolean collide, Paint brush, int stepsPerWord, String text) {
		super(x, y, img, gameState, mask, animations, soundName, soundOffset, collide);
		
		this.text = text;
		nextWord = 0;
		steps = 0;
		
		this.stepsPerWord = stepsPerWord;
		
		WHITE_SPACE_SIZE =  (this.gameState.getContext().getResources().getDimension(R.dimen.white_space_size)/GameState.scale);
		fontSize =  (this.gameState.getContext().getResources().getDimension(R.dimen.font_size_intro)/GameState.scale);
		font = Typeface.createFromAsset(this.gameState.getContext().getAssets(),RuntimeConfig.FONT_PATH);
		if(brush != null){
			this.brush = brush;
		}else{
			font = Typeface.createFromAsset(this.gameState.getContext().getAssets(),RuntimeConfig.FONT_PATH);
			this.brush = new Paint();
			this.brush.setTextSize(fontSize);
			this.brush.setARGB(255, 255, 255, 204);
			if(font != null)
				this.brush.setTypeface(font);
		}
		
        StringTokenizer stk = new StringTokenizer(text);
        nTokens =  stk.countTokens();
	}

	public int getnTokens() {
		return nTokens;
	}

	public String getText() {
		return text;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		introTextEffect(canvas);
		super.onDraw(canvas);
	}
	
	private void introTextEffect(Canvas canvas) {
		int i; int row;
		String aux;
        StringTokenizer stk = new StringTokenizer(text);
        int nTokens =  stk.countTokens();
        i = 0;
        row = 1;
        int lineSize = 0;
        while(i < nextWord && stk.hasMoreTokens()){
                aux  = stk.nextToken();
                if(this.x +  (lineSize) + brush.measureText(aux) > GameState.SCREEN_WIDTH || aux.equals("//")){
                        row++;
                        lineSize = 0;
                        if(this.y + (row+1)*fontSize > GameState.SCREEN_HEIGHT){
                        	row = 1;
                        	clearText();
                        }
                }
                if(!aux.equals("//")){
                	canvas.drawText(aux, this.x + lineSize, this.y + row*fontSize, brush);
                	lineSize += brush.measureText(aux) + WHITE_SPACE_SIZE;
                }
                i++;
        }
		if (steps == stepsPerWord){
			steps = 0;
			if(nextWord <= nTokens)
				nextWord++;
		}
	}
	
	private void clearText() {
		StringTokenizer stk = new StringTokenizer(text, SEPARATOR);
		String aux;
		while(stk.hasMoreTokens()){
			aux = stk.nextToken();
			if(!stk.hasMoreTokens()){
				text = aux;
				nextWord = 0;
				steps = 0;
			}
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		steps++;
	}
	
	@Override
	public void onCollision(Entity e) {}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}

	@Override
	public void onRemove() {}

	public boolean isFinished() {
		return nTokens == nextWord-1;
	}

	public void setStepsPerWord(int stepsPerWord) {
		this.stepsPerWord = stepsPerWord;
		steps = 0;
	}

	public void setText(String text) {
		this.text = text;
		nextWord = 0;
		steps = 0;
	}

	public boolean isWriting() {
		StringTokenizer stk = new StringTokenizer(text);
		return nextWord <= stk.countTokens();
	}

	public void concatText(String speech) {
		this.text += SEPARATOR + speech;
	}

}
