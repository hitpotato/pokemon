package model;

/*
 * file: Transection.java
 * author: Hang Chen, Long Chen
 * 
 * a interface allow GUI to touch all model through it
 * it will automatically call others function, so the only thing GUI need, is this interface 
 */
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import model.items.Bait;
import model.pokemons.*;

public class Transection implements Serializable {
	private Map map;
	private Point position;
	private char[][] grid;
	private Trainer player;
	private Vector<Pokemon> PokemonList = new Vector<Pokemon>();

	private Pokemon pok = null; 
	private char[][] visiableZone;

	public Transection() {
		map = new Map();

		// all type of pokemons we have 
		PokemonList.add(new Kangaskhan());
		PokemonList.add(new Paras());
		PokemonList.add(new Rhyhorn());
		PokemonList.add(new Tangela());
		PokemonList.add(new Venomoth());
		PokemonList.add(new Pikachu());
		PokemonList.add(new Charizard());
		PokemonList.add(new Doduo());
		PokemonList.add(new Venusaur());
		PokemonList.add(new Blastoise());

		position = new Point(12, 23);

		position.x = 23;
		position.y = 23;

		visiableZone = map.visiableMap(position.x, position.y);
		grid = map.getCurrentMap();

		player = new Trainer();
	}

	// the place that in screen
	public char[][] getVisiableZone() {
		return visiableZone;
	}

	public int getNumberOfStepLeft() {
		return player.getNumberOfStepsLeft();
	}

	public int getNumberOfPokemon() {
		return player.getNumberOfPokemon();
	}
	public int getNumberOfBaits() {
		return player.getNumberOfBaitsLeft();
	}
	public int getNumberOfRock() {
		return player.getNumberOfRocksLeft();
	}
	// ADD IN
	public int getNumberOfPokemonBalls() {
		return player.getNumberOfBallsLeft();
	}

	public boolean isGameOver() {
		return player.isGameOver();
	}

	public boolean isWin() {
		return player.isWin();
	}

	public double getHPPercentage() {
		if (pok == null)
			return -1;
		return pok.getHPbyPercet();
	}

	/**
	 * 
	 * @return 'g' if the color is green 'y' if the color is yellow 'r' if the
	 *         color is red
	 */
	public char getHPColor() {
		return pok.getColor();
	}

	/**
	 * 
	 * @return Pokemon, GUI can use getName() or ""instanceof"" to find out what
	 *         this Pokemon is.
	 */
	public Pokemon doesBattle() {
		if (grid[position.x][position.y] != 'g')
			return null;
		boolean isBattle;
		isBattle = battleGenerator();
		// TODO: should build a battle rate generator. and pokemonsSelector
		if (isBattle) {
			pok = pokemonsSelector();
			player.battle();
			return pok;
		}
		return null;
	}

	/**
	 * @return Pokemon which appeared from the grass randomly by the weight.
	 */
	private Pokemon pokemonsSelector() {
		// TODO Auto-generated method stub
		Random r = new Random();
		ArrayList<Pokemon> list = new ArrayList<Pokemon>();

		for (Pokemon pok : PokemonList) {
			int num = (int) (pok.getRate() * 100);
			for (int i = 0; i < num; i++) {
				if (pok.getName().equals("Kangaskhan"))
					list.add(new Kangaskhan());
				if (pok.getName().equals("Paras"))
					list.add(new Paras());
				if (pok.getName().equals("Rhyhorn"))
					list.add(new Rhyhorn());
				if (pok.getName().equals("Tangela"))
					list.add(new Tangela());
				if (pok.getName().equals("Venomoth"))
					list.add(new Venomoth());
				if (pok.getName().equals("Pikachu"))
					list.add(new Pikachu());
				if (pok.getName().equals("Charizard"))
					list.add(new Charizard());
				if (pok.getName().equals("Doduo"))
					list.add(new Doduo());
				if (pok.getName().equals("Venusaur"))
					list.add(new Venusaur());
				if (pok.getName().equals("Blastoise"))
					list.add(new Blastoise());
			}
		}
		int index = r.nextInt(list.size());
		return list.get(index);
	}

	private boolean battleGenerator() {
		// TODO battle rate generator
		Random r = new Random();
		double rate = r.nextDouble();
		if (rate > 0.7)
			return true;
		return false;
	}

	private int battleEndType = -1;

