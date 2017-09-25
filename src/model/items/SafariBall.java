package model.items;
/*
 * file: SafariBall.java
 * author: Long Chen
 * 
 * a item, ball
 */
import model.Items;

public class SafariBall extends Items{
	private double CapRateBy;
	public SafariBall(String name) {
		super(name);
		CapRateBy = 0.8;
		// TODO Auto-generated constructor stub
	}
	
	public double getReduceCapRate(){
		return this.CapRateBy;
	}

}
