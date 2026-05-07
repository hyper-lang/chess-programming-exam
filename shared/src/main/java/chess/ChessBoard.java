package chess;

import java.util.HashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private HashMap<ChessPosition, ChessPiece> board = new HashMap<>();

    public ChessBoard() {}

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board.get(position);
    }

    /**
     * Helper function for reset board. Sets a rectangle of pieces, based on a quadrant 3 position.
     * @param position
     * @param type
     */
    private void setRectangle(ChessPosition position, ChessPiece.PieceType type){
        int row = position.getRow();
        int col = position.getColumn();
        addPiece(new ChessPosition(row, col), new ChessPiece(ChessGame.TeamColor.WHITE, type));
        addPiece(new ChessPosition(row, 9 - col), new ChessPiece(ChessGame.TeamColor.WHITE, type));
        addPiece(new ChessPosition(9 - row, col), new ChessPiece(ChessGame.TeamColor.BLACK, type));
        addPiece(new ChessPosition(9 - row, 9 - col), new ChessPiece(ChessGame.TeamColor.BLACK, type));
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new HashMap<ChessPosition, ChessPiece>();
        for(int i = 1; i < 5; i++){
            setRectangle(new ChessPosition(2, i), ChessPiece.PieceType.PAWN);
        }
        setRectangle(new ChessPosition(1, 1), ChessPiece.PieceType.ROOK);
        setRectangle(new ChessPosition(1, 2), ChessPiece.PieceType.KNIGHT);
        setRectangle(new ChessPosition(1, 3), ChessPiece.PieceType.BISHOP);
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((board == null) ? 0 : board.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChessBoard other = (ChessBoard) obj;
        if (board == null) {
            if (other.board != null)
                return false;
        } else if (!board.equals(other.board))
            return false;
        return true;
    }
}