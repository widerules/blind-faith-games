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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Entry {
	
	public static final String DATE_FORMAT_NOW = "HH:mm:ss dd-MM-yyyy";
	
	private String tag;
	private Date timestamp;
	private String configurationSettings;
	
	// Event information
	private String type;
	private String path;
	
	// Random info
	private String comment;
	
	public Entry(String tag, String configurationSettings, String type, String path,String comment){
		this.tag = tag;
		
	    Calendar cal = Calendar.getInstance();
	    timestamp = cal.getTime();
		
		this.configurationSettings = configurationSettings;
		this.type = type;
		this.path = path;
		this.comment =comment;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getConfigurationSettings() {
		return configurationSettings;
	}

	public void setConfigurationSettings(String configurationSettings) {
		this.configurationSettings = configurationSettings;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String toString(){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return tag + " " + sdf.format(timestamp) +  " " 
				+ configurationSettings + " " + type + " " + path + " " + comment;
	}
}
