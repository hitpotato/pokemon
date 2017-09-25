package model;

import java.io.Serializable;

/*
 * file: model.java
 * author: Long Chen
 * 
 * Item,  just a superclass represent all item like bait, rock and ball
 */
public abstract class Items implements Serializable{
	private String name;
	
	public Items(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

}
