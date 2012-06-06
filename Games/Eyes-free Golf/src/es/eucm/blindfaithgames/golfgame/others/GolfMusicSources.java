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
package es.eucm.blindfaithgames.golfgame.others;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import es.eucm.blindfaithgames.golfgame.R;

public class GolfMusicSources {
	
	private static final int bing = R.raw.bing;
	private static final int bip  = R.raw.bip;
	private static final int clue_feed_back_sound = R.raw.clue_feed_back_sound;
	private static final int hit_ball = R.raw.hit_ball;
	private static final int left = R.raw.left;
	private static final int main = R.raw.main;
	private static final int missed = R.raw.missed;
	private static final int previous_shoot_feedback_sound = R.raw.previous_shoot_feedback_sound;
	private static final int right = R.raw.right;
	private static final int sound_shot = R.raw.sound_shot;
	private static final int win_sound = R.raw.win_sound;
	
	public static Map<Integer, String> getMap(Context c) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		
		result.put(bing, c.getString(R.string.bing_ono));
		result.put(bip, c.getString(R.string.bing_ono));
		result.put(clue_feed_back_sound, c.getString(R.string.clue_feed_back_sound_ono));
		result.put(hit_ball, c.getString(R.string.hit_ball_ono));
		result.put(left, c.getString(R.string.left_ono));
		result.put(main, c.getString(R.string.main_ono));
		result.put(missed, c.getString(R.string.missed_ono));
		result.put(previous_shoot_feedback_sound, c.getString(R.string.previous_shoot_feedback_sound_ono));
		result.put(right, c.getString(R.string.right_ono));
		result.put(sound_shot, c.getString(R.string.sound_shot_ono));
		result.put(win_sound, c.getString(R.string.win_sound_ono));
		return result;
	}

}
