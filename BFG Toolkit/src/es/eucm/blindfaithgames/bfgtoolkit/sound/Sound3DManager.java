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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pielot.openal.Buffer;
import org.pielot.openal.SoundEnv;
import org.pielot.openal.Source;

import android.app.Activity;

/**
 * This class manages everything about 3D sound.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * 
 * */
public class Sound3DManager {
	
	private SoundEnv env; // OpenAL bridge

	protected Map<String,List<Source>> sources; // Map<soundName, setSources>
	
	protected Map<String,Buffer> buffers; // openAL bridge stuff
	
	private static Sound3DManager sm = null; // singleton
	
	/**
	 * Unique constructor of the class
	 * 
	 * @param a Context where the class is instantiated.
	 * 
	 * */
	public Sound3DManager(Activity a){
		sources = new HashMap<String,List<Source>>();
		buffers = new HashMap<String, Buffer>();
		this.env = SoundEnv.getInstance(a);
		this.env.setListenerOrientation(20);
	}
// ----------------------------------------------------------- Getters ----------------------------------------------------------- 	
	/**
	 * Gets a unique instance of Sound3DManager. Singleton pattern.
	 * 
	 * @return An instance of Sound3DManager.
	 * 
	 * */
	public static Sound3DManager getSoundManager(Activity a){
		if(sm == null){
			return sm = new  Sound3DManager(a);
		}else{
			return sm;
		}
	}
// ----------------------------------------------------------- Setters -----------------------------------------------------------
	/**
	 * To indicate where the listener is on the screen.
	 * 
	 * */
	public void setListenerPosition(float x, float y, float z) {
		env.setListenerPos(x, y, z);
	}
	
	/**
	 * To indicate where the listener is looking in the space.
	 * 
	 * */
	public void setListenerOrientation(float heading) {
		env.setListenerOrientation(heading);
	}
	
// ----------------------------------------------------------- Others ----------------------------------------------------------- 	
	/**
	 * Adds a new source to the 3D sound engine using a sound name which exists in the assets directory.
	 * 
	 * @param soundName Source name.
	 * 
	 * @return the source created.
	 * 
	 * */
	public Source addSource(String soundName){
		Source source = null;
		Buffer b; List<Source> lAux;
		if(soundName != null){
			b = buffers.get(soundName);
			if(b == null){
				try {
					b = env.addBuffer(soundName);
					buffers.put(soundName, b);
					source = env.addSource(b);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				source = env.addSource(b);
				lAux = sources.get(soundName);
				if(lAux == null){
					lAux = new ArrayList<Source>();
					lAux.add(source);
					sources.put(soundName,lAux);
				}
				else{
					lAux.add(source);
				}
			}
			
		}
		return source;
	}
	
	/**
	 * Called when the amount of memory is low.
	 * 
	 * */
	public void onLowMemory() {
		this.env.onLowMemory();
	}

	/**
	 * Stops all sound sources.
	 * 
	 * */
	public void stopAllSources() {
		this.env.stopAllSources();
	}
	
	/**
	 * Releases memory if it's needed.
	 * 
	 * */
	public void release() {
		this.env.release();
	}
	
	/**
	 * Plays every 3d sound source created by the application.
	 * 
	 * */
	public void playAllSources() {
		this.env.playAllSources(true);
	}
}
