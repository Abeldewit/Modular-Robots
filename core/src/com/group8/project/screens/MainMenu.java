package com.group8.project.screens;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.plaf.FileChooserUI;

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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.group8.project.ScreenDivide;
import static com.group8.project.screens.MainGame.DEBUG;


public class MainMenu extends ApplicationAdapter implements Screen {


	private final JFileChooser fc;
	private Stage stage;
	private Game game;
	private Skin skin;
	private File tmp;
	//private File Mfile;
	private PositionManager reader;
	private Map<Float, Vector3> mMap;
	private Map<Float, Vector3> oMap;
	private Label outputLabel;
	private final boolean DEBUG = false;
	private SelectBox dropdown;



	public MainMenu(Game aGame) {

		fc = new JFileChooser();
		reader = new PositionManager();
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		game = aGame;

		///////////////////////////////////////////
		int row_height = Gdx.graphics.getWidth()/12;
		int col_width = Gdx.graphics.getHeight()/12;
		///////////////////////////////////////////
		
		skin = new Skin(Gdx.files.internal("skin/metal-ui.json"));

		/////////////////////////////////@Module Adding Button
		Button button1 = new TextButton("Read file \n with Modules", skin, "default");
		button1.setSize(col_width*3, row_height);
		button1.setPosition(col_width*3,Gdx.graphics.getHeight()-row_height*2);
		button1.addListener(new InputListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File tmp = fc.getSelectedFile();
					/*mMap = reader.startRead(tmp);
					completeModuleMap.putAll(mMap);
					 */
					reader.startRead(tmp, true);
					if(DEBUG) System.out.println(tmp.getAbsolutePath() + "WAS SELECTED FOR MODULES");

					outputLabel.setText("File was chosen");
				}
				else {
					outputLabel.setText("No File chosen");
				}

			}
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

				return true;
			}
		});
		stage.addActor(button1);
		/////////////////////////////////

		/////////////////////////////////@Obstacles Adding Button
		Button button3 = new TextButton("Read file \n with Obstacles", skin, "default");
		button3.setSize(col_width*3, row_height);
		button3.setPosition(col_width*3,Gdx.graphics.getHeight()-row_height*4);
		button3.addListener(new InputListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

				int returnVal = fc.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File tmp2 = fc.getSelectedFile();
					reader.startRead(tmp2, false);
					

					if(DEBUG) System.out.println(tmp2.getAbsolutePath() + "WAS SELECTED FOR OBSTACLES");

					outputLabel.setText("Obstacles File Loaded");
				}
				else {
					outputLabel.setText("No File chosen");
				}

			}
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

				return true;
			}
		});
		stage.addActor(button3);
		/////////////////////////////////

		/////////////////////////////////@Starting Button
		Button button2 = new TextButton("Start Simulation", skin, "default");
		button2.setSize(col_width*3, row_height);
		button2.setPosition(col_width*10,Gdx.graphics.getHeight()-row_height*2);
		button2.addListener(new InputListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				String gridSize = (String) dropdown.getSelected();
				game.setScreen(new MainGame(game, gridSize));
				outputLabel.setText("Modules File Loaded");
				
			}
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

				return true;
			}
		});
		stage.addActor(button2);
		/////////////////////////////////

		/////////////////////////////////@Changing to Manual Input
		Button button4 = new TextButton("Manual Input", skin, "default");
		button4.setSize(col_width*3, row_height);
		button4.setPosition(col_width*10,Gdx.graphics.getHeight()-row_height*4);
		button4.addListener(new InputListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				String gridSize = (String) dropdown.getSelected();
				game.setScreen(new Manual(game, gridSize));

			}
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

				return true;
			}
		});
		stage.addActor(button4);
		/////////////////////////////////
		
		/////////////////////////////////@Grid Size Button
		String[] values = {"25,25,25","50,50,50","75,75,75","100,100,100"};
		dropdown = new SelectBox(skin);
		dropdown.setItems(values);
		dropdown.setSelected("25,25,25");
		dropdown.setSize(col_width*3, row_height);
		dropdown.setPosition(col_width*5, Gdx.graphics.getHeight()-row_height*6);
		stage.addActor(dropdown);
		TextField grid = new TextField("Choose grid size", skin, "default");
		grid.setPosition(col_width*2,Gdx.graphics.getHeight()-row_height*6);
		grid.setSize(col_width*3, row_height);
		stage.addActor(grid);
		/////////////////////////////////

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
	public void hide() {
		// TODO Auto-generated method stub

	}



}
