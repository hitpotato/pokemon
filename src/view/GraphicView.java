/** CSC 335 
 * Final Project: Pokemon Safari Zone
 * @Author: Long Chen, Thai Pham, Yang Yang Lu
 * 
 * This class represent the graphic view of the game and calls
 * model methods to update and check win conditions.
 *  */

package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import model.Items;
import model.Pokemon;
import model.Transection;
import songplayer.EndOfSongEvent;
import songplayer.EndOfSongListener;
import songplayer.SongPlayer;
import sprite.SpriteManager;

//public class GraphicView extends JPanel implements Observer {
public class GraphicView extends JPanel {

	// State: what object have
	private Transection _game;
	private char[][] _graphicBoard;
	private BufferedImage _ground;
	private BufferedImage _smallTree;
	private BufferedImage _bigTree;
	private BufferedImage _water;
	private BufferedImage _wall;
	private BufferedImage _trainer;
	private BufferedImage _grass;
	private int _size;
	private int width;
	private int height;

	// use for animation purpose
	private int move = 0; // move is direction
	private int nextFrame = 0; // next frame of the animation
	private int walkCount = 0;
	private int offsetX = 0;
	private int offsetY = 0;
	// camera need offset to not see un-rendered area
	private int camOffset = 32;

	// localTime is used as speed for moving animation
	private LocalTime lt = LocalTime.now();
	// used for cutting the sprite sheet
	private SpriteManager sm = new SpriteManager();
	// used to calculate when to move trainer in the char[][] board
	private Point trainerPos = null;
	private Point trainerPosOld = null;
	// used alone with trainerPos and trainerPosOld to give a new position for
	// trainer
	private boolean changed = true;
	private final JPanel panelInformation = new JPanel();
	private final JLabel lblDisplayNumOfStepLeft = new JLabel("Number of Steps Left:");
	private final JLabel lblNumberOfStepLeft = new JLabel("10");
	private final JLabel lblDisplayNumberOfPokemon = new JLabel("             Number of Pokemon: ");
	private final JLabel lblNumPokemon = new JLabel("New label");
	private Timer timer;
	private Timer bgmTimer;
	private boolean isMove = false;
	private boolean isBattle = false;
	// private int battleCount = 0;

	private Pokemon battlePokemon = null;

	private Timer battleTimer;
	// private int battleOpening = 0;

	private int battleOffsetX = 240 * 3;
	// private int battleOffsetY = 112 * 3;

	private int actionX = 0;
	private int actionY = 0;

	private boolean isBattleAction = false;

	private int battleActionCounter = 0;

	private boolean justPressed = false;

	// throw pokeball
	private int throwOffsetX = 20 + sm.getPokeballFirstFrame().getWidth() * 3 / 2;
	private int throwOffsetY = sm.getPokeballFirstFrame().getHeight() * 3 / 2;

	// bounce up and down
	private int bounceDown = 0;
	private int bounceUp = 0;
	private int bounceCount = 0;

	// shrink pokemon
	private int shrinkX = 0;
	private int shrinkY = 0;
	private boolean notCaught = false;

	// these are start up 2 points of the battle field in pixels
	// . <- (startX, startY)
	//
	//
	// >>>>>>>>>>>> . <- (endX, endY)
	// these two points makes the battle field
	private final int startX;
	private final int startY;
	private final int endX;
	private final int endY;

	// this is what changes the size of the battle field
	// personally believe 3 is good to go.
	private final int pixelMultiplier = 3;

	private boolean musicLoop = false;

	private String msg = null;

	// ADD IN
	private final JLabel lblDisPlayNumberOfPokemonBalls = new JLabel("             Number of Pokemon Balls: ");
	private final JLabel lblNumberOfPokemonBalls = new JLabel("");
	private final JPanel panelList = new JPanel();
	private final JButton btnPokemonList = new JButton("PokemonList");
	private final JButton btnItemList = new JButton("Item List");
	private final JButton btnIncreaseHP = new JButton("Reduce HP of wild Pokemon in Battle");

