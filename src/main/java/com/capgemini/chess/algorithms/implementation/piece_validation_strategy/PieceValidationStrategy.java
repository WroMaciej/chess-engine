package com.capgemini.chess.algorithms.implementation.piece_validation_strategy;

import java.util.Collection;
import java.util.Map;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.generated.Board;

public interface PieceValidationStrategy {
	
	/**
	 * Map of all legal moves
	 * @param piecePosition position of piece to be moved
	 * @param board situation of game
	 * @return map of all legal moves
	 * Key - final coordinate
	 * Value - Move
	 */
	public Map<Coordinate, Move> allLegalMoves(Coordinate piecePosition, Board board);
	
	
	/**
	 * Collection of all moves leading to check of own king
	 * @param piecePosition position of piece to be moved
	 * @param board situation of game
	 * @return collection of all moves leading to make a check for own king
	 */
	public Collection<Coordinate> allOwnKingInCheckMoves(Coordinate piecePosition, Board board);
	
	
	

}
