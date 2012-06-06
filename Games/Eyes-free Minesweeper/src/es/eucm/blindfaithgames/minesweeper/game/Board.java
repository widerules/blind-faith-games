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

import java.util.Random;

import es.eucm.blindfaithgames.minesweeper.game.Cell.CellStates;

public class Board {
	private Cell[][] board;
	private int rowN, colN;
	int mines;
	private int pushedCells;
	private boolean finished;
	private String difficulty;

	public Board(int difficulty) {
		mines = 0;
		// Build the matrix of Cells which depends on difficult
		switch (difficulty) {
		case (0):
			rowN = 6;
			colN = 6;
			mines = 5;
			this.difficulty = "easy";
			break;
		case (1):
			rowN = 8;
			colN = 8;
			mines = 10;
			this.difficulty = "medium";
			break;
		case (2):
			rowN = 10;
			colN = 10;
			mines = 13;
			this.difficulty = "hard";
			break;
		}
		board = new Cell[rowN][colN];
		for (int row = 0; row < rowN; row++)
			for (int col = 0; col < colN; col++) {
				board[row][col] = new Cell();
			}

		// To allocate mines
		allocateMines(board, mines);
		// To calculate each block value
		allocateValues(board);
	}

	public CellStates getCellState(int row, int col) {
		return board[row][col].getState();
	}

	public int getCellValue(int row, int col) {
		return board[row][col].getValue();
	}

	public int getNCol() {
		return colN;
	}

	public int getNRow() {
		return rowN;
	}

	public boolean getCellVisibility(int selRow, int selCol) {
		return board[selRow][selCol].isVisible();
	}

	public boolean isFinished() {
		return finished;
	}
	
	public Cell getCell(int row, int col) {
		return board[row][col];
	}

	/**
	 * It sets the cell's status through the given coordinates and indicates when the game is over
	 * @param row Row in which the cell is situated
	 * @param col Column in which the cell is situated
	 */
	public void setCellStatePushed(int row, int col) {
		board[row][col].setState(CellStates.PUSHED);
		pushedCells++;
		if (pushedCells == (colN * rowN - mines))
			finished = true;
	}
	
	
	public void setCellState(int row, int col, CellStates state) {
		board[row][col].setState(state);
	}

	public void setCellVisibility(int selRow, int selCol) {
		board[selRow][selCol].setCellVisible(true);
	}
	
	private void allocateValues(Cell[][] board2) {
		int minesN = 0;
		for (int row = 0; row < rowN; row++) {
			for (int col = 0; col < colN; col++) {
				minesN = checkAround(row, col);
				if(board[row][col].getState() != CellStates.MINE)
					board[row][col].setValue(minesN);
			}
		}
	}

	private int checkAround(int row, int col) {
		// Looks for a mine in all senses
		int n = 0;
		if (row - 1 >= 0) {
			if (board[row - 1][col].getState() == CellStates.MINE)
				n++;
		}

		if (col - 1 >= 0) {
			if (board[row][col - 1].getState() == CellStates.MINE)
				n++;
		}

		if (row - 1 >= 0 && col - 1 >= 0) {
			if (board[row - 1][col - 1].getState() == CellStates.MINE)
				n++;
		}

		if (col + 1 < colN) {
			if (board[row][col + 1].getState() == CellStates.MINE)
				n++;
		}

		if (row - 1 >= 0 && col + 1 < colN) {
			if (board[row - 1][col + 1].getState() == CellStates.MINE)
				n++;
		}

		if (row + 1 < rowN) {
			if (board[row + 1][col].getState() == CellStates.MINE)
				n++;
		}

		if (row + 1 < rowN && col - 1 >= 0) {
			if (board[row + 1][col - 1].getState() == CellStates.MINE)
				n++;
		}

		if (row + 1 < rowN && col + 1 < colN) {
			if (board[row + 1][col + 1].getState() == CellStates.MINE)
				n++;
		}

		return n;
	}

	private void allocateMines(Cell[][] board, int mines) {
		int n = mines;
		int row = 0, col = 0;
		Random positionGenerator = new Random();
		while (n > 0) {
			row = positionGenerator.nextInt(rowN);
			col = positionGenerator.nextInt(colN);
			if (board[row][col].getState() != CellStates.MINE) {
				board[row][col].setState(CellStates.MINE);
				n--;
			}
		}
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < board.length; i++){
			s += "\n";
			for (Cell c : board[i]) {
				switch (c.getState()) {
				case MINE: s+= "B";
					break;
				default: s += (c.getValue());
					break;
				}
			}
		}
		return s;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public String getMines() {
		String s = "";
		Cell c;
		for (int i = 0; i < rowN; i++){
			for (int j = 0; j < colN; j++) {
				c = board[i][j];
				if(c.getState() == CellStates.MINE)
					s+="[" + i + "," + j +  "]";
			}
		}
		return s;
	}

}
