package game.player.ai;

import game.board.Board;
import game.board.Move;

/**
 * Class determining strategies of the computer to determine the best move.
 */
public interface MoveStrategy {

	// minimax: designate cutoff (e.g. depth 4) and score possible sets
	
	Move execute(Board board, int depth);
	
	
}
