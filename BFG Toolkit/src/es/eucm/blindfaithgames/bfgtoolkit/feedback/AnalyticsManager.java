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

import org.acra.ErrorReporter;

import android.content.Context;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class AnalyticsManager {
	 public static String TRACK_ID;

	 private static AnalyticsManager analyticsManager = null;

	 private static GoogleAnalyticsTracker tracker;
	 private static final int frecuencyUpdateGoogle = 10;
	 private static Context context;
	 
	 public static String getUACodeAnalytics(){return TRACK_ID;}

	 public static AnalyticsManager getAnalyticsManager(Context context){
		 if(analyticsManager == null){
			   analyticsManager = new AnalyticsManager(context);
			   tracker = GoogleAnalyticsTracker.getInstance();
			   tracker.startNewSession(TRACK_ID, frecuencyUpdateGoogle , context);
		 }
	  	 return analyticsManager;
	 }
	  
	 public static AnalyticsManager getAnalyticsManager(){
		 return analyticsManager;
	 }
	 
	 public static GoogleAnalyticsTracker getTracker(){
		 if (tracker == null){
			   analyticsManager = new AnalyticsManager(context);
			   tracker = GoogleAnalyticsTracker.getInstance();
			   tracker.startNewSession(TRACK_ID, frecuencyUpdateGoogle , context);
		 }
		return tracker;
	 }
	 
	 public AnalyticsManager(Context context){
		 AnalyticsManager.context = context;  
	 }
	 
	 public void registerPage(String page, String parameter){
		 try {
			 getTracker().trackPageView("/" + page + "/" + parameter);
		 } catch(Exception e) {
			 ErrorReporter.getInstance().handleSilentException(new Exception("Null pointer resgister page: \n" + e.getMessage() + "\n" + 
						e.getStackTrace() + "\n Page: " + page + "\n Parameter: " + parameter + "\n\n"));
		 }
	 }
	 
	 public void registerPage(String page){  
		 try {
			 getTracker().trackPageView("/" + page);
		 } catch(Exception e) {
			 ErrorReporter.getInstance().handleSilentException(new Exception("Null pointer register page: \n" + e.getMessage() + "\n" + 
						e.getStackTrace() + "\n Page: " + page + "\n\n"));
		 }
	 }
	 
	 public void registerAction(String Category, String Action, String Label, int Value){
		 try {
			 getTracker().trackEvent(Category, Action, Label, Value);
		 } catch(Exception e) {
			 ErrorReporter.getInstance().handleSilentException(new Exception("Null pointer register action: \n" + e.getMessage() + "\n" + 
						e.getStackTrace() + "\n Category: " + Category + "\n Action: " + Action + "\n Label: " + Label + "\n Value: " + Value + "\n\n"));
		 }
	 }
	 
	 public static void stopTracker(){
		 if(getTracker() != null)
			 getTracker().stopSession();
	 }

	public static void dispatch() {
		if(getTracker() != null)
			  getTracker().dispatch();
	}
}
