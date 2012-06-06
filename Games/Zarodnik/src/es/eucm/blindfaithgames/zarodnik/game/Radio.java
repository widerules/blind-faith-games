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
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Point;
import es.eucm.blindfaithgames.bfgtoolkit.general.Entity;
import es.eucm.blindfaithgames.bfgtoolkit.general.GameState;
import es.eucm.blindfaithgames.bfgtoolkit.general.Mask;
import es.eucm.blindfaithgames.bfgtoolkit.graphics.SpriteMap;
import es.eucm.blindfaithgames.bfgtoolkit.others.RuntimeConfig;

public class Radio extends Item{

	private static final int MAX_CLUES = 10;
	private Entity prey;
	private Entity player;
	
	private int clueNumber;

	private static int instancesNo;
	
	public Radio(int x, int y, Bitmap img, GameState gameState,
			List<Mask> mask, SpriteMap animations, String soundName,
			Point soundOffset, boolean collide, Entity prey) {
		super(x, y, img, gameState, mask, animations, soundName, soundOffset, collide);
		
		this.prey = prey;
		Random numberGenerator = new Random();
		clueNumber = numberGenerator.nextInt(MAX_CLUES);
		
		instancesNo++;
	}

	@Override
	public void onCollision(Entity e) {
		if(e instanceof Player){
			this.setTimer(0, RuntimeConfig.FRAMES_PER_STEP);
			this.setTimer(1, RuntimeConfig.FRAMES_PER_STEP*5);
			this.setCollidable(false);
			state = State.EATEN;
			player = e;
		}
	}

	@Override
	public void onTimer(int timer) {
		if(timer == 0){
			this.setVisible(false);
			this.stopAllSources();
		}else{
			if(timer == 1){
				if(clueNumber > 0){
					this.setTimer(1, RuntimeConfig.FRAMES_PER_STEP*5);
					speakClue();
					clueNumber--;
				}
				if(clueNumber == 0)
					this.remove();
			}
		}
	}

	private void speakClue() {
		if(prey != null && !prey.isRemovable()){
			if (Math.abs(player.getX() - prey.getX()) > Math.abs(player.getY() - prey.getY())){
				if (prey.getX() < player.getX())
					this.gameState.getTTS().speak("left");
				else
					this.gameState.getTTS().speak("right");
			}
			else{
				if (prey.getY() < player.getY())
					this.gameState.getTTS().speak("up");
				else
					this.gameState.getTTS().speak("down");
			}
		}
	}

	@Override
	public void onInit() {}


	@Override
	public void onRemove() {}

	@Override
	public boolean isFirstInstance() {
		return instancesNo == 2;
	}
}
