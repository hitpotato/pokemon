/** CSC 335 
 * Final Project: Pokemon Safari Zone
 * @Author: Thai Pham, Yang Yang Lu
 * 
 * This class is Game representation (something that manages all the other models).
 * It represents the graphic view of the game and calls
 * model methods to update and check win conditions.
 *  */

package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import model.Transection;
import view.GraphicView;

/*
 *  * Note: - 
 * - T: stand for big tree on the map 
 * - t: stand for small tree on the map
 * - o: stand for water 
 * - w: stand wall (trainer can not pass the wall) 
 * - g: stand for green area (glass)  
 */ 

public class pokemonGUI extends JFrame {

	public static void main(String[] args) throws IOException {
		pokemonGUI p = new pokemonGUI();
		p.setVisible(true);
	}// end main

	// available render space
	private int width = 900;
	private int height = 900;
	Transection game = new Transection();
	private GraphicView newGraphicView = new GraphicView(game, width, height);
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnGradersNote = new JMenu("Grader`s Note");
	private final JMenu mnGivingUp = new JMenu("Surrender");

	public pokemonGUI() throws IOException {
		this.setSize(width, height);
		this.setLocation(200, 0);
		getContentPane().add(newGraphicView); 
		this.addKeyListener(newGraphicView.getKeyListeners()[0]);
		this.addWindowListener(new CloseWindowListener());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setupMenuBar();// add in method
		this.setVisible(true);
		this.setResizable(false); // bad looking if resized

	} // End constructor()

	private void setupMenuBar() {
		setJMenuBar(menuBar);
		mnGradersNote.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "Pokemon:\n" + "- Use narrow keys to move Trainer.\n"
						+ "- In the battle, use narrow keys to switch between options && key Z for action.\n"
						+ "- MAP: to switch to different map, use the open spare on the upper left corner. It works like a gate to other map.\n"//  
						+ "- All items are hide under the grass. There is pop up message will let trainer know new item is added in.\n"
						+ "- Each pokemon need to reach certain level of HP before trainer can catch them. This is our WOW factor.\n"
						+ "- Each Pokemon has different HP, so we display HP bar by color.\n"
						+ "- When HP bar is green, It means that you can NOT catch the pokemon.\n"
						+ "- When HP bar is Red or Yelow, It means that You have a good chance to catch the Pokemon.\n"
						+ "- When HP bar is red, the run rate of pokemon has higher than 50%.\n"
						+ "- NOTE: We also add in the reduce HP button to help trainer to catch pokemon easier.\n"); 
			}
		});

		menuBar.add(mnGradersNote);
		mnGivingUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int userInput = JOptionPane.showConfirmDialog(null,
						"Are you sure that you want to giving up the game?");

				if (userInput == JOptionPane.YES_OPTION) {
					JOptionPane.showMessageDialog(null, "Game is over!");
					System.exit(0);
				}
			}
		});

		menuBar.add(mnGivingUp);
	}

	/*
	 * CloseWindowListener - Private Class
	 * 
	 * This class is used to listen for window closing and ask for save game.
	 */
	private class CloseWindowListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			// cannot save in battle things can mess up
			if (!newGraphicView.isBattle()) {
				int userInput = JOptionPane.showConfirmDialog(null, "Do you want to save game");

				// save data
				if (userInput == JOptionPane.YES_OPTION) {
					ObjectOutputStream save = null;
					try {
						save = new ObjectOutputStream(new FileOutputStream("savedData"));
						save.writeObject(newGraphicView.get_game());
						save = new ObjectOutputStream(new FileOutputStream("savedData1"));
						save.writeObject(newGraphicView.getMove());
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					System.exit(0);

				} else if (userInput == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
			}
		}
	}
}
