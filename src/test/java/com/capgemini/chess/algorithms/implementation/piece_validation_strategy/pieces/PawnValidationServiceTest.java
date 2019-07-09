package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.BoardManager;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.PieceValidationStrategy;
import com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces.PawnValidationService;

public class PawnValidationServiceTest {
	
	@Test
	public void shouldHaveNoLegalMovesIfBlocked(){
		// given
		Board board = new Board();
		PieceValidationStrategy pawnStrategy = new PawnValidationService();
		Coordinate whitePawn = new Coordinate(1, 2);
		Coordinate blackPawn = new Coordinate(1, 3);
		board.setPieceAt(Piece.WHITE_PAWN, whitePawn);
		board.setPieceAt(Piece.BLACK_PAWN, blackPawn);
		
		//when
		Collection<Coordinate> legalMoves = pawnStrategy.allLegalMoves(whitePawn, board).keySet();
		
		//then
		assertEquals(0, legalMoves.size());
	}
	
	@Test
	public void shouldPerformDoubleAttackForNotMovedPawns(){
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		Coordinate whitePawn = new Coordinate(3, 1);
		Coordinate doubleWhiteAttack = new Coordinate(3, 3);
		Coordinate doubleBlackAttack = new Coordinate(3, 4);
		Coordinate blackPawn = new Coordinate(3, 6);
		board.setPieceAt(Piece.WHITE_PAWN, whitePawn);
		board.setPieceAt(Piece.BLACK_PAWN, blackPawn);
				
		//when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(whitePawn, doubleWhiteAttack);
			boardManager.performMove(blackPawn, doubleBlackAttack);
			
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertFalse(exceptionThrown);
	}
	
	@Test
	public void shouldPerformCapture(){
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		Coordinate whitePawn = new Coordinate(3, 3);
		Coordinate blackPawn = new Coordinate(4, 4);
		Coordinate whiteCapture = new Coordinate(4, 4);
		board.setPieceAt(Piece.WHITE_PAWN, whitePawn);
		board.setPieceAt(Piece.BLACK_PAWN, blackPawn);
				
		//when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(whitePawn, whiteCapture);			
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertFalse(exceptionThrown);
	}
	
	
	
	@Test
	public void shouldPerformEnPassant(){
		// given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		Coordinate whitePawnInit = new Coordinate(3, 3);
		Coordinate blackPawnInit = new Coordinate(4, 6);
		Coordinate whiteSingleAttack = new Coordinate(3, 4);
		Coordinate blackDoubleAttack = new Coordinate(4, 4);
		Coordinate whiteEnPassant = new Coordinate(4, 5);
		
		board.setPieceAt(Piece.WHITE_PAWN, whitePawnInit);
		board.setPieceAt(Piece.BLACK_PAWN, blackPawnInit);
				
		//when
		boolean exceptionThrown = false;
		try {
			boardManager.performMove(whitePawnInit, whiteSingleAttack);
			boardManager.performMove(blackPawnInit, blackDoubleAttack);
			boardManager.performMove(whiteSingleAttack, whiteEnPassant);
			
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}
		
		// then 
		assertFalse(exceptionThrown);
	}

}
