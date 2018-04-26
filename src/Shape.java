
import java.awt.Graphics;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alu20919409n
 */
public class Shape {

    private Tetrominoes pieceShape;
    private int[][] coordinates;
    private static int[][][] coordsTable = new int[][][]{
        {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
        {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
        {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
        {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
        {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
        {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
        {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
        {{1, -1}, {0, -1}, {0, 0}, {0, 1}}
    };

    public Shape(Tetrominoes pieceShape) {
        this.pieceShape = pieceShape;
        coordinates = new int[4][2];
        for (int point = 0; point < coordinates.length; point++) {
            coordinates[point][0] = coordsTable[pieceShape.ordinal()][point][0];
            coordinates[point][1] = coordsTable[pieceShape.ordinal()][point][1];
        }
    }

    public Shape() {
        int randomNumber = (int) (Math.random() * 7 + 1);
        pieceShape = Tetrominoes.values()[randomNumber];
        coordinates = new int[4][2];
        for (int point = 0; point < coordinates.length; point++) {
            coordinates[point][0] = coordsTable[randomNumber][point][0];
            coordinates[point][1] = coordsTable[randomNumber][point][1];
        }
    }

    public static Shape getRandomShape() {
        return new Shape();
    }

    public int[][] getCoordinates() {
        return coordinates;
    }

    public Tetrominoes getShape() {
        return pieceShape;
    }

    public Shape rotateRight() {
        Shape rotatedShape = new Shape(pieceShape);
        for (int point = 0; point < rotatedShape.coordinates.length; point++) {
            rotatedShape.coordinates[point][0] = coordinates[point][0];
            rotatedShape.coordinates[point][1] = coordinates[point][1];
        }
        if (pieceShape != Tetrominoes.SquareShape) {
            for (int point = 0; point < rotatedShape.coordinates.length; point++) {
                int temp = rotatedShape.coordinates[point][0];
                rotatedShape.coordinates[point][0] = rotatedShape.coordinates[point][1];
                rotatedShape.coordinates[point][1] = -temp;
            }
        }
        return rotatedShape;
    }

    public int getXmin() {
        int candidate = coordinates[0][0];
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i][0] < candidate) {
                candidate = coordinates[i][0];
            }
        }
        return candidate;
    }

    public int getXmax() {
        int candidate = coordinates[0][0];
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i][0] > candidate) {
                candidate = coordinates[i][0];
            }
        }
        return candidate;
    }

    public int getYmin() {
        int candidate = coordinates[0][1];
        for (int i = 1; i < coordinates.length; i++) {
            if (coordinates[i][1] < candidate) {
                candidate = coordinates[i][1];
            }
        }
        return candidate;
    }

    public int getYmax() {
        int candidate = coordinates[0][1];
        for (int i = 1; i < coordinates.length; i++) {
            if (coordinates[i][1] > candidate) {
                candidate = coordinates[i][1];
            }
        }
        return candidate;
    }
}
