package model;

import java.io.Serializable;
/*
 * file: Pokemon.java
 * author: hangchen
 * purpose: the superclass for all Pokemons
 */
import java.util.Random;

/*
 * file: Pokemon.java
 * author: Hang Chen
 * 
 * a superclass for all pokemon, it has much of function both useful for battle and trainer
 */
public abstract class Pokemon implements Serializable {
	private String name;
	private double rate;
	private int LimitedDurationForRun;
	private int capAbleHP;

	private int fullHP;
	private int currentHP;
	private int currDuration;
	private double runRate; // the poss to run, count 1 as max
	private double capRate;

	public Pokemon(String name, double rate, int fullHP, int captureHP, int limitedDuration, double runRate,
			double capRate) {
		this.name = name;
		this.rate = rate;
		this.fullHP = fullHP;
		this.currentHP = fullHP;
		this.capAbleHP = captureHP;
		this.LimitedDurationForRun = limitedDuration;
		this.runRate = runRate;
		this.capRate = capRate;
		this.currDuration = 0;
	}

	// start a new duration
	public void newDuration() {
		currDuration++;
		runRate += .03;
	}

	// return true if Pokemon want to run
	public boolean doesRun() {
		if (currDuration < LimitedDurationForRun)
			return false;
		Random ram = new Random();
		if (ram.nextInt(100) < 100 * (runRate))
			return true;
		return false;
	}

	// return true if Pokemon is captured
	public boolean doesCap() {
		if (currentHP > capAbleHP)
			return false;
		Random ram = new Random();

		if (ram.nextInt(100) < 100 * (capRate))
			return true;
		return false;
	}

	public String getName() {
		return name;
	}

	// ADD IN
	public double getRate() {
		return rate;
	}

	public int getLimitedDurationForRun() {
		return LimitedDurationForRun;
	}

	public int getCapAbleHP() {
		return capAbleHP;
	}

	public int getFullHP() {
		return fullHP;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public int getCurrDuration() {
		return currDuration;
	}

	public double getRunRate() {
		return runRate;
	}

	public double getCapRate() {
		return capRate;
	}
	// end ADD IN
	// it allows items to change rate of run
	public void changeRunRate(double change) {
		runRate += change;
	}

	// it allows items to change rate of capable rate
	public void changeCapRate(double change) {
		//System.out.println("caprate before: "+capRate);
		capRate += change;
		//System.out.println("after: "+capRate);
	}

	// it allows items to change Pokemon current HP
	public void changeHP(int change) {
		currentHP += change;
	}

	public void changeRate(double change) {
		this.rate += change;
	}

	public double getHPbyPercet() {
		//System.out.print("current Hp:"+currentHP+"\n");
		//System.out.print("full Hp:"+fullHP+"\n");
		double num = (double)currentHP/fullHP;
		//cost it to two decimal 
		int nums = (int)(num*10000);
		num = (double)nums/10000;
		return num;
	}

	public char getColor() {
		char color = ' ';
		if (currentHP > capAbleHP)
			color = 'g';
		else
			color = 'y';
		if (runRate > 0.5)
			color = 'r';
		return color;

	}
}
