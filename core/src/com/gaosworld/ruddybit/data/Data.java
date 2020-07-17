package com.gaosworld.ruddybit.data;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 
 * @author Mikhael D'Amato
 * @version 1.0.0
 * 
 * */
public class Data implements Serializable {
	
	public static final long serialVersionUID = 1L;

	private int score;
	public Data(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}
}

