package com.mgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AssetsFileUtility extends BaseFileUtil {
	
	private AssetManager assetManager;
	
	public AssetsFileUtility(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	@Override
	public Bitmap getBitmapFromName(String name) {
		InputStream inputStream = getInputStreamFromName(name);
		return (inputStream == null) ? null : BitmapFactory.decodeStream(inputStream);
	}
	
	@Override
	public BufferedReader getReaderFromName(String name) {
		InputStream inputStream = getInputStreamFromName(name);
		return (inputStream == null) ? null : new BufferedReader(new InputStreamReader(inputStream));
	}
	
	private InputStream getInputStreamFromName(String name) {
		InputStream inputStream;
		if (baseFolder != null) {
			try {
				inputStream = assetManager.open(baseFolder+name);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			try {
				inputStream = assetManager.open(name);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return inputStream;
	}
	

}
