package model;

/*
 * file: Trainer.java
 * author: Long Chen
 * 
 * just trainer, which have the full ability that a trainer need
 */
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import model.items.*;

public class Trainer implements Serializable {

	//private DefaultListModel<Pokemon> pokemonListModel;
	//private DefaultListModel<Items> itemListModel;
	private Hashtable<Pokemon, Integer> pokemons;
	private Hashtable<Items, Integer> items;
	private int ballsLeft, stepsLeft;
	private int pokNum, winNum;
	private boolean isBattling, gameover, win;
	private SafariBall ball; 
	private Bait bait;
	private Rock rock;

	// win condition
	private int winCondition = -1;

	public Trainer() {
		pokemons = new Hashtable<Pokemon, Integer>();
		//pokemonListModel = new DefaultListModel<>();
		items = new Hashtable<Items, Integer>();

		//itemListModel = new DefaultListModel<>();
		
		ball = new SafariBall("SafariBall");
		rock = new Rock("Rock");
		bait = new Bait("Bait");
		initItems(ball, 30);
		initItems(bait, 10);// FIX ME: remove it
		initItems(rock, 10);// FIX ME: remove it
		//ballsLeft = 50;
		

		stepsLeft = 500;
		pokNum = 0;
		winNum = 5;
		isBattling = false;
		gameover = false;
		win = false;
	}
	private void initItems(Items item, int num) {
		items.put(item, num);	
//		for(int i = 0; i < num; i++)
//			itemListModel.addElement(item);
	}
	
	public void addItems(Items item, int num) {
		items.put(item, num + items.get(item));	
//		for(int i = 0; i < num; i++)
//			itemListModel.addElement(item);
	}
	
	/*
	 * return num of balls left
	 */
	public int getNumberOfItemsLeft(){
		return items.get(ball) + items.get(rock) + items.get(bait);
	}

	/*
	 * return num of balls left
	 */
	public int getNumberOfBallsLeft(){
		return items.get(ball);
	}
	
	/*
	 * return num of baits left
	 */
	public int getNumberOfBaitsLeft(){
		return items.get(bait);
	}
	
	/*
	 * return num of rocks left
	 */
	public int getNumberOfRocksLeft(){
		return items.get(rock);
	}
	
	/*
	 * return num of steps left
	 */
	public int getNumberOfStepsLeft(){
		return stepsLeft;
	}
	
	/*
	 * return pokemons captured
	 */
	public int getNumberOfPokemon(){
		return pokNum;
	}
	
	/*
	 * return hashtable of pokemons captured
	 */
	public Hashtable<Pokemon, Integer> getCapPokemon(){
		return pokemons;
	}
	
	/*
	 * return listmodel of pokemons captured
	 */
//	public DefaultListModel<Pokemon>  pokemonListModel(){
//		return pokemonListModel;
//	}
	
	/*
	 * get the hashtable of items, you can get a number of certain item left
	 */
	public Hashtable<Items, Integer> getItemsLeft(){
		return items;
	}
	
	/*
	 * get the defaultlistmodel for JList
	 */
//	public DefaultListModel<Items>  itemListModel(){
//		return itemListModel;
//	}
	
	/*
	 * check if win
	 */

	public boolean isWin() {
		if (this.winCondition == 0) {
			if (this.stepsLeft <= 0) {
				win = true;
			}
		} else if (this.winCondition == 1) {
			// this is where the number of balls are actually stored
			if (items.get(ball) <= 0) {
				win = true;
			}
		} else {
			// changed to what is store in pokemons
			if (pokemons.size() == 3) {
				win = true;
			}
		} // end if:0

		return win;
	}

	/*
	 * use a ball
	 */
	public boolean useABall(){
//		if(!(ballsLeft > 0))
//			return false;
		return useAItem(ball);
		//ballsLeft = items.get(new SafariBall("SafariBall"));
		
	}
	public boolean useARock(){
		return useAItem(rock);
		//ballsLeft = items.get(new SafariBall("SafariBall"));
		//return true;
	}
	public boolean useABait(){
		return useAItem(bait);
		//ballsLeft = items.get(new SafariBall("SafariBall"));
		//return true;
	}

