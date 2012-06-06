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

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import es.eucm.blindfaithgames.zarodnik.R;

public class ZarodnikMusicSources {
	
	private static final int appear = R.raw.appear;
	private static final int apple_bite  = R.raw.apple_bite;
	private static final int barn_beat = R.raw.barn_beat;
	private static final int bip = R.raw.bip;
	private static final int bubble = R.raw.bubble;
	private static final int die = R.raw.die;
	private static final int mysterioso_march = R.raw.intro;
	private static final int predator = R.raw.predator;
	private static final int prey = R.raw.prey;
	private static final int prey_dead = R.raw.prey_dead;
	private static final int radio = R.raw.radio;
	private static final int start = R.raw.start;
	private static final int the_path_of_the_goblin = R.raw.main;
	private static final int chain = R.raw.chain;
	private static final int pill = R.raw.pill;
	
	public static Map<Integer, String> getMap(Context c) {
		Map<Integer, String> result = new HashMap<Integer, String>();
		
		result.put(appear, c.getString(R.string.appear_ono));
		result.put(apple_bite, c.getString(R.string.apple_bite_ono));
		result.put(barn_beat, c.getString(R.string.barn_beat_ono));
		result.put(bip, c.getString(R.string.bip_ono));
		result.put(bubble, c.getString(R.string.bubble_ono));
		result.put(die, c.getString(R.string.die_ono));
		result.put(mysterioso_march, c.getString(R.string.frost_walkz_ono));
		result.put(predator, c.getString(R.string.predator_ono));
		result.put(prey, c.getString(R.string.prey_ono));
		result.put(prey_dead, c.getString(R.string.prey_dead_ono));
		result.put(radio, c.getString(R.string.radio_ono));
		result.put(start, c.getString(R.string.start_ono));
		result.put(the_path_of_the_goblin, c.getString(R.string.the_path_of_the_goblin_ono));
		result.put(chain, c.getString(R.string.chain));
		result.put(pill, c.getString(R.string.pill));
		
		
		return result;
	}
}
