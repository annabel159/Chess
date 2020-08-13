package game.board;

import game.board.Board.Builder;
import game.pieces.Pawn;
import game.pieces.Piece;
import game.pieces.Rook;

/**
 * Class defining the moves a piece can make
 */
public abstract class Move {

	protected final Board board;
	protected final Piece movedPiece;
	protected final int destinationCoordinate;
	protected final boolean isFirstMove;

	public static final Move NULL_MOVE = new NullMove();

	private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
		this.isFirstMove = movedPiece.isFirstMove();
	}

	private Move(final Board board, final int destinationCoordinate) {
		this.board = board;
		this.destinationCoordinate = destinationCoordinate;
		this.movedPiece = null;
		this.isFirstMove = false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.destinationCoordinate;
		result = prime * result + this.movedPiece.hashCode();
		result = prime * result + this.movedPiece.getPiecePosition();
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Move)) {
			return false;
		}
		final Move otherMove = (Move) other;
		return getCurrentCoordinate() == otherMove.getCurrentCoordinate()
				&& getDestinationCoordinate() == otherMove.getDestinationCoordinate()
				&& getMovedPiece().equals(otherMove.getMovedPiece());
	}
	
	/**
	 * Returns board on which chess is being played.
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * Returns piece that makes the move.
	 */
	public Piece getMovedPiece() {
		return this.movedPiece;
	}

	/**
	 * Returns coordinate where the piece is moved to.
	 */
	public int getDestinationCoordinate() {
		return this.destinationCoordinate;
	}
	
	/**
	 * Returns coordinate where the piece is initially located - before the move has been performed.
	 */
	public int getCurrentCoordinate() {
		return this.movedPiece.getPiecePosition();
	}

	/**
	 * Returns if the move is an Attack to another piece.
	 */
	public boolean isAttack() {
		return false;
	}

	/**
	 * Returns if the move is a castling move.
	 */
	public boolean isCastlingMove() {
		return false;
	}

	/**
	 * Returns the piece that is being attacked.
	 */
	public Piece getAttackedPiece() {
		return null;
	}

	/**
	 * Executes the move by returning a new board.
	 */
	public Board execute() {
		final Builder builder = new Builder();
		for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
			if (!this.movedPiece.equals(piece)) { // pieces that are not moved are set on the same place on the new //
													// board
				builder.setPiece(piece);
			}
		}

		for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece); // same thing for the pieces of the enemy
		}
		// move the moved piece:
		builder.setPiece(this.movedPiece.movePiece(this));
		// change color MoveMaker
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

		return builder.build();
	}

	/**
	 * Class defining the attack of a Major piece - not a pawn.
	 */
	public static class MajorAttackMove extends AttackMove {

		public MajorAttackMove(final Board board, final Piece pieceMoved, final int destinationCoordinate,
				final Piece pieceAttacked) {
			super(board, pieceMoved, destinationCoordinate, pieceAttacked);
		}

		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof MajorAttackMove && super.equals(other);
		}

		@Override
		public String toString() {
			return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}

	/**
	 * Class defining the move of a Major piece.
	 */
	public static final class MajorMove extends Move {

		public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}

		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof MajorMove && super.equals(other);
		}

		@Override
		public String toString() {
			return movedPiece.getPieceType().toString()
					+ BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}

	}

	/**
	 * Class defining an attack move.
	 */
	public static class AttackMove extends Move {

		final Piece attackedPiece;

		public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
				final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}

		@Override
		public int hashCode() {
			return this.attackedPiece.hashCode() + super.hashCode();
		}

		@Override
		public boolean equals(final Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof AttackMove)) {
				return false;
			}
			final AttackMove otherAttackMove = (AttackMove) other;
			return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
		}

		@Override
		public boolean isAttack() {
			return true;
		}

		@Override
		public Piece getAttackedPiece() {
			return this.attackedPiece;
		}

	}

	/**
	 * Class defining the move of a pawn.
	 */
	public static final class PawnMove extends Move {

		public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}

		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnMove && super.equals(other);
		}

		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}

	}

	/**
	 * Class defining the attack of a pawn.
	 */
	public static class PawnAttackMove extends AttackMove {
		public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
				final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate, attackedPiece);
		}

		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnAttackMove && super.equals(other);
		}

		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x"
					+ BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}
	}

	/**
	 * Class defining an en passant move.
	 */
	public static final class PawnEnPassantAttackMove extends PawnAttackMove {
		public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
				final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate, attackedPiece);
		}

		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
		}

		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				if (!piece.equals(this.getAttackedPiece())) {
					builder.setPiece(piece);
				}
			}
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}

	}

	/**
	 * Class defining a pawn promotion.
	 */
	public static class PawnPromotion extends Move {
		
		final Move decoratedMove;
		final Pawn promotedPawn;
		
		public PawnPromotion(final Move decoratedMove) {
			super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
			this.decoratedMove = decoratedMove;
			this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
		}
		
		@Override
		public int hashCode() {
			return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnPromotion && super.equals(other);
		}
		
		@Override
		public Board execute() {
			final Board pawnMoveBoard = this.decoratedMove.execute();
			final Builder builder = new Builder();
			for(final Piece piece : pawnMoveBoard.currentPlayer().getActivePieces()) {
				if(!this.promotedPawn.equals(piece)) {
					builder.setPiece(piece);
				}
				
			}
			for (final Piece piece : pawnMoveBoard.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
			builder.setMoveMaker(pawnMoveBoard.currentPlayer().getAlliance());
			return builder.build();
		}
		
		@Override
		public boolean isAttack() {
			return this.decoratedMove.isAttack();
		}
		
		@Override
		public Piece getAttackedPiece() {
			return this.decoratedMove.getAttackedPiece();
		}
		
		@Override
		public String toString() {
			return "";
		}
	}
	
	/**
	 * Class defining a pawn jump.
	 */
	public static final class PawnJump extends Move {

		public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}

		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
			builder.setPiece(movedPawn);
			builder.setEnPassantPawn(movedPawn);
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}

		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
		}

	}

	/**
	 * Class defining a castle move.
	 */
	static abstract class CastleMove extends Move {

		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDestination;

		public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
				final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate);
			this.castleRook = castleRook;
			this.castleRookStart = castleRookStart;
			this.castleRookDestination = castleRookDestination;
		}

		public Rook getCastleRook() {
			return this.castleRook;
		}

		@Override
		public boolean isCastlingMove() {
			return true;
		}

		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + this.castleRook.hashCode();
			result = prime * result + this.castleRookDestination;
			return result;
		}

		@Override
		public boolean equals(final Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof CastleMove)) {
				return false;
			}
			final CastleMove otherCastleMove = (CastleMove) other;
			return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
		}

	}

	/**
	 * Class defining a castle move on the king's side.
	 */
	public static final class KingSideCastleMove extends CastleMove {

		public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
				final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
		}

		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof KingSideCastleMove && super.equals(other);

		}

		@Override
		public String toString() {
			return "O-O";
		}

	}

	/**
	 * Class defining a castle move on the queen's side.
	 */
	public static final class QueenSideCastleMove extends CastleMove {

		public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate,
				final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
		}

		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof QueenSideCastleMove && super.equals(other);

		}
		
		@Override
		public String toString() {
			return "O-O-O";
		}
	}

	/**
	 * Class defining a null move. 
	 */
	public static final class NullMove extends Move {

		public NullMove() {
			super(null, 65);
		}

		@Override
		public Board execute() {
			throw new RuntimeException("Cannot execute the NullMove!");
		}

		@Override
		public int getCurrentCoordinate() {
			return -1;
		}

	}

	public static class MoveFactory {

		private MoveFactory() {
			throw new RuntimeException("Not instantiable!");

		}

		public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
			for (final Move move : board.getAllLegalMoves()) {
				if (move.getCurrentCoordinate() == currentCoordinate
						&& move.getDestinationCoordinate() == destinationCoordinate) {
					return move;
				}

			}
			return NULL_MOVE;
		}
	}

}
