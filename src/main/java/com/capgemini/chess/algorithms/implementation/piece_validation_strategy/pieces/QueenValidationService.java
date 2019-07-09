package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import java.util.ArrayList;
import java.util.Collection;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.generated.Board;

/**
 * Move strategy for QUEEN
 * Constructed from Bishop and Rook moves
 * @author MACIEWRO
 *
 */
public class QueenValidationService extends AnyPieceValidationService{
	
	private BishopValidationService bishopMove; //all legal bishop moves
	private RookValidationService rookMove; //all legal rook moves


	public QueenValidationService() {
		super();
		//TODO think about injection
		bishopMove = new BishopValidationService();
		rookMove = new RookValidationService();
	}

	@Override
	protected Collection<Coordinate> legalAttackWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		Collection<Coordinate> bishopAndRookAttack = new ArrayList<>();
		//add attack moves from both bishop and rook
		bishopAndRookAttack.addAll(bishopMove.legalAttackWithKingInCheckCoordinates(piecePosition, board));
		bishopAndRookAttack.addAll(rookMove.legalAttackWithKingInCheckCoordinates(piecePosition, board));
		return bishopAndRookAttack;
	}

	@Override
	protected Collection<Coordinate> legalCaptureWithKingInCheckCoordinates(Coordinate piecePosition, Board board) {
		Collection<Coordinate> bishopAndRookCapture = new ArrayList<>();
		//add capture moves from both bishop and rook
		bishopAndRookCapture.addAll(bishopMove.legalCaptureWithKingInCheckCoordinates(piecePosition, board));
		bishopAndRookCapture.addAll(rookMove.legalCaptureWithKingInCheckCoordinates(piecePosition, board));
		return bishopAndRookCapture;
	}

	@Override
	public Collection<Coordinate> allOwnKingInCheckMoves(Coordinate piecePosition, Board board) {
		Collection<Coordinate> bishopAndRookOwnKingCheck = new ArrayList<>();
		//add kingInChech moves from both bishop and rook
		bishopAndRookOwnKingCheck.addAll(bishopMove.allOwnKingInCheckMoves(piecePosition, board));
		bishopAndRookOwnKingCheck.addAll(rookMove.allOwnKingInCheckMoves(piecePosition, board));
		return bishopAndRookOwnKingCheck;
	}
	
	
	
	

}
