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
 * 	  This file is part of EYES-FREE MINESWEEPER, developed in the Blind Faith Games project.
 *  
 *        EYES-FREE MINESWEEPER, is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *   
 *       EYES-FREE MINESWEEPER is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *   
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with Adventure.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.blindfaithgames.minesweeper.game;

import android.content.Context;

import es.eucm.blindfaithgames.minesweeper.R;



public class Cell {
	private int value;
	private boolean visible;
	private CellStates state;

	public static enum CellStates {
		MINE, PUSHED, NOTPUSHED, FLAGGED
	};

	public Cell() {
		super();
		value = -1;
		state = CellStates.NOTPUSHED;
	}

	public Cell(int value, CellStates state, boolean visible) {
		super();
		this.value = value;
		this.state = state;
	}

	public CellStates getState() {
		return state;
	}

	public int getValue() {
		return value;
	}

	public void setState(CellStates state) {
		this.state = state;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setCellVisible(boolean visible) {
		this.visible = visible;
	}

	public String cellToString(Context c) {
		switch (state){
		case PUSHED:
			return c.getString(R.string.cellStatePushed) + " "  + value;
		case NOTPUSHED :
			return c.getString(R.string.cellStateNotPushed);
		case FLAGGED:
			return c.getString(R.string.cellStateFlagged);
		case MINE:
			if(!visible)
				return c.getString(R.string.cellStateNotPushed);
			else
				return c.getString(R.string.cellStateMine);
		}
		
		return c.getString(R.string.cellStateUnknown);	
	}
	
}
