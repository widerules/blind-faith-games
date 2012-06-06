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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * This class contains a simple animation. It's supposed the frames of the graphic are only in horizontal sense.
 * 
 * @author Javier Álvarez & Gloria Pozuelo. 
 * 
 * */

public class AnimatedSprite {
	
	private Bitmap animation; // The sprite sheet
	private int xPos; // The animation position on the x axis
	private int yPos; // The animation position on the y axis
	private Rect sRectangle; // Indicates the current frame on the sprite sheet

	private int numFrames; // Number of frames in the sheet
	private int currentFrame; // Current frame 
	private int spriteHeight; // Frame height
	private int spriteWidth; // Frame width
	private boolean stop; // Enables the animation
	
	private int frameDelay;
	private int triggerDelay;
	
	/**
	 * Default constructor.
	 * 
	 * */
	public AnimatedSprite() {
		sRectangle = new Rect(0, 0, 0, 0);

		frameDelay = 5;
		triggerDelay = 5;
	}
// ----------------------------------------------------------- Getters ----------------------------------------------------------- 

	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public int getFrameCount() {
		return numFrames;
	}
	
	public int getNumFrames(){
		return numFrames;
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}
	
	public boolean isFinished(){
		return stop;
	}

// ----------------------------------------------------------- Setters -----------------------------------------------------------  
	
	public void setXPos(int value) {
		xPos = value;
	}
	
	public void setYPos(int value) {
		yPos = value;
	}

	public void setFrameDelay(int framesPerStep) {
		frameDelay = framesPerStep;
	}

	public void play() {
		stop = false;
	}
	
	public void stop() {
		stop = true;
	}
	
// ----------------------------------------------------------- Others -----------------------------------------------------------  
	
	/**
	 * To initialize an animation.
	 * 
	 * @param bitmap // The sprite sheet.
	 * @param height // Sprite sheet height.
	 * @param width // Sprite sheet width.
	 * @param frameCount // Number of frames in the sheet.
	 * 
	 * */
	public void Initialize(Bitmap bitmap, int height, int width, int frameCount) {
		this.animation = bitmap;
		this.spriteHeight = height;
		this.spriteWidth = width;
		this.sRectangle.top = 0;
		this.sRectangle.bottom = spriteHeight;
		this.sRectangle.left = 0;
		this.sRectangle.right = spriteWidth;
		this.numFrames = frameCount;
		stop = true;
	}
	
	/**
	 * Sets the delay of the image.
	 * 
	 * @param frameDelay To hang up the animation.
	 * */
	public void play(int frameDelay){
		stop = false;
		this.frameDelay = frameDelay;
		triggerDelay = this.frameDelay;
	}

	/**
	 * Updates the logic of animation. It checks which is the current frame and computes the 
	 * rectangle of the current frame.
	 * 
	 * */
	public void onUpdate() {
		if (frameDelay >= 0 && --triggerDelay <= 0){
			// Reset the frame trigger
			triggerDelay = frameDelay;
			
			// Increment the frame
			if(!stop){
				currentFrame += 1;
				
				if( currentFrame >= numFrames ) {
					currentFrame = 0;
				}
				
				sRectangle.left = currentFrame * spriteWidth;
				sRectangle.right = sRectangle.left + spriteWidth;
				}
			}
	}
	
	/**
	 * Draws on the screen the actual frame.
	 * 
	 * @param canvas Canvas object which will be painted.
	 * @param x Coordinate on x axis where the actual frame will be painted.
	 * @param y Coordinate on y axis where the actual frame will be painted.
	 * 
	 * */
	public void onDraw(int x, int y, Canvas canvas) {
		Rect dest = new Rect(x, y, x + spriteWidth,
										y + spriteHeight);
		canvas.drawBitmap(animation, sRectangle, dest, null);
	}
}
