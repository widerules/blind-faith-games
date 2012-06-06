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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;

/**
 * 
 * Class that manages everything related to saving an edited XML keyboard.
 * 
 * @author Gloria Pozuelo & Javier Álvarez
 * 
 */

public class KeyboardWriter {
	private Document doc;

	public KeyboardWriter() {
	}

	// ---------------------------------------------------------------------------
	// //
	/* DOM Tree construction */
	// ---------------------------------------------------------------------------
	// //
	@SuppressWarnings("rawtypes")
	public void saveEditedKeyboard(int num, HashMap<Integer, String> keyList,
			FileOutputStream fos) throws ParserConfigurationException {
		// Instantiates the DOM object
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.newDocument();

		// Makes the root document and puts it as the only child of the document 
		Element keyboardNode = doc.createElement("keyboard");
		keyboardNode.setAttribute("num", "" + num);
		doc.appendChild(keyboardNode);

		
		Iterator it = keyList.entrySet().iterator();
		// For each keyboard row
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			Element rowNode = createRowNode(e.getKey(), e.getValue());
			// Puts it with the root node
			keyboardNode.appendChild(rowNode);
		}
		try {
			saveXML(doc, fos);
		} catch (TransformerException e) {
			Log.d("XMLHELPER", "Exception: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("XMLHELPER", "Exception: " + e);
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------- Setters -----------------------------------------------------------

	private Element createRowNode(Object key, Object value) {
		// Makes the a node among its attributes
		Element keyNode = doc.createElement("rowmap");
		keyNode.setAttribute("action", "" + value);
		keyNode.setAttribute("key", "" + key);

		return keyNode;
	}

	/**
	 * Saves a XML keyboard.
	 * 
	 * @param doc
	 * @param fos
	 * @throws TransformerException
	 * @throws TransformerException
	 * @throws IOException
	 */
	private void saveXML(Document doc, FileOutputStream fos) throws TransformerException, IOException {
		// Gets a new instance of transformer from a factory
		TransformerFactory factory = TransformerFactory.newInstance();
		// factory.setAttribute("indent-number", 4);
		Transformer transformer = factory.newTransformer();

		// Instantiates a Source from the dom tree
		DOMSource origen = new DOMSource(doc);

		// Instantiates a Result from the input file
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		// Sets up the transformer and executes it
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"keyboard.dtd");
		transformer.transform(origen, new StreamResult(osw));

		osw.flush();
		osw.close();

	}
	
}
