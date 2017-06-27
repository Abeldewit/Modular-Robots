package com.group8.project;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.group8.project.screens.MainMenu;


public class ScreenDivide extends Game {

	static public Skin gameSkin;

	public void create () {
		gameSkin = new Skin(Gdx.files.internal("skin/metal-ui.json"));
		this.setScreen(new MainMenu(this));
	}

	public void render () {
		super.render();
	}
	

	public void dispose () {
	}
}