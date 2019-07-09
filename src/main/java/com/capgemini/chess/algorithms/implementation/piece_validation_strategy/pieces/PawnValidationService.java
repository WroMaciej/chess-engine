package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

/**
 * Strategy of move validation for pawn
 */
public class PawnValidationService extends AnyPieceValidationService {

	private final static int whitePawnRowToEnPassant = 4;
	private final static int blackPawnRowToEnPassant = 3;
	/**
	 * Number of row to enable en passant for both colors
	 */
	private final Map<Color, Integer> pawnRowToEnPassant;
	
	
	
	
	public PawnValidationService() {
		super();
		//populate rows needed to en passant
		pawnRowToEnPassant = new HashMap<>();
		pawnRowToEnPassant.put(Color.WHITE, Integer.valueOf(whitePawnRowToEnPassant));
		pawnRowToEnPassant.put(Color.BLACK, Integer.valueOf(blackPawnRowToEnPassant));
	}

	/**
	 * Adds en passant to attack and capture
	 */
	@Override
	public Map<Coordinate, Move> allLegalMoves(Coordinate piecePosition, Board board) {
		// gets all attack and capture moves
		Map<Coordinate, Move> allLegalMoves = super.allLegalMoves(piecePosition, board);
		// add en passant
		Move moveToAdd;
		Collection<Coordinate> allLegalEnPassantCoordinates = legalEnPassantCoordinates(piecePosition, board);
		for (Coordinate legalEnPassantCoordinate : allLegalEnPassantCoordinates) {
			moveToAdd = new Move(piecePosition, legalEnPassantCoordinate, MoveType.EN_PASSANT,
					board.getPieceAt(piecePosition));
			allLegalMoves.put(legalEnPassantCoordinate, moveToAdd);
		}
		return allLegalMoves;
	}

	@Override
	protected Collection<Coordinate> legalAttackWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		Collection<Coordinate> attackCoordinates = new ArrayList<>();
		Color pawnColor = board.getPieceAt(piecePosition).getColor();
		
		//regular walk by 1 field
		Coordinate regularWalk;
		regularWalk = new Coordinate(piecePosition.getX(), piecePosition.getY() + ( 1 * pawnColor.moveVector));
		attackCoordinates.add(regularWalk);
		
		//speed move by 2 fields if pawn didnt move
		if (!coordinatesFiltering.isEverMoved(piecePosition, board.getMoveHistory())){
			//because tests doesnt care about history it is necessary to force pawn position (cheating tests is not a good option...)
			if ((pawnColor == Color.WHITE && piecePosition.getY()==1) || (pawnColor == Color.BLACK && piecePosition.getY()==6))
			{
				Coordinate doubledWalk;
				doubledWalk = new Coordinate(piecePosition.getX(), piecePosition.getY() + ( 2 * pawnColor.moveVector));
				attackCoordinates.add(doubledWalk);
			}
		}
		//removes coordinates outside board
		attackCoordinates = coordinatesFiltering.onlyOnBoardCoordinates(attackCoordinates);
		//removes taken coordinates
		attackCoordinates = coordinatesFiltering.onlyNotTakenCoordinates(attackCoordinates, board);
	
