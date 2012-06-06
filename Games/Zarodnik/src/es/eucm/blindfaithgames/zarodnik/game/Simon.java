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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.content.Context;

public class Simon {
	
	public static final int UP_LEFT = 0;
	public static final int UP_RIGHT = 1;
	public static final int DOWN_LEFT = 2;
	public static final int DOWN_RIGHT = 3;

	private Context mContext;
	
	private ArrayList<Integer> sequence;
	
	private int next;
	private int chances;
	
	public Simon(Context context){
		mContext = context;
		sequence = new ArrayList<Integer>();
		next = 0;
		chances = 3;
	}
	
	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
	
	public void playSequence(){
		Random numberGenerator = new Random();
		sequence.add(numberGenerator.nextInt(4));
		playSounds(sequence);
	}
	
	private void playSounds(ArrayList<Integer> sequence) {
		Iterator<Integer> it = sequence.iterator();
		int n;
		while(it.hasNext()){
			n = it.next();
			switch(n){
			// TODO find sounds
				case UP_LEFT:
					//Music.getInstanceMusic().play(mContext, R.raw.up_left, false);
					break;
				case UP_RIGHT:
					//Music.getInstanceMusic().play(mContext, R.raw.up_right, false);
					break;
				case DOWN_LEFT:
					//Music.getInstanceMusic().play(mContext, R.raw.down_left, false);
					break;
				case DOWN_RIGHT:
					//Music.getInstanceMusic().play(mContext, R.raw.down_right, false);
					break;
				default:
					break;
			}
		}
		
	}

	public boolean checkAnswer(int ans){
		boolean found = false;
		
		if(next < sequence.size())
			found = sequence.get(next) == ans;

		if(found)
				chances--;
			
		next++;
		
		return next == sequence.size();
	}

	public boolean isGameOver() {
		return chances == 0;
	}
}
