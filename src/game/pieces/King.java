package game.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import game.Alliance;
import game.board.Board;
import game.board.BoardUtils;
import game.board.Move;
import game.board.Move.MajorAttackMove;
import game.board.Move.MajorMove;
import game.board.Tile;

/**
 * Class defining the king piece.
 */
public class King extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATE = { -9, -8, -7, -1, 1, 7, 8, 9 };
	private final boolean kingSideCastleCapable;
	private final boolean queenSideCastleCapable;
	private final boolean isCastled;

	public King(final Alliance pieceAlliance, final int piecePosition, final boolean kingSideCastleCapable,
			final boolean queenSideCastleCapable) {
		super(PieceType.KING, pieceAlliance, piecePosition, true);
		this.isCastled = false;
		this.kingSideCastleCapable = kingSideCastleCapable;
		this.queenSideCastleCapable = queenSideCastleCapable;
	}

	public King(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove,
			final boolean isCastled, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable) {
		super(PieceType.KING, pieceAlliance, piecePosition, isFirstMove);
		this.isCastled = isCastled;
		this.kingSideCastleCapable = kingSideCastleCapable;
		this.queenSideCastleCapable = queenSideCastleCapable;
	}

	/**
	 * Returns if king is castled.
	 */
	public boolean isCastled() {
		return this.isCastled;
	}

	/**
	 * Returns if king is capable of castling on the king's side.
	 */
	public boolean isKingSideCastleCapable() {
		return this.kingSideCastleCapable;
	}

	/**
	 * Returns if the king is capable of castling on the queen's side.
	 */
	public boolean isQueenSideCastleCapable() {
		return this.queenSideCastleCapable;
	}

	/**
	 * Returns a collection of the legal moves of the king on the board.
	 */
	@Override
	public Collection<Move> calculateLegalMoves(Board board) {

		final List<Move> legalMoves = new ArrayList<>();

		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {

			final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

			if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)
					|| isEightColumnExclusion(this.piecePosition, currentCandidateOffset)) {
				continue;
			}

			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

				if (!candidateDestinationTile.isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				} else {

					final Piece pieceAtDestination = candidateDestinationTile.getPiece();
					final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

					if (this.pieceAlliance != pieceAlliance) {
						legalMoves.add(
								new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); // enemy
																														// piece
					}
				}
			}
		}

		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public String toString() {
		return PieceType.KING.toString();
	}

	/**
	 * Returns the special case of being in the first column.
	 */
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		// if current position is in first column of the chess board
		return BoardUtils.FIRST_COLUMN[currentPosition]
				&& ((candidateOffset == -9) || (candidateOffset == -1) || (candidateOffset == 7));
	}

	/**
	 * Returns the special case of being in the eight column.
	 */
	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHT_COLUMN[currentPosition]
				&& ((candidateOffset == -7) || (candidateOffset == 1) || (candidateOffset == 9));
	}

	/**
	 * Returns a king that had been moved.
	 */
	@Override
	public King movePiece(final Move move) {
		return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate(), false,
				move.isCastlingMove(), false, false);
	}

}
