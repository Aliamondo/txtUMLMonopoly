package monopoly.x;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import monopoly.x.model.Board;
import monopoly.x.model.Game;


class MonopolyStateMachineDiagram extends StateMachineDiagram<Game>{
	@Column({Game.Init.class, Game.Playing.class, Game.MakingMove.class})
	class L extends Layout{}
}

class BoardDiagram extends StateMachineDiagram<Board> {
	@Row({Board.Init.class, Board.Filling.class, Board.Playing.class})
	class L extends Layout{}
}