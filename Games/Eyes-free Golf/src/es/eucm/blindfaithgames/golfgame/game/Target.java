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

import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Point;
import es.eucm.blindfaithgames.bfgtoolkit.general.Entity;
import es.eucm.blindfaithgames.bfgtoolkit.general.GameState;
import es.eucm.blindfaithgames.bfgtoolkit.general.Mask;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input.EventType;
import es.eucm.blindfaithgames.bfgtoolkit.sound.Music;
import es.eucm.blindfaithgames.golfgame.R;
import es.eucm.blindfaithgames.golfgame.activities.SettingsActivity;
import es.eucm.blindfaithgames.golfgame.activities.TutorialActivity;
import es.eucm.blindfaithgames.golfgame.game.GolfGameplay.steps;

public class Target extends Entity {

	private static final int clue_feedback_sound = R.raw.clue_feed_back_sound;

	public Target(int x, int y, Bitmap img, GameState game, List<Mask> mask) {
		super(x, y, img, game, mask, null, null, null, true);
	}

	public Point changePosition() {
		Random positions = new Random();
		int ancho = this.gameState.getView().getWidth() - this.getImgWidth();
		this.x = positions.nextInt(ancho);
		return new Point(this.x + 2 * this.getImgWidth() / 5, this.y + 4
				* this.getImgWidth() / 5);
	}

	@Override
	public void onUpdate() {
		EventType e = Input.getInput().getEvent("onDrag");

		if (e != null && (SettingsActivity.getNotifyTarget(gameState.getContext()) || this.gameState.getContext() instanceof TutorialActivity)) {
			float x = e.getMotionEventE1().getX();
			float y = e.getMotionEventE1().getY();
			if ((x >= this.x && x < this.x + this.getImgWidth())
					&& (y >= this.y && y < this.y + this.getImgHeight())) {
				
				if(!Music.getInstanceMusic().isPlaying(clue_feedback_sound))
					Music.getInstanceMusic().play(this.gameState.getContext(),clue_feedback_sound, false);
				
				if (((GolfGameplay) this.gameState).isTutorialMode()) {
					if (((GolfGameplay) this.gameState).getStep() == steps.STEP4) {
						((GolfGameplay) this.gameState).nextState();
					}
				}
			}
		}

		super.onUpdate();
	}

	@Override
	public void onCollision(Entity e) {}

	@Override
	public void onTimer(int timer) {}

	@Override
	public void onInit() {}

	@Override
	public void onRemove() {}
}
