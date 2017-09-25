package modelTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.Items;
import model.Map;
import model.Pokemon;
import model.Trainer;
import model.Transection;
import model.items.Bait;
import model.items.Rock;
import model.items.SafariBall;
import model.pokemons.Blastoise;
import model.pokemons.Charizard;
import model.pokemons.Doduo;
import model.pokemons.Kangaskhan;
import model.pokemons.Paras;
import model.pokemons.Pikachu;
import model.pokemons.Rhyhorn;
import model.pokemons.Tangela;
import model.pokemons.Venomoth;
import model.pokemons.Venusaur;

public class TestAll {

	@Test
	public void testBattleAction() {
		Transection tran = new Transection();
		tran.battleAction("bait");
		tran.doesBattle();
		tran.doesBattle();
		tran.doesBattle();
		tran.doesBattle();
		tran.doesBattle();
		tran.doesBattle();
		tran.battleAction("bait");
		tran.battleAction("ball");
		tran.battleAction("ball");
		tran.battleAction("rock");
		tran.battleAction("run");
		tran.doesBattle();
		tran.doesBattle();
		tran.doesBattle();
		tran.battleAction("Rock");
		tran.battleAction("Rock");
		tran.battleAction("Rock");
		tran.battleAction("ball");
		tran.battleAction("ball");
		tran.battleAction("ball");
	}
	@Test
	public void test1() {
		Transection tran = new Transection();
		tran.getVisiableZone();
		assertFalse(tran.isGameOver());
		Pokemon pok = tran.doesBattle();
		assertFalse(tran.isWin());
		while (true) {
			if (pok != null)
				break;
			pok = tran.doesBattle();
		}
		tran.battleAction("run");
		assertFalse(tran.isGameOver());
		assertFalse(tran.isWin());
		while (true) {
			if (pok != null)
				break;
			pok = tran.doesBattle();
		}

		tran.battleAction("Rock");
		assertFalse(tran.isGameOver());
		assertFalse(tran.isWin());
		while (true) {
			if (pok != null)
				break;
			pok = tran.doesBattle();
		}

		tran.battleAction("bait");
		assertFalse(tran.isGameOver());
		assertFalse(tran.isWin());
		
		tran.Move('E');
		tran.Move('W');
		tran.Move('S');
		tran.Move('N');
	}


	@Test
	public void testVisiableMap() {
		Map a = new Map();
		char[][] grid1 = a.visiableMap(49, 23);
		for (int i = 0; i < grid1.length; i++) {
			for (int j = 0; j < grid1[0].length; j++) {
				System.out.print(grid1[i][j]);
			}
			System.out.println("");
		}
		grid1 = a.visiableMap(49, 48);
		for (int i = 0; i < grid1.length; i++) {
			for (int j = 0; j < grid1[0].length; j++) {
				System.out.print(grid1[i][j]);
			}
			System.out.println("");
		}
		grid1 = a.visiableMap(0, 4);
		for (int i = 0; i < grid1.length; i++) {
			for (int j = 0; j < grid1[0].length; j++) {
				System.out.print(grid1[i][j]);
			}
			System.out.println("");
		}
	}
	@Test
	public void test2() {
		Map a = new Map();
		char[][] b = a.getCurrentMap();
		a.switchMap();
		assertFalse(b == a.getCurrentMap());
		a.switchMap();
		assertTrue(b == a.getCurrentMap());
		
	}


