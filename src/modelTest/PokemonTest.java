package modelTest;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Pokemon;
import model.pokemons.*;

public class PokemonTest {

	@Test
	public void test() {
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
		a.getHPbyPercet();
		c.getCapAbleHP();
		c.getCurrDuration();
		c.getFullHP();
		c.getRunRate();
		c.getCapRate();
		c.getCurrentHP();
		c.getLimitedDurationForRun();
		for(int bb = 0;bb < 100; bb++) {
			c.doesCap();
			c.doesRun();
			
			c.newDuration();
			c.getColor();
		}
		for(int bb = 0;bb < 100; bb++) {
			c.changeHP(-10);
			c.changeRunRate(.01);
			c.doesCap();
			c.doesRun();
			c.newDuration();
			c.getColor();
		}
	}

}
