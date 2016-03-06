package com.mygdx.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MyGdxGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;
    private Cell cellGrid[][];
    private Cell selection1, selection2;
    private Grid grid;
    private int gridRows;
    private int gridCols;
    private boolean clicked, pathFound, pathFinding;
    private float screenHeight, screenWidth;

    int count;
    
    @Override
    public void create () {
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        batch = new SpriteBatch();
        File inputFile = Gdx.files.internal("Input.txt").file();
        createGridFromFile(inputFile);
        setNeighborReferencesForGrid(cellGrid);
        grid = new Grid(cellGrid);
        
        
        
        camera = new OrthographicCamera(screenHeight, screenWidth);
        
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            if (!clicked) {
                select(Gdx.input.getX(),Gdx.input.getY());
                clicked = true;
            }
        } else
            clicked = false;
        if (pathFinding && ++count > 1) {
            count = 0;
            Path path = grid.findPath(selection1, selection2);
            if (path != null) {
                cross(path);
                selection1 = null;
                selection2 = null;
                pathFinding = false;
                pathFound = true;
            }
        }
        drawGrid();
        batch.end();
    }

    private void cross(Path path) {
        for (Cell cell : path.getOrderedArray()) {
            cell.cross();
        }
    }

    private void select(int x, int y) {
        if (pathFound) {
            grid.resetStates();
            pathFound = false;
        }
        if (!pathFinding) {
            Vector3 input = new Vector3(x,y,0);
            camera.unproject(input);
            for (int row = 0; row < gridRows; row++)
                for (int col = 0; col < gridCols; col++)
                    if (cellGrid[row][col].getSprite().getBoundingRectangle().contains(input.x,input.y) && !(cellGrid[row][col] instanceof Barrier)) {
                        if (cellGrid[row][col].select()) {
                            if (selection1 != null) {
                                selection2 = cellGrid[row][col];
                                pathFinding = true;
                            } else
                                selection1 = cellGrid[row][col];
                        } else {
                            selection1 = null;
                        }
                        break;
                    }
        }
    }
        
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 3, 0);
        }
        camera.update();
    }

    
    
    private void drawGrid() {
        for (int row = 0; row < cellGrid.length; row++)
            for (int col = 0; col < cellGrid[row].length; col++) {
                Cell cell = cellGrid[row][col];
                Sprite sprite = cell.getSprite();
                sprite.draw(batch);
                float x = sprite.getX() + (sprite.getWidth()/2);
                float y = sprite.getY() + (sprite.getHeight()/2);
                font.draw(batch, cell.getText(), x, y);
            }
    }

    private void setDimensions(File f) {
        try {
            Scanner reader = new Scanner(f);
            int rows = 1;
            int cols = 0;
            String[] tokens = reader.nextLine().split(" +");
            cols = tokens.length;
            while (reader.hasNextLine()) {
                rows++;
                reader.nextLine();
            }
            gridRows = rows;
            gridCols = cols;
            cellGrid = new Cell[gridRows][gridCols];
        } catch (FileNotFoundException e) {
            System.out.println("Error -- File Not Found");
            System.exit(0);
        }
    }

    private void createGridFromFile(File f) {
        try {
            setDimensions(f);
            Scanner reader = new Scanner(f);
            int row = 0;
            while (reader.hasNextLine()) {
                String[] tokens = reader.nextLine().split(" +");
                for (int col = 0; col < gridCols; col++) {
                    if (tokens[col].equalsIgnoreCase("F") || tokens[col].equalsIgnoreCase("B")) {
                        cellGrid[row][col] = new Barrier();
                    } else if (tokens[col].substring(0,1).equalsIgnoreCase("T")) {
                        cellGrid[row][col] = new Teleporter(tokens[col].substring(1));
                    } else {
                        cellGrid[row][col] = new Terrain(Integer.parseInt(tokens[col]));
                    }
                    cellGrid[row][col].setPosition(row, col, screenHeight);
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error -- File Not Found");
            System.exit(0);
        }
    }


    /* Loop through the cellGrid to add neighbor references to all cells. */
    private void setNeighborReferencesForGrid(Cell[][] cellGrid) {
        ArrayList<LinkAddress> linkerList = new ArrayList<LinkAddress>(0);

        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                Cell cell = cellGrid[row][col];
                if (! (cell instanceof Barrier)) {
                    if (cell instanceof Teleporter) {            
                        boolean linkNotCompleted= true;
                        for (Object linko : linkerList.toArray()) {
                            LinkAddress link = (LinkAddress)linko;
                            if (link.getId().equals(cell.getText())) {
                                Teleporter partner = (Teleporter)cellGrid[link.getRow()][link.getCol()];
                                ((Teleporter)cell).setLink((Teleporter)partner);
                                partner.setLink((Teleporter)cell);
                                linkNotCompleted = false;
                                break;
                            }
                        }
                        if (linkNotCompleted) {
                            linkerList.add(new LinkAddress(cell.getText(), row, col));
                        }
                    }
                    if (row > 0)
                        if (! (cellGrid[row-1][col] instanceof Barrier))
                            cell.addNeighbor(cellGrid[row-1][col]);
                    if (row < gridRows - 1)
                        if (! (cellGrid[row+1][col] instanceof Barrier))
                            cell.addNeighbor(cellGrid[row+1][col]);
                    if (col > 0)
                        if (! (cellGrid[row][col-1] instanceof Barrier))
                            cell.addNeighbor(cellGrid[row][col-1]);
                    if (col < gridCols - 1)
                        if (! (cellGrid[row][col+1] instanceof Barrier))
                            cell.addNeighbor(cellGrid[row][col+1]);
                }
            }
        }
    }
}
