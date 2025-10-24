package msku.ceng.madlab.week5;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // player symbols for X and O
    static final String PLAYER_1 = "X";
    static final String PLAYER_2 = "O";

    // tracks which player's turn it is (true -> player 1, false -> player 2)
    boolean player1_Turn = true;

    // 3x3 game board to store player moves (0 = empty, 1 = X, 2 = O)
    byte[][]board = new byte[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // adjusts padding for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.board), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get the game board layout from XML
        TableLayout table = findViewById(R.id.board);

        // loop through rows and columns to set click listeners for each button
        for(int i=0; i<3; i++){
            TableRow  tableRow = (TableRow) table.getChildAt((i));
            for (int j=0; j<3; j++){
                Button button = (Button) tableRow.getChildAt(j);
                // assign a listener for each cell with its (row, column) position
                button.setOnClickListener(new CellListener(i,j));
            }
        }

    }

    // checks if the selected cell is empty (valid move)
    public boolean isValidMove(int row, int column){
        return board[row][column] == 0;
    }

    public int gameEnded(int row, int col) {
        int symbol = board[row][col];
        boolean win = true;

        // check column
        for (int i = 0; i < 3; i++) {
            if (board[i][col] != symbol) {
                win = false;
                break;
            }
        }
        if (win) return symbol;

        // check row
        win = true;
        for (int j = 0; j < 3; j++) {
            if (board[row][j] != symbol) {
                win = false;
                break;
            }
        }
        if (win) return symbol;

        // check main diagonal
        if (row == col) {
            win = true;
            for (int i = 0; i < 3; i++) {
                if (board[i][i] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return symbol;
        }

        // check anti-diagonal
        if (row + col == 2) {
            win = true;
            for (int i = 0; i < 3; i++) {
                if (board[i][2 - i] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return symbol;
        }

        // check for draw - when no cells left
        boolean draw = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    draw = false;
                    break;
                }
            }
        }
        if (draw) return 0;

        // otherwise, game continues
        return -1;
    }


    // handles click actions for each cell in the table
    class CellListener implements View.OnClickListener {
        int row, column; // stores the cell's row and column indexes

        public CellListener(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public void onClick(View v) {

            // prevents move if the cell is already filled
            if (!isValidMove(row, column)) {
                Toast.makeText(MainActivity.this, "Cell is already occupied", Toast.LENGTH_LONG).show();
                return;
            }

            // marks the cell depending on which player's turn it is
            if (player1_Turn) {
                ((Button) v).setText(PLAYER_1);
                board[row][column] = 1;
            } else {
                ((Button) v).setText(PLAYER_2);
                board[row][column] = 2;
            }

            // checks if the game has ended after each move
            if(gameEnded(row,column) == -1){
                // if game continues, switch turns
                player1_Turn =!player1_Turn;
            }
            else if(gameEnded(row,column) == 0){
                // game ended in a draw
                Toast.makeText(MainActivity.this, "It's a draw",Toast.LENGTH_LONG).show();
            }
            else if(gameEnded(row,column) == 1){
                // player 1 wins
                Toast.makeText(MainActivity.this, "Player 1 Wins",Toast.LENGTH_LONG).show();
            }
            else{
                // player 2 wins
                Toast.makeText(MainActivity.this, "Player 2 Wins",Toast.LENGTH_LONG).show();
            }
        }
    }



    // saves game progress before activity is destroyed (e.g. during rotation)
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("Player1Turn",player1_Turn);

        // converts 2D board array into 1D array for saving
        byte [] boardSingle = new byte[9];
        for(int i= 0; i<3; i++){
            for (int j=0; j<3; j++){
                boardSingle[3*i+j] = board[i][j];
            }
        }
        outState.putByteArray("board",boardSingle);
        super.onSaveInstanceState(outState);
    }

    // restores game progress when activity is recreated
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // restore turn info
        player1_Turn = savedInstanceState.getBoolean("Player1Turn");

        // restore board state from saved byte array
        byte [] boardSingle = savedInstanceState.getByteArray("board");
        for (int i = 0; i <9; i++){
            board[i/3][i%3] = boardSingle[i];
        }

        // update button texts according to restored board state
        TableLayout table = findViewById(R.id.board);
        for(int i= 0; i<3; i++){
            TableRow row = (TableRow) table.getChildAt(i);
            for(int j = 0; j<3; j++ ){
                Button button = (Button) row.getChildAt(j);
                if(board[i][j] == 1){
                    button.setText("X");
                }
                else if(board[i][j]==2){
                    button.setText("O");
                }
            }
        }
    }
}