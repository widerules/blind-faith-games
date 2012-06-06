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

import android.graphics.Bitmap;
import android.graphics.Point;
import es.eucm.blindfaithgames.bfgtoolkit.general.GameState;
import es.eucm.blindfaithgames.bfgtoolkit.general.Mask;
import es.eucm.blindfaithgames.bfgtoolkit.graphics.SpriteMap;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input.EventType;

public class Boss extends Creature{

	
	private boolean playing;
	private Simon simon;
	
	public Boss(int x, int y, Bitmap img, GameState game, List<Mask> mask,
			SpriteMap animations, String soundName, Point soundOffset,
			boolean collidable, int speed) {
		super(x, y, img, game, mask, animations, soundName, soundOffset, collidable,
				speed);
		
		playing = false;
		
		simon = new Simon(this.gameState.getContext());
		
		//this.playAnim("stay", RuntimeConfig.FRAMES_PER_STEP, true);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		int ans;
		float touchX, touchY;
		EventType e = Input.getInput().removeEvent("onDown");
		if(e != null && !playing){
				touchX = e.getMotionEventE1().getX();
				touchY = e.getMotionEventE1().getY();
				if(touchX > GameState.SCREEN_WIDTH/2){
					if(touchY > GameState.SCREEN_HEIGHT/2){
						ans = Simon.UP_RIGHT;
					}
					else{
						ans = Simon.DOWN_RIGHT;
					}
				}
				else{
					if(touchY > GameState.SCREEN_HEIGHT/2){
						ans = Simon.UP_LEFT;
					}
					else{
						ans = Simon.DOWN_LEFT;
					}
				}
				playing = simon.checkAnswer(ans);
		}
		
		if(playing){
			simon.playSequence();
			playing = false;
		}
		
		if(simon.isGameOver()){
			finishGame();
		}
	}

	private void finishGame() {
		this.game.getPlayer().onDie();
	}

	@Override
	public void onDie() {}
}
