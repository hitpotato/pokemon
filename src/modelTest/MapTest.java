package modelTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;

import model.Map;

public class MapTest {

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
		Point point = a.getTranPos();
	}

	@Test
	public void test1() {
		Map a = new Map();
	}
	@Test
	public void test() {
		Map a = new Map();
		char[][] b = a.getCurrentMap();
		a.switchMap();
		assertFalse(b == a.getCurrentMap());
		a.switchMap();
		assertTrue(b == a.getCurrentMap());
		
	}
	
}
