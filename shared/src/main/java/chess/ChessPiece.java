package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition current) {
        if(type == PieceType.PAWN){
            return pawnMoves(board, current);
        } else if(type == PieceType.ROOK){
            return rookMoves(board, current);
        } else if(type == PieceType.KNIGHT){
            return knightMoves(board, current);
        } else if(type == PieceType.BISHOP){
            return bishopMoves(board, current);
        } else if(type == PieceType.QUEEN){
            return queenMoves(board, current);
        } else if(type == PieceType.KING){
            return kingMoves(board, current);
        }
        return null;
    }

    /**
     * Helper function that checks if a ChessPosition is on the board.
     * @param position
     * @return true if the position is on the board.
     */
    private boolean checkBounds(ChessPosition position){
        if(position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8){
            return true;
        }
        return false;
    }

    /**
     * Helper function for pawns, bishops, and rooks. Determines if a piece can land on a square (i.e. move forward)
     * @param board
     * @param position
     * @return true if a piece can land on that square.
     */
    private boolean canLand(ChessBoard board, ChessPosition position){
        if(checkBounds(position) && board.getPiece(position) == null){
            return true;
        }
        return false;
    }

    /**
     * Helper function for pawns, bishops and rooks. Determins if a piece can take on a square (i.e. its diagonal moves)
     * @param board
     * @param position
     * @return true if a piece can take on that square.
     */
    private boolean canTake(ChessBoard board, ChessPosition position){
        if(!checkBounds(position)){
            return false;
        }
        ChessPiece occupant = board.getPiece(position);
        if(occupant != null && occupant.getTeamColor() != pieceColor){
            return true;
        }
        return false;
    }

    /**
     * Helper function for knights and kings. Determines if they can land or take on a square.
     * @param board
     * @param position
     * @return true if a knight or king could land on that square.
     */
    private boolean canMove(ChessBoard board, ChessPosition position){
        if(canLand(board, position) || canTake(board, position)){
            return true;
        }
        return false;
    }

    /**
     * Helper function for bishops and rooks. Adds all possible moves in one direction to passed collection.
     * @param board
     * @param current
     * @param directions
     * @param moves
     */
    private void checkOneDirection(ChessBoard board, ChessPosition current, int[] directions, Collection<ChessMove> moves){
        int row = current.getRow();
        int col = current.getColumn();
        int count = 1;
        ChessPosition test;

        while(true){
            test = new ChessPosition(row + directions[0] * count, col + directions[1] * count);
            if(!checkBounds(test)){
                break;
            }else if(canTake(board, test)){
                moves.add(new ChessMove(current, test, null));
                break;
            }else if(canLand(board, test)){
                moves.add(new ChessMove(current, test, null));
            }else{
                break;
            }
            count++;
        }
    }

    /**
     * Helper function for knights and kings. Their possible landing squares are passed in, and this function adds all viable move to the passed collection.
     * @param board
     * @param positions
     * @param current
     * @param moves
     */
    private void validateMoves(ChessBoard board, int[][] positions, ChessPosition current, Collection<ChessMove> moves){
        int row = current.getRow();
        int col = current.getColumn();
        ChessPosition test;
        for(int i = 0; i < positions.length; i++){
            test = new ChessPosition(row + positions[i][0], col + positions[i][1]);
            if(checkBounds(test) && canMove(board, test)){
                moves.add(new ChessMove(current, test, null));
            }
        }
    }

    /**
     * Helper function for the pawnMoves function. Adds the correct pawn moves depending if the move is a promotion or not
     * @param current
     * @param end
     * @param moves
     */
    private void addPawnMove(ChessPosition current, ChessPosition end, Collection<ChessMove> moves){
        int row = current.getRow();
        if((pieceColor == ChessGame.TeamColor.BLACK && row == 2) || (pieceColor == ChessGame.TeamColor.WHITE && row == 7)){
            moves.add(new ChessMove(current, end, PieceType.QUEEN));
            moves.add(new ChessMove(current, end, PieceType.ROOK));
            moves.add(new ChessMove(current, end, PieceType.KNIGHT));
            moves.add(new ChessMove(current, end, PieceType.BISHOP));
        }else{
            moves.add(new ChessMove(current, end, null));
        }
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition current){
        Collection<ChessMove> moves = new ArrayList<>();
        int row = current.getRow();
        int col = current.getColumn();
        int direction = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        boolean isStart = (pieceColor == ChessGame.TeamColor.WHITE && row == 2) || (pieceColor == ChessGame.TeamColor.BLACK && row == 7) ? true : false;
        int[][] takePositions = {{direction, -1}, {direction, 1}};
        ChessPosition test;
        for(int i = 0; i < takePositions.length; i++){
            test = new ChessPosition(row + takePositions[i][0], col + takePositions[i][1]);
            if(checkBounds(test) && canTake(board, test)){
                addPawnMove(current, test, moves);
            }
        }
        test = new ChessPosition(row + direction, col);
        if(checkBounds(test) && canLand(board, test)){
            addPawnMove(current, test, moves);
            test = new ChessPosition(row + direction * 2, col);
            if(isStart && checkBounds(test) && canLand(board, test)){
                addPawnMove(current, test, moves);
            }
        }
        return moves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition current){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for(int i = 0; i < directions.length; i++){
            checkOneDirection(board, current, directions[i], moves);
        }
        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition current){
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] positions = {{2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}};
        validateMoves(board, positions, current, moves);
        return moves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition current){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
        for(int i = 0; i < directions.length; i++){
            checkOneDirection(board, current, directions[i], moves);
        }
        return moves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition current){
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(rookMoves(board, current));
        moves.addAll(bishopMoves(board, current));
        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition current){
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] positions = {{1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};
        validateMoves(board, positions, current, moves);
        return moves;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pieceColor == null) ? 0 : pieceColor.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        ChessPiece other = (ChessPiece) obj;
        if (pieceColor != other.pieceColor)
            return false;
        if (type != other.type)
            return false;
        return true;
    }
}
