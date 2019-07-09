package com.capgemini.chess.algorithms.data.generated;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.BoardState;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;

/**
 * Board representation.
 * Board objects are generated based on move history.
 * 
 * @author Michal Bejm
 *
 */
public class Board {
	
	/**
	 * Number of chessboard`s fields used in 1 row and 1 column
	 */
	public static final int SIZE = 8;
	/**
	 * Representation of chessboard and pieces on it
	 */
	private Piece[][] pieces = new Piece[SIZE][SIZE];
	/**
	 * History of all moves done in game
	 */
	private List<Move> moveHistory = new ArrayList<>();
	/**
	 * Type of situation on the board (check...)
	 */
	private BoardState state;
	
	private boolean isFakeBoard = false;
	
	
	/**
	 * Checks if pieces needed for castling moved
	 * Key - Color of player
	 * Value - information about castling`s pieces moves
	 */
	
	public Board() {
	}
	
	public Board(Piece[][] pieceSetup){
		this.pieces = pieceSetup;
	}
	
	public Board(Piece[][] pieceSetup, List<Move> moveHistory){
		this.pieces = pieceSetup;
		this.moveHistory = moveHistory;
	}

	public List<Move> getMoveHistory() {
		return moveHistory;
	}

	public Piece[][] getPieces() {
		return pieces;
	}

	public BoardState getState() {
		return state;
	}

	public void setState(BoardState state) {
		this.state = state;
	}
	
	/**
	 * Sets chess piece on board based on given coordinates
	 * 
	 * @param piece chess piece
	 * @param board chess board
	 * @param coordinate given coordinates
	 */
	public void setPieceAt(Piece piece, Coordinate coordinate) {
		pieces[coordinate.getX()][coordinate.getY()] = piece;
	}
	
	/**
	 * Gets chess piece from board based on given coordinates
	 * 
	 * @param coordinate given coordinates
	 * @return chess piece
	 * @throws InvalidMoveException 
	 */
	public Piece getPieceAt(Coordinate coordinate) {
		return pieces[coordinate.getX()][coordinate.getY()];
	}
	
	public Move lastMove(){
		if (moveHistory.isEmpty()) return null;
		else return moveHistory.get(moveHistory.size() - 1);
	}
	
	/**
	 * Which color should move now
	 * @return
	 */
	public Color actualColor(){
		if (moveHistory.isEmpty()) return Color.WHITE;
		else return lastMove().getMovedPiece().getColor().getOppositeColor();
	}

	public boolean isFakeBoard() {
		return isFakeBoard;
	}

	public void setFakeBoard(boolean isFakeBoard) {
		this.isFakeBoard = isFakeBoard;
	}

	
	
	
	

}
