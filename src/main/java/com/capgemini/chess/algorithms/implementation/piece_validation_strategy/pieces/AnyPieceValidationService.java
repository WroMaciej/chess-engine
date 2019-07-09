package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.PieceValidationStrategy;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.CoordinatesFiltering;

/**
 * Strategy elements common for all pieces
 * @author MACIEWRO
 *
 */
public abstract class AnyPieceValidationService implements PieceValidationStrategy {
	protected CoordinatesFiltering coordinatesFiltering;
	
	

	public AnyPieceValidationService() {
		coordinatesFiltering = new CoordinatesFiltering();
	}

	/**
	 * Collection of almost legal attack moves
	 * There are also coordinates which can lead to own king check
	 * @param piecePosition
	 * @param board
	 * @return
	 */
	protected abstract Collection<Coordinate> legalAttackWithKingInCheckCoordinates(Coordinate piecePosition, Board board);
	
	/**
	 * Collection of almost legal capture moves
	 * There are also coordinates which can lead to own king check
	 * @param piecePosition
	 * @param board
	 * @return
	 */
	protected abstract Collection<Coordinate> legalCaptureWithKingInCheckCoordinates(Coordinate piecePosition, Board board);
	
	
	/**
	 * Collection of legal attack coordinates basing on coordinates including own king checking
	 * @param piecePosition
	 * @param board
	 * @return
	 */
	protected Collection<Coordinate> legalAttackCoordinates(Coordinate piecePosition, Board board){
		Collection<Coordinate> legalAttackWithKingInCheckCoordinates = legalAttackWithKingInCheckCoordinates(piecePosition, board);
		Collection<Coordinate> legalAttackCoordinates = coordinatesFiltering.onlyFreeOfOwnCheckCoordinates(piecePosition, legalAttackWithKingInCheckCoordinates, MoveType.ATTACK, board);
		return legalAttackCoordinates;
	}

	/**
	 * Collection of legal capture coordinates basing on coordinates including own king checking
	 * @param piecePosition
	 * @param board
	 * @return
	 */
	protected Collection<Coordinate> legalCaptureCoordinates(Coordinate piecePosition, Board board){
		Collection<Coordinate> legalCaptureWithKingInCheckCoordinates = legalCaptureWithKingInCheckCoordinates(piecePosition, board);
		Collection<Coordinate> legalCaptureCoordinates = coordinatesFiltering.onlyFreeOfOwnCheckCoordinates(piecePosition, legalCaptureWithKingInCheckCoordinates, MoveType.CAPTURE, board);
		return legalCaptureCoordinates;
	}


	/**
	 * Collects legal attack and legal capture moves
	 */
	@Override
	public Map<Coordinate, Move> allLegalMoves(Coordinate piecePosition, Board board) {
		Map<Coordinate, Move> legalAttackAndCaptureMoves = new HashMap<>();
		Move moveToAdd;
		Collection<Coordinate> allLegalAttackCoordinates = legalAttackCoordinates(piecePosition, board);
		Collection<Coordinate> allLegalCaptureCoordinates = legalCaptureCoordinates(piecePosition, board);
				
		for (Coordinate legalAttackCoordinate : allLegalAttackCoordinates){
			moveToAdd = new Move(piecePosition, legalAttackCoordinate, MoveType.ATTACK, board.getPieceAt(piecePosition));
			legalAttackAndCaptureMoves.put(legalAttackCoordinate, moveToAdd);
		}
		for (Coordinate legalCaptureCoordinate : allLegalCaptureCoordinates){
			moveToAdd = new Move(piecePosition, legalCaptureCoordinate, MoveType.CAPTURE, board.getPieceAt(piecePosition));
			legalAttackAndCaptureMoves.put(legalCaptureCoordinate, moveToAdd);
		}
		return legalAttackAndCaptureMoves;
	}


	/**
	 * Collects ownKingInCheck moves
	 * default is from attack and capture
	 */
	@Override
	public Collection<Coordinate> allOwnKingInCheckMoves(Coordinate piecePosition, Board board) {
		//get own king check moves from difference of almost legal and legal ATTACK and CAPTURE positions
		
		//remove from ATTACK
		Collection<Coordinate> ownKingInCheckAttackCoordinates = legalAttackWithKingInCheckCoordinates(piecePosition, board);
		ownKingInCheckAttackCoordinates.removeAll(legalAttackCoordinates(piecePosition, board));
		//remove from CAPTURE
		Collection<Coordinate> ownKingInCheckCaptureCoordinates = legalCaptureWithKingInCheckCoordinates(piecePosition, board);
		ownKingInCheckCaptureCoordinates.removeAll(legalCaptureCoordinates(piecePosition, board));
		//sum attack and capture
		Collection<Coordinate> ownKingInCheckFromAttackAndCaptureCoordinates = new ArrayList<>();
		ownKingInCheckFromAttackAndCaptureCoordinates.addAll(ownKingInCheckAttackCoordinates);
		ownKingInCheckFromAttackAndCaptureCoordinates.addAll(ownKingInCheckCaptureCoordinates);
		return ownKingInCheckFromAttackAndCaptureCoordinates;
	}
	
	
	

	

	
	
}
