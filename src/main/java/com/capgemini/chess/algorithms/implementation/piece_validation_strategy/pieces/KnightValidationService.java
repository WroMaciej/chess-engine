package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import java.util.ArrayList;
import java.util.Collection;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.generated.Board;


/**
 * Strategy of move validaton for knight
 */
public class KnightValidationService extends AnyPieceValidationService {
	

	/**
	 * All knight moves which lays on board, even if its taken by any other piece
	 * @param piecePosition
	 * @return stream with all coordinates
	 */
	private Collection<Coordinate> knightOnBoardCoordinates(Coordinate piecePosition){
		Collection<Coordinate> knightCoordinates = new ArrayList<>();
		knightCoordinates.add(new Coordinate(piecePosition.getX() + 1, piecePosition.getY() + 2));
		knightCoordinates.add(new Coordinate(piecePosition.getX() + 2, piecePosition.getY() + 1));
		knightCoordinates.add(new Coordinate(piecePosition.getX() + 2, piecePosition.getY() - 1));
		knightCoordinates.add(new Coordinate(piecePosition.getX() + 1, piecePosition.getY() - 2));
		knightCoordinates.add(new Coordinate(piecePosition.getX() - 1, piecePosition.getY() - 2));
		knightCoordinates.add(new Coordinate(piecePosition.getX() - 2, piecePosition.getY() - 1));
		knightCoordinates.add(new Coordinate(piecePosition.getX() - 2, piecePosition.getY() + 1));
		knightCoordinates.add(new Coordinate(piecePosition.getX() - 1, piecePosition.getY() + 2));
		//remove positions outside board
		knightCoordinates = coordinatesFiltering.onlyOnBoardCoordinates(knightCoordinates);
		return knightCoordinates;	
	}

	@Override
	protected Collection<Coordinate> legalAttackWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		//remove taken positions from on board coordinates
		return coordinatesFiltering.onlyNotTakenCoordinates(knightOnBoardCoordinates(piecePosition), board);
	}

	@Override
	protected Collection<Coordinate> legalCaptureWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		//leave only coordinates taken by opponent from all on board moves
		return coordinatesFiltering.onlyTakenByOpponentCoordinates(piecePosition, knightOnBoardCoordinates(piecePosition), board);
	}


}
