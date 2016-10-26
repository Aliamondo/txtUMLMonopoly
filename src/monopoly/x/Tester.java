package monopoly.x;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import monopoly.x.model.Board;
import monopoly.x.model.Cell;
import monopoly.x.model.ChanceCardCell;
import monopoly.x.model.ChanceCardCellsOnBoard;
import monopoly.x.model.CommunityChestCardCell;
import monopoly.x.model.CommunityChestCardCellsOnBoard;
import monopoly.x.model.CompanyCell;
import monopoly.x.model.CompanyCellsOnBoard;
import monopoly.x.model.Dice;
import monopoly.x.model.Game;
import monopoly.x.model.GameBoard;
import monopoly.x.model.GameDice;
import monopoly.x.model.GamePlayers;
import monopoly.x.model.GoToJailCell;
import monopoly.x.model.GoToJailCellsOnBoard;
import monopoly.x.model.JailCell;
import monopoly.x.model.JailCellsOnBoard;
import monopoly.x.model.Player;
import monopoly.x.model.StartCell;
import monopoly.x.model.StartCellsOnBoard;
import monopoly.x.model.ThrowDice;

public class Tester extends JFrame implements GUIInterface{
	/**
	 * Tester for Monopoly
	 */
	
	private static final long serialVersionUID = 1L;
	private static final int ROWS = 11, COLS = 11, DICE_SIDES = 6, CARDS = 16;
	private static JButton[][] boardCells = new JButton[ROWS][COLS];
	private static Cell[] cells = new Cell[40];
	private static ImageIcon playerIcon = new ImageIcon(new ImageIcon("blockdude_square.png").getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH));
	private static ImageIcon playerIconGray = new ImageIcon(new ImageIcon("blockdude_square_gray.png").getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH));
	private static ArrayList<ImageIcon> dieFaces = new ArrayList<>();
	private static Game game;
	private static Board board;
	private static Player player1;
	private static Player player2;
	private static Dice dice1;
	private static Dice dice2;
	private static Cards communityChestCards;
	private static Cards chanceCards;
	private static RandomNum random = new RandomNum();
	
	static void init() {
		communityChestCards = new Cards(CARDS);
		communityChestCards.addCard(0); // Go to Start
		communityChestCards.addCard(10); // Go to Jail
		communityChestCards.fillMissing();
//		communityChestCards.shuffleCards();

		chanceCards = new Cards(CARDS);
		chanceCards.addCard(0);
		chanceCards.addCard(10);
		chanceCards.addCard(11);
		chanceCards.addCard(24);
		chanceCards.addCard(39);
		chanceCards.addCard(5);
		chanceCards.addCard(-3); // Move 3 steps back
		chanceCards.fillMissing();
		chanceCards.shuffleCards();
		
		Tester tester = new Tester();
		game = Action.create(Game.class, Integer.valueOf(2), tester);
		board = Action.create(Board.class, communityChestCards, chanceCards);
		player1 = Action.create(Player.class);
		player2 = Action.create(Player.class);
		dice1 = Action.create(Dice.class, DICE_SIDES, random);
		dice2 = Action.create(Dice.class, DICE_SIDES, random);
//		Cell[] cells = new Cell[40];
		cells[0] = Action.create(StartCell.class);
		for (int i = 1; i < cells.length; i++) {
			if (i == 10) cells[i] = Action.create(JailCell.class);
			else if (i == 2 || i == 17 || i == 33) cells[i] = Action.create(CommunityChestCardCell.class, "Do community chest #" + i);
			else if (i == 7 || i == 22 || i == 36) cells[i] = Action.create(ChanceCardCell.class, "Do chance #" + i);
			else if (i == 30) cells[i] = cells[i] = Action.create(GoToJailCell.class);
			else cells[i] = Action.create(CompanyCell.class, i * 100, "" + i);
		}
		
		Action.link(GameBoard.game.class, game, GameBoard.board.class, board);
		Action.link(GameDice.game.class, game, GameDice.dice.class, dice1);
		Action.link(GameDice.game.class, game, GameDice.dice.class, dice2);
		Action.link(GamePlayers.game.class, game, GamePlayers.player.class, player1);
		Action.link(GamePlayers.game.class, game, GamePlayers.player.class, player2);
		for (Cell cell : cells) {
			if (cell instanceof CompanyCell) {
				Action.link(CompanyCellsOnBoard.board.class, board, CompanyCellsOnBoard.cell.class, (CompanyCell) cell);
			}
			else if (cell instanceof StartCell) {
				Action.link(StartCellsOnBoard.board.class, board, StartCellsOnBoard.cell.class, (StartCell) cell);
			}
			else if (cell instanceof JailCell) {
				Action.link(JailCellsOnBoard.board.class, board, JailCellsOnBoard.cell.class, (JailCell) cell);
			}
			else if (cell instanceof GoToJailCell) {
				Action.link(GoToJailCellsOnBoard.board.class, board, GoToJailCellsOnBoard.cell.class, (GoToJailCell) cell);
			}
			else if (cell instanceof CommunityChestCardCell) {
				Action.link(CommunityChestCardCellsOnBoard.board.class, board, CommunityChestCardCellsOnBoard.cell.class, (CommunityChestCardCell) cell);
			}
			else if (cell instanceof ChanceCardCell) {
				Action.link(ChanceCardCellsOnBoard.board.class, board, ChanceCardCellsOnBoard.cell.class, (ChanceCardCell) cell);
			}
			else {// Something is wrong, as the cell class is not one of the allowed classes
				System.out.println("ERROR. Expected Cell type class, got " + cell.getClass().getName());
			}
		}
		
		Action.start(game);
		Action.start(board);
		Action.start(dice1);
		Action.start(dice2);
		Action.start(player1);
		Action.start(player2);
		for (int i = 0; i < cells.length; i++) {
			Action.start(cells[i]);
		}
	}
	
	public static void main(String[] args) {
		ModelExecutor.create().start(Tester::init).awaitInitialization();
		Tester t = new Tester();
		t.view();
	}

	@Override
	public void view() {
		for (int i = 0; i < DICE_SIDES; i++) {
			dieFaces.add(new ImageIcon(new ImageIcon("die_face_" + (i + 1) + ".png").getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH)));
		}

		setTitle("txtUML Monopoly by Andrey Khasanov (VXDW14)");
		GridLayout layout = new GridLayout(ROWS, COLS);
        setLayout(layout);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				JButton tempButton = new JButton();
				int temp = getCellIndexOfButton(i, j);
				
				if (temp == -1) {
					tempButton.setEnabled(false);
				}
				
				tempButton.setText(suggestName(temp));
				tempButton.setBackground(temp >= 0 ? suggestColor(cells[temp]) : suggestColor(null));
				tempButton.setPreferredSize(new Dimension(30, 20));
//				tempButton.addActionListener(null);
				boardCells[i][j] = tempButton;
				getContentPane().add(tempButton);

			}
		}
        // Put a player on first cell and delete the text
        boardCells[0][0].setText("");
        boardCells[0][0].setIcon(playerIcon);

        JButton simButton = boardCells[ROWS / 2][COLS / 2];
        simButton.setText("Next");
		simButton.setEnabled(true);
        simButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Action.send(new ThrowDice(), dice1);
				Action.send(new ThrowDice(), dice2);
			}
		});
        
        setPreferredSize(new Dimension(ROWS * 80 + 100, COLS * 60 + 100));
        pack();
        setVisible(true);
	}
	
	private int getCellIndexOfButton(int i, int j) {
		int temp = -1; //will be used for calculations
		
		if (i == 0) {
			temp = j;
		}
		else if (i == ROWS - 1) {
			temp = 19 + (COLS - j);
		}
		else if (j == COLS - 1) {
			temp = COLS + i - 1;
		}
		else if (j == 0) {
			temp = 29 + (ROWS - i);
		}
		return temp;
	}
	
	private static Color suggestColor(Cell cell) {
		if (cell == null) return Color.WHITE;
		if (cell instanceof CompanyCell) return Color.ORANGE;
		if (cell instanceof StartCell) return Color.YELLOW.brighter();
		if (cell instanceof JailCell) return Color.RED;
		if (cell instanceof GoToJailCell) return Color.PINK;
		if (cell instanceof CommunityChestCardCell) return Color.GREEN.brighter().brighter();
		if (cell instanceof ChanceCardCell) return Color.GREEN.darker().darker();
		return Color.WHITE;
	}
	
	private static String suggestName(int i) {
		if (i < 0) return "";
		if (i == 0) return "Start";
		if (i == 10) return "Jail";
		if (i == 30) return "Go to Jail";
		return i + "";
	}

	@Override
	public void showPlayer(int pos) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				JButton button = boardCells[i][j];
				if (button.getIcon() == playerIcon) {
					button.setIcon(null);
					button.setText(suggestName(getCellIndexOfButton(i, j)));
				}
			}
		}
		
		int i = 0, j = 0;
		
		if (pos <= 10) {
			j = pos;
		}
		else if (pos <= 20) {
			i = pos - COLS + 1;
			j = COLS - 1;
		}
		else if (pos <= 30) {
			i = ROWS - 1;
			j = 30 - pos;
		}
		else if (pos < 40) {
			i = 40 - pos;
		}
		boardCells[i][j].setIcon(playerIcon);
		boardCells[i][j].setText("");
	}

	@Override
	public void showOriginalPlayer(int pos) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				JButton button = boardCells[i][j];
				if (button.getIcon() == playerIconGray) {
					button.setIcon(null);
					button.setEnabled(true);
					button.setBackground(suggestColor(cells[getCellIndexOfButton(i, j)]));
					button.setText(suggestName(getCellIndexOfButton(i, j)));
				}
			}
		}
		if (pos == -1) return; // If the movement was validated without a change, we don't need to update the view
		
		int i = 0, j = 0;
		
		if (pos <= 10) {
			j = pos;
		}
		else if (pos <= 20) {
			i = pos - COLS + 1;
			j = COLS - 1;
		}
		else if (pos <= 30) {
			i = ROWS - 1;
			j = 30 - pos;
		}
		else if (pos < 40) {
			i = 40 - pos;
		}
		boardCells[i][j].setIcon(playerIconGray);
		boardCells[i][j].setEnabled(false);
		boardCells[i][j].setBackground(Color.GRAY);
		boardCells[i][j].setText("");
	}

	@Override
	public void showDice(int x, int y) {
		boardCells[6][4].setIcon(dieFaces.get(x - 1));
		boardCells[6][4].setDisabledIcon(dieFaces.get(x - 1));
		boardCells[6][6].setIcon(dieFaces.get(y - 1));
		boardCells[6][6].setDisabledIcon(dieFaces.get(y - 1));
	}	
	
}
