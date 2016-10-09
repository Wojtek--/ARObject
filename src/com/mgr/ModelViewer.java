package com.mgr;

import java.io.BufferedReader;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;

public class ModelViewer  extends AndARActivity implements SurfaceHolder.Callback {
	public static final int TYPE_INTERNAL = 0;
	public static final int TYPE_EXTERNAL = 1;
	public static final boolean DEBUG = false;
	private final int MENU_SCALE = 0;
	private final int MENU_ROTATE = 1;
	private final int MENU_TRANSLATE = 2;
	private int mode = MENU_SCALE;
	private Model model;
	private Model3D model3d;
	private ProgressDialog waitDialog;
	private Resources res;
	
	ARToolkit artoolkit;
	public ModelViewer() {
		super(false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setNonARRenderer(new LightingRenderer());
		res=getResources();
		artoolkit = getArtoolkit();		
		getSurfaceView().setOnTouchListener(new TouchEventHandler());
		getSurfaceView().getHolder().addCallback(this);
	}
	
	public void uncaughtException(Thread thread, Throwable ex) {
		System.out.println("");
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_TRANSLATE, 0, res.getText(R.string.translate))
    		.setIcon(R.drawable.translate);
        menu.add(0, MENU_ROTATE, 0, res.getText(R.string.rotate))
        	.setIcon(R.drawable.rotate);
        menu.add(0, MENU_SCALE, 0, res.getText(R.string.scale))
        	.setIcon(R.drawable.scale);     
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case MENU_SCALE:
	            mode = MENU_SCALE;
	            return true;
	        case MENU_ROTATE:
	        	mode = MENU_ROTATE;
	            return true;
	        case MENU_TRANSLATE:
	        	mode = MENU_TRANSLATE;
	            return true;
        }
        return false;
    }
    
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	super.surfaceCreated(holder);

    	if(model == null) {
			waitDialog = ProgressDialog.show(this, "", getResources().getText(R.string.loading), true);
			waitDialog.show();
			new ModelLoader().execute();
		}
    }
    
    class TouchEventHandler implements OnTouchListener {
    	
    	private float lastX=0;
    	private float lastY=0;


		public boolean onTouch(View v, MotionEvent event) {
			if(model!=null) {
				switch(event.getAction()) {
					default:
					case MotionEvent.ACTION_DOWN:
						lastX = event.getX();
						lastY = event.getY();
						break;
					case MotionEvent.ACTION_MOVE:
						float dX = lastX - event.getX();
						float dY = lastY - event.getY();
						lastX = event.getX();
						lastY = event.getY();
						if(model != null) {
							switch(mode) {
								case MENU_SCALE:
									model.setScale(dY/100.0f);
						            break;
						        case MENU_ROTATE:
						        	model.setXrot(-1*dX);
									model.setYrot(-1*dY);
						            break;
						        case MENU_TRANSLATE:
						        	model.setXpos(dY/10f);
									model.setYpos(dX/10f);
						        	break;
							}		
						}
						break;
					case MotionEvent.ACTION_CANCEL:	
					case MotionEvent.ACTION_UP:
						lastX = event.getX();
						lastY = event.getY();
						break;
				}
			}
			return true;
		}
    	
    }
    
	private class ModelLoader extends AsyncTask<Void, Void, Void> {
		
		
		private String modelName2patternName (String modelName) {
			String patternName = "android";
			
			
			return patternName;
		}
		
		
    	@Override
    	protected Void doInBackground(Void... params) {
    		
			Intent intent = getIntent();
			Bundle data = intent.getExtras();
			String modelFileName = data.getString("name");
			BaseFileUtil fileUtil= null;
			fileUtil = new AssetsFileUtility(getResources().getAssets());
			fileUtil.setBaseFolder("models/");
			
			ObjParser parser = new ObjParser(fileUtil);
			try {
				if(Config.DEBUG)
					Debug.startMethodTracing("AndObjViewer");
				if(fileUtil != null) {
					BufferedReader fileReader = fileUtil.getReaderFromName(modelFileName);
					if(fileReader != null) {
						model = parser.parse("Model", fileReader);
//							Log.w("ModelLoader", "model3d = new Model3D(model, android.patt");
						model3d = new Model3D(model, "android.patt");
//							model3d = new Model3D(model, modelName2patternName(modelFileName) + ".patt");
					} else {
						Log.w("ModelLoader", "no file reader");
					}
				}
				if(Config.DEBUG)
					Debug.stopMethodTracing();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
    		return null;
    	}
    	@Override
    	protected void onPostExecute(Void result) {
    		super.onPostExecute(result);
    		waitDialog.dismiss();
    		
    		try {
    			if(model3d!=null) {
    				artoolkit.registerARObject(model3d);
    			}
			} catch (AndARException e) {
				e.printStackTrace();
			}
			startPreview();
    	}
    }
	   
}