		return attackCoordinates;
	}
	
	/**
	 * Gives both capture and en passant coordinates which are on board
	 * @param piecePosition
	 * @param board
	 * @return
	 */
	private Collection<Coordinate> captureAndEnPassantCoordinatesOnBoard(Coordinate piecePosition, Board board) {
		Collection<Coordinate> captureAndEnPassantCoordinates = new ArrayList<>();
		Color pawnColor = board.getPieceAt(piecePosition).getColor();

		// regular capture to left
		Coordinate captureToLeft;
		captureToLeft = new Coordinate(piecePosition.getX() - 1, piecePosition.getY() + (1 * pawnColor.moveVector));
		captureAndEnPassantCoordinates.add(captureToLeft);
		// regular capture to right
		Coordinate captureToRight;
		captureToRight = new Coordinate(piecePosition.getX() + 1, piecePosition.getY() + (1 * pawnColor.moveVector));
		captureAndEnPassantCoordinates.add(captureToRight);

		// removes coordinates outside board
		captureAndEnPassantCoordinates = coordinatesFiltering.onlyOnBoardCoordinates(captureAndEnPassantCoordinates);
		return captureAndEnPassantCoordinates;
	}

	@Override
	protected Collection<Coordinate> legalCaptureWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		Collection<Coordinate> captureCoordinates = captureAndEnPassantCoordinatesOnBoard(piecePosition, board);
		// stay only with coordinates taken by opponent
		captureCoordinates = coordinatesFiltering.onlyTakenByOpponentCoordinates(piecePosition, captureCoordinates, board);
		return captureCoordinates;
	}
	
	/**
	 * Gives coordinates of en passent moves
	 * @param piecePosition
	 * @param board
	 * @return
	 */
	private Collection<Coordinate> legalEnPassantWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		Collection<Coordinate> captureAndEnPassantCoordinates = captureAndEnPassantCoordinatesOnBoard(piecePosition, board);
		Collection<Coordinate> bothEnPassantCoordinates = new ArrayList<>();
		Color pawnColor = board.getPieceAt(piecePosition).getColor();
		//return empty collection if given pawn is on wrong position
		if (piecePosition.getY() != pawnRowToEnPassant.get(pawnColor)) return new ArrayList<>();
		//get last moved piece
		Move lastMove = board.lastMove();
		Piece lastMovePiece = lastMove.getMovedPiece();
		//return empty collection if last moved piece was different than pawn
		if (lastMovePiece.getType() != PieceType.PAWN) return new ArrayList<>();
		Coordinate lastMoveFrom = lastMove.getFrom();
		Coordinate lastMoveTo = lastMove.getTo();
		for (Coordinate enPassantCaptureCoordinate : captureAndEnPassantCoordinates){
			//check that last move x position from and to are equal capturing coordinate
			boolean isSprintedInCorrectColumn = lastMoveFrom.getX() == enPassantCaptureCoordinate.getX() && lastMoveTo.getX() == enPassantCaptureCoordinate.getX();
			//check if move was sprint
			boolean isSprintedInCorrectRow = lastMoveFrom.getY() == enPassantCaptureCoordinate.getY() - lastMovePiece.getColor().moveVector
					&& lastMoveTo.getY() == enPassantCaptureCoordinate.getY() + lastMovePiece.getColor().moveVector;
			boolean isOpponentsPawnSprinted = isSprintedInCorrectColumn && isSprintedInCorrectRow;
			if (isOpponentsPawnSprinted){
					bothEnPassantCoordinates.add(enPassantCaptureCoordinate);
			}
		}
		//get both (if any) en passant coordinates
		return bothEnPassantCoordinates;
	}

	/**
	 * All legal en passant coordinates for given pawn`s position
	 * @param piecePosition
	 * @param board
	 * @return
	 */
	protected Collection<Coordinate> legalEnPassantCoordinates(Coordinate piecePosition, Board board) {
		Collection<Coordinate> legalEnPassantWithKingInCheckCoordinates = legalEnPassantWithKingInCheckCoordinates(piecePosition, board);
		Collection<Coordinate> legalEnPassantCoordinates = coordinatesFiltering.onlyFreeOfOwnCheckCoordinates(piecePosition, legalEnPassantWithKingInCheckCoordinates, MoveType.EN_PASSANT, board);
		return legalEnPassantCoordinates;
	}
	
	


	@Override
	public Collection<Coordinate> allOwnKingInCheckMoves(Coordinate piecePosition, Board board) {
		Collection<Coordinate> attackAndCaptureKingInCheckCoordinates = super.allOwnKingInCheckMoves(piecePosition, board);
		//ADD EN PASSANT
		Collection<Coordinate> enPassantKingInCheckCoordinates = legalEnPassantWithKingInCheckCoordinates(piecePosition, board);
		enPassantKingInCheckCoordinates.removeAll(legalEnPassantCoordinates(piecePosition, board));
		Collection<Coordinate> attackCaptureEnPassantKingInCheckCoordinates = new ArrayList<>();
		attackCaptureEnPassantKingInCheckCoordinates.addAll(attackAndCaptureKingInCheckCoordinates);
		attackCaptureEnPassantKingInCheckCoordinates.addAll(enPassantKingInCheckCoordinates);
		return attackCaptureEnPassantKingInCheckCoordinates;
	}


	

}
