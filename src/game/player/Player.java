package game.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import game.Alliance;
import game.board.Board;
import game.board.Move;
import game.pieces.King;
import game.pieces.Piece;

/**
 * Class determining a Player.
 */
public abstract class Player {

	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;

	private final boolean isInCheck;

	Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {
		this.board = board;
		this.playerKing = establishKing();
		this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastles(legalMoves, opponentMoves)));
		this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
	}

	/**
	 * Returns the king of this player.
	 */
	public King getPlayerKing() {
		return this.playerKing;
	}

	/**
	 * Returns the legal moves of this player.
	 */
	public Collection<Move> getLegalMoves() {
		return this.legalMoves;
	}

	/**
	 * Returns collection of attackmoves.
	 */
	protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
		final List<Move> attackMoves = new ArrayList<>();
		for (final Move move : moves) {
			if (piecePosition == move.getDestinationCoordinate()) {
				attackMoves.add(move);
			}
		}
		return ImmutableList.copyOf(attackMoves);
	}

	/**
	 * Returns the king of the player.
	 */
	private King establishKing() {
		for (final Piece piece : getActivePieces()) {
			if (piece.getPieceType().isKing()) {
				return (King) piece;
			}
		}
		throw new RuntimeException("Should not reach here! Not a valid board!!!");
	}

	/**
	 * Check if move is legal.
	 */
	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}

	/**
	 * Check if king of player is in check.
	 */
	public boolean isInCheck() {
		return this.isInCheck;
	}

	/**
	 * Check if king of player is in check mate.
	 */
	public boolean isInCheckMate() {
		return this.isInCheck && !hasEscapeMoves();
	}

	/**
	 * Check if king of player is in stalemate.
	 */
	public boolean isInStaleMate() {
		return !this.isInCheck && !hasEscapeMoves();
	}

	/**
	 * Check if king of player is castled.
	 */
	public boolean isCastled() {
		return this.playerKing.isCastled();
	}
	
	/**
	 * Check if king of player is capable of castling on the king's side.
	 */
	public boolean isKingSideCastleCapable() {
		return this.playerKing.isKingSideCastleCapable();
	}
	
	/**
	 * Check if king of player is capable of castling on the queen's side.
	 */
	public boolean isQueenSideCastleCapable() {
		return this.playerKing.isQueenSideCastleCapable();
	}

	/**
	 * Check if move can be performed.
	 */
	protected boolean hasEscapeMoves() {
		for (final Move move : this.legalMoves) {
			final MoveTransition transition = makeMove(move);
			if (transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Let player make a move.
	 */
	public MoveTransition makeMove(final Move move) {

		if (!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}

		final Board transitionBoard = move.execute();

		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(
				transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
				transitionBoard.currentPlayer().getLegalMoves());
		if (!kingAttacks.isEmpty()) {
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}

		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}

	public abstract Collection<Piece> getActivePieces();

	public abstract Alliance getAlliance();

	public abstract Player getOpponent();
	
	protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);

}