	// because we only need implement bait so we use another versions
	public boolean useAItem(Items item) {
		if (items.get(item) == null || items.get(item) == 0)
			return false;
		items.put(item, items.get(item) - 1);
		System.out.println("Item: " + item.getName() + " " + items.get(item));
		//itemListModel.removeElement(item);
		return true;
	}
	
	/*
	 * cap a pokemon and update the list and hashtable
	 */

	public void capAPokemon(Pokemon pok) {
		pokNum++;
		if (!pokemons.containsKey(pok))
			pokemons.put(pok, 1);
		else
			pokemons.put(pok, pokemons.get(pok) + 1);
		//pokemonListModel.addElement(pok);
	}
	/*
	 * check if the player is battling
	 */
	public boolean isBattling(){
		return isBattling;
	}

	public void battle() {
		isBattling = true;
	}

	public void leaveBattle() {
		isBattling = false;
	}

	private void gameover() {
		gameover = true;
	}

	public boolean isGameOver() {
		return gameover;
	}

	/**
	 * 
	 * @return true, if the trainer can move. false, game over
	 */
	public boolean isMove() {
//		if (stepsLeft > 0) {
//			stepsLeft--;
//			return true;
//		}
		
		if (this.win == false) {
			stepsLeft--;
			this.isWin();
			return true;
		}

		gameover();
		return false;
	}

	/*
	 * As Trainer explore the map, Item is added 
	 * to trainer`s items
	 */
	public void addItemsRandom() {
		Random rand = new Random();
		int  numItemsAdded = rand.nextInt(5)+1;
		int  num = rand.nextInt(3);
		
		// items maybe add in
		//addItems(new SafariBall("SafariBall"), 50);
		//addItems(new Bait("Bait"), 10);// FIX ME: remove it
		//addItems(new Rock("Rock"), 10);// FIX ME: remove it
		
		if(this.stepsLeft % 20 == 19){
			if(this.winCondition == 1){
				if(num == 0){
					this.addItems(bait, numItemsAdded);
					JOptionPane.showMessageDialog(null,"You have found new Item!.\n"
							+ numItemsAdded +" bait(s) added to your Item List."); 
				}else{
					this.addItems(rock, numItemsAdded);
					JOptionPane.showMessageDialog(null,"You have found new Item!.\n"
							+ numItemsAdded +" rock(s) added to your Item List."); 
				}// end if:2
			}else { 
				if(num == 0){
					this.addItems(bait, numItemsAdded);
					JOptionPane.showMessageDialog(null,"You have found new Item!.\n"
							+ numItemsAdded +" bait(s) added to your Item List."); 
				}else if(num == 1){
					this.addItems(rock, numItemsAdded);
					JOptionPane.showMessageDialog(null,"You have found new Item!.\n"
							+ numItemsAdded +" rock(s) added to your Item List."); 
				}
//				}else{
//					this.addItems(ball, numItemsAdded);
//					JOptionPane.showMessageDialog(null,"You have found new Item!.\n"
//							+ numItemsAdded +" ball(s) added to your Item List."); 
//				}// end if:2
			}// end if:1
		}// end if: 0
		
	}

	/*
	 * Before starting the game, user is asked to choose the condition to end
	 * the game
	 * 
	 * There are 3 condtion
	 * 
	 * winCondtion = 0 if user chose "Win Condition 0: 500 steps"
	 * 
	 * winCondtion = 1 if user chose "Win Condition 1: out of ball"
	 * 
	 * winCondtion = 2 if user chose "Win Condition 2: Number of pokemon cached"
	 * 
	 * Base on this number the method this.isWin() is set(check isWin() for more
	 * details.
	 */
	public void setWinCondition(int conditionNum) {
		this.winCondition = conditionNum;
	}
	
	public int getWinCondition(){
		return this.winCondition;
	}

}// END CLASS
