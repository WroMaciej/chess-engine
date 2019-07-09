package com.capgemini.chess.algorithms.implementation.piece_validation_strategy.pieces;

import static org.junit.Assert.*;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.BoardManager;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

public class KingValidationServiceTest {

	@Test
	public void shouldAllowCastlingIfRookInCheckAndLeadToCheckOpponent() throws InvalidMoveException{
		//given
		Board board = new Board();
		BoardManager boardManager = new BoardManager(board);
		board.setPieceAt(Piece.BLACK_KING, new Coordinate(4, 7));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 7));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(7, 0));
		board.setPieceAt(Piece.WHITE_ROOK, new Coordinate(0, 0));
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(6, 7));
		//when
		boolean exceptionThrownIn1Move = false;
		try {
			boardManager.performMove(new Coordinate(7, 0), new Coordinate(1, 0));
		} catch (InvalidMoveException e) {
			exceptionThrownIn1Move = true;
		}
		boolean exceptionThrownIn2Move = false;
		try {
			boardManager.performMove(new Coordinate(4, 7), new Coordinate(2, 7));
		} catch (InvalidMoveException e) {
			exceptionThrownIn2Move = true;
		}
		boardManager.updateBoardState();
		// then		
		boolean isCheck = boardManager.getBoard().getState() == BoardState.CHECK;
		assertFalse(exceptionThrownIn1Move);
		assertFalse(exceptionThrownIn2Move);
		assertTrue(isCheck);
	}
}
