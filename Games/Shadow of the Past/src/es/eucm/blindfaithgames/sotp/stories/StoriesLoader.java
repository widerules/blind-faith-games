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

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * 
 * Class to manage events sent by a parser SAX.
 * 
 * @author Gloria Pozuelo and Javier Ã�lvarez.
 */
public class StoriesLoader extends DefaultHandler {
	// Scenes
	private List<Scene> scenes;
	private List<NPC> npcs;
	private SceneManager sceneManager;
	private SceneType type;
	private int id;
	private String description, introMsg;
	private List<Integer> endCondition, transitionCondition, nextScenes;

	// NPCs
	private String name, author;
	private List<String> dialog;
	private String transition;

	private StringBuilder buf;

	public SceneManager getSceneManager() {
		return sceneManager;
	}

	// --------------------------------------------------------------------- //
	/* DEFINE METHODS OF DefaultHandler */
	// --------------------------------------------------------------------- //
	public void error(SAXParseException e) throws SAXParseException {
		throw e;
	}

	public void startDocument() {
		endCondition = new ArrayList<Integer>();
		transitionCondition = new ArrayList<Integer>();
		nextScenes = new ArrayList<Integer>();
		scenes = new ArrayList<Scene>();
		npcs = new ArrayList<NPC>();
		dialog = new ArrayList<String>();
		buf = new StringBuilder();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes att) {
		if (qName.equals("scene")) {
			type = SceneType.valueOf(att.getValue("type").toUpperCase());
			id = Integer.parseInt(att.getValue("id"));
		} else if (qName.equals("introMessage")) {
			buf.delete(0, buf.length());
		} else if (qName.equals("description")) {
			buf.delete(0, buf.length());
		} else if (qName.equals("idTransitionCondition")) {
			transitionCondition.add(Integer.parseInt(att.getValue("id")));
		} else if (qName.equals("idEndCondition")) {
			endCondition.add(Integer.parseInt(att.getValue("id")));
		} else if (qName.equals("idNextScenes")) {
			nextScenes.add(Integer.parseInt(att.getValue("id")));
		}

		// NPC
		else if (qName.equals("npc")) {
			name = att.getValue("name");
			transition = att.getValue("transition");
		} else if (qName.equals("answer")) {
			author = att.getValue("author");
			buf.delete(0, buf.length());
		}
	}

	public void endElement(String uri, String localName, String qName) {
		if (qName.equals("scene")) {
			scenes.add(new Scene(npcs, id, type, introMsg, description,
					nextScenes, transitionCondition, endCondition));
			npcs = new ArrayList<NPC>();
			transitionCondition = new ArrayList<Integer>();
			nextScenes = new ArrayList<Integer>();
			endCondition = new ArrayList<Integer>();
		} else if (qName.equals("answer")) {
			if (author != null)
				dialog.add(author + ": " + buf.toString());
			else
				dialog.add(buf.toString());
			author = "";
		} else if (qName.equals("npc")) {
			if (transition != null) {
				if (transition.equalsIgnoreCase("true"))
					npcs.add(new NPC(dialog, name, true));
				else
					npcs.add(new NPC(dialog, name, false));
			} else {
				npcs.add(new NPC(dialog, name, false));
			}
			dialog = new ArrayList<String>();
		} else if (qName.equals("introMessage")) {
			introMsg = buf.toString();
			buf.delete(0, buf.length());
		} else if (qName.equals("description")) {
			description = buf.toString();
			buf.delete(0, buf.length());
		} else if (qName.equals("sceneManager")) {
			sceneManager = new SceneManager(scenes);
		}
	}

	public void characters(char ch[], int start, int length) {
		if (buf != null) {
			for (int i = start; i < start + length; i++) {
				buf.append(ch[i]);
			}
		}
	}
}
