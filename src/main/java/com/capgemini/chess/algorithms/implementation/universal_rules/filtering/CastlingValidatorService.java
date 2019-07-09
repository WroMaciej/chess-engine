package com.capgemini.chess.algorithms.implementation.universal_rules.filtering;

import java.util.HashMap;
import java.util.Map;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

/**
 * Service used for validate castling
 * 
 * @author MACIEWRO
 *
 */
public class CastlingValidatorService {

	CoordinatesFilteringHelper coordinatesFilteringHelper;

	/**
	 * Initial positions of pieces used in castling for given color
	 * 
	 * @author MACIEWRO
	 *
	 */
	private class InitialCastlingPiecesPosition {
		private Coordinate kingInitialPosition;
		private Coordinate longRookInitialPosition;
		private Coordinate shortRookInitialPosition;

		public InitialCastlingPiecesPosition(Coordinate kingInitialPosition, Coordinate longRookInitialPosition,
				Coordinate shortRookInitialPosition) {
			this.kingInitialPosition = kingInitialPosition;
			this.longRookInitialPosition = longRookInitialPosition;
			this.shortRookInitialPosition = shortRookInitialPosition;
		}
	}

	//Coordinates of initial positions
	private static final Coordinate whiteKingInitialPosition = new Coordinate(4, 0);
	private static final Coordinate whiteLongRookInitialPosition = new Coordinate(0, 0);
	private static final Coordinate whiteShortRookInitialPosition = new Coordinate(7, 0);
	private static final Coordinate blackKingInitialPosition = new Coordinate(4, 7);
	private static final Coordinate blackLongRookInitialPosition = new Coordinate(0, 7);
	private static final Coordinate blackShortRookInitialPosition = new Coordinate(7, 7);

	/**
	 * Initial positions for every color
	 * Key - color
	 * Value - positions of king, long rook, short rook
	 */
	private final Map<Color, InitialCastlingPiecesPosition> initialCastlingPiecesPosition;
	private final InitialCastlingPiecesPosition whiteInitialCastlingPiecePosition;
	private final InitialCastlingPiecesPosition blackInitialCastlingPiecePosition;

	public CastlingValidatorService(CoordinatesFilteringHelper coordinatesFilteringHelper) {
		this.coordinatesFilteringHelper = coordinatesFilteringHelper;

		// populating initial positions
		whiteInitialCastlingPiecePosition = new InitialCastlingPiecesPosition(whiteKingInitialPosition,
				whiteLongRookInitialPosition, whiteShortRookInitialPosition);
		blackInitialCastlingPiecePosition = new InitialCastlingPiecesPosition(blackKingInitialPosition,
				blackLongRookInitialPosition, blackShortRookInitialPosition);
		initialCastlingPiecesPosition = new HashMap<>();
		initialCastlingPiecesPosition.put(Color.WHITE, whiteInitialCastlingPiecePosition);
		initialCastlingPiecesPosition.put(Color.BLACK, blackInitialCastlingPiecePosition);
	}
	

	private boolean isKingMoved(Color color, Board board) {
		return !isKingOnInitialPosition(color, board) || coordinatesFilteringHelper.isEverMoved(initialCastlingPiecesPosition.get(color).kingInitialPosition,
				board.getMoveHistory());
	}
	
	private boolean isLongRookMoved(Color color, Board board) {
		return !isLongRookOnInitialPosition(color, board) || coordinatesFilteringHelper.isEverMoved(initialCastlingPiecesPosition.get(color).longRookInitialPosition,
				board.getMoveHistory());
	}
	
	private boolean isShortRookMoved(Color color, Board board) {
		return !isShortRookOnInitialPosition(color, board) || coordinatesFilteringHelper.isEverMoved(initialCastlingPiecesPosition.get(color).shortRookInitialPosition,
				board.getMoveHistory());
	}
	
	private boolean isKingOnInitialPosition(Color color, Board board){
		Coordinate kingInitialPosition = initialCastlingPiecesPosition.get(color).kingInitialPosition;
		Piece actualPieceOnKingPosition = board.getPieceAt(kingInitialPosition);
		return actualPieceOnKingPosition == null ? false : actualPieceOnKingPosition.getType() == PieceType.KING && actualPieceOnKingPosition.getColor() == color;
	}
	
	private boolean isLongRookOnInitialPosition(Color color, Board board){
		Coordinate rookInitialPosition = initialCastlingPiecesPosition.get(color).longRookInitialPosition;
		Piece actualPieceOnRookPosition = board.getPieceAt(rookInitialPosition);
		return actualPieceOnRookPosition == null ? false : actualPieceOnRookPosition.getType() == PieceType.ROOK && actualPieceOnRookPosition.getColor() == color;
	}
	
	private boolean isShortRookOnInitialPosition(Color color, Board board){
		Coordinate rookInitialPosition = initialCastlingPiecesPosition.get(color).shortRookInitialPosition;
		Piece actualPieceOnRookPosition = board.getPieceAt(rookInitialPosition);
		return actualPieceOnRookPosition == null ? false : actualPieceOnRookPosition.getType() == PieceType.ROOK && actualPieceOnRookPosition.getColor() == color;
	}

	/**
	 * Checks if king and long rook ever moved
	 * 
	 * @param color
	 *            color of player
	 * @return true if moved
	 */
	public boolean isLongCastlingPiecesMoved(Color color, Board board) {
		return isKingMoved(color, board) || isLongRookMoved(color, board);
	}
	
	/**
	 * Checks if king and short rook ever moved
	 * 
	 * @param color
	 *            color of player
	 * @return true if moved
	 */
	public boolean isShortCastlingPiecesMoved(Color color, Board board) {
		return isKingMoved(color, board) || isShortRookMoved(color, board);
	}

}
