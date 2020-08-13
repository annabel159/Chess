package game.player;

import game.board.Board;
import game.board.Move;

/**
 * Class determining a transition of board after a move.
 */
public class MoveTransition {

	private final Board transitionBoard;
	private final Move move;
	private final MoveStatus moveStatus;

	public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus) {
		this.transitionBoard = transitionBoard;
		this.move = move;
		this.moveStatus = moveStatus;
	}

	/**
	 * Returns the state of a move.
	 */
	public MoveStatus getMoveStatus() {
		return this.moveStatus;
	}
	
	/**
	 * Returns the board where it is transitioned to after move.
	 */
	public Board getTransitionBoard() {
		return this.transitionBoard;
	}
	
}
