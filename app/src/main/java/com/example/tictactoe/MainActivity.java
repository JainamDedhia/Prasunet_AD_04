// src/main/java/com/example/tictactoe/MainActivity.java
package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button[] buttons = new Button[9];
    private int[] board = new int[9];
    private int currentPlayer = 1; // 1 for X, 2 for O
    private boolean gameActive = true;
    private int[] winLine = null; // Array to store winning line indices
    private Dialog winnerDialog; // Dialog to show winner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            buttons[i] = (Button) gridLayout.getChildAt(i);
            board[i] = 0;
        }

        winnerDialog = new Dialog(this);
        winnerDialog.setContentView(R.layout.dialog_winner);
        winnerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button okButton = winnerDialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                winnerDialog.dismiss();
                resetGameWithDelay();
            }
        });
    }

    public void onGridButtonClick(View view) {
        if (!gameActive) {
            return;
        }

        Button clickedButton = (Button) view;
        int buttonIndex = Integer.parseInt(clickedButton.getTag().toString());

        if (board[buttonIndex] == 0) {
            board[buttonIndex] = currentPlayer;
            clickedButton.setText(currentPlayer == 1 ? "X" : "O");
            if (checkForWin()) {
                gameActive = false;
                String winner = currentPlayer == 1 ? "X" : "O";
                String message = "Player " + winner + " wins!";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                showWinnerDialog(message);
                highlightWinningLine();
            } else if (isBoardFull()) {
                gameActive = false;
                Toast.makeText(this, "It's a draw!", Toast.LENGTH_LONG).show();
                showWinnerDialog("It's a draw!");
            } else {
                currentPlayer = currentPlayer == 1 ? 2 : 1;
            }
        }
    }

    public void onResetButtonClick(View view) {
        resetGame();
    }

    private void resetGame() {
        gameActive = true;
        currentPlayer = 1;
        winLine = null; // Clear winning line
        for (int i = 0; i < 9; i++) {
            board[i] = 0;
            buttons[i].setText("");
            buttons[i].setBackgroundResource(android.R.drawable.btn_default); // Reset button backgrounds
        }
    }

    private void resetGameWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetGame();
            }
        }, 1000); // Delay in milliseconds
    }

    private boolean checkForWin() {
        int[][] winPositions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Horizontal
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Vertical
                {0, 4, 8}, {2, 4, 6}             // Diagonal
        };

        for (int[] winPosition : winPositions) {
            if (board[winPosition[0]] == board[winPosition[1]] &&
                    board[winPosition[1]] == board[winPosition[2]] &&
                    board[winPosition[0]] != 0) {
                winLine = winPosition; // Store the winning line indices
                return true;
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i : board) {
            if (i == 0) {
                return false;
            }
        }
        return true;
    }

    private void highlightWinningLine() {
        if (winLine != null) {
            for (int index : winLine) {
                buttons[index].setBackgroundResource(R.color.purple_200); // Highlight winning buttons
            }
        }
    }

    private void showWinnerDialog(String message) {
        TextView winnerTextView = winnerDialog.findViewById(R.id.winnerTextView);
        winnerTextView.setText(message);
        winnerDialog.show();
    }
}
