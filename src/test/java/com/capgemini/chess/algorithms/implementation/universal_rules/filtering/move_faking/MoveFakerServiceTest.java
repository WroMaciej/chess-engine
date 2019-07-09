package com.capgemini.chess.algorithms.implementation.universal_rules.filtering.move_faking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.BoardManager;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.universal_rules.filtering.move_faking.MoveFakerService;

public class MoveFakerServiceTest {

	@Test
	public void shouldCauseOwnCheck() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(3, 0));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 0));
		MoveFakerService moveFaker = new MoveFakerService();
		Move whiteQueenExposesKing = new Move(new Coordinate(3, 0), new Coordinate(3, 1), MoveType.ATTACK,
				Piece.WHITE_KING);
		// when
		boolean isMoveCauseOwnCheck = moveFaker.isFakeMoveCauseOwnCheck(whiteQueenExposesKing, board);
		// then
		assertTrue(isMoveCauseOwnCheck);
	}

	@Test
	public void shouldNotCauseOwnCheck() {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(4, 0));
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(3, 0));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(0, 0));
		MoveFakerService moveFaker = new MoveFakerService();
		Move whiteQueenExposesKing = new Move(new Coordinate(3, 0), new Coordinate(0, 0), MoveType.CAPTURE,
				Piece.WHITE_KING);
		// when
		boolean isMoveCauseOwnCheck = moveFaker.isFakeMoveCauseOwnCheck(whiteQueenExposesKing, board);
		// then
		assertFalse(isMoveCauseOwnCheck);
	}
	
	@Test
	public void shouldEnPassantCauseOwnCheck() throws InvalidMoveException  {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(0, 4));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(3, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(4, 6));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(7, 4));
		MoveFakerService moveFaker = new MoveFakerService();
		BoardManager boardManager = new BoardManager(board);
		boardManager.performMove(new Coordinate(0, 4), new Coordinate(1, 4));
		boardManager.performMove(new Coordinate(4, 6), new Coordinate(4, 4));
		// when
		Move whitePawnEnPassant = new Move(new Coordinate(3, 4), new Coordinate(4, 5), MoveType.EN_PASSANT, Piece.WHITE_PAWN);
		boolean isMoveCauseOwnCheck = moveFaker.isFakeMoveCauseOwnCheck(whitePawnEnPassant, board);
		// then
		assertTrue(isMoveCauseOwnCheck);
	}
	
	@Test
	public void shouldPawnAttackNotCauseOwnCheck() throws InvalidMoveException {
		// given
		Board board = new Board();
		board.setPieceAt(Piece.WHITE_KING, new Coordinate(0, 4));
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(3, 4));
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(4, 6));
		board.setPieceAt(Piece.BLACK_ROOK, new Coordinate(7, 4));
		MoveFakerService moveFaker = new MoveFakerService();
		BoardManager boardManager = new BoardManager(board);
		boardManager.performMove(new Coordinate(0, 4), new Coordinate(1, 4));
		boardManager.performMove(new Coordinate(4, 6), new Coordinate(4, 4));
		// when
		Move whitePawnAttack = new Move(new Coordinate(3, 4), new Coordinate(3, 5), MoveType.ATTACK, Piece.WHITE_PAWN);
		boolean isMoveCauseOwnCheck = moveFaker.isFakeMoveCauseOwnCheck(whitePawnAttack, board);
		// then
		assertFalse(isMoveCauseOwnCheck);
	}
	
	@Test
	public void shouldReturnArrayCopy(){
		//given
		Board originalBoard = new Board();
		MoveFakerService moveFakerService = new MoveFakerService();
		//Piece [][] original = new Piece[Board.SIZE][Board.SIZE];
		for (int x = 0; x < Board.SIZE; x++){
			for (int y = 0; y < Board.SIZE; y++){
				originalBoard.setPieceAt(Piece.WHITE_PAWN, new Coordinate(x, y));
			}
		}
		//when
		boolean isAnyElementNotEqual = false;
		Piece[][] piecesCopy = moveFakerService.piecesSetupDeepCopy(originalBoard);
		for (int x = 0; x < Board.SIZE; x++){
			for (int y = 0; y < Board.SIZE; y++){
				if (!originalBoard.getPieceAt(new Coordinate(x, y)).equals(piecesCopy[x][y])){
					isAnyElementNotEqual = true;
				}
			}
		}
		//then
		assertFalse(isAnyElementNotEqual);
	}

}
