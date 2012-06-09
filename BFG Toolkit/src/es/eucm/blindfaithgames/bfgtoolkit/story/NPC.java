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
 *       BFG TOOLKIT is free software: you can redistribute it and/or modify
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
package es.eucm.blindfaithgames.bfgtoolkit.story;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an npc in game story.
 * 
 * */

public class NPC {
	
	private List<String> dialog;// list of dialogs
	private String name; // npc's name
 	
	private int nextDialog; // counter
	private boolean transition; // is it necessary to change dialog?
	
	/**
	 * NPC default constructor  
	 * 
	 * */
	public NPC(){
		dialog = new ArrayList<String>();
		nextDialog = 0;
		name = "";
		transition = false;
	}
	
	/**
	 * Allows specify a npc's name
	 * 
	 * @param n npc's name
	 * 
	 * */
	public NPC(String n){
		dialog = new ArrayList<String>();
		name = n;
		nextDialog = 0;
		transition = false;
	}
	
	/**
	 * Allows specify a npc's name
	 * 
	 * @param dialog constains a new sequence of commentaries
	 * 
	 * */
	public NPC(List<String> dialog){
		this.dialog = dialog;
		nextDialog = 0;
		name = "";
		transition = false;
	}
	
	/**
	 * Allows specify a npc's name
	 * 
	 * @param dialog constains a new sequence of commentaries
	 * @param name npc's name
	 * @param transition this dialog changes scene's state
	 * 
	 * */
	public NPC(List<String> dialog, String name, boolean transition){
		this.dialog = dialog;
		nextDialog = 0;
		this.name = name;
		this.transition = transition;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------
	public String getName(){
		return name;
	}

	public String getDialog() {
		String result = "";
		for(String s:dialog){
			result += " " + s; 
		}
		return result;
	}

	public boolean getTransition() {
		return transition;
	}
	
// ----------------------------------------------------------- Setters -----------------------------------------------------------
	
	public void reset() {
		nextDialog = 0;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------
	/**
	 * Changes the npc's dialog showed on the screen
	 * 
	 * @return next dialog
	 * 
	 * */
	public String nextDialog(){
		String speech = null;
		if(nextDialog < dialog.size()){
			speech = dialog.get(nextDialog);
			nextDialog++;
		}
		return speech;
	}
}
