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

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;



/**
 * 
 * Class that validates and reads XML files.
 * 
 * @author Gloria Pozuelo & Javier �lvarez
 *
 */
public class ScenesReader {

	public ScenesReader(){}
	
	/**
	 * Loads a keyboard from XML file.
	 * @param is XML file
	 * @return The new keyboard
	 */
	public SceneManager loadAdventure(InputStream is) {
		try {
			
			/** Handling XML */
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser sp = factory.newSAXParser();

			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			StoriesLoader saxHandler = new StoriesLoader();
			sp.parse(is, saxHandler);
			return saxHandler.getSceneManager();
			
		} catch (Exception e) {
			System.out.println("XML Parsing Exception = " + e);
			return null;
		}		
	}
}
