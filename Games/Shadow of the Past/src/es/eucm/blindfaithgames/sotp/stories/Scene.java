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
 * 	  This file is part of SHADOW OF THE PAST, developed in the Blind Faith Games project.
 *  
 *       SHADOW OF THE PAST is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *   
 *       SHADOW OF THE PAST is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *   
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with Adventure.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.blindfaithgames.sotp.stories;

import java.util.List;


public class Scene {

	private NPC currentNPC;
	private List<NPC> npcs;
	private int id;
	private SceneType type;
	private List<Integer> nextScenes;		/* Scene ids of the next possible scenes */
	private String introMsg, description;
	
	private List<Integer> transitionCondition;
	private List<Integer> endCondition;
	private boolean firstTime;
	
	
	public Scene(List<NPC> npcs, int id, SceneType type, String introMsg, String description,
					List<Integer> nextScenes,List<Integer> transitionCondition, List<Integer> endCondition){
		this.npcs = npcs;
		this.id = id;
		this.type = type;
		this.introMsg = introMsg;
		this.description = description;
		this.nextScenes = nextScenes;
		this.transitionCondition = transitionCondition;
		this.endCondition = endCondition;
		firstTime = true;
	}

	public int getID() {
		return id;
	}
	
	public String getintroMsg() {
		if(firstTime){
			firstTime = false;
			return introMsg;
		}else
			return null;
	}
	
	public List<Integer> getNextScenes() {
		return nextScenes;
	}

	public List<NPC> getNPCS() {
		return npcs;
	}
	
	public List<Integer> getTransitionCondition() {
		return transitionCondition;
	}
	
	public List<Integer> getEndCondition() {
		return endCondition;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean changeNPC(int selectedNPC) {
		currentNPC = npcs.get(selectedNPC);
		currentNPC.reset();
		return currentNPC.getTransition();
		//npcs.remove(selectedNPC);
	}
	
	public boolean equals(Object o){
		Scene sc = (Scene) o;
		return this.id == sc.id;
	}

	public boolean updateDialog(Text text) {
		String speech = currentNPC.nextDialog();
		if(speech != null)
			text.concatText(speech);
		return speech != null;
	}

	public boolean isInNextScene(int selectedScene) {
		boolean found = false;
		for(Integer sc:nextScenes){
			found = (sc == selectedScene);
		}
		return found;
	}

	public String getNPCSOptions() {
		String options = "";
		int counter = 0;
		for(NPC npc:npcs){
			options += counter + "- " + npc.getName() + Text.SEPARATOR;
		}
		return options;
	}

	public int showNPCOptions(Text text) {
		String options = "";
		int counter = 0;
		for(NPC npc:npcs){
				options += counter + " - " + npc.getName() + Text.SEPARATOR;
				counter++;
		}
		text.setText(options);
		return counter;
	}

	public String getCurrentDialog() {
		String result = null;
		if(currentNPC!= null){
			result = currentNPC.getDialog();
		}
		return result;
	}
}
