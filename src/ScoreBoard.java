
import javax.swing.JLabel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alu20919409n
 */
public class ScoreBoard extends JLabel {

    private int score;

    public ScoreBoard() {
        super();
        score = 0;
        setText("Score: " + score);
    }

    public void increment(int points) {
        score += points;
        setText("Score: " + score);

    }

    public void reset() {
        score = 0;
        setText("Score: " + score);

    }

    public int getScore() {
        return score;
    }

    public void paused() {
        setText("PAUSED");
    }

    public void resume() {
        setText("Score: " + score);
    }

    public void gameOver() {
        setText("GAME OVER");
    }

}
