package model.items;
import java.io.Serializable;

/*
 * file: Bait.java
 * author: Long Chen
 * 
 * a item, Luck
 */
import model.Items;

public class Bait extends Items implements Serializable {
	private double reduceRunRateBy, reduceCapRateBy;
	public Bait(String name) {
		super(name);
		reduceRunRateBy = 0.1;
		reduceCapRateBy = 0.1;
		// TODO Auto-generated constructor stub
	}
	public double getReduceRunRate(){
		return reduceRunRateBy;
	}
	
	public double getReduceCapRate(){
		return reduceCapRateBy;
	}

}
