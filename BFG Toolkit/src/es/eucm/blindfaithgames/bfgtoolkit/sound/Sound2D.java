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
 * 	  This file is part of BFG TOOLKIT, developed in the Blind Faith Games project.
 *  
 *       BFG TOOLKIT, is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *   
 *       BFG TOOLKIT is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *   
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with Adventure.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.blindfaithgames.bfgtoolkit.sound;

import org.pielot.openal.Source;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;

/**
 * Associates a 3D sound source with a position 2D.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */
public class Sound2D {

	private Point p; // Position 
	 
	private Source s; // 3D sound source

	/**
	 * Unique constructor of the class.
	 * 
	 * @param p The position on the screen where the source will be played.
	 * @param s The source which will be played.
	 * 
	 * */
	public Sound2D(Point p, Source s) {
		super();
		this.p = p;
		this.s = s;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------
	public Point getP() {
		return p;
	}
	
	public Source getS() {
		return s;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------
	
	public void setP(Point p) {
		this.p = p;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------
	
	/**
	 * Draws a transcription of the sound on the given canvas.
	 * 
	 * @param canvas The canvas object that will be painted. 
	 * @param x the coordinate on the x axis of the entity linked to the source. 
	 * @param y the coordinate on the y axis of the entity linked to the source.
	 * */
	public void onDraw(Canvas canvas, int x, int y) {
		Paint brush = new Paint();
		if(s.getTranscription() != null)
			canvas.drawText(s.getTranscription(), x + p.x, y, brush);
	}

	/**
	 * Saves sound state
	 * @param i 
	 * @param j
	 * 
	 * */
	public void onSavedInstance(Bundle savedInstanceState, int i, int j) {
		savedInstanceState.putInt(i + " " + j + " p.x", p.x);
		savedInstanceState.putInt(i + " " + j + " p.y", p.y);
	}
	
	/**
	 * Restores sound state
	 * @param i 
	 * @param j
	 * 
	 * */
	public void onRestoreSavedInstance(Bundle savedInstanceState, int i, int j) {
		Point p = new Point();
		p.x = savedInstanceState.getInt(i + " " + j + " p.x");
		p.y = savedInstanceState.getInt(i + " " + j + " p.y");
	}
}
