package model.items;
/*
 * file: Rock.java
 * author: Long Chen
 * 
 * a item, rock
 */
import model.Items;

public class Rock extends Items{

	private double increaseRunRateBy, increaseCapRateBy;
	public Rock(String name) {
		super(name);
		increaseRunRateBy = 0.1;
		increaseCapRateBy = 0.1;
		// TODO Auto-generated constructor stub
	}
	public double getIncreaseRunRateBy(){
		return increaseRunRateBy;
	}
	public double getIncreaseCapRateBy(){
		return increaseCapRateBy;
	}
}
