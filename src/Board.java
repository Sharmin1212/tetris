
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import sun.audio.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alu20919409n
 */
public class Board extends JPanel implements ActionListener {

    class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (timer.isRunning()) {
                        if (canMoveTo(currentShape, currentRow, currentCol - 1)) {
                            currentCol--;
                        }
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (timer.isRunning()) {
                        if (canMoveTo(currentShape, currentRow, currentCol + 1)) {
                            currentCol++;
                        }
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (timer.isRunning()) {
                        Shape rotShape = currentShape.rotateRight();
                        if (canMoveTo(rotShape, currentRow, currentCol)) {
                            currentShape = rotShape;
                        }
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (timer.isRunning()) {
                        if (canMoveTo(currentShape, currentRow + 1, currentCol)) {
                            currentRow++;
                        }
                    }
                    break;
                case KeyEvent.VK_P:
                    if (!gameOver) {
                        if (timer.isRunning()) {
                            timer.stop();
                            AudioPlayer.player.stop(audios);
                            scoreBoard.paused();
                        } else {
                            timer.start();
                            AudioPlayer.player.start(audios);
                            scoreBoard.resume();
                        }
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (timer.isRunning()) {
                        while (canMoveTo(currentShape, currentRow + 1, currentCol)) {
                            currentRow++;
                        }
                    }
                    break;
                default:
                    break;
            }
            repaint();
        }
    }

    private JFrame parentFrame;

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }
    
    public ScoreBoard scoreBoard;
    private NextPiecePanel nextPiecePanel;

    public static final int NUM_ROWS = 22;
    public static final int NUM_COLS = 10;

    private Tetrominoes[][] matrix;
    private int deltaTime;

    private Shape currentShape;
    private int currentRow;
    private int currentCol;

    private Timer timer;

    boolean gameOver = false;

    MyKeyAdapter keyAdepter;

    AudioStream audios = null;
    AudioStream audioEf = null;

    public static final int INIT_ROW = -2;

    public Board() {
        super();
        matrix = new Tetrominoes[NUM_ROWS][NUM_COLS];
        initValues();
        timer = new Timer(deltaTime, this);
        keyAdepter = new MyKeyAdapter();
    }

    public void setNextPiecePanel(NextPiecePanel p) {
        nextPiecePanel = p;
    }

    public void initValues() {
        setFocusable(true);
        cleanBoard();
        deltaTime = 500;
        currentShape = null;
        currentRow = INIT_ROW;
        currentCol = NUM_COLS / 2;
    }

    public void initGame() {
        AudioPlayer.player.stop(audios);
        removeKeyListener(keyAdepter);
        initValues();
        timer.setDelay(deltaTime);
        nextPiecePanel.generateNewShape();
        currentShape = Shape.getRandomShape();
        timer.start();
        addKeyListener(keyAdepter);
        gameOver = false;

        playSong("tetris.wav");

    }

    public void cleanBoard() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                matrix[row][col] = Tetrominoes.NoShape;
            }
        }
    }

    private boolean canMoveTo(Shape shape, int newRow, int newCol) {
        if ((newCol + shape.getXmin() < 0) || (newCol + shape.getXmax() >= NUM_COLS) || (newRow + shape.getYmax() >= NUM_ROWS) || hitWithMatrix(shape, newRow, newCol)) {
            return false;
        }
        return true;
    }

    private boolean hitWithMatrix(Shape shape, int row, int col) {
        int[][] squaresArray = shape.getCoordinates();
        for (int point = 0; point <= 3; point++) {
            int hRow = row + squaresArray[point][1];
            int hCol = col + squaresArray[point][0];
            if (hRow >= 0) {
                if (matrix[hRow][hCol] != Tetrominoes.NoShape) {
                    return true;
                }
            }
        }
        return false;
    }

    //Game Main Loop
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (canMoveTo(currentShape, currentRow + 1, currentCol)) {
            currentRow++;
            repaint();
        } else {
            if (currentShape.getYmin() + currentRow < 0) {
                try {
                    gameOver();
                } catch (IOException ex) {
                }
                timer.stop();
            } else {
                moveCurrentShapeToMatrix();
                checkRows();
                currentShape = nextPiecePanel.getShape();
                nextPiecePanel.generateNewShape();
                currentRow = INIT_ROW;
                currentCol = NUM_COLS / 2;
            }

        }
    }

    private void moveCurrentShapeToMatrix() {
        int[][] squaresArray = currentShape.getCoordinates();
        for (int point = 0; point <= 3; point++) {
            //matrix[currentRow + squaresArray[point][1]][currentCol + squaresArray[point][0]] = currentShape.getShape();
            int row = currentRow + squaresArray[point][1];
            int col = currentCol + squaresArray[point][0];
            matrix[row][col] = currentShape.getShape();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        if (currentShape != null) {
            currentShape.draw(g, currentRow, currentCol, squareWidth(), squareHeight());
        }

        drawBoarder(g);
    }

    public void drawBoarder(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(0, 0, NUM_COLS * squareWidth(), NUM_ROWS * squareHeight());
    }

    public void drawBoard(Graphics g) {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Util.drawSquare(g, row, col, matrix[row][col], squareWidth(), squareHeight());
            }
        }
    }

    private int squareWidth() {
        return getWidth() / NUM_COLS;
    }

    private int squareHeight() {
        return getHeight() / NUM_ROWS;
    }

    public void checkRows() {
        for (int i = 0; i < NUM_ROWS; i++) {
            int counter = 0;
            for (int j = 0; j < NUM_COLS; j++) {
                if (matrix[i][j] != Tetrominoes.NoShape) {
                    counter++;
                }
                if (counter == 10) {
                    playEffect("LineEffect.wav");
                    scoreBoard.increment(100);
                    if (scoreBoard.getScore() % 500 == 0 && deltaTime > 100) {
                        deltaTime = deltaTime - 100;
                        timer.setDelay(deltaTime);
                    }

                    for (int k = i; k > 0; k--) {
                        for (int l = 0; l < NUM_COLS; l++) {
                            matrix[k][l] = matrix[k - 1][l];
                        }
                    }
                    for (int k = 0; k < NUM_COLS; k++) {
                        matrix[0][k] = Tetrominoes.NoShape;
                    }
                }
            }
        }
    }

    public void setScoreboard(ScoreBoard scoreboard) {
        this.scoreBoard = scoreboard;
    }

    public void playSong(String song) {
        InputStream music;
        try {
            music = new FileInputStream(new File(song));
            audios = new AudioStream(music);
            AudioPlayer.player.start(audios);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    public void playEffect(String effect) {
        InputStream music;
        try {
            music = new FileInputStream(new File(effect));
            audioEf = new AudioStream(music);
            AudioPlayer.player.start(audioEf);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    public void gameOver() throws IOException {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                matrix[row][col] = Tetrominoes.ZShape;
            }
            currentShape = null;
            AudioPlayer.player.stop(audios);
            playSong("GameOver.wav");
            gameOver = true;
        }
        scoreBoard.gameOver();
        RecordsDialog d = new RecordsDialog(parentFrame, true, scoreBoard.getScore());
        d.setVisible(true);

    }
}
