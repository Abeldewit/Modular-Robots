package com.group8.project.screens;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.group8.project.ScreenDivide;

public class Manual extends ApplicationAdapter implements Screen {

	private Game game;
	private Stage stage;
	private Skin skin;
	private Label outputLabel;
	private String position;
	private TextField text1;
	private TextField text2;
	private TextField text3;
	private TextField text4;
	private TextField text1b;
	private TextField text2b;
	private TextField text3b;
	private TextField text4b;
	private HashMap<Integer, Vector3D> mMap;
	private HashMap<Integer, Vector3D> oMap;
	private PositionManager reader;
	private int modulesNumber;
	private int obstaclesNumber;
	private int x;
	private final boolean DEBUG = true;
	private String grid;




	public Manual(Game aGame,  String gridSize) {


		reader = new PositionManager();
		oMap = new HashMap<Integer, Vector3D>();
		mMap = new HashMap<Integer, Vector3D>();
		position = new String();
		modulesNumber = 0;
		obstaclesNumber = 0;
		x = 0;
		grid = gridSize;

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		game = aGame;



		int row_height = Gdx.graphics.getWidth()/12;
		int col_width = Gdx.graphics.getHeight()/12;

		skin = new Skin(Gdx.files.internal("skin/metal-ui.json"));

		Label title = new Label("Manual Input",skin,"default");
		title.setSize(Gdx.graphics.getWidth(),row_height*2);
		title.setPosition(0,Gdx.graphics.getHeight()-row_height*2);
		title.setAlignment(Align.center);
		stage.addActor(title);

		Label hint1 = new Label("Module number  /", skin, "default");
		hint1.setSize(Gdx.graphics.getWidth(),row_height);
		hint1.setPosition(col_width*12/10,Gdx.graphics.getHeight()-row_height*39/10);
		stage.addActor(hint1);

		Label hint2 = new Label("Parameter X  /", skin, "default");
		hint2.setSize(Gdx.graphics.getWidth(),row_height);
		hint2.setPosition(col_width*42/10,Gdx.graphics.getHeight()-row_height*39/10);
		stage.addActor(hint2);

		Label hint3 = new Label("Parameter Y  /", skin, "default");
		hint3.setSize(Gdx.graphics.getWidth(),row_height);
		hint3.setPosition(col_width*68/10,Gdx.graphics.getHeight()-row_height*39/10);
		stage.addActor(hint3);

		Label hint4 = new Label("Parameter Z", skin, "default");
		hint4.setSize(Gdx.graphics.getWidth(),row_height);
		hint4.setPosition(col_width*94/10,Gdx.graphics.getHeight()-row_height*39/10);
		stage.addActor(hint4);

		Label hint1b = new Label("Obsacle number /", skin, "default");
		hint1b.setSize(Gdx.graphics.getWidth(),row_height);
		hint1b.setPosition(col_width*12/10,Gdx.graphics.getHeight()-row_height*61/10);
		stage.addActor(hint1b);

		Label hint2b = new Label("Parameter X  /", skin, "default");
		hint2b.setSize(Gdx.graphics.getWidth(),row_height);
		hint2b.setPosition(col_width*42/10,Gdx.graphics.getHeight()-row_height*61/10);
		stage.addActor(hint2b);

		Label hint3b = new Label("Parameter Y  /", skin, "default");
		hint3b.setSize(Gdx.graphics.getWidth(),row_height);
		hint3b.setPosition(col_width*68/10,Gdx.graphics.getHeight()-row_height*61/10);
		stage.addActor(hint3b);

		Label hint4b = new Label("Parameter Z", skin, "default");
		hint4b.setSize(Gdx.graphics.getWidth(),row_height);
		hint4b.setPosition(col_width*94/10,Gdx.graphics.getHeight()-row_height*61/10);
		stage.addActor(hint4b);

		/////////////////////////////////@Module Adding Button
		Button button1 = new TextButton("Add \n Module", skin, "default");
		button1.setSize(col_width*2, row_height);
		button1.setPosition(col_width*12,Gdx.graphics.getHeight()-row_height*3);
		button1.addListener(new InputListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (text1.getText().isEmpty() || text2.getText().isEmpty() || text3.getText().isEmpty() || text4.getText().isEmpty()) {
					outputLabel.setText("One of the inputs is empty, cannot create Module");
				}
				else {
					modulesNumber++;
					position = "Module #" + modulesNumber + " = " + text1.getText() + "  " + text2.getText() + "  " + text3.getText() + "  " + text4.getText();
					outputLabel.setText(position);
					String ID = text1.getText();
					String X = text2.getText();
					String Y = text3.getText();
					String Z = text4.getText();
					Integer xf = Integer.parseInt(X);
					Integer yf = Integer.parseInt(Y);
					Integer zf = Integer.parseInt(Z);
					Integer IDf = 0;
					if (ID.equals("X") || ID.equals("x")) {
						IDf = (int) (2000 + x);
						x++;
					}
					else {
						IDf = Integer.parseInt(ID);
					}

					addToMap(mMap, IDf, xf, yf, zf);
					if (DEBUG) System.out.println("module loaded is " + mMap.get(IDf));
				}

			}
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				outputLabel.setText("Pressed a Button");
				return true;
			}
		});
		stage.addActor(button1);
		/////////////////////////////////


		/////////////////////////////////@Obstacles Adding Button
		Button button2 = new TextButton("Add \n Obstacle", skin, "default");
		button2.setSize(col_width*2, row_height);
		button2.setPosition(col_width*12,Gdx.graphics.getHeight()-row_height*51/10);
		button2.addListener(new InputListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (text1b.getText().isEmpty() || text2b.getText().isEmpty() || text3b.getText().isEmpty() || text4b.getText().isEmpty()) {
					outputLabel.setText("One of the inputs is empty, cannot create Obstacle");
				}
				else {
					obstaclesNumber++;
					position = "Obstacle #" + obstaclesNumber + " = " + text1b.getText() + "  " + text2b.getText() + "  " + text3b.getText() + "  " + text4b.getText();
					outputLabel.setText(position);
					String ID = text1b.getText();
					String X = text2b.getText();
					String Y = text3b.getText();
					String Z = text4b.getText();
					Integer xf = Integer.parseInt(X);
					Integer yf = Integer.parseInt(Y);
					Integer zf = Integer.parseInt(Z);
					Integer IDf = 0;
					if (ID.equals("X") || ID.equals("x")) {
						IDf = (int) (2000 + x);
						x++;
					}
					else {
						IDf = Integer.parseInt(ID);
					}

					addToMap(oMap, IDf, xf, yf, zf);	
					if (DEBUG) System.out.println("obstacle loaded is " + oMap.get(IDf));

				}
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				outputLabel.setText("Pressed a Button");
				return true;
			}
		});
		stage.addActor(button2);
		/////////////////////////////////

		/////////////////////////////////@Starting Button
		Button button3 = new TextButton("Start \n Simulation", skin, "default");
		button3.setSize(col_width*4, row_height);
		button3.setPosition(col_width*105/10,Gdx.graphics.getHeight()-row_height*7);
		button3.addListener(new InputListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				outputLabel.setText("Press a Button");
				//reader.positionWriterM(mMap);
				//reader.positionWriterO(oMap);
				reader.setModules(mMap);
				reader.setObstacles(oMap);
				game.setScreen(new MainGame(game, grid));
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				outputLabel.setText("Pressed a Button");
				return true;
			}
		});
		stage.addActor(button3);
		/////////////////////////////////

		text1 = new TextField("", skin, "default");
		text1.setPosition(col_width*2,Gdx.graphics.getHeight()-row_height*3);
		text1.setSize(col_width*2, row_height);
		stage.addActor(text1);

		text2 = new TextField("", skin, "default");
		text2.setPosition(col_width*45/10,Gdx.graphics.getHeight()-row_height*3);
		text2.setSize(col_width*2, row_height);
		stage.addActor(text2);

		text3 = new TextField("", skin, "default");
		text3.setPosition(col_width*7,Gdx.graphics.getHeight()-row_height*3);
		text3.setSize(col_width*2, row_height);
		stage.addActor(text3);

		text4 = new TextField("", skin, "default");
		text4.setPosition(col_width*95/10,Gdx.graphics.getHeight()-row_height*3);
		text4.setSize(col_width*2, row_height);
		stage.addActor(text4);

		text1b = new TextField("", skin, "default");
		text1b.setPosition(col_width*2,Gdx.graphics.getHeight()-row_height*51/10);
		text1b.setSize(col_width*2, row_height);
		stage.addActor(text1b);

		text2b = new TextField("", skin, "default");
		text2b.setPosition(col_width*45/10,Gdx.graphics.getHeight()-row_height*51/10);
		text2b.setSize(col_width*2, row_height);
		stage.addActor(text2b);

		text3b = new TextField("", skin, "default");
		text3b.setPosition(col_width*7,Gdx.graphics.getHeight()-row_height*51/10);
		text3b.setSize(col_width*2, row_height);
		stage.addActor(text3b);

		text4b = new TextField("", skin, "default");
		text4b.setPosition(col_width*95/10,Gdx.graphics.getHeight()-row_height*51/10);
		text4b.setSize(col_width*2, row_height);
		stage.addActor(text4b);

		outputLabel = new Label("Press a Button",skin,"default");
		outputLabel.setSize(Gdx.graphics.getWidth(),row_height);
		outputLabel.setPosition(0,row_height);
		outputLabel.setAlignment(Align.center);
		stage.addActor(outputLabel);

	}

	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {

	}

	public void addToMap(Map<Integer,Vector3D> map, Integer ID, float x, float y, float z)
	{

		map.put(ID, new Vector3D(x, y, z));

	}








}
