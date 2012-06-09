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

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.acra.ErrorReporter;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * 
 * This class implements the music manager using MediaPlayer class.
 * 
 * 
 *
 */

public class Music {
	private static HashMap<Integer, MediaPlayer> sounds = null; // Map<ResourceId, Mediaplayer Object> Links a resource witch a Mediaplayer object

	private static SubtitleManager subs; // Manages the transcription of a game music
	
	private static Music m = null; // singleton
	
	/**
	 * Private constructor for singleton pattern.
	 * 
	 * */
	private Music(){
		sounds = new HashMap<Integer, MediaPlayer>();
		subs = new SubtitleManager();
	}
// ----------------------------------------------------------- Getters ----------------------------------------------------------- 	
	/**
	 * Singleton getter.
	 * 
	 * */
	public static Music getInstanceMusic(){
		if (m == null)
			return m = new Music();
		else 
			return m;
	}
	
// ----------------------------------------------------------- Others ----------------------------------------------------------- 
	/**
	 * Enables the subtitle system in the context "c".
	 * 
	 * @param context Context currently active.
	 * @param sInfo All information needed to play subtitles.
	 * 
	 * */
	public void enableTranscription(Context c, SubtitleInfo sInfo){
		subs = new SubtitleManager(c, sInfo);
		subs.setEnabled(true);
		if(sInfo != null)
				subs.setsInfo(sInfo);

	}
	/**
	 * 
	 * Disables the subtitle system in the current context. If It has already disabled, It do nothing.
	 * 
	 * */
	public void disableTranscription(){
		if(subs != null){
			subs.setEnabled(false);
		}
	}
	
	/** 
	 * Stops old song and starts new one. 
	 * 
	 * @param context Context where the sound will be played.
	 * @param resource Sound which will be played.
	 * @param looping Indicates if the sound will be reset if finishes. 
	 * 
	 * */
	public void play(Context context, int resource, boolean looping) {
		try{
			// Start music only if not disabled in preferences
			stop(resource);
			
			MediaPlayer mp;
			mp = MediaPlayer.create(context, resource);
			if(mp != null){
				sounds.put(resource, mp);
				mp.setLooping(looping);
				mp.start();
				
				if(subs != null){
					String aux = subs.getOnomatopeia(resource);
					subs.setDuration(mp.getDuration()/1000);
					if(aux != null)
						subs.showSubtitle(aux);
					else
						subs.showSubtitle(Integer.toString(resource));
				}
			} else {
				 ErrorReporter.getInstance().handleSilentException(new Exception("Problema con recursos de sonido"
						 													+ context.getResources().getResourceName(resource)));
			}
		} catch (IllegalStateException e) {
			 ErrorReporter.getInstance().handleSilentException(e);
		}
	}
	
	/** 
	 * 
	 * Stops old song and starts new one.
	 * 
	 * @param context Context where the sound will be played.
	 * @param resource Sound which will be played.
	 * @param looping If it's true the sound will be reset after finish. 
	 * 
	 *  */
	public void playWithBlock(Context context, int resource, boolean looping) {
		try{
			stop(resource);
			
			MediaPlayer mp;
			mp = MediaPlayer.create(context, resource);
			if(mp != null){
				sounds.put(resource, mp);
				mp.setLooping(looping);
				mp.start();
				
				if(subs != null){
					String aux = subs.getOnomatopeia(resource);
					subs.setDuration(mp.getDuration()/1000);
					if(aux != null)
						subs.showSubtitle(aux);
					else
						subs.showSubtitle(Integer.toString(resource));
				}
				
				while(mp.isPlaying()){};
			} else {
				 ErrorReporter.getInstance().handleSilentException(new Exception("Problema con recursos de sonido"
							+ context.getResources().getResourceName(resource)));
			}
		} catch (IllegalStateException e) {
			 ErrorReporter.getInstance().handleSilentException(e);
		}
	}

	/**
	 * Changes the volume of a sound.
	 * 
	 * @param leftVolume the volume of the left speaker.
	 * @param rightVolume the volume of the left speaker.
	 * @param resource Sound id in the application.
	 * 
	 * */
	public void setVolume(float leftVolume, float rightVolume, int resource){
		try {
			MediaPlayer mp;
			mp = sounds.get(resource); 
			if(mp != null){
				mp.setVolume(leftVolume, rightVolume);
			}
		} catch (IllegalStateException e) {
			 ErrorReporter.getInstance().handleSilentException(e);
		}
	}
	
	/** 
	 * 
	 * Stops a music mapped with id "resource". 
	 * 
	 * @param resource Sound id in the application.
	 * 
	 * */
	public void stop(int resource) {
		try{
			MediaPlayer mp = sounds.get(resource);
			if (mp != null) {
					mp.stop();
					mp.release();
					mp = null;
					sounds.remove(resource);
			}
		} catch (IllegalStateException e) {
			 ErrorReporter.getInstance().handleSilentException(e);
		}
	}

	/**
	 * Checks if a resource given by parameter is being played.
	 * 
	 * @param resource Sound id in the application.
	 * 
	 * */
	public boolean isPlaying(int resource) {
			MediaPlayer mp  = sounds.get(resource);
		 	if(mp != null){
		 		return mp.isPlaying();
		 	}
		 		return false;
	}
	

	/**
	 * 
	 * Stops all sound source currently active in the class.
	 * 
	 * */
	public void stopAllResources() {
		try{
			if(sounds != null){
				Set<Integer> keys = sounds.keySet();
				Set<Integer> keysAux = new CopyOnWriteArraySet<Integer>(keys);
				for(Integer n : keysAux){
					MediaPlayer mp = sounds.get(n);
					if (mp != null) {
							mp.stop();
							mp.release();
							mp = null;
							sounds.remove(n);
					}
				}
			}
		} catch (IllegalStateException e) {
			 ErrorReporter.getInstance().handleSilentException(e);
		}
	}
}