	/**
	 * 
	 * @return return -1 if wrong call, 0 if trainer run 1 if Pok is captured, 2
	 *         if Pok is run away
	 */
	public int battleEndType() {
		if (pok != null)
			return -1;
		return battleEndType;
	}

	/**
	 * 
	 * @param command(must
	 *            be "run","ball","rock","specialItem"(for now, we just use
	 *            "bait")),
	 * @return 1 if end battle, 0 if still battle, -1 if no pokemon in battle
	 */
	public int battleAction(String command) {
		if (pok == null)
			return -1;
		pok.newDuration();
		battleEndType = -1;
		command = command.toUpperCase();
		if (command.equals("RUN")) {
			player.leaveBattle();
			pok = null;
			battleEndType = 0;
			return 1;
		}
		if (command.equals("BALL") && player.useABall()) {
			if (pok.doesCap()) {
				player.capAPokemon(pok);
				player.leaveBattle();
				pok = null;
				battleEndType = 1;
				return 1;
			}
			// pokemon around
			if (pok.doesRun()) {
				player.leaveBattle();
				battleEndType = 2;
				pok = null;
				return 1;
			}
			return 0;
		}
		if (command.equals("ROCK") && player.useARock()) {
			pok.changeHP(-10);
			pok.changeCapRate(-0.1);
			pok.changeRunRate(0.05);
			// pokemon around
			if (pok.doesRun()) {
				player.leaveBattle();
				battleEndType = 2;
				pok = null;
				return 1;
			}
		}

		if (command.equals("BAIT") && player.useABait()) {
			pok.changeCapRate(0.3);
			pok.changeRunRate(-0.1);
			// pokemon around
			if (pok.doesRun()) {
				player.leaveBattle();
				battleEndType = 2;
				pok = null;
				return 1;
			}
		}
		return 0;
	}

	/**
	 * give a char e.g. 'E' 'W' 'N' 'S' this function will check the eligibility
	 * first, and update all the change due to this move. So you should call
	 * getVisiableZone(), getPosInVisZone(),doesBattle(), isGameOver() and
	 * isWin() after that.
	 */
	public void Move(char direction) {
		int x = 0, y = 0;
		if (direction == 'E')
			x = 1;
		if (direction == 'W')
			x = -1;
		if (direction == 'S')
			y = 1;
		if (direction == 'N')
			y = -1;
		// check board
		if (position.x + x < 0 || position.y + y < 0 || position.x + x == grid.length
				|| position.y + y == grid[0].length)
			return;
		// if the moving direction is obstacle or another need to switch board.
		// then end move
		char nextStep = grid[position.x + x][position.y + y];
		// TODO: should check the switch board

		if (nextStep == 'D') {
			map.switchMap();
			position = map.getTranPos();
			grid = map.getCurrentMap();
			// this fixed the problem that switching map didn't work
			position.x = position.x + x;
			position.y = position.y + y;
			visiableZone = map.visiableMap(position.x, position.y);
			return;
		}
		if (nextStep == 'T' || nextStep == 't' || nextStep == 'w' || nextStep == 'o')
			return;
		position.x = position.x + x;
		position.y = position.y + y;
		visiableZone = map.visiableMap(position.x, position.y);

		// game over !
		if (!player.isMove()) {
			return;
		}

	}

	// ADD IN
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
	 * Base on this number the method isWin() in class Trainer.java is set
	 * (check isWin() for more details).
	 */
	public void setWinCondition(int winCondition) {
		player.setWinCondition(winCondition);
	}

	public int getWinCondition() {
		return player.getWinCondition();
	}

	// ADD IN
	// add in Item as Trainer explore the map
	public void addItems() {
		player.addItemsRandom();
	}

	/*
	 * get the hashtable of items, you can get a number of certain item left
	 */
	public Hashtable<Items, Integer> getItemsLeft() {
		return player.getItemsLeft();
	}

	/*
	 * return hashtable of pokemons captured
	 */
	public Hashtable<Pokemon, Integer> getCapPokemon() {
		return player.getCapPokemon();
	}

	// use an Ball
	public boolean useBall() {
		return player.useABall();
	}

	// use an Ball
	public boolean useBait() {
		return player.useABait();
	}

	// use an Ball
	public boolean useRock() {
		return player.useARock();
	}

}// END CLASS
