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
package es.eucm.blindfaithgames.bfgtoolkit.general;

import java.util.ArrayList;
import java.util.List;

import org.acra.ErrorReporter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;


/**
 * State machine that contains a list of game state and an order that indicates how to be used each of them.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */

public class Game {
	
	private List<GameState> gameStates; // List of game State that contains our game
	private GameState currentState; // Active game state
	
	private List<Integer> order; // Defines a default order for the states transition. It can be changed during the game execution
	
	private int next; // Next position in the list order. It will contain an integer used to access to an appropriated game state in gameStates
	private boolean endGame; // Used to finish a game
	private boolean stateChangedLastStep; // It's used to fill the canvas with black if a state change has happened
	
	private int clearCanvas = 0; // Two game steps are needed to clear the canvas surface
	
	private boolean disabled; // To stop a game
	
	/**
	 * Default constructor.
	 * */
	public Game() {
		disabled = false;
	}
	
	/**
	 * Main constructor of the class.
	 * 
	 * @param gameStates List of game State that contains our game.
	 * @param order Defines a default order for the states transition. It can be changed during the game execution.
	 * */
	public Game(List<GameState> gameStates, ArrayList<Integer> order){
		this.gameStates = gameStates;
		this.order = order;
		next = 0;
		disabled = false;
		if(next < order.size()){
			if(gameStates != null  && gameStates.size() > 0){
				currentState = gameStates.get(order.get(next));
			}
		}
		stateChangedLastStep = false;
	}
	
// ----------------------------------------------------------- Getters -----------------------------------------------------------
	
	public int getNext() {
		return next;
	}
	
	public List<Integer> getOrder(){
		return order;
	}

	public boolean getDisabled() {
		return disabled;
	}
	
	public boolean isEndGame(){
		return endGame;
	}
	
// ----------------------------------------------------------- Setters -----------------------------------------------------------	
	
	public void setDisabled(boolean disabled){
		this.disabled = disabled;
	}
	
	public void changeOrder(ArrayList<Integer> order){
		this.order = order;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------
	/**
	 * Called before the game loop beginning.
	 * 
	 * */
	public void onInit() {
		currentState.onInit();
	}
	/**
	 *  
	 *  Manages the game render.
	 *  Fills the canvas with black if a state change happens calls game state onDraw method.
	 *  
	 *  @param canvas The surface that will be painted.
	 *
	 * */
	public void onDraw(Canvas canvas) {
		if(stateChangedLastStep){
			clearCanvas(canvas);
		}
		currentState.onDraw(canvas);
		if(disabled){
			canvas.drawColor(Color.BLACK);
		}
	}
	
	/**
	 * 
	 * Manages the game logic.
	 * If a state change happens calls game state onUpdate method.
	 *
	 * */
	public void onUpdate() {
		if(stateChangedLastStep){
			if(!currentState.isOnInitialized())
				currentState.onInit();
		}
		
		currentState.onUpdate();
		isThereChangeState();
	}
	
	/**
	 * Checks if the actual game state has already finished and if this happens makes a state change.
	 * 
	 * */
	private void isThereChangeState(){
		if(!currentState.isRunning()){
			next++;
			if(next < order.size()){
				try {
					currentState = gameStates.get(order.get(next));
					currentState.run();
					stateChangedLastStep = true;
				} catch (Exception e) {
					 ErrorReporter.getInstance().handleSilentException(new Exception("Desbordamiento gamestates: \n" + e.getMessage() + "\n" + 
								e.getStackTrace() + "\n Game States: " + gameStates.toString() + "\n\n"));
					endGame = true;
				}
			}
			else
				endGame = true;
		}
	}
	
	/**
	 * Fill the canvas with black to reset the view.
	 * 
	 * */
	private void clearCanvas(Canvas canvas) {
		clearCanvas++;
		if(clearCanvas == 2){
			clearCanvas = 0;
			stateChangedLastStep = false;
		}
		canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
	}

	/**
	 * This class can be used with the default constructor and before calling this method or with the main constructor of the class.
	 * 
	 * */
	public void initialize(ArrayList<GameState> gameStates,ArrayList<Integer> order) {
		this.gameStates = gameStates;
		this.order = order;
		next = 0;
		if(next < order.size()){
			if(gameStates != null  && gameStates.size() > 0){
				currentState = gameStates.get(order.get(next));
			}
		}
		stateChangedLastStep = false;
	}
	
	/**
	 * It deletes every game state.
	 */
	public void clear(){
		this.gameStates.clear();
	}

	/**
	 * Saves game states before the application is paused
	 * 
	 * */
	public void onSaveInstance(Bundle outState) {
		outState.putIntegerArrayList("Game.order" , (ArrayList<Integer>) order);
		
		outState.putInt("Game.next", next);
		outState.putBoolean("Game.endGame", endGame);
		outState.putBoolean("Game.stateChangedLastStep", stateChangedLastStep);
		
		outState.putInt("Game.clearCanvas", clearCanvas);
		
		outState.putBoolean("Game.disabled", disabled);
		
		for (int i = 0; i < gameStates.size(); i++){
			gameStates.get(i).onSaveInstance(outState, i);
		}
	}
	
	/**
	 * Loads game states before the application is restored
	 * 
	 * */
	public void onRestoreInstance(Bundle savedInstanceState) {
		try{
			next = savedInstanceState.getInt("Game.next", 0);
			endGame= savedInstanceState.getBoolean("Game.endGame", false);
			stateChangedLastStep = savedInstanceState.getBoolean("Game.stateChangedLastStep", false);
			
			clearCanvas = savedInstanceState.getInt("Game.clearCanvas", 0);
			
			disabled = savedInstanceState.getBoolean("Game.disabled", false);
			
			if(next < order.size()){
				int n = order.get(next);
				currentState = gameStates.get(n);
			}
			
			for (int i = 0; i < gameStates.size(); i++){
				gameStates.get(i).onRestoreInstance(savedInstanceState, i);
			}
		}catch(Exception e){
			ErrorReporter.getInstance().handleSilentException(new Exception("Fallo restaurar estado: \n" + e.getMessage() + "\n" +
									" " + e.getStackTrace() +  " " + gameStates.toString()));
		}
	}

	/**
	 * Release all graphic resource before the is stopped
	 * 
	 * */
	public void delete() {
		for (GameState gs: gameStates){
			gs.delete();
		}
	}
}

	
