package com.group8.project.screens;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame extends ApplicationAdapter implements Screen {
    
    private final int SPEED = 10;
    private final Vector3D END = new Vector3D(24,0,24);
    public static final int DEBUG = 0;
    
	public MainGame(Game aGame, String gridSize) {
		
		game = aGame;
		
		/* ------------------------------------------------ SIMULATION ------------------------------------------------------ */
		
		//this is for the lighting (try commenting this out and see how flat everything is)
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		//Got the String with grid Size from Menu and changed it to the Integer
		String gridS = gridSize.substring(0,2);
		int grid = Integer.parseInt(gridS);
		
		modelBatch = new ModelBatch();

		//All the camera settings, it's at an 67 degree angle
		cam = new PerspectiveCamera(67, 1920f, 1080f);
		

		//moved 10x,10y, and 10z away from the center
		cam.position.set(24f, 12f, 24f);
		//make the camera look at the center
		cam.lookAt(12f,0f,12f);
		

		//set the field of depth
		cam.near = 1f;
		cam.far = 300f;
		
		cam.update();
		viewport = new FitViewport(1080, 1280, cam);
		modelBuilder = new ModelBuilder();

		plane = modelBuilder.createLineGrid(grid, grid, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position | Usage.Normal);
		ModelInstance planeInstance = new ModelInstance(plane);
		planeInstance.transform.setToTranslation(12f, -0.5f, 12f);
		instances.add(planeInstance);

		camController = new CameraInputController(cam);
		camController.target = new Vector3(12, 0, 12);
		
		Gdx.input.setInputProcessor(camController);
		
		/* ------------------------------------------------------------------------------------------------------------------ */
		
		
		/* ------------------------------------------------ SETUP FOR AI ---------------------------------------------------- */
		
		FileReader = new PositionManager();
		testEngine = new RuleEngine(END);
		
		ArrayList<ModelInstance> instanceList = testEngine.getModules();
		for(ModelInstance model : instanceList) {
			instances.add(model);
		}
		
		savedTime = System.currentTimeMillis();
		
		/* ------------------------------------------------------------------------------------------------------------------ */
	
	}

	@Override
	public void render (float delta) {

		if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			
			if(StartSign == false) {
				StartSign = true;
			} else if(StartSign == true) {
				StartSign = false;
			}
		}
		
		if(StartSign) {
			testEngine.runRules();
		}
		if(Gdx.input.isKeyJustPressed(Keys.R)) { 
			if(rotate) {
				rotate = false;
			} else if(!rotate) {
				rotate = true;
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			testEngine.runRules();
		}
		


		if((System.currentTimeMillis() - savedTime) > SPEED && StartSign) {
			testEngine.runRules();
			savedTime = System.currentTimeMillis();
		}

		if(rotate) { cam.rotateAround(new Vector3(12,0,12), new Vector3(0,1,0), (float)0.2); }
		
		cam.update();
		
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		camController.update();

		
		//here you start the models and environment
		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	@Override
	public void dispose () {
		modelBatch.dispose();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	
		
		
	

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	public Environment environment;
	public PerspectiveCamera cam;
	public Viewport viewport;
    public ModelBatch modelBatch;
    public ModelBuilder modelBuilder;
    public ModelInstance instance;
    public Model plane;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public CameraInputController camController;
    public RuleEngine testEngine;
    public PositionManager FileReader;
    long savedTime;
    boolean StartSign = false;
    boolean rotate = false;

    private Game game;

    int stepCounter = 0;
}
