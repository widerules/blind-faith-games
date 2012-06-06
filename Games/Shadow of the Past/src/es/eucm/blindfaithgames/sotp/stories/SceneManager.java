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

import java.util.Iterator;
import java.util.List;

public class SceneManager {

	private Scene currentScene;
	
	private List<Scene> sceneBuffer;
	
	public SceneManager(List<Scene> sceneBuffer){
		this.sceneBuffer = sceneBuffer;
		if(!sceneBuffer.isEmpty())
			currentScene = sceneBuffer.get(0);
	}

	public Scene getScene(int i){
		return findScene(i);
	}
	
	public boolean changeNPC(int selectedNPC){
		if(currentScene.getNPCS().size() > 1 ){
			if(currentScene != null && selectedNPC < currentScene.getNPCS().size())
				return currentScene.changeNPC(selectedNPC);
		}
		else{
			return currentScene.changeNPC(0);
		}
		return false;
	}
	
	public boolean changeScene(int selectedScene){
		int nextScene;
		boolean success = true;
		
		// Check if currentScene has already finished checking its finished condition
		if(isFinished(currentScene)){
			sceneBuffer.remove(currentScene);
		}
		
		// If there is more than one option the player chooses
		if(currentScene.getNextScenes().size() > 1){
			if(selectedScene < currentScene.getNextScenes().size()){
				 nextScene = currentScene.getNextScenes().get(selectedScene);
				 //currentScene.getNextScenes().remove(selectedScene);
				 currentScene = findScene(nextScene);
			}
			else{
				success = false;
			}		
		}else{
			if(currentScene.getNextScenes().isEmpty()){
				success = false;
			}
			else{
				nextScene = currentScene.getNextScenes().get(0);
				currentScene = findScene(nextScene);
			}
		}
			
		return success;
	}

	private boolean isFinished(Scene currentScene) {
		List<Integer> conditionEnd = currentScene.getEndCondition();
		boolean found = true;
		Scene scene;
		for(Integer sc : conditionEnd){
			scene = findScene(sc);
			found &= (scene == null);
			if(sc.equals(currentScene))
				return true;
		}
		if(conditionEnd.isEmpty())
			return false;
		else
			return found;
	}

	public boolean updateDialog(Text text) {
		if(currentScene != null)
			return currentScene.updateDialog(text);
		else 
			return false;
	}

	public List<Integer> getNextScenes() {
		if(currentScene != null)
			return currentScene.getNextScenes();
		else
			return null;
	}

	public List<NPC> getNPCS() {
		if(currentScene != null)
			return currentScene.getNPCS();
		else
			return null;
	}

	public boolean setIntro(Text text) {
		String intro = currentScene.getintroMsg();
		if(intro != null) {
			text.setText(intro);
			return true;
		} else
				return false;
	}

	public int showNPCOptions(Text text) {
		return currentScene.showNPCOptions(text);
	}

	public int showSceneOptions(Text text) {
		String options = "";
		int counter = 0;
		Scene scene;
		for(Integer sc: currentScene.getNextScenes()){
			scene = findScene(sc);
			if(isAccessible(sc) &&  (scene != null)){
				options += counter + " - " + scene.getDescription() + Text.SEPARATOR;
				counter++;
			}
		}
		text.setText(options);
		return counter;
	}

	private boolean isAccessible(Integer sc) {
		boolean found = true;
		Scene sceneChecked = findScene(sc);
		Scene scene;
		if(sceneChecked != null){
			for(Integer s: sceneChecked.getTransitionCondition()){
				scene = findScene(s);
				found &= (scene == null);
			}
		}
		return found;
	}

	public String getCurrentDialog() {
		String result = null;
		
		if(currentScene != null)
			result = currentScene.getCurrentDialog();
		
		return result;
	}
	
	public int getIDCurrentScene() {
		return currentScene.getID();
	}
	
	private Scene findScene(int id){
		boolean found = false;
		Scene scene = null;
		Iterator<Scene> it = sceneBuffer.iterator();
		while(it.hasNext() && !found){
			scene = it.next();
			found = scene.getID() == id;
		}
		if(found)
			return scene;
		else
			return null;
	}

	public void deleteScenes() {
		Iterator<Scene> it = sceneBuffer.iterator();
		Scene sc;
		while(it.hasNext()){
			sc = it.next();
			if(isFinished(sc)){
				it.remove();
			}
		}
	}
}
