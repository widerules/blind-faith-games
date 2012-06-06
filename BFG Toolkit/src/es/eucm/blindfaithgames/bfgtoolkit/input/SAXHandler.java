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
package es.eucm.blindfaithgames.bfgtoolkit.input;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * 
 * Class to manage events sent by a parser SAX.
 * 
 * @author Gloria Pozuelo and Javier Álvarez.
 */
public class SAXHandler extends DefaultHandler {
	private XMLKeyboard k;
	private int counter;
	private String action;
	private int key;

	
	public XMLKeyboard getXMLKeyboard() {
		return k;
	}
	
// --------------------------------------------------------------------- //
/* DEFINE METHODS OF DefaultHandler */
// --------------------------------------------------------------------- //
	public void error(SAXParseException e) throws SAXParseException {
		throw e;
	}
	
	public void startDocument(){	
		counter = 0;
	}	
	
	public void startElement(String uri, String localName, String qName, Attributes att){
		if (qName.equals("keyboard")){			
			k = Input.getKeyboard();
			k.setNum(Integer.parseInt(att.getValue("num")));
		}
		else if (qName.equals("rowmap")){
			action = att.getValue("action");
			key = Integer.parseInt(att.getValue("key"));
		}	
	}

	public void endElement(String uri, String localName, String qName){
		if (qName.equals("rowmap")){
			k.addObject(key, action);
			counter++;
		}		
		else if (qName.equals("keyboard")){
			if (counter != k.getNum())
				k.riseNumberOfErrors(1);
		}
	}
}

