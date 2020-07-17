package com.gaosworld.ruddybit.data;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * 
 * @author Mikhael D'Amato
 * @version 1.0.0
 * 
 * */
public class GameData {
	
	private File file;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String path;
	private String directory;
	private Preferences preferences;
	
	public GameData() {

		if ( Gdx.app.getType() == Application.ApplicationType.Android ) {
			preferences = Gdx.app.getPreferences("myprefs");
		} else {
			directory = Gdx.app.getType() == Application.ApplicationType.Android ? "/ruddybit" : "/RuddyBit";
			path = Gdx.app.getType() == Application.ApplicationType.Android ? Gdx.files.local("").path() : System.getProperty("user.home");
			Gdx.app.log("DATA", path);

			File fileDir = new File(path + directory);

			// check dir and info dir
			if ( !fileDir.exists() ) {
				fileDir.mkdirs();
			}

			// create file
			file = new File(fileDir, "/data.save");
		}
		
	}
	
	public void save(Data data) {
		
		if ( Gdx.app.getType() == Application.ApplicationType.Android ) {
			int score = preferences.getInteger("score");
			score = data.getScore() > score ? data.getScore() : score;
			preferences.putInteger("score", score);
			preferences.flush();
		} else {
			try {
				saveInFile(data);
			} catch(IOException e) {
				try {
					file.delete();
					saveInFile(data);
				} catch (IOException e2) {
					e2.getStackTrace();
				}
			}
		}
		
	}
	
	public Object load() throws IOException, ClassNotFoundException {
		// create input
		Object object = null;

		if ( Gdx.app.getType() == Application.ApplicationType.Android) {
			object = new Data(preferences.getInteger("score"));
			preferences.flush();
		} else {
			if ( file.exists() ) {
				input = new ObjectInputStream(new FileInputStream(file));

				object = this.input.readObject();
				Gdx.app.log("DATA", "score: " + ( (Data) object).getScore() );
				input.close();
			}

			if ( object == null ) {
				object = new Data(0);
			}
		}

		return object;
	}
	
	public void saveInFile(Data data) throws IOException{
		
		FileOutputStream fileStream = new FileOutputStream(file);
		output = new ObjectOutputStream(fileStream);
		output.writeObject(data);
		output.close();
		fileStream.close();
		
	}

}
