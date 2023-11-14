package com.belaschinke.webgamebackend.service.tocTacToe;

import com.belaschinke.webgamebackend.entity.Player;
import com.belaschinke.webgamebackend.service.GameInterface;
import com.belaschinke.webgamebackend.service.messageProtocol.GameUpdateResponse;
import com.belaschinke.webgamebackend.service.messageProtocol.TurnRequest;
import com.belaschinke.webgamebackend.service.messageProtocol.TurnResponse;
import com.belaschinke.webgamebackend.service.messageProtocol.TurnResponseUpdateWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class TicTacToeGame implements GameInterface {
    private int[][] board;
    private int turn;
    private int winner;
    private Player player1;
    private Player player2;

    public TicTacToeGame(Player player1, Player player2) {
        board = new int[3][3];
        turn = new Random().nextInt(2) + 1;
        winner = 0;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void play(int x, int y, TurnResponse turnResponse) {
        //legal move?
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            turnResponse.setErrorMsg("Out of bounds move!");
            return;
        }
        if (board[x][y] != 0) {
            turnResponse.setErrorMsg("Spot already taken!");
            return;
        }
        //legal move
        turnResponse.setValid(true);
        board[x][y] = turn;
        checkWinner();
        //set winner
        if (winner != 0) {
            turnResponse.setWin(winner == turn);
            turnResponse.setLose(winner != turn && winner != -1);
            turnResponse.setDraw(winner == -1);
        }
        turn = turn == 1 ? 2 : 1;
    }

    private void checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                winner = board[i][0];
                return;
            }
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                winner = board[0][i];
                return;
            }
        }
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            winner = board[0][0];
            return;
        }
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            winner = board[0][2];
            return;
        }
        if (boardFull()) {
            winner = -1;
        }
    }

    private boolean boardFull() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public TurnResponseUpdateWrapper handleTurn(TurnRequest turnRequest) {
        TurnResponse turnResponse = new TurnResponse();
        turnResponse.setValid(false);
        int playerNumber = turnRequest.getPlayerId() == player1.getId() ? 1 : 2;
        //game ended
        if (winner != 0) {
            turnResponse.setErrorMsg("Game already finished!");
            turnResponse.setDraw(winner == -1);
            turnResponse.setWin(winner == playerNumber);
            turnResponse.setLose(winner != playerNumber && winner != -1);
            return new TurnResponseUpdateWrapper(turnResponse, null);
        }
        //player not in game
        if (turnRequest.getPlayerId() != player1.getId() && turnRequest.getPlayerId() != player2.getId()) {
            turnResponse.setErrorMsg("Player not in this game!");
            return new TurnResponseUpdateWrapper(turnResponse, null);
        }
        //wrong turn
        if (playerNumber != turn) {
            turnResponse.setErrorMsg("Not your turn!");
            return new TurnResponseUpdateWrapper(turnResponse, null);
        }

        play(turnRequest.getX(), turnRequest.getY(), turnResponse);

        //create update
        if (!turnResponse.isValid()) {
            return new TurnResponseUpdateWrapper(turnResponse, null);
        }
        GameUpdateResponse gameUpdateResponse = new GameUpdateResponse();
        gameUpdateResponse.setX(turnRequest.getX());
        gameUpdateResponse.setY(turnRequest.getY());
        gameUpdateResponse.setPlayerId(turnRequest.getPlayerId());
        gameUpdateResponse.setRoomId(-1);//filled in upper layer
        gameUpdateResponse.setWin(turnResponse.isLose());
        gameUpdateResponse.setLose(turnResponse.isWin());
        gameUpdateResponse.setDraw(turnResponse.isDraw());
        gameUpdateResponse.setGameEnd(winner != 0);

        return new TurnResponseUpdateWrapper(turnResponse, gameUpdateResponse);
    }
}
