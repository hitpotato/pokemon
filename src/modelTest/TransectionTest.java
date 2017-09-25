 package modelTest;

import static org.junit.Assert.*;

import org.junit.Test;
import model.*;

public class TransectionTest {

	@Test
	public void test() {
		Transection tran = new Transection();
		tran.getCapPokemon();
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

}
