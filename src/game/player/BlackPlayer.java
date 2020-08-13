package game.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import game.Alliance;
import game.board.Board;
import game.board.Move;
import game.board.Move.KingSideCastleMove;
import game.board.Move.QueenSideCastleMove;
import game.board.Tile;
import game.pieces.Piece;
import game.pieces.Rook;

/**
 * Class determining black player.
 */
public class BlackPlayer extends Player {

	public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves,
			final Collection<Move> blackStandardLegalMoves) {
		super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
	}

	/**
	 * Returns the active black pieces on the board.
	 */
	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	/**
	 * Returns the alliance.
	 */
	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	/**
	 * Returns the opponent of the blackPlayer, namely the whitePlayer.
	 */
	@Override
	public Player getOpponent() {
		return this.board.whitePlayer();

	}

	/**
	 * Returns castle moves.
	 */
	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
			final Collection<Move> opponentsLegals) {

		final List<Move> kingCastles = new ArrayList<>();
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {

			// blacks king side castle
			if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(7);

				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					if (Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty()
							&& Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 6,
								(Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
					}

				}
			}
			if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied()
					&& !this.board.getTile(3).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(0);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
						Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty() && Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty() &&
						rookTile.getPiece().getPieceType().isRook()) {
					kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 2, (Rook) rookTile.getPiece(),
							rookTile.getTileCoordinate(), 3));
				}
			}
		}
		return ImmutableList.copyOf(kingCastles);
	}

}