	// constructor ()
	public GraphicView(Transection game, int width, int height) throws IOException {
		// check for existing image
		try {
			this._ground = ImageIO.read(getClass().getResourceAsStream("/emptyGround.png"));
			this._smallTree = ImageIO.read(getClass().getResourceAsStream("/tree.png"));

			this._water = ImageIO.read(getClass().getResourceAsStream("/water.png"));
			this._wall = ImageIO.read(getClass().getResourceAsStream("/fire.png"));
			this._trainer = ImageIO.read(getClass().getResourceAsStream("/trainer.png"));
			this._grass = ImageIO.read(getClass().getResourceAsStream("/grass.png"));
			// FIX ME: need big tree pic
			this._bigTree = ImageIO.read(getClass().getResourceAsStream("/tree.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} // end try: check existing image

		// initialize variables
		this._game = game;
		this.width = width;
		this.height = height;

		// initialize points for battle view
		startX = (width - 240 * 3) / 2;
		startY = (height - 160 * 3) / 2;
		endX = 240 * 3;
		endY = 112 * 3;

		// get the visible part of the map
		_graphicBoard = _game.getVisiableZone();

		// check to see if the game should load saved data
		loadCheck();

		this.setupPanelInformation();// add in

		// add keyListener to listen for input
		this.addKeyListener(new KeyboardListener());

		// 0.05
		timer = new Timer(50, new TimeListener());
		timer.start();

		// play once then play in timer
		// SongPlayer.playFile(new SongWaiter(),
		// "./musics/pokemonBGM50%Volume.wav");

		bgmTimer = new Timer(0, new MusicListener());
		bgmTimer.start();

		battleTimer = new Timer(50, new BattleListener());
		// battleTimer.start();
	}// end constructor()

	// set up panel display number of step left
	private void setupPanelInformation() {
		// steps counter
		add(panelInformation);
		lblDisplayNumOfStepLeft.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelInformation.add(lblDisplayNumOfStepLeft);
		lblNumberOfStepLeft.setText("" + _game.getNumberOfStepLeft());
		lblNumberOfStepLeft.setForeground(Color.RED);
		lblNumberOfStepLeft.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelInformation.add(lblNumberOfStepLeft);
		lblDisplayNumberOfPokemon.setFont(new Font("Tahoma", Font.BOLD, 13));

		// number of pokemon
		panelInformation.add(lblDisplayNumberOfPokemon);
		lblNumPokemon.setForeground(Color.RED);
		lblNumPokemon.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelInformation.add(lblNumPokemon);
		lblNumPokemon.setText("" + _game.getNumberOfPokemon());

		// pokemon balls
		lblDisPlayNumberOfPokemonBalls.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelInformation.add(lblDisPlayNumberOfPokemonBalls);

		lblNumberOfPokemonBalls.setForeground(Color.RED);
		lblNumberOfPokemonBalls.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNumberOfPokemonBalls.setText("" + _game.getNumberOfPokemonBalls());
		panelInformation.add(lblNumberOfPokemonBalls);

		add(panelList);
		btnPokemonList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Set<Pokemon> items = _game.getCapPokemon().keySet();
				ArrayList<String> itemName = new ArrayList<>();
				ArrayList<Integer> itemCount = new ArrayList<>();
				for (Pokemon aItem : items) {
					itemName.add(aItem.getName());
					itemCount.add(_game.getCapPokemon().get(aItem));
					System.out.println(aItem.getName() + ": " + _game.getCapPokemon().get(aItem));
				} // end for loop

				Hashtable<String, Integer> itemsHash = new Hashtable<String, Integer>();
				for (int i = 0; i < itemCount.size(); i++) {
					if (itemsHash.containsKey(itemName.get(i))) {
						itemsHash.put(itemName.get(i), itemsHash.get(itemName.get(i)) + itemCount.get(i));
					} else {
						itemsHash.put(itemName.get(i),  itemCount.get(i));
					}
				} // end for:0

				Set<String> items1 = itemsHash.keySet();

				String str = "";
				for (String aItem1 : items1) {
					str += aItem1 + ":    " + itemsHash.get(aItem1) + "\n";
				}

				if (str.equals("")) {
					str = "Trainer has not had any Pokemon yet!";
				}
				JOptionPane.showMessageDialog(null, "Trainer`s Pokemon List:\n" + str);
			}

		});
		btnPokemonList.setFocusable(false);

		panelList.add(btnPokemonList);
		btnItemList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Set<Items> items = _game.getItemsLeft().keySet();
				ArrayList<String> itemName = new ArrayList<>();
				ArrayList<Integer> itemCount = new ArrayList<>();
				for (Items aItem : items) {
					itemName.add(aItem.getName());
					itemCount.add(_game.getItemsLeft().get(aItem));
					System.out.println(aItem.getName() + ":  " + _game.getItemsLeft().get(aItem));
				} // end for loop

				Hashtable<String, Integer> itemsHash = new Hashtable<String, Integer>();
				for (int i = 0; i < itemCount.size(); i++) {
					if (itemsHash.containsKey(itemName.get(i))) {
						itemsHash.put(itemName.get(i), itemsHash.get(itemName.get(i)) + itemCount.get(i));
					} else {
						itemsHash.put(itemName.get(i), itemCount.get(i));
					}
				} // end for:0

				Set<String> items1 = itemsHash.keySet();

				String str = "";
				for (String aItem1 : items1) {
					str += aItem1 + ":     " + itemsHash.get(aItem1) + "\n";
				}

				JOptionPane.showMessageDialog(null, "Trainer`s Item List:\n" + str);
			}
		});
		btnItemList.setFocusable(false);

		panelList.add(btnItemList);
		btnIncreaseHP.setFocusable(false);
		btnIncreaseHP.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (battlePokemon != null) {
					battlePokemon.changeHP(-10);

					System.out.println("Pokemon name: " + battlePokemon.getName());

					// ADD IN
					// getHPbyPercet = this.currentHP/this.fullHP
					System.out.println("Pokemon Full HP = " + battlePokemon.getFullHP());
					System.out.println("Pokemon current HP = " + battlePokemon.getCurrentHP());
					System.out.println(
							"Pokemon HP by percent = this.currentHP/this.fullHP = " + battlePokemon.getHPbyPercet());
					System.out.println("Decreased HP by 10. The NEW HP = " + battlePokemon.getCurrentHP());

				}

				// JOptionPane.showMessageDialog(null,
				// "FIX ME: For now, I increase HP of the battle pokemon(check
				// console for details), Not out pokemon!\n"
				// + "This button is uses when batle with other trainer\n"
				// + "Need to add a if stament for the condition the that make
				// the button stop working when => full HP");
			}
		});

		panelList.add(btnIncreaseHP);

	}

	// ask the user if the saved game should be loaded
	private void loadCheck() {
		int userInput = JOptionPane.showConfirmDialog(null, "Do you want to load game");
		if (userInput == JOptionPane.YES_OPTION) {
			ObjectInputStream load = null;

			// loads necessary data
			try {
				load = new ObjectInputStream(new FileInputStream("savedData"));
				_game = (Transection) load.readObject();
				load = new ObjectInputStream(new FileInputStream("savedData1"));
				move = (int) load.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (userInput == JOptionPane.CANCEL_OPTION) {
			System.exit(0);
		} else if (userInput == JOptionPane.NO_OPTION) {
			// ADD IN: win conditions
			this.winCondtions();
		}

	}

	/*
	 * This method will set game win condition
	 */
	/*
	 * i in the for loop: represent which win condition we have chosen
	 * 
	 * @TO DO: need to add in the somclass.java (trainer maybe) a control
	 * variable to set win conditions.
	 * 
	 * int winCondtion = 0 "Win Condition 0: 500 steps"
	 * 
	 * winCondtion = 1 "Win Condition 1: out of ball"
	 * 
	 * winCondtion = 2 "Win Condition 2: Number of pokemon cached"
	 */
	private void winCondtions() {
		ButtonGroup gWinCondtions = new ButtonGroup();
		JRadioButton[] rbWinCondtion = new JRadioButton[3];
		JPanel pWinConstions = new JPanel(new GridLayout(3, 1));
		rbWinCondtion[0] = new JRadioButton("Win Condition 0: 500 steps.");
		gWinCondtions.add(rbWinCondtion[0]);
		pWinConstions.add(rbWinCondtion[0]);
		rbWinCondtion[1] = new JRadioButton("Win Condition 1: Out of balls.");
		gWinCondtions.add(rbWinCondtion[1]);
		pWinConstions.add(rbWinCondtion[1]);
		rbWinCondtion[2] = new JRadioButton("Win Condition 2:  Number of pokemons catched = 3");
		gWinCondtions.add(rbWinCondtion[2]);
		pWinConstions.add(rbWinCondtion[2]);

		JOptionPane.showMessageDialog(null, pWinConstions);

		for (int i = 0; i < 3; i++) {
			if (rbWinCondtion[i].isSelected()) {
				System.out.println("Win Condition: " + i);
				this._game.setWinCondition(i);
			}
		}

		// if user have not choose the win condition
		// set win condition to default 500 steps
		if (_game.getWinCondition() == -1) {
			JOptionPane.showMessageDialog(null,
					"You have not choose, win condtion!\n" + "Therefore,  win condition is set to default.\n"
							+ "The game will end when Trainer reachs 500 steps.");
			this._game.setWinCondition(0);
		}

	}

	// initialize the graphic view
	// and update when needed
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

//		if (!_game.isGameOver()) {
			// this is battle view
			if (isBattle) {
				try {
					inBattleActions(g2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				if (msg != null) {
					JOptionPane.showMessageDialog(null, msg);
					msg = null;
//					if (_game.isWin()) {
//						System.exit(0);
//					}
				}
				inMapActions(g2);
			} // loop bracket for else statement up top.
//		}

	}// End painComponent

	/*
	 * MusicListener - private class
	 * 
	 * this is used to listen for time in a new thread, whenever the bgm have
	 * finished playing it plays another one.
	 */
	private class MusicListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("in timer");
			if (!isBattle) {
				System.out.println("is In map");
				bgmTimer.stop();
				// SongPlayer.stop();
				// SongPlayer.stop("./musics/BattleBGM.mp3");
				// 57.0 seconds is the delay for mapBGM.
				SongPlayer.playMap(new SongWaiter(), "./musics/pokemonBGM50%Volume.wav");
				bgmTimer.setDelay(57000);
				bgmTimer.start();
				System.out.println("Delay: " + bgmTimer.getDelay());
			} else {
				System.out.println("is In battle");
				bgmTimer.stop();
				// SongPlayer.stop();
				// SongPlayer.stop("./musics/pokemonBGM.mp3");
				if (!musicLoop) {
					// 115.0 seconds is the delay for BattleBGM
					SongPlayer.playBattle(new SongWaiter(), "./musics/BattleBGM50%Volume.wav");
					bgmTimer.setDelay(115000);
					musicLoop = true;
				} else {
					// 112.0 seconds is the delay for BattleBGM
					SongPlayer.playBattle(new SongWaiter(), "./musics/BattleBGM50%VolumeLoop.wav");
					bgmTimer.setDelay(112000);
				}
				bgmTimer.start();
				System.out.println("Delay: " + bgmTimer.getDelay());
			}
		}
	}

	/*
	 * BattleListener - private class
	 * 
	 * this is used to listen for time in a new thread.
	 */
	private class BattleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (isBattle) {
				if (isBattleAction) {
					// 0.1 second
					// for battle actions
					battleTimer.setDelay(100);
				} else {
					// 0.05 second
					// for opening the battle
					battleTimer.setDelay(50);
				}
				repaint();
				if (battleOffsetX > 0) {
					battleOffsetX -= 20;
				}

				// action ends
				if (battleActionCounter >= 100 && _game.battleEndType() != 1) {
					resetBattleActions();
				}
			}
			// if (battleOpening == 4) {
			// battleTimer.stop();
			// }
		}

		private void resetBattleActions() {
			battleActionCounter = 0;
			sm.resetTrainerBackFrame();
			sm.resetpokeballFrame();
			throwOffsetX = 20 + sm.getPokeballFirstFrame().getWidth() * 3 / 2;
			throwOffsetY = sm.getPokeballFirstFrame().getHeight() * 3 / 2;
			shrinkX = 0;
			shrinkY = 0;
			bounceUp = 0;
			bounceDown = 0;
			bounceCount = 0;
			isBattleAction = false;
			notCaught = false;
		}

	}

	// all battle actions are performed here
	private void inBattleActions(Graphics2D g2) throws IOException {
		// here is after the opening and listening to input is available
		if (sm.isLastFrame()) {
			waitForActionsInBattle(g2);
		} else {
			openTheBattle(g2);
		}

		// only when the action animation is finished then check on battle end
		// type or when no action is taken
		if (battleActionCounter >= 100 || battleActionCounter == 0) {
			// these checks for ending battle conditions
			if (_game.battleEndType() == 0) {
				System.out.println("trainer run away");
				msg = "trainer run away";
				isBattle = false;
				battleOffsetX = 240 * 3;
				sm.resetBattleFrame();
				actionX = 0;
				actionY = 0;
				bgmTimer.restart();
				musicLoop = false;
				battleTimer.stop();
				repaint();
			} else if (_game.battleEndType() == 1) {
				System.out.println("pokemon caught");
				msg = battlePokemon.getName() + " is captured!";
				isBattle = false;
				battleOffsetX = 240 * 3;
				sm.resetBattleFrame();
				actionX = 0;
				actionY = 0;
				bgmTimer.restart();
				musicLoop = false;
				battleTimer.stop();
				repaint();
			}
			// trigger run away when health is gone
			else if (_game.battleEndType() == 2 || _game.getHPPercentage() <= 0) {
				System.out.println("pokemon run away");
				msg = battlePokemon.getName() + " Run Away!";
				isBattle = false;
				battleOffsetX = 240 * 3;
				sm.resetBattleFrame();
				actionX = 0;
				actionY = 0;
				bgmTimer.restart();
				musicLoop = false;
				battleTimer.stop();
				SongPlayer.playFile(new SongWaiter(), "musics/RunAway150%Volume.wav");
				repaint();
			}

			// This is moved to make sure it only updates after an action
			// animation is over
			// ADD IN
			lblNumPokemon.setText("" + _game.getNumberOfPokemon());

			if (_game.isWin()) {
				isBattle = false;
				battleTimer.stop();
				repaint();
			} // end if:0
		}
	}

	// this is where it waits for input from the keyboard for certain actions
	private void waitForActionsInBattle(Graphics2D g2) {
		int startX = this.startX;
		int startY = this.startY;
		int endX = this.endX;
		int endY = this.endY;

		// this calculates the starting x position of action menu
		int startXAM = startX + endX - sm.getActionMove()[actionX][actionY].getWidth() * 3;

		// calculation for trainer grass field starting x and y
		int startXTF = startX + battleOffsetX;
		int startYTF = startY + endY - sm.getTrainerField().getHeight() * pixelMultiplier;

		// calculation for pokemon grass field starting x and y
		int startXPF = startX + endX - sm.getPokemonField().getWidth() * pixelMultiplier - battleOffsetX;
		int startYPF = startY + 44 * pixelMultiplier;

		// calculation for the offset for pokemon in middle of grass field
		int offsetXPOG = (sm.getPokemonField().getWidth() * pixelMultiplier
				- sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier) / 2;
		int offsetYPOG = sm.getPokemonField().getHeight() * pixelMultiplier
				- sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier;

		// draw battle chat box
		g2.drawImage(sm.getNextBattleChatFrame(), startX, startY + endY,
				sm.getNextBattleChatFrame().getWidth() * pixelMultiplier,
				sm.getNextBattleChatFrame().getHeight() * pixelMultiplier, null);

		// draw action menu
		g2.drawImage(sm.getActionMove()[actionX][actionY], startXAM, startY + endY,
				sm.getActionMove()[actionX][actionY].getWidth() * 3,
				sm.getActionMove()[actionX][actionY].getHeight() * 3, null);

		// draw the battle field
		g2.drawImage(sm.getNextBattleFrame(), startX, startY, endX, endY, null);

		try {
			if (battlePokemon.getColor() == 'g') {
				// draw the health bar Green
				g2.drawImage(sm.getHealthBarG(), startX + 60, startYPF + offsetYPOG,
						sm.getHealthBarG().getWidth() * pixelMultiplier,
						sm.getHealthBarG().getHeight() * pixelMultiplier, null);
			} else if (battlePokemon.getColor() == 'y') {
				// draw the health bar Yellow
				g2.drawImage(sm.getHealthBarY(), startX + 60, startYPF + offsetYPOG,
						sm.getHealthBarY().getWidth() * pixelMultiplier,
						sm.getHealthBarY().getHeight() * pixelMultiplier, null);
			} else if (battlePokemon.getColor() == 'r') {
				// draw the health bar Yellow
				g2.drawImage(sm.getHealthBarR(), startX + 60, startYPF + offsetYPOG,
						sm.getHealthBarR().getWidth() * pixelMultiplier,
						sm.getHealthBarR().getHeight() * pixelMultiplier, null);
			}
		} catch (NullPointerException e) {
			System.out.println("getHPColor throw exception");
		 	}

		if (battlePokemon.getHPbyPercet() < 0) {
			g2.drawImage(sm.getEmptyBar(),
					(startX + 60 + (sm.getHealthBarG().getWidth() - 2) * pixelMultiplier)
							- (int) (((sm.getHealthBarG().getWidth() - 18) * pixelMultiplier)),
					startYPF + offsetYPOG + 6, (int) ((sm.getHealthBarG().getWidth() - 18) * pixelMultiplier),
					sm.getEmptyBar().getHeight() * pixelMultiplier, null);
		} else {
			// draw the empty bar
			g2.drawImage(sm.getEmptyBar(),
					(startX + 60 + (sm.getHealthBarG().getWidth() - 2) * pixelMultiplier)
							- (int) (((sm.getHealthBarG().getWidth() - 18) * pixelMultiplier)
									* (1 - battlePokemon.getHPbyPercet())),
					startYPF + offsetYPOG + 6,
					(int) ((sm.getHealthBarG().getWidth() - 18) * pixelMultiplier
							* (1 - battlePokemon.getHPbyPercet())),
					sm.getEmptyBar().getHeight() * pixelMultiplier, null);
		}

		// draw trainer's grass field
		g2.drawImage(sm.getTrainerField(), startXTF, startYTF, sm.getTrainerField().getWidth() * pixelMultiplier,
				sm.getTrainerField().getHeight() * pixelMultiplier, null);

		// draw pokemon's grass field
		// 45 pixels is where the pokemonField was on in the actual image
		g2.drawImage(sm.getPokemonField(), startXPF, startYPF, sm.getPokemonField().getWidth() * pixelMultiplier,
				sm.getPokemonField().getHeight() * pixelMultiplier, null);

		// white cover
		g2.setColor(Color.WHITE);
		g2.fillRect(endX + startX, startY, startX, endY + sm.getNextBattleChatFrame().getHeight() * pixelMultiplier);
		g2.fillRect(0, startY, startX, endY + sm.getNextBattleChatFrame().getHeight() * pixelMultiplier);

		// perform actions
		if (isBattleAction && battleActionCounter < 100) {
			if (actionX == 0 && actionY == 0) {
				throwBall(g2);
			} else if (actionX == 1 && actionY == 0) {
				throwBait(g2);
			} else if (actionX == 0 && actionY == 1) {
				throwRock(g2);
			}
		}
		// if no action is performed just draw trainer and pokemon with no
		// actions
		else {
			// draw pokemon
			g2.drawImage(sm.getPokemon(battlePokemon.getName().toLowerCase()), startXPF + offsetXPOG,
					startYPF + offsetYPOG,
					sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier,
					sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier, null);

			// draw trainer
			g2.drawImage(sm.getTrainerBackFirstFrame(),
					startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
							- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - 36,
					startY + endY - sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier,
					sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier,
					sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier, null);
		}
	}

	// opens the battle
	private void openTheBattle(Graphics2D g2) {
		int startX = this.startX;
		int startY = this.startY;
		int endX = this.endX;
		int endY = this.endY;

		// must be before battleFrame
		g2.drawImage(sm.getNextBattleChatFrame(), startX, startY + endY,
				sm.getNextBattleChatFrame().getWidth() * pixelMultiplier,
				sm.getNextBattleChatFrame().getHeight() * pixelMultiplier, null);
		// opening
		// field size (240 X 112) pixels
		// zoomed in, in the GUI
		g2.drawImage(sm.getNextBattleFrame(), startX, startY, endX, endY, null);

		// white cover
		g2.setColor(Color.WHITE);
		g2.fillRect(endX + startX, startY, startX, endY + sm.getNextBattleChatFrame().getHeight() * pixelMultiplier);
		g2.fillRect(0, startY, startX, endY + sm.getNextBattleChatFrame().getHeight() * pixelMultiplier);
	}

	// all throw ball animation is performed here
	private void throwBall(Graphics2D g2) {
		int startX = this.startX;
		int startY = this.startY;
		int endX = this.endX;
		int endY = this.endY;

		// calculation for trainer grass field starting x and y
		int startXTF = startX + battleOffsetX;

		// calculation for pokemon grass field starting x and y
		int startXPF = startX + endX - sm.getPokemonField().getWidth() * pixelMultiplier - battleOffsetX;
		int startYPF = startY + 44 * pixelMultiplier;

		// calculation for the offset for pokemon in middle of grass field
		int offsetXPOG = (sm.getPokemonField().getWidth() * pixelMultiplier
				- sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier) / 2;
		int offsetYPOG = sm.getPokemonField().getHeight() * pixelMultiplier
				- sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier;

		System.out.println("ball");
		// the trainer throws pokeball and pokeball travels toward pokemon
		if (battleActionCounter < 10) {
			// draw pokeball
			g2.drawImage(sm.getPokeballFirstFrame(),
					startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
							- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - throwOffsetX,
					startY + endY - throwOffsetY, sm.getPokeballFirstFrame().getWidth() * pixelMultiplier,
					sm.getPokeballFirstFrame().getHeight() * pixelMultiplier, null);
			throwOffsetX -= 36;
			throwOffsetY += 30;

			// draw pokemon
			g2.drawImage(sm.getPokemon(battlePokemon.getName().toLowerCase()), startXPF + offsetXPOG,
					startYPF + offsetYPOG,
					sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier,
					sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier, null);

			// draw trainer
			g2.drawImage(sm.getTrainerBackNextFrame(),
					startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
							- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - 36,
					startY + endY - sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier,
					sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier,
					sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier, null);
		}
		// after pokeball have traveled to pokemon where it must open up to
		// catch pokemon
		else if (battleActionCounter < 17) {
			// draw pokeball
			g2.drawImage(sm.getPokeballNextFrame(),
					startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
							- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - throwOffsetX,
					startY + endY - throwOffsetY, sm.getPokeballFirstFrame().getWidth() * pixelMultiplier,
					sm.getPokeballFirstFrame().getHeight() * pixelMultiplier, null);

			// draw pokemon
			g2.drawImage(sm.getPokemon(battlePokemon.getName().toLowerCase()), startXPF + offsetXPOG + shrinkX / 2,
					startYPF + offsetYPOG

							- shrinkY / 2,
					sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier - shrinkX,
					sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier - shrinkY, null);

			shrinkX += 20;
			shrinkY += 20;

			// draw trainer
			g2.drawImage(sm.getTrainerBackNextFrame(),
					startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
							- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - 36,
					startY + endY - sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier,
					sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier,
					sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier, null);
		} else {
			// draw pokeball
			g2.drawImage(sm.getPokeballNextFrame(),
					startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
							- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - throwOffsetX,
					startY + endY - throwOffsetY + bounceDown - bounceUp,
					sm.getPokeballFirstFrame().getWidth() * pixelMultiplier,
					sm.getPokeballFirstFrame().getHeight() * pixelMultiplier, null);

			// if pokemon not cuaght release it from pokeball
			if (notCaught) {
				if (battleActionCounter == 99) {
					// draw pokemon
					g2.drawImage(sm.getPokemon(battlePokemon.getName().toLowerCase()), startXPF + offsetXPOG,
							startYPF + offsetYPOG,
							sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier,
							sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier, null);
				} else {
					// draw pokemon
					g2.drawImage(sm.getPokemon(battlePokemon.getName().toLowerCase()),
							startXPF + offsetXPOG + shrinkX / 2, startY + endY - throwOffsetY - bounceUp + shrinkY,
							sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier - shrinkX,
							sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier
									- shrinkY,
							null);

					shrinkX -= 20;
					shrinkY -= 20;
				}
			}

			// perform pokeball bounce up and down
			if (bounceCount < 3) {
				if (bounceUp >= 60) {
					bounceDown -= bounceUp;
					bounceUp = 0;
					bounceCount++;
				}
				if (bounceDown >= 150) {
					if (bounceUp == 0) {
						SongPlayer.playFile(new SongWaiter(), "musics/BallFalling.wav");
					}
					bounceUp += 10;
				} else {
					bounceDown += 15;
				}
			} else {
				if (bounceDown < 150) {
					if (bounceDown == 135) {
						SongPlayer.playFile(new SongWaiter(), "musics/BallFalling.wav");
					}
					bounceDown += 15;
				}

			}

			if (battleActionCounter == 90) {
				if (_game.battleEndType() != 1) {
					sm.resetpokeballFrame();
					notCaught = true;
				}
			}

			// draw trainer
			g2.drawImage(sm.getTrainerBackNextFrame(),
					startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
							- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - 36,
					startY + endY - sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier,
					sm.getTrainerBackFirstFrame().getWidth() * 3,
					sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier, null);
		}

		battleActionCounter++;
	}

	// all throw bait animation is performed here
	private void throwBait(Graphics2D g2) {
		int startX = this.startX;
		int startY = this.startY;
		int endX = this.endX;
		int endY = this.endY;

		// calculation for trainer grass field starting x and y
		int startXTF = startX + battleOffsetX;

		// calculation for pokemon grass field starting x and y
		int startXPF = startX + endX - sm.getPokemonField().getWidth() * pixelMultiplier - battleOffsetX;
		int startYPF = startY + 44 * pixelMultiplier;

		// calculation for the offset for pokemon in middle of grass field
		int offsetXPOG = (sm.getPokemonField().getWidth() * pixelMultiplier
				- sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier) / 2;
		int offsetYPOG = sm.getPokemonField().getHeight() * pixelMultiplier
				- sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier;
		// draw pokemon
		g2.drawImage(sm.getPokemon(battlePokemon.getName().toLowerCase()), startXPF + offsetXPOG,
				startYPF + offsetYPOG - (throwOffsetY % 80),
				sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier,
				sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier, null);

		System.out.println("bait");
		if (battleActionCounter < 10) {
			// draw bait
			// bait size is reduced
			g2.drawImage(sm.getBait(),
					startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
							- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - throwOffsetX,
					startY + endY - throwOffsetY, sm.getPokeballFirstFrame().getWidth() * 2,
					sm.getPokeballFirstFrame().getHeight() * 2, null);
			throwOffsetX -= 37;
			throwOffsetY += 20;
		} else {
			battleActionCounter = 100;
		}

		// draw trainer 
		g2.drawImage(sm.getTrainerBackNextFrame(),
				startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
						- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - 36,
				startY + endY - sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier,
				sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier,
				sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier, null);
		battleActionCounter++;
	}

	// all throw Rock animations are performed here
	private void throwRock(Graphics2D g2) {
		int startX = this.startX;
		int startY = this.startY;
		int endX = this.endX;
		int endY = this.endY;

		// calculation for trainer grass field starting x and y
		int startXTF = startX + battleOffsetX;

		// calculation for pokemon grass field starting x and y
		int startXPF = startX + endX - sm.getPokemonField().getWidth() * pixelMultiplier - battleOffsetX;
		int startYPF = startY + 44 * pixelMultiplier;

		// calculation for the offset for pokemon in middle of grass field
		int offsetXPOG = (sm.getPokemonField().getWidth() * pixelMultiplier
				- sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier) / 2;
		int offsetYPOG = sm.getPokemonField().getHeight() * pixelMultiplier
				- sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier;

		System.out.println("Health Left: " + battlePokemon.getHPbyPercet() * 100 + "%");
		System.out.println("rock");
		if (battleActionCounter < 10) {
			// draw pokemon
			g2.drawImage(sm.getPokemon(battlePokemon.getName().toLowerCase()), startXPF + offsetXPOG,
					startYPF + offsetYPOG,
					sm.getPokemon(battlePokemon.getName().toLowerCase()).getWidth() * pixelMultiplier,
					sm.getPokemon(battlePokemon.getName().toLowerCase()).getHeight() * pixelMultiplier, null);

			// draw rock
			g2.drawImage(sm.getRock(),
					startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
							- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - throwOffsetX,
					startY + endY - throwOffsetY, sm.getPokeballFirstFrame().getWidth() * pixelMultiplier,
					sm.getPokeballFirstFrame().getHeight() * pixelMultiplier, null);
			throwOffsetX -= 37;
			throwOffsetY += 20;
		} else {
			battleActionCounter = 100;
		}

		// draw trainer
		g2.drawImage(sm.getTrainerBackNextFrame(),
				startXTF + sm.getTrainerField().getWidth() * pixelMultiplier
						- sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier - 36,
				startY + endY - sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier,
				sm.getTrainerBackFirstFrame().getWidth() * pixelMultiplier,
				sm.getTrainerBackFirstFrame().getHeight() * pixelMultiplier, null);
		battleActionCounter++;
	}

	/*
	 * TimeListener - private class
	 * 
	 * this is used to listen for time in a new thread so that it won't conflict
	 * with the actual game.
	 */
	private class TimeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (isMove) {
				nextFrame++;
				if (nextFrame == 4) {
					nextFrame = 0;
				}
				System.out.println(nextFrame);
				if (move == 3) {
					if (_graphicBoard[trainerPos.y / 32][(trainerPos.x - 4) / 32] == 'T'
							|| _graphicBoard[trainerPos.y / 32][(trainerPos.x - 4) / 32] == 't'
							|| _graphicBoard[trainerPos.y / 32][(trainerPos.x - 4) / 32] == 'w'
							|| _graphicBoard[trainerPos.y / 32][(trainerPos.x - 4) / 32] == 'o') {
						walkCount = 0;
						isMove = false;
						return;
					}
					offsetX += 8;
				} else if (move == 2) {
					if (_graphicBoard[trainerPos.y / 32][(trainerPos.x + 32) / 32] == 'T'
							|| _graphicBoard[trainerPos.y / 32][(trainerPos.x + 32) / 32] == 't'
							|| _graphicBoard[trainerPos.y / 32][(trainerPos.x + 32) / 32] == 'w'
							|| _graphicBoard[trainerPos.y / 32][(trainerPos.x + 32) / 32] == 'o') {
						walkCount = 0;
						isMove = false;
						return;
					}
					offsetX -= 8;
				} else if (move == 1) {
					if (_graphicBoard[(trainerPos.y - 4) / 32][trainerPos.x / 32] == 'T'
							|| _graphicBoard[(trainerPos.y - 4) / 32][trainerPos.x / 32] == 't'
							|| _graphicBoard[(trainerPos.y - 4) / 32][trainerPos.x / 32] == 'w'
							|| _graphicBoard[(trainerPos.y - 4) / 32][trainerPos.x / 32] == 'o') {
						walkCount = 0;
						isMove = false;
						return;
					}
					offsetY += 8;
				} else if (move == 0) {
					if (_graphicBoard[(trainerPos.y + 32) / 32][trainerPos.x / 32] == 'T'
							|| _graphicBoard[(trainerPos.y + 32) / 32][trainerPos.x / 32] == 't'
							|| _graphicBoard[(trainerPos.y + 32) / 32][trainerPos.x / 32] == 'w'
							|| _graphicBoard[(trainerPos.y + 32) / 32][trainerPos.x / 32] == 'o') {
						walkCount = 0;
						isMove = false;
						return;
					}
					offsetY -= 8;
				} else {
					return;
				}

				walkCount++;

				if (walkCount == 4) {
					walkCount = 0;
				}

				repaint();
			}
		}

	}

	// all in map graphic drawings here
	private void inMapActions(Graphics2D g2) {
		battlePokemon = _game.doesBattle();
		// try to move
		// if (trainerPos != null && trainerPosOld != null) {
		// map is (y, x) form
		if (offsetX == 32) {
			_game.Move('N');
			changed = true;
			isMove = false;
			if (battlePokemon != null) {
				isBattle = true;
				battleTimer.start();
				bgmTimer.restart();
				repaint();
			}
			offsetX = 0;
		} else if (offsetX == -32) {
			_game.Move('S');
			changed = true;
			isMove = false;
			if (battlePokemon != null) {
				isBattle = true;
				battleTimer.start();
				bgmTimer.restart();
				repaint();
			}
			offsetX = 0;
		} else if (offsetY == 32) {
			_game.Move('W');
			changed = true;
			isMove = false;
			if (battlePokemon != null) {
				isBattle = true;
				battleTimer.start();
				bgmTimer.restart();
				repaint();
			}
			offsetY = 0;
		} else if (offsetY == -32) {
			_game.Move('E');
			changed = true;
			isMove = false;
			if (battlePokemon != null) {
				isBattle = true;
				battleTimer.start();
				bgmTimer.restart();
				repaint();
			}
			offsetY = 0;
		}

		// get the updated visible part of the map
		_graphicBoard = _game.getVisiableZone();

		// first draw the ground
		for (int i = 0; i < _graphicBoard.length; i++) {
			for (int j = 0; j < _graphicBoard[i].length; j++) {
				g2.drawImage(this._ground, j * 32 + offsetX - camOffset, i * 32 + offsetY - camOffset, null);
			}
		}

		// draw others over the ground
		for (int i = 0; i < _graphicBoard.length; i++) {
			for (int j = 0; j < _graphicBoard[i].length; j++) {
				if (i != _graphicBoard.length / 2 || j != _graphicBoard.length / 2) {
					// System.out.print(this._graphicBoard[i][j]);
				}
				// draw grass
				if (this._graphicBoard[i][j] == 'g') {
					g2.drawImage(this._grass, j * 32 + offsetX - camOffset, i * 32 + offsetY - camOffset, null);
				}

				// draw wall
				if (this._graphicBoard[i][j] == 'w') {
					g2.drawImage(this._wall, j * 32 + offsetX - camOffset, i * 32 + offsetY - camOffset, null);
				}

				// draw small tree
				if (this._graphicBoard[i][j] == 't') {
					g2.drawImage(this._smallTree, j * 32 + offsetX - camOffset, i * 32 + offsetY - camOffset, null);
				}

				// draw water
				if (this._graphicBoard[i][j] == 'o') {
					g2.drawImage(this._water, j * 32 + offsetX - camOffset, i * 32 + offsetY - camOffset, null);
				}

				// only used to get the new position of player
				if (changed) {
					if (i == _graphicBoard.length / 2 && j == _graphicBoard.length / 2) {
						trainerPos = new Point((j + 1) * 32, (i + 1) * 32);
						trainerPosOld = trainerPos;
						changed = false;
					}
				}
				// if (i == _graphicBoard.length / 2 && j ==
				// _graphicBoard.length / 2) {
				// System.out.println("P");
				// }

			} // in loop
				// System.out.println();
		} // end loop drawing elements

		// draw player based on input from keyboard and animated
		// System.out.println("is moving: " + move);
		g2.drawImage(sm.getTrainerMove()[nextFrame][move], trainerPos.x - camOffset, trainerPos.y - camOffset, null);
		System.out.println(nextFrame);

		// whenever move happened update
		// update number of steps left // add in
		lblNumberOfStepLeft.setText("" + _game.getNumberOfStepLeft());

		// check for end game
		// FIX ME: have not update game over bolean variabe
		// if (_game.getNumberOfStepLeft() == 0) {

		// ADD IN
		if (_game.isWin()) {
			isBattle = false;
			if (_game.getWinCondition() == 0) {
				lblNumberOfStepLeft.setText("0");
				JOptionPane.showMessageDialog(null, "You have reached 500 steps!\n" + "GAME OVER!\n");
			} else if (_game.getWinCondition() == 1) {
				JOptionPane.showMessageDialog(null, "You are out of pokemon balls!\n" + "GAME OVER!\n");
			} else {
				JOptionPane.showMessageDialog(null,
						" You have had enough number of pokemons needed. Congratulation.You won!\n");
			}

			System.exit(0);
		} // end if:0
	}

	private class KeyboardListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent key) {
			// System.out.println("pressing key");

			// Z = click, arrow keys to switch action
			if (isBattle) {
				if (battleOffsetX == 0 && battleActionCounter == 0) {
					if (key.getKeyCode() == KeyEvent.VK_LEFT) {
						SongPlayer.playFile(new SongWaiter(), "musics/actionMenuSound.wav");
						actionX--;
						if (actionX < 0) {
							actionX = 1;
						}
						repaint();
					} else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
						SongPlayer.playFile(new SongWaiter(), "musics/actionMenuSound.wav");
						actionX++;
						if (actionX > 1) {
							actionX = 0;
						}
						repaint();
					} else if (key.getKeyCode() == KeyEvent.VK_UP) {
						SongPlayer.playFile(new SongWaiter(), "musics/actionMenuSound.wav");
						actionY--;
						if (actionY < 0) {
							actionY = 1;
						}
						repaint();
					} else if (key.getKeyCode() == KeyEvent.VK_DOWN) {
						SongPlayer.playFile(new SongWaiter(), "musics/actionMenuSound.wav");
						actionY++;
						if (actionY > 1) {
							actionY = 0;
						}
						repaint();
					}
					// no holding on the key Z by using justPressed
					// must release key Z before next press
					else if (key.getKeyCode() == KeyEvent.VK_Z && !justPressed) {
						SongPlayer.playFile(new SongWaiter(), "musics/actionMenuSound.wav");
						justPressed = true;
						isBattleAction = true;
						if (actionX == 0 && actionY == 0) {
							System.out.println("throw ball");
							_game.battleAction("BALL");
//							_game.useBall();
							SongPlayer.playFile(new SongWaiter(), "musics/ThrowBall150%Volume.wav");

							// must update amount of pokeballs right when throw
							// ball action begins
							lblNumberOfPokemonBalls.setText("" + _game.getNumberOfPokemonBalls());

						} else if (actionX == 1 && actionY == 0) {
							System.out.println("throw bait");
							_game.battleAction("BAIT");
							//_game.useBait();
							// throw ball sound happens to be possible for all
							// other two action base
							SongPlayer.playFile(new SongWaiter(), "musics/ThrowBall150%Volume.wav");
						} else if (actionX == 0 && actionY == 1) {
							System.out.println("throw rock");
							_game.battleAction("ROCK");
							//_game.useRock();
							SongPlayer.playFile(new SongWaiter(), "musics/ThrowRock150%Volume.wav");
						} else if (actionX == 1 && actionY == 1) {
							isBattleAction = false;
							System.out.println("run away");
							_game.battleAction("RUN");
							SongPlayer.playFile(new SongWaiter(), "musics/RunAway150%Volume.wav");
							repaint();
						}
					}
				}
			} else {
				inMapMove(key);
			}
		}

		private void inMapMove(KeyEvent key) {
			if (walkCount == 0) {
				nextFrame = 0;
				System.out.println(trainerPos.x + ", " + trainerPos.y);
				System.out.println(trainerPosOld.x + ", " + trainerPosOld.y);
				// change variables to move based off the given input
				trainerPosOld = new Point(trainerPos.x, trainerPos.y);
				if (key.getKeyCode() == KeyEvent.VK_LEFT) {
					isMove = true;
					move = 3;
					// ADD IN
					if (isBattle == false) {
						_game.addItems();
					}

				} else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
					isMove = true;
					move = 2;
					// ADD IN
					if (isBattle == false) {
						_game.addItems();
					}
				} else if (key.getKeyCode() == KeyEvent.VK_UP) {
					isMove = true;
					move = 1;
					// ADD IN
					if (isBattle == false) {
						_game.addItems();
					}
				} else if (key.getKeyCode() == KeyEvent.VK_DOWN) {
					isMove = true;
					move = 0;
					// ADD IN
					if (isBattle == false) {
						_game.addItems();
					}
				} else {
					move = -1;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent key) {
			justPressed = false;
			trainerPosOld = new Point(trainerPos.x, trainerPos.y);
		}

		@Override
		public void keyTyped(KeyEvent key) {

		}

	}

	private class SongWaiter implements EndOfSongListener {

		public void songFinishedPlaying(EndOfSongEvent eosEvent) {
			System.out.println("Finished " + eosEvent.fileName() + ", " + eosEvent.finishedDate() + ", "
					+ eosEvent.finishedTime());
		}
	}

	// ___________________________________
	// getters below

	public char[][] get_graphicBoard() {
		return _graphicBoard;
	}

	public int getMove() {
		return move;
	}

	public Point getTrainerPos() {
		return trainerPos;
	}

	public Point getTrainerPosOld() {
		return trainerPosOld;
	}

	public Transection get_game() {
		return _game;
	}

	public boolean isBattle() {
		return isBattle;
	}

}// END CLASS GRAPHIC_VIEW