	@Test
	public void test3() {
		Pokemon a = new Blastoise();
		Pokemon b = new Charizard();
		Pokemon c = new Doduo();
		Pokemon d = new Kangaskhan();
		Pokemon e = new Paras();
		Pokemon f = new Pikachu();
		Pokemon g = new Rhyhorn();
		Pokemon h = new Tangela();
		Pokemon i = new Venomoth();
		Pokemon j = new Venusaur();
		assertFalse(a.doesCap());
		assertFalse(b.doesRun());
		c.newDuration();
		d.changeCapRate(-0.1);
		d.changeRate(-0.1);
		assertEquals("Tangela", h.getName());
		assertFalse(a.doesCap());
		
		Trainer t = new Trainer();
		t.getCapPokemon();
		t.getItemsLeft();
		t.getNumberOfBallsLeft();
		t.getNumberOfPokemon();
		t.getNumberOfStepsLeft();
		t.getCapPokemon();
		t.isWin();
		t.useABall();
		Items I = new Bait("a");
		
		Items I2 = new Rock("a");
		Items I3 = new SafariBall("a");
		t.useAItem(I);
		t.useAItem(I2);
		t.useAItem(I3);
		Transection tran = new Transection(); 
		tran.getNumberOfStepLeft();
		tran.getNumberOfPokemon();
		tran.battleAction("BALL");
		for (int k = 0; k < 500; k++) {
			t.capAPokemon(new Charizard());
			t.isBattling();
			t.getCapPokemon();
			t.getItemsLeft();
			t.getNumberOfBallsLeft();
			t.getNumberOfPokemon();
			t.getNumberOfStepsLeft();
			t.getCapPokemon();
			t.isWin();
			t.useABall();
			tran.getNumberOfStepLeft();
			tran.getNumberOfPokemon();
			tran.battleAction("BALL");
		}
		
		t.battle();
		for (int k = 0; k < 500; k++) {
			t.capAPokemon(new Pikachu());
			t.isBattling();
			t.getCapPokemon();
			t.getItemsLeft();
			t.getNumberOfBallsLeft();
			t.getNumberOfPokemon();
			t.getNumberOfStepsLeft();
			t.getCapPokemon();
			t.isWin();
			t.useABall();
			t.isMove();
			tran.getNumberOfStepLeft();
			tran.getNumberOfPokemon();
			tran.battleAction("BALL");
		}
		
		t.isMove();
		t.isGameOver();
	}
	
	@Test
	public void test4() {
		Transection tran = new Transection();
		tran.getVisiableZone();
		assertFalse(tran.isGameOver());
		Pokemon pok = tran.doesBattle();
		assertFalse(tran.isWin());
		while (true) {
			if (pok != null)
				break;
			pok = tran.doesBattle();
		}
		tran.battleAction("run");
		assertFalse(tran.isGameOver());
		assertFalse(tran.isWin());
		while (true) {
			if (pok != null)
				break;
			pok = tran.doesBattle();
		}
		while(pok == null) {
			tran.battleAction("Rock");
		}
		assertFalse(tran.isGameOver());
		assertFalse(tran.isWin());
		while (true) {
			if (pok != null)
				break;
			pok = tran.doesBattle();
		}
		while(pok == null)
			tran.battleAction("bait");
		assertFalse(tran.isGameOver());
		assertFalse(tran.isWin());
		
		tran.Move('E');
		tran.Move('W');
		tran.Move('S');
		tran.Move('N');
	}

	@Test
	public void test5() {
		Transection tran = new Transection();
		tran.getVisiableZone();
		assertFalse(tran.isGameOver());
		Pokemon pok = tran.doesBattle();
		assertFalse(tran.isWin());
		while (true) {
			if (pok != null)
				break;
			pok = tran.doesBattle();
		}
		tran.battleAction("run");
		assertFalse(tran.isGameOver());
		assertFalse(tran.isWin());
		while (true) {
			if (pok != null)
				break;
			pok = tran.doesBattle();
		}

		tran.battleAction("Rock");
		assertFalse(tran.isGameOver());
		assertFalse(tran.isWin());
		while (true) {
			if (pok != null)
				break;
			pok = tran.doesBattle();
		}

		tran.battleAction("bait");
		assertFalse(tran.isGameOver());
		assertFalse(tran.isWin());
		
		tran.Move('E');
		tran.Move('W');
		tran.Move('S');
		tran.Move('N');
	}
	
	@Test
	public void test6() {
		Transection tran = new Transection();
		tran.getNumberOfBaits();
		tran.getNumberOfPokemonBalls();
		tran.getNumberOfRock();
		Pokemon pok = tran.doesBattle();
		assertFalse(tran.isWin());
		while (true) {
			if (pok != null)
				break;
			pok = tran.doesBattle();
		}
		tran.getHPPercentage();
		tran.getHPColor();
		for(int i = 0; i < 3; i++ ) {
			tran.Move('N');
		}
		for(int i = 0; i < 10; i++ ) {
			tran.Move('W');
		}
		tran.setWinCondition(1);
		tran.getWinCondition();
		tran.addItems();
		tran.battleEndType();
		tran.getItemsLeft();
		tran.useBait();
		tran.useBall();
		tran.useRock();
	}

}// end class
