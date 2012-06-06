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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;


/**
 * 
 * More powerful class than CustomBimap. Provides methods to scale an image from different sources.
 * 
 * @author Javier Álvarez & Gloria Pozuelo.
 *  
 * */
public class BitmapScaler {
	
	private static class Size {
		int sample;
		float scale;
	}

	private Bitmap scaled;


	public BitmapScaler(Resources resources, int resId, int newWidth)
			throws IOException {
		Size size = getRoughSize(resources, resId, newWidth);
		roughScaleImage(resources, resId, size);
		scaleImage(newWidth);
	}

	public BitmapScaler(File file, int newWidth) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			Size size = getRoughSize(is, newWidth);
			try {
				is = new FileInputStream(file);
				roughScaleImage(is, size);
				scaleImage(newWidth);
			} finally {
				is.close();
			}
		} finally {
			is.close();
		}
	}

	public BitmapScaler(AssetManager manager, String assetName, int newWidth)
			throws IOException {
		InputStream is = null;
		try {
			is = manager.open(assetName);
			Size size = getRoughSize(is, newWidth);
			try {
				is = manager.open(assetName);
				roughScaleImage(is, size);
				scaleImage(newWidth);
			} finally {
				is.close();
			}
		} finally {
			is.close();
		}
	}

	public Bitmap getScaled() {
		return scaled;
	}

	private void scaleImage(int newWidth) {
		int width = scaled.getWidth();
		int height = scaled.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float ratio = ((float) scaled.getWidth()) / newWidth;
		int newHeight = (int) (height / ratio);
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		scaled = Bitmap.createBitmap(scaled, 0, 0, width, height, matrix, true);
	}

	private void roughScaleImage(InputStream is, Size size) {
		Matrix matrix = new Matrix();
		matrix.postScale(size.scale, size.scale);

		BitmapFactory.Options scaledOpts = new BitmapFactory.Options();
		scaledOpts.inSampleSize = size.sample;
		scaled = BitmapFactory.decodeStream(is, null, scaledOpts);
	}

	private void roughScaleImage(Resources resources, int resId, Size size) {
		Matrix matrix = new Matrix();
		matrix.postScale(size.scale, size.scale);

		BitmapFactory.Options scaledOpts = new BitmapFactory.Options();
		scaledOpts.inSampleSize = size.sample;
		scaled = BitmapFactory.decodeResource(resources, resId, scaledOpts);
	}

	private Size getRoughSize(InputStream is, int newWidth) {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, o);

		Size size = getRoughSize(o.outWidth, o.outHeight, newWidth);
		return size;
	}

	private Size getRoughSize(Resources resources, int resId, int newWidth) {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resId, o);

		Size size = getRoughSize(o.outWidth, o.outHeight, newWidth);
		return size;
	}

	private Size getRoughSize(int outWidth, int outHeight, int newWidth) {
		Size size = new Size();
		size.scale = outWidth / newWidth;
		size.sample = 1;

		int width = outWidth;
		int height = outHeight;

		int newHeight = (int) (outHeight / size.scale);

		while (true) {
			if (width / 2 < newWidth || height / 2 < newHeight) {
				break;
			}
			width /= 2;
			height /= 2;
			size.sample *= 2;
		}
		return size;
	}
}
