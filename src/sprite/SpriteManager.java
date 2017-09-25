/** CSC 335 
 * Final Project: Pokemon Safari Zone
 * @Author: Thai Pham, Yang Yang Lu
 * 
 * This class represent the graphic view of trainer move
 *  */
package sprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * 
 * @author Yang Yang Lu
 *
 *         This is used to cut up the sprite sheet right now only for trainer.
 */

public class SpriteManager {

	private final int SIZE = 32; // 32 pixels

	private BufferedImage trainer = ImageIO.read(new File("images/trainerSprite.png"));

	private BufferedImage battle = ImageIO.read(new File("images/battleFieldGrass.png"));

	private BufferedImage pokemonField = ImageIO.read(new File("images/pokemonField.png"));

	private BufferedImage trainerField = ImageIO.read(new File("images/trainerField.png"));

	private BufferedImage trainerBackSprite = ImageIO.read(new File("images/trainerBack.png"));

	private BufferedImage pokemonSprite = ImageIO.read(new File("images/battlePokemonSprite.png"));

	private BufferedImage[][] trainerMove = new BufferedImage[4][4];

	private BufferedImage[] trainerBack = new BufferedImage[5];

	private BufferedImage[] battleSet = new BufferedImage[4];

	private HashMap<String, BufferedImage> pokemons = new HashMap<String, BufferedImage>();

	private String[] pokemonNames = { "blastoise", "charizard", "doduo", "kangaskhan", "paras", "pikachu", "rhyhorn",
			"tangela", "venomoth", "venusaur" };

	private BufferedImage battleActionSprite = ImageIO.read(new File("images/battleActionSprite.png"));

	private BufferedImage[][] actionMove = new BufferedImage[2][2];

	private BufferedImage[] chatSet = new BufferedImage[4];

	private BufferedImage pokeballSprite = ImageIO.read(new File("images/pokeballSprite.png"));

	private BufferedImage[] pokeballSet = new BufferedImage[4];

	private BufferedImage rock = ImageIO.read(new File("images/battleRock.png"));

	private BufferedImage bait = ImageIO.read(new File("images/bait.png"));

	private BufferedImage healthBarG = ImageIO.read(new File("images/healthBarGreen.png"));
	
	private BufferedImage healthBarY = ImageIO.read(new File("images/healthBarYellow.png"));
	
	private BufferedImage healthBarR = ImageIO.read(new File("images/healthBarRed.png"));
	
	private BufferedImage emptyBar = ImageIO.read(new File("images/emptyBar.png"));

	private int next = 0;
	private int trainerNext = 0;

	public BufferedImage getHealthBarY() {
		return healthBarY;
	}

	public BufferedImage getHealthBarR() {
		return healthBarR;
	}

	private int looping = 0;

	private int pokeballNext = 0;

	// can be used to cut any sprite sheet necessary
	public SpriteManager() throws IOException {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				trainerMove[x][y] = trainer.getSubimage(x * SIZE, y * SIZE, SIZE, SIZE);
			}
		}

		for (int i = 0; i < 5; i++) {
			trainerBack[i] = trainerBackSprite.getSubimage(i * SIZE * 2 + 8 * i, 8, SIZE * 2, SIZE * 2);
		}

		for (int i = 0; i < 10; i++) {
			pokemons.put(pokemonNames[i], pokemonSprite.getSubimage(i * SIZE * 2, 0, SIZE * 2, SIZE * 2));
		}

		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 2; x++) {
				// size of menu (120, 48)
				actionMove[x][y] = battleActionSprite.getSubimage(x * 120, y * 48, 120, 48);
			}
		}

		for (int i = 0; i < 4; i++) {
			pokeballSet[i] = pokeballSprite.getSubimage(i * 12, 0, 12, 16);
		}

		battleSet[0] = ImageIO.read(new File("images/battleFieldFrame1.png"));
		battleSet[1] = ImageIO.read(new File("images/battleFieldFrame2.png"));
		battleSet[2] = ImageIO.read(new File("images/battleFieldFrame3.png"));
		battleSet[3] = battle;

		chatSet[0] = ImageIO.read(new File("images/battleChatFrame1.png"));
		chatSet[1] = ImageIO.read(new File("images/battleChatFrame2.png"));
		chatSet[2] = ImageIO.read(new File("images/battleChatFrame3.png"));
		chatSet[3] = ImageIO.read(new File("images/battleChat.png"));
	}

	// _________________________________
	// getters below

	public BufferedImage[][] getActionMove() {
		return actionMove;
	}

	// called together with getNextBattleFrame()
	public BufferedImage getNextBattleChatFrame() {
		return chatSet[next];
	}

	public BufferedImage getTrainer() {
		return trainer;
	}

	public BufferedImage[][] getTrainerMove() {
		return trainerMove;
	}

	public int getSIZE() {
		return SIZE;
	}

	public BufferedImage getNextBattleFrame() {
		BufferedImage temp = battleSet[next];
		next++;
		if (looping < 6 && next == battleSet.length - 1) {
			next = 0;
			looping++;
		} else if (next == battleSet.length) {
			next--;
		}
		return temp;
	}

	public void resetBattleFrame() {
		next = 0;
		looping = 0;
	}

	public boolean isLastFrame() {
		return next == battleSet.length - 1;
	}

	public BufferedImage getPokemonField() {
		return pokemonField;
	}

	public BufferedImage getTrainerField() {
		return trainerField;
	}

	public BufferedImage getTrainerBackFirstFrame() {
		return trainerBack[0];
	}

	public BufferedImage getTrainerBackNextFrame() {
		if (trainerNext >= trainerBack.length) {
			return trainerBack[0];
		}

		BufferedImage temp = trainerBack[trainerNext];
		trainerNext++;

		return temp;
	}

	public void resetTrainerBackFrame() {
		trainerNext = 0;
	}

	public void resetpokeballFrame() {
		pokeballNext = 0;
	}

	public BufferedImage getPokemon(String name) {
		return pokemons.get(name);
	}

	public BufferedImage getPokeballFirstFrame() {
		return pokeballSet[0];
	}

	public BufferedImage getPokeballNextFrame() {
		if (pokeballNext >= pokeballSet.length) {
			return pokeballSet[0];
		}

		BufferedImage temp = pokeballSet[pokeballNext];
		pokeballNext++;

		return temp;
	}

	public BufferedImage getRock() {
		return rock;
	}

	public BufferedImage getBait() {
		return bait;
	}

	public BufferedImage getHealthBarG() {
		return healthBarG;
	}

	public BufferedImage getEmptyBar() {
		return emptyBar;
	}
}
