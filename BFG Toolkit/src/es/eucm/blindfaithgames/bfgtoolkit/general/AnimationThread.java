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
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Provides a separate thread where the game loop acts.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 * */

public class AnimationThread extends Thread {
	
	public static int DELAY = 1000/60; // 
	public static int FRAMES_PER_SECOND = 30; //
 	public static int SKIP_TICKS = 1000 / FRAMES_PER_SECOND; // Amount that have to be added in order to check the FPS
	
	private SurfaceHolder surfaceHolder; // To manage the surface
	private ISurface panel; // Custom Surface
	
	private boolean run = false; // Game is paused?
	
	/**
	 * Unique constructor of the class
	 * 
	 * @param surfaceHolder To manage the surface.
	 * @param panel Custom Surface.
	 * 
	 * */
	public AnimationThread(SurfaceHolder surfaceHolder, ISurface panel) {
		this.surfaceHolder = surfaceHolder;
		this.panel = panel;
	}
// ----------------------------------------------------------- Getters -----------------------------------------------------------
	
	public boolean isRunning() {
		return run;
	}
	
// ----------------------------------------------------------- Setters -----------------------------------------------------------
	
	public void setRunning(boolean value) {
		run = value;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------
	/**
	 * Method that represents a classical game loop.
	 * It provides FPS control, updates logic and renders the view. 
	 * 
	 * */
	@Override
	public void run() {
		float fps = 0;
		int counter = 0;
		long next_game_tick = System.currentTimeMillis();
		long sleep_time = 0;
		Canvas c = null;
		long initialTime = System.currentTimeMillis();
		
    	try {
            c = surfaceHolder.lockCanvas(null);
            if (c != null) {
            	synchronized (surfaceHolder) {
            		panel.onDraw(c);	
            	}
            }
        } finally {
            // do this in a finally so that if an exception is thrown
            // during the above, we don't leave the Surface in an
            // inconsistent state
            if (c != null) {
                surfaceHolder.unlockCanvasAndPost(c);
            }
        }
		
		panel.onInitalize();
		
	    while (run) {
	        c = null;
	    	next_game_tick += SKIP_TICKS;
	    	sleep_time = next_game_tick - System.currentTimeMillis();
	    	
	        if(sleep_time >= 0) {
	            try {
	    			Thread.sleep(sleep_time);
	    		} catch (InterruptedException e) {
	    			e.printStackTrace();
	    		}
	        }

        	try {
	            c = surfaceHolder.lockCanvas(null);
	            if (c != null) {
		            synchronized (surfaceHolder) {
		            	panel.onDraw(c);
		            }
	            }
	        } finally {
	            // do this in a finally so that if an exception is thrown
	            // during the above, we don't leave the Surface in an
	            // inconsistent state
	            if (c != null) {
	                surfaceHolder.unlockCanvasAndPost(c);
	            }
	        }
        	panel.onUpdate();
        	counter++;
        	long actualTime = System.currentTimeMillis();
	        if(actualTime - initialTime >= 1000) {
	        	 fps = counter;
	        	 counter = 0;
	        	 initialTime = actualTime;
	        }
	    }    		
	}
}




	
    
    

	


