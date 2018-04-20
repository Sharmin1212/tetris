
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alu20919409n
 */
public class NextPiecePanel extends JPanel {

    private Shape shape;
    private Tetrominoes[][] matrixNextPiece;
    public static final int NUM_ROWS_COLS = 4;

    /**
     * Creates new form NextPiecePanel
     */
    public NextPiecePanel() {
        initComponents();
        shape = Shape.getRandomShape();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (shape != null) {
            shape.draw(g, 1, 1, squareWidth(), squareHeight());
        }
        drawBorder(g);
    }

    public Shape getShape() {
        return shape;
    }

    public void generateNewShape() {
        shape = Shape.getRandomShape();
        repaint();
    }

    private int squareWidth() {
        return getWidth() / NUM_ROWS_COLS;
    }

    private int squareHeight() {
        return getHeight() / NUM_ROWS_COLS;
    }

   
    public void drawBorder(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(0, 0, NUM_ROWS_COLS * squareWidth(), NUM_ROWS_COLS * squareHeight());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 129, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
