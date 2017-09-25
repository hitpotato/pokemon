package modelTest;

import static org.junit.Assert.*;

import org.junit.Test;

import model.*;
import model.items.*;
import model.pokemons.*;

public class TrainerTest {

	@Test
	public void test() {
		Trainer red = new Trainer();

		assertEquals(30, red.getNumberOfBallsLeft());
		red.useABall(); 
		assertEquals(29, red.getNumberOfBallsLeft());
		assertEquals(500, red.getNumberOfStepsLeft());
		assertEquals(0, red.getCapPokemon().size());
		assertEquals(0, red.getNumberOfPokemon());
		assertEquals(false, red.isBattling());
		red.useAItem(new Rock("Rock"));
		assertEquals(null, red.getItemsLeft().get("Rock"));
		red.battle();
		red.useAItem(new Rock("Rock"));
		red.useAItem(new Bait("Bait"));

		red.capAPokemon(new Charizard());
		red.leaveBattle();
		assertEquals(false, red.isGameOver());
		assertEquals(true, red.isMove());
		assertEquals(false, red.isWin());
		for (int i = 0; i < 80; i++) {
			// red.addItemsRandom();
			red.isMove();
			red.useABall();
		}
		assertEquals(false, red.isGameOver());
		red.useABait();
		red.useABall();
		red.useARock();
		red.getNumberOfBaitsLeft();
		red.getNumberOfItemsLeft();
		red.getNumberOfBallsLeft();
		red.getNumberOfRocksLeft();
		red.setWinCondition(1);
		red.isWin();
		// red.addItemsRandom();
		red.setWinCondition(0);
		// red.addItemsRandom();
		red.isWin();
		red.setWinCondition(1);
		assertEquals(red.getWinCondition(), 1);

		for (int i = 0; i < 51; i++) {
			// red.addItemsRandom();
			red.isMove();
			red.useABall();
		}

		// red.addItems(new Rock("Rock"), 1);
		red.isWin();

		red.isGameOver();
	}

}
