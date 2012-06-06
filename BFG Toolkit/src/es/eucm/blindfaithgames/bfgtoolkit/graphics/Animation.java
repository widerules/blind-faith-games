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
package es.eucm.blindfaithgames.bfgtoolkit.graphics;

import java.util.ArrayList;

/**
 * This class represents a simple animation.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */

public class Animation {

	private String name; //  name of the animation
	private ArrayList<Integer> frameList; // List that contains the order which will be played this animation
	private boolean loop; // Indicates if the animation loops
	private int frameCount; // Counts the last frame showed
	private int framesPerStep; // Number of frames per game step
	
	/**
	 * Unique constructor of the class.
	 * 
	 * @param name Name of the animation.
	 * @param frameList List that contains the order which will be used to play this animation.
	 * @param framesPerStep Number of frames per game step.
	 * @param loop Indicates if the animation loops.
	 * 
	 * */
	
	public Animation(String name, ArrayList<Integer> frameList, int framesPerStep, boolean loop){
		this.name = name;
		this.frameList = frameList;
		this.framesPerStep = framesPerStep;
		this.loop = loop;
		this.frameCount = frameList.size();
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------  
	
	public boolean isLoop() {
		return loop;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Integer> getFrameList() {
		return frameList;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public int getFramesPerStep() {
		return framesPerStep;
	}
	
// ----------------------------------------------------------- Setters -----------------------------------------------------------
	
	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void setFrameDelay(int framesPerStep) {
		this.framesPerStep = framesPerStep;
	}
}
