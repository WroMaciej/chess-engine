package com.capgemini.chess.algorithms.data.enums;

/**
 * Chess piece definition
 * 
 * @author Michal Bejm
 *
 */
public enum Piece {
	
	WHITE_KING(PieceType.KING, Color.WHITE),
    WHITE_QUEEN(PieceType.QUEEN, Color.WHITE),
    WHITE_BISHOP(PieceType.BISHOP, Color.WHITE),
    WHITE_KNIGHT(PieceType.KNIGHT, Color.WHITE),
    WHITE_ROOK(PieceType.ROOK, Color.WHITE),
    WHITE_PAWN(PieceType.PAWN, Color.WHITE),
    BLACK_KING(PieceType.KING, Color.BLACK),
    BLACK_QUEEN(PieceType.QUEEN, Color.BLACK),
    BLACK_BISHOP(PieceType.BISHOP, Color.BLACK),
    BLACK_KNIGHT(PieceType.KNIGHT, Color.BLACK),
    BLACK_ROOK(PieceType.ROOK, Color.BLACK),
    BLACK_PAWN(PieceType.PAWN, Color.BLACK);

    private final PieceType type;
    private final Color color;

    Piece(PieceType type, Color color) {
        this.type = type;
        this.color = color;
    }
    
    /**
     * Returns piece basics on its type and color
     * @param type piece type
     * @param color piece color
     * @return piece as enum
     */
    public static Piece pieceByTypeAndColor(PieceType type, Color color){
    	for (Piece piece : Piece.values()){
    		if (piece.type == type && piece.color == color) return piece;
    	}
    	throw new IllegalArgumentException("Searching piece doesnt exist. Type: " + type + " Color: " + color);
    }
    
    

	public PieceType getType() {
		return type;
	}

	public Color getColor() {
		return color;
	}
	
}
