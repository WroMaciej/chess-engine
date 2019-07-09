package com.capgemini.chess.algorithms.data;

import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;

/**
 * Chess move definition.
 * 
 * @author Michal Bejm
 *
 */
public class Move {
	
	public Move(){}
	
	public Move(Coordinate from, Coordinate to, MoveType type, Piece movedPiece) {
		this.from = from;
		this.to = to;
		this.type = type;
		this.movedPiece = movedPiece;
	}

	/**
	 * Starting coordinates
	 */
	private Coordinate from;
	/**
	 * Final coordinates
	 */
	private Coordinate to;
	/**
	 * Type of move (attack, castling...)
	 */
	private MoveType type;
	/**
	 * Piece performed the move
	 * In case of castling it is KING move!
	 */
	private Piece movedPiece;

	public Coordinate getFrom() {
		return from;
	}

	public void setFrom(Coordinate from) {
		this.from = from;
	}

	public Coordinate getTo() {
		return to;
	}

	public void setTo(Coordinate to) {
		this.to = to;
	}

	public MoveType getType() {
		return type;
	}

	public void setType(MoveType type) {
		this.type = type;
	}

	public Piece getMovedPiece() {
		return movedPiece;
	}

	public void setMovedPiece(Piece movedPiece) {
		this.movedPiece = movedPiece;
	}

	@Override
	public String toString() {
		return "Move [from=" + from + ", to=" + to + ", type=" + type + ", movedPiece=" + movedPiece + "]";
	}
	
	
}
