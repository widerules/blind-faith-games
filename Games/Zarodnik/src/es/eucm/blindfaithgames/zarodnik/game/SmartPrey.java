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
import es.eucm.blindfaithgames.bfgtoolkit.general.Entity;
import es.eucm.blindfaithgames.bfgtoolkit.general.GameState;
import es.eucm.blindfaithgames.bfgtoolkit.general.Mask;
import es.eucm.blindfaithgames.bfgtoolkit.graphics.SpriteMap;
import es.eucm.blindfaithgames.bfgtoolkit.others.RuntimeConfig;
import es.eucm.blindfaithgames.bfgtoolkit.sound.Music;

public class SmartPrey extends Creature {

	private int die_sound;
	
	public SmartPrey(int x, int y, Bitmap img, GameState game, List<Mask> mask, SpriteMap animations, String soundName, Point soundOffset, boolean collidable, int dieSound) {

		super(x, y, img, game, mask, animations, soundName, soundOffset, collidable, 1);
	
		die_sound = dieSound;
	}
	
    @Override
	public void onUpdate() {
    	super.onUpdate();
    	if(checkAround() && !this.die)
    			avoidPlayer();
    }

	private void avoidPlayer() {
		double dx, dy;

		dx = this.game.getPlayer().getX() - this.x;
		dy = this.game.getPlayer().getY() - this.y;
		if(dx < 0 && dy < 0 && this.x + speed < GameState.SCREEN_WIDTH - getImgWidth() 
							&& this.y + speed < GameState.SCREEN_HEIGHT - getImgHeight()){
			this.x += speed;
			this.y += speed;
		}
		else{
			if(dx < 0 && dy > 0 && this.x + speed < GameState.SCREEN_WIDTH - getImgWidth() 
								&& this.y - speed > getImgHeight()){
				this.x += speed;
				this.y -= speed;
			}
			else{
				if(dx > 0 && dy < 0 && this.x - speed > getImgWidth() 
									&& this.y + speed < GameState.SCREEN_HEIGHT - getImgHeight()){
					this.x -= speed;
					this.y += speed;
				}
				else{
					if(this.x - speed > getImgWidth() && this.y - speed > getImgHeight()){
    					this.x -= speed;
    					this.y -= speed;
					}
				}
			}
		}
	}

	private boolean checkAround() {
		if(this.game != null){
			boolean result = (Math.abs(this.game.getPlayer().getX() - this.x) < 50)
				&& (Math.abs(this.game.getPlayer().getY() - this.y) < 50);
			return result;
		}
		else 
			return false;
	}
	
	@Override
	public void onRemove() {
		super.onRemove();
	}
	
	@Override
	public void onCollision(Entity e) {
		super.onCollision(e);
		if (e instanceof Player){
		}
	}
	
	@Override
	public void onTimer(int timer) {
		if(timer == 0)
			this.remove();
	}

	@Override
	public void onDie() {
		Music.getInstanceMusic().play(this.gameState.getContext(), die_sound, false);
		this.setDie(true);
		this.setTimer(0, RuntimeConfig.FRAMES_PER_STEP*4);
		this.playAnim("die", RuntimeConfig.FRAMES_PER_STEP, false);
		this.setCollidable(false);
	}
}
