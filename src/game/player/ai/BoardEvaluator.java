package game.player.ai;

import game.board.Board;

public interface BoardEvaluator {

	int evaluate(Board board, int depth);
	
}
