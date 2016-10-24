package monopoly.x;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import monopoly.x.model.Board;
import monopoly.x.model.CardCell;
import monopoly.x.model.Cell;
import monopoly.x.model.ChanceCardCell;
import monopoly.x.model.CommunityChestCardCell;
import monopoly.x.model.CompanyCell;
import monopoly.x.model.Dice;
import monopoly.x.model.Game;
import monopoly.x.model.JailCell;
import monopoly.x.model.Player;
import monopoly.x.model.StartCell;

public class GameDiagram extends ClassDiagram {
	@Row({Game.class, Board.class, Player.class, Cell.class})
	@Row({CompanyCell.class, JailCell.class, StartCell.class, CardCell.class})
	@Row({CommunityChestCardCell.class, ChanceCardCell.class})
	@Row({Dice.class})
	
	class ExampleLayout extends Layout {}
}
