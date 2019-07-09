package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.BishopRookValidationService.StraightMoveDirection;


/**
 * Strategy of move validation for bishop
 */
public class KingValidationService extends AnyPieceValidationService {
	
	private static final int CASTLING_KING_DISTANCE = 2;
		
	public KingValidationService() {
		super();
	}
	

	

	@Override
	protected Collection<Coordinate> legalAttackWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		// remove taken coordinates from these on board
		return coordinatesFiltering
				.onlyNotTakenCoordinates(legalOnBoardWithKingInCheckCoordinates(piecePosition, board), board);

	}


	@Override
	protected Collection<Coordinate> legalCaptureWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		// leave only coordinates taken by opponent
		return coordinatesFiltering
				.onlyTakenByOpponentCoordinates(piecePosition, legalOnBoardWithKingInCheckCoordinates(piecePosition, board), board);
	}

	private Collection<Coordinate> legalCastlingWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		Collection<Coordinate> bothCastlingKingCoordinates = new ArrayList<>();
		Color kingColor = board.getPieceAt(piecePosition).getColor();
		
		boolean isLongCastlingPiecesNotMoved = !coordinatesFiltering.getCastlingValidator().isLongCastlingPiecesMoved(kingColor , board);
		boolean isShortCastlingPiecesNotMoved = !coordinatesFiltering.getCastlingValidator().isShortCastlingPiecesMoved(kingColor , board);
		boolean isLongCastlingPathEmpty = coordinatesFiltering.onlyNotTakenCoordinates(emptySpaceForLongCastlingCoordinates(piecePosition), board).size() == 3;
		boolean isShortCastlingPathEmpty = coordinatesFiltering.onlyNotTakenCoordinates(emptySpaceForShortCastlingCoordinates(piecePosition), board).size() == 2;
		//add coordinate if castling pieces didnt move and path of move is free on other pieces
		if(isLongCastlingPiecesNotMoved && isLongCastlingPathEmpty){
			bothCastlingKingCoordinates.add(longCastlingKingFinalCoordinate(piecePosition));
		}
		if (isShortCastlingPiecesNotMoved && isShortCastlingPathEmpty){
			bothCastlingKingCoordinates.add(shortCastlingKingFinalCoordinate(piecePosition));
		}
		return bothCastlingKingCoordinates;
	}
	
	private Collection<Coordinate> legalCastlingCoordinates(Coordinate piecePosition, Board board) {
		Collection<Coordinate> bothCastlingKingCoordinates = legalCastlingWithKingInCheckCoordinates(piecePosition, board);
		boolean isLongCastlingAllowed = bothCastlingKingCoordinates.contains(longCastlingKingFinalCoordinate(piecePosition));
		boolean isShortCastlingAllowed = bothCastlingKingCoordinates.contains(shortCastlingKingFinalCoordinate(piecePosition));
		if (!isLongCastlingAllowed && !isShortCastlingAllowed) return new ArrayList<>();
		//set coordinates which should not be in check
		Collection<Coordinate> notInCheckLongCastlingCoordinates = emptySpaceForLongCastlingCoordinates(piecePosition);
		notInCheckLongCastlingCoordinates.add(piecePosition);
		notInCheckLongCastlingCoordinates.remove(new Coordinate(1, piecePosition.getY())); //rook path could be in check
		Collection<Coordinate> notInCheckShortCastlingCoordinates = emptySpaceForShortCastlingCoordinates(piecePosition);
		notInCheckShortCastlingCoordinates.add(piecePosition);
		
		//remove checking
		if (isLongCastlingAllowed){
			notInCheckLongCastlingCoordinates = coordinatesFiltering.onlyFreeOfOwnCheckCoordinates(piecePosition, notInCheckLongCastlingCoordinates, MoveType.CASTLING, board);
		}
		if (isShortCastlingAllowed){
			notInCheckShortCastlingCoordinates = coordinatesFiltering.onlyFreeOfOwnCheckCoordinates(piecePosition, notInCheckShortCastlingCoordinates, MoveType.CASTLING, board);
		}
		
		boolean isLongCastlingPathFreeOfCheck = notInCheckLongCastlingCoordinates.size() == 3;
		boolean isShortCastlingPathFreeOfCheck = notInCheckShortCastlingCoordinates.size() == 3;
		
		bothCastlingKingCoordinates = new ArrayList<>();
		if(isLongCastlingAllowed && isLongCastlingPathFreeOfCheck){
			bothCastlingKingCoordinates.add(longCastlingKingFinalCoordinate(piecePosition));
		}
		if (isShortCastlingAllowed && isShortCastlingPathFreeOfCheck){
			bothCastlingKingCoordinates.add(shortCastlingKingFinalCoordinate(piecePosition));
		}
		return bothCastlingKingCoordinates;
		
	}
	
	/**
	 * All positions around king which lays on board
	 * @param piecePosition
	 * @param board
	 * @return
	 */
	private Collection<Coordinate> legalOnBoardWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		Collection<Coordinate> onBoardCoordinates = new ArrayList<>();
		// populate coordinates around king using moving vectors from BishopRook
		for (StraightMoveDirection direction : BishopRookValidationService.StraightMoveDirection.values()) {
			onBoardCoordinates.add(new Coordinate(piecePosition.getX() + direction.moveVectorX, piecePosition.getY() + direction.moveVectorY));
		}
		// remove coordinates outside board
		onBoardCoordinates = coordinatesFiltering.onlyOnBoardCoordinates(onBoardCoordinates);
		return onBoardCoordinates;
	}


	@Override
	public Map<Coordinate, Move> allLegalMoves(Coordinate piecePosition, Board board) {
		// gets all attack and capture moves
		Map<Coordinate, Move> allLegalMoves = super.allLegalMoves(piecePosition, board);
		// add castling
		Move moveToAdd;
		Collection<Coordinate> legalCastlingCoordinates = legalCastlingCoordinates(piecePosition, board);
		for (Coordinate legalEnPassantCoordinate : legalCastlingCoordinates) {
			moveToAdd = new Move(piecePosition, legalEnPassantCoordinate, MoveType.CASTLING,
					board.getPieceAt(piecePosition));
			allLegalMoves.put(legalEnPassantCoordinate, moveToAdd);
		}
		return allLegalMoves;
	}

	@Override
	public Collection<Coordinate> allOwnKingInCheckMoves(Coordinate piecePosition, Board board) {
		Collection<Coordinate> attackAndCaptureKingInCheckCoordinates = super.allOwnKingInCheckMoves(piecePosition,
				board);
		// add castling
		Collection<Coordinate> castlingKingInCheckCoordinates = legalCastlingWithKingInCheckCoordinates(piecePosition,
				board);
		castlingKingInCheckCoordinates.removeAll(legalCastlingCoordinates(piecePosition, board));
		Collection<Coordinate> attackCaptureCastlingKingInCheckCoordinates = new ArrayList<>();
		attackCaptureCastlingKingInCheckCoordinates.addAll(attackAndCaptureKingInCheckCoordinates);
		attackCaptureCastlingKingInCheckCoordinates.addAll(castlingKingInCheckCoordinates);
		return attackCaptureCastlingKingInCheckCoordinates;
	}
	
	private Collection<Coordinate> emptySpaceForLongCastlingCoordinates(Coordinate piecePosition){
		Collection<Coordinate> emptySpaceForLongCastlingCoordinates = new ArrayList<>();
		emptySpaceForLongCastlingCoordinates.add(new Coordinate(1, piecePosition.getY()));
		emptySpaceForLongCastlingCoordinates.add(new Coordinate(2, piecePosition.getY()));
		emptySpaceForLongCastlingCoordinates.add(new Coordinate(3, piecePosition.getY()));
		return emptySpaceForLongCastlingCoordinates;
	}
	
	private Collection<Coordinate> emptySpaceForShortCastlingCoordinates(Coordinate piecePosition){
		Collection<Coordinate> emptySpaceForShortCastlingCoordinates = new ArrayList<>();
		emptySpaceForShortCastlingCoordinates.add(new Coordinate(5, piecePosition.getY()));
		emptySpaceForShortCastlingCoordinates.add(new Coordinate(6, piecePosition.getY()));
		return emptySpaceForShortCastlingCoordinates;
	}
	
	private Coordinate longCastlingKingFinalCoordinate(Coordinate piecePosition){
		return new Coordinate(piecePosition.getX() - CASTLING_KING_DISTANCE, piecePosition.getY());
	}
	
	private Coordinate shortCastlingKingFinalCoordinate(Coordinate piecePosition){
		return new Coordinate(piecePosition.getX() + CASTLING_KING_DISTANCE, piecePosition.getY());
	}

}
