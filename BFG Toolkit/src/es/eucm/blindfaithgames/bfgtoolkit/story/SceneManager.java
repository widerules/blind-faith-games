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

import java.util.Iterator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import es.eucm.blindfaithgames.bfgtoolkit.general.GameState;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input;
import es.eucm.blindfaithgames.bfgtoolkit.input.Input.EventType;
import es.eucm.blindfaithgames.bfgtoolkit.sound.TTS;

/**
 * it's a state machine which manages the transition between scenes and npcs in the scenes.
 * 
 * */

public class SceneManager {
	
	private enum StateGameplay { SET_DIALOG, SET_INTRO, SHOW_SELECT_NPC, SHOW_DIALOG, SHOW_TEXT_INTRO,
		SET_SELECT_NPC, SET_SELECT_SCENE, SHOW_SELECT_SCENE}; // SceneManager states
		
	private Scene currentScene;	// current focused scene
	
	private StateGameplay state; // scene state
	private int focus; // to read option on the screen
	private boolean focusChange; // to read option on the screen
	
	private int nScenes;// number of scenes
	private int nNPCs; // number of npcs
	
	private boolean transitionDialog; 
	
	private List<Scene> sceneBuffer;// list of game scenes
	
	/**
	 * Unique class constructor
	 * 
	 * @param sceneBuffer list of scenes which represents a game
	 * 
	 * */
	public SceneManager(List<Scene> sceneBuffer){
		this.sceneBuffer = sceneBuffer;
		if(!sceneBuffer.isEmpty())
			currentScene = sceneBuffer.get(0);
		
		state = StateGameplay.SET_INTRO;
		
		focus = -1;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------
	
	public Scene getScene(int i){
		return findScene(i);
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
	
// ----------------------------------------------------------- Setters -----------------------------------------------------------
	
	public boolean setIntro(Text text) {
		String intro = currentScene.getintroMsg();
		if(intro != null) {
			text.setText(intro);
			return true;
		} else
				return false;
	}

// ----------------------------------------------------------- Others -----------------------------------------------------------	
	
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
	
	public boolean updateDialog(Text text) {
		if(currentScene != null)
			return currentScene.updateDialog(text);
		else 
			return false;
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
	
	public boolean manageSceneManager(Text text, TTS textToSpeech){
		EventType e;
		boolean finish = false;
		switch(state){
			case SET_INTRO:
				if(setIntro(text))
					state = StateGameplay.SHOW_TEXT_INTRO;
				else
					state = StateGameplay.SET_SELECT_NPC;
				textToSpeech.speak(text.getText().replace(" //", "."));
				break;
			case SHOW_TEXT_INTRO:
				e = Input.getInput().removeEvent("onDoubleTap");
				if(e != null)
					state = StateGameplay.SET_SELECT_NPC;
				break;
			case SET_SELECT_NPC:
				nNPCs = showNPCOptions(text);
				if(nNPCs == 0)
					state = StateGameplay.SET_SELECT_SCENE;
				else{
					if(textToSpeech != null)
						textToSpeech.speak(text.getText().replace(" //", "."));
					state = StateGameplay.SHOW_SELECT_NPC;
				}
				break;
			case SHOW_SELECT_NPC:
				e = Input.getInput().removeEvent("onDoubleTap");
				if(e != null){
					int selectedNPC = screenPosToSelectedOption(e, nNPCs);
					transitionDialog = changeNPC(selectedNPC);
					state = StateGameplay.SET_DIALOG;
					text.setText("");
					if(textToSpeech != null)
						textToSpeech.speak(getCurrentDialog().replace(" //", "."));
				}
				break;
			case SET_DIALOG:
				if(!text.isWriting()){
					if(!updateDialog(text))
						state = StateGameplay.SHOW_DIALOG;
				}
				break;
			case SHOW_DIALOG:
				e = Input.getInput().removeEvent("onDoubleTap");
				if(e != null){
					if(!transitionDialog)
						state = StateGameplay.SET_SELECT_NPC;
					else
						state = StateGameplay.SET_SELECT_SCENE;
				}
				break;
			case SET_SELECT_SCENE:
				//delete finished scenes
				deleteScenes();
				nScenes = showSceneOptions(text);
				if(nScenes == 0)
					finish = true;
				if(textToSpeech != null)
					textToSpeech.speak(text.getText().replace(" //", "."));
				state = StateGameplay.SHOW_SELECT_SCENE;
				break;
			case SHOW_SELECT_SCENE:
				e = Input.getInput().removeEvent("onDoubleTap");
				if(e != null){
					int selectedScene = screenPosToSelectedOption(e, nScenes);
					changeScene(selectedScene);
					state = StateGameplay.SET_INTRO;
				}
				break;
		}
		return finish;
	}
	
	private void readButtons(EventType e, TTS textToSpeech, boolean focusRestriction) {
		int optionSelected = -1;
		String msg = null;
		if(state == StateGameplay.SHOW_SELECT_NPC){
			optionSelected = screenPosToSelectedOption(e, nNPCs);
			if((optionSelected < getNPCS().size() && optionSelected  >= 0)){
				msg = getNPCS().get(optionSelected).getName();
			}
		}else{
			if(state == StateGameplay.SHOW_SELECT_SCENE){
				optionSelected = screenPosToSelectedOption(e, nScenes);
				Integer scene;
				if(optionSelected  < getNextScenes().size() && optionSelected  >= 0) {
					scene = getNextScenes().get(optionSelected);
					msg = getScene(scene).getDescription();
				}
			}
		}
		focusChange = optionSelected != focus;
		if(textToSpeech != null && msg != null && focusChange){
			textToSpeech.speak(msg.replace(" //", "."));
			focus = optionSelected;
		}
	}
	
	public void manageTTS(TTS textToSpeech) {
		EventType e1 = Input.getInput().removeEvent("onDrag");
		EventType e2 = Input.getInput().removeEvent("onDown");
		if(e1 != null){
			readButtons(e1, textToSpeech, true);
		}
		else if(e2 != null) {
			readButtons(e2, textToSpeech, false);
		}
	}

	private int screenPosToSelectedOption(EventType e, int nOptions){
		int inc = GameState.SCREEN_HEIGHT / nOptions;
		int top = 0;
		int bot = inc;
		int counter = 0;
		boolean found = false;
		while(!found && counter < nOptions){
			if(e.getMotionEventE1().getY() > top && e.getMotionEventE1().getY() < bot){
				found = true;
			}
			if(!found){
				counter++;
				top += inc;
				bot += inc;
			}
		}
		if (found)
			return counter;
		else
			return -1;
	}

	public void drawButtons(Canvas canvas, String text, Paint localBrush) {
		// Debug
		canvas.drawText("ID: " + String.valueOf(getIDCurrentScene()), 25, 25, localBrush);
	
		if(state == StateGameplay.SHOW_SELECT_NPC)
			drawButtons(canvas,text,localBrush,nNPCs);
		else
			if(state == StateGameplay.SHOW_SELECT_SCENE)
				drawButtons(canvas,text,localBrush,nScenes);
	}
	
	public void drawButtons(Canvas canvas, String text, Paint localBrush, int nOptions) {
		Paint brush;
		if(nOptions > 0){
			int inc = GameState.SCREEN_HEIGHT / nOptions;
			int bot = inc;
			if(localBrush != null)
				brush = localBrush;
			else
				brush = new Paint();
			canvas.drawText(text + " " + 0, GameState.SCREEN_WIDTH/2, inc/2, localBrush);
			for(int i = 0; i < nOptions; i++){
				canvas.drawText(text + " " +  (i + 1), GameState.SCREEN_WIDTH/2, bot + inc/2, brush);
				canvas.drawLine(0, bot, GameState.SCREEN_WIDTH, bot, brush);
				bot += inc;
			}
		}
	}
}
