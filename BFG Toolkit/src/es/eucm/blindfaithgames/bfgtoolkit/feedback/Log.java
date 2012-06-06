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
package es.eucm.blindfaithgames.bfgtoolkit.feedback;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;



import es.eucm.blindfaithgames.bfgtoolkit.feedback.Log;

/**
 * Used to store every meaningful event in the games that we want to send to our remote server.
 * 
 * @author Javier Alvarez & Gloria Pozuelo.
 * 
 * */

public class Log {

	private static Log log = null; // Used to implement the singleton pattern

	public static final String NONE = "None";
	public static final String ONCREATE = "OnCreate";
	public static final String DEVICE = "Device";
	public static final String INSTRUCTIONS = "Instructions";
	public static final String EXIT = "Exit";
	public static final String KEY_EVENT = "KeyEvent";
	public static final String TAP_EVENT = "TapEvent";
	public static final String DOUBLE_TAP_EVENT = "DoubleTapEvent";
	public static final String TRIPLE_TAP_EVENT = "TripleTapEvent";

	private static final int N_QUESTIONS = 12;
	
	private String tag; // game name
	private Date date; // Log creation date
	private Map<Integer,Entry> logEntries; // Log Entries 
	private String comment; // Any random stuff about a game or whatever
	private int logCounter; // Number of entries
	private String id;
	
	private String[] formAnswers; // Answer about a possible game form.
	
	/**
	 * Gets a unique instance of Input. Singleton pattern.
	 * 
	 * @return An instance of Input.
	 * 
	 * */
	public static  Log  getLog(){
		if(log == null)
			return log = new Log();
		else
			return log;
	}
	
	/**
	 * Constructor of the class. Singleton
	 * 
	 * */
	protected Log(){
		logEntries = new HashMap<Integer,Entry>();
		logCounter = 0;
		
		formAnswers = new String[N_QUESTIONS];
		
		tag = "DEFAULT";
	}
// ----------------------------------------------------------- Getters -----------------------------------------------------------	
		
	public String getTag() {
		return tag;
	}

	public Date getDate() {
		return date;
	}

	public Map<Integer, Entry> getLogEntries() {
		return logEntries;
	}

	public String getComment() {
		return comment;
	}

	public String[] getFormAnswers() {
		return formAnswers;
	}
	
	public String getAnswer(int question){
		return formAnswers[question];
	}
	
	public Entry getEntry(int key){
		Entry e = logEntries.get(key);
		if(e != null)
			return e;
		else
			return null;
	}
	
	public Set<Integer> getEntryKeys(){
		return logEntries.keySet();
	}
	
	public String getId() {
		return id;
	}
	
// ----------------------------------------------------------- Setters -----------------------------------------------------------	
	
	public void setTag(String tag){
		this.tag = tag;
	}

	public void setComment(String comment){
		this.comment = comment;
	}

	public void setID(String id) {
		this.id = id;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------
	/**
	 * Adds a new log entry.
	 * 
	 * @param tag An id common to an entry set.
	 * @param configurationSettings String that contains information related with the game configuration.
	 * @param type Event type.
	 * @param path ClassPath where the event that we want to save is created.
	 * @param comment  Any random stuff about a game or whatever
	 * 
	 * @return Entry id in the log.
	 * */	
	public int addEntry(String tag, String activeConfigurationSettings, String type, String path, String comment){
		logCounter++;
		logEntries.put(logCounter, new Entry(tag,activeConfigurationSettings,type,path,comment));
		return logCounter;
	}

	/**
	 * Removes an log entry.
	 * 
	 * @param key Entry id.
	 * 
	 * @return The entry removed.
	 * */
	public Entry removeEntry(int key){
		return logEntries.remove(key);
	}
	
	/**
	 * Adds a form answer 
	 * 
	 * @param question The number of the question whose answer will be added.
	 * @param answer Answer in string format.
	 * */
	public void addAnswer(int question, String answer){
		formAnswers[question] = answer;
	} 

	
	/**
	 * Removes a form answer.
	 *  
	 * @param question The number of the question whose answer will be removed.
	 * 
	 * @return The removed answer.
	 * */
	public String removeAnswer(int question){
		return formAnswers[question] = null;
	}
	
	public void clearAnswers(){
		for(int i = 0; i < formAnswers.length; i++){
			formAnswers[i] = null; 
		}
	}

	public void clear() {
		logEntries.clear();
	}
	
	public String toString() {
		String res = new String();
		for(int i = 0; i < formAnswers.length; i++){
			res +=  "q" + (i + 1) + "- " + formAnswers[i] + "\n";
		}
		return res;
	}
}
