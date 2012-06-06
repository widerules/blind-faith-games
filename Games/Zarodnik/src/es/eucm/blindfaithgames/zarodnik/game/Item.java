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
package es.eucm.blindfaithgames.zarodnik.game;

import java.util.List;

import org.pielot.openal.Source;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import es.eucm.blindfaithgames.bfgtoolkit.general.Entity;
import es.eucm.blindfaithgames.bfgtoolkit.general.GameState;
import es.eucm.blindfaithgames.bfgtoolkit.general.Mask;
import es.eucm.blindfaithgames.bfgtoolkit.graphics.SpriteMap;
import es.eucm.blindfaithgames.bfgtoolkit.others.RuntimeConfig;
import es.eucm.blindfaithgames.bfgtoolkit.sound.Sound2D;

public abstract class Item extends Entity {
	
	protected ZarodnikGameplay gameState;
	
	public static final int ITEM_NUMBER = 3;
	
	public static final int RADIO = 0;
	public static final int CHAINFISH = 1;
	public static final int CAPSULE = 2;
	
	protected enum State{ EATEN, NORMAL };
	
	protected State state;

	public Item(int x, int y, Bitmap img, GameState gameState, List<Mask> mask,
			SpriteMap animations, String soundName, Point soundOffset,
			boolean collide) {
		super(x, y, img, gameState, mask, animations, soundName, soundOffset, collide);
		
		if(gameState instanceof ZarodnikGameplay)
			this.gameState = (ZarodnikGameplay) gameState;
		
		state = State.NORMAL;
	}

	public abstract boolean isFirstInstance();
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(RuntimeConfig.IS_DEBUG_MODE){
			Paint brush = new Paint();
			brush.setColor(Color.RED);
			brush.setStyle(Style.STROKE);
			int auxX = (int) (this.x - this.getImgWidth()*3.5);
			int auxY = (int) (this.y - this.getImgHeight()*3.5);
			canvas.drawRect(auxX, auxY, auxX + 8*this.getImgWidth(), auxY + 8*this.getImgHeight(), brush);
		}
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(checkAround() && state != State.EATEN){
			this.playAllSources();
		}
		else{
			this.stopAllSources();
		}
		if(this.gameState!= null){
			if(this.gameState.getPlayer().isInMovement())
				changePitch();
		}
	}
	
	private void changePitch() {
		List<Sound2D> sources = this.getSources();
		Source s;
		float n;
		if(this.gameState.getPlayer().getY() > this.y)
			n = 1;
		else
			n = 1.2f;
		if(!sources.isEmpty()){
			s = sources.get(0).getS();
			s.setPitch(n);
		}
	}

	private boolean checkAround() {
		if(this.gameState != null){
			boolean result = (Math.abs(this.gameState.getPlayer().getX() - (2*this.x + this.getImgWidth())/2) < this.getImgWidth()*4)
					&& (Math.abs(this.gameState.getPlayer().getY() - (2*this.y + this.getImgHeight())/2) < this.getImgHeight()*4);
			return result;
		}
			return false;
	}
}
