package com.mygdx.game;

import java.util.PriorityQueue;
import java.util.ArrayList;

public class Grid {
    
    Cell[][] cells;
    Teleporter teleporters[];
    Cell end;
    PriorityQueue<Path> queue = new PriorityQueue<Path>(100000);
    
    public Grid(Cell[][] cells) {
        this.cells = cells;
        int numberOfTeleporters = 0;
        ArrayList<Teleporter> teleporters = new ArrayList(0);
        for (Cell[] row : cells)
            for (Cell cell : row)
                if (cell instanceof Teleporter)
                    teleporters.add((Teleporter)cell);
        this.teleporters = teleporters.toArray(new Teleporter[teleporters.size()]);
    }

    /* Create paths from start to each neighbor.
     * Paths keep track of cells along the path
     * as well as the total terrain crossed.
     *
     * When teleporters are visited, their links
     * are immediately added to the path.
     *
     * Neighbors which have been visited will not
     * be put through this procedure.
     */
    public Path findPath(Cell start, Cell e) {
        Path current;
        if (queue.size() == 0) {
            this.end = e;
            current = new Path(start, this);
            for (Cell neighbor : current.getHead().getNeighbors()) {
                if (neighbor == end) {
                        current.addCell(neighbor);
                        queue.clear();
                        end = null;
                        return current;
                } else {
                    queue.add(new Path(current, neighbor, this));
                }
            }
        }
        current = queue.poll();
        Cell c = current.getHead();
        c.visit();
        if ((c instanceof Teleporter)) {
            if (!((Teleporter)c).teleported()) {
                Path newTeleporterPath = new Path(current, ((Teleporter)c).getLink(), this);
                c.visit();
                ((Teleporter)c).teleport();
                ((Teleporter)c).getLink().visit();
                if (((Teleporter)c).getLink() == end) {
                    queue.clear();
                    end = null;
                    return newTeleporterPath;
                }
                current = newTeleporterPath;
                for (Cell neighbor : current.getHead().getNeighbors()) {
                    if (!neighbor.wasVisited() || (neighbor instanceof Teleporter && !(((Teleporter)neighbor).teleported()))) {
                        if (neighbor == end) {
                            current.addCell(neighbor);
                            queue.clear();
                            end = null;
                            return current;
                        }
                        Path newPath = new Path(current, neighbor, this);
                        boolean pathDealtWith = false;
                        for (Path path : queue.toArray(new Path[queue.size()]))
                            if (path.getHead() == newPath.getHead()) {
                                if (newPath.compareTo(path) < 0) {
                                    queue.remove(path);
                                    queue.add(newPath);
                                    pathDealtWith = true;
                                } else {
                                    pathDealtWith = true;
                                }
                                break;
                            }
                        if (!pathDealtWith)
                            queue.add(newPath);
                    }
                }
            }
        } else {
            for (Cell neighbor : current.getHead().getNeighbors()) {
                if (!neighbor.wasVisited() || (neighbor instanceof Teleporter && !(((Teleporter)neighbor).teleported()))) {
                    if (neighbor == end) {
                        current.addCell(neighbor);
                        queue.clear();
                        start = null;
                        end = null;
                        return current;
                    }
                    Path newPath = new Path(current, neighbor, this);
                    boolean pathDealtWith = false;
                    for (Path path : queue.toArray(new Path[queue.size()]))
                        if (path.getHead() == newPath.getHead()) {
                            pathDealtWith = true;
                            if (newPath.compareTo(path) < 0) {
                                queue.remove(path);
                                queue.add(newPath);
                            }
                            break;
                        }
                    if (!pathDealtWith)
                        queue.add(newPath);
                }
            }
        }
        return null;
    }

    private void resetPathFinding() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.resetPathFindingVariables();
                if (cell instanceof Teleporter)
                    ((Teleporter)cell).reset();
            }
        }
    }

    public void resetStates() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.resetPathFindingVariables();
                cell.deselect();
                if (cell instanceof Teleporter)
                    ((Teleporter)cell).reset();
            }
        }
    }

    public int getHeuristic(Cell c) {
        if (c instanceof Teleporter)
            c = ((Teleporter)c).getLink();
        float distance = Math.abs((end.getSprite().getX() - c.getSprite().getX()))/c.getSprite().getWidth()
            + Math.abs((end.getSprite().getY() - c.getSprite().getY()))/c.getSprite().getHeight();
        for (Teleporter teleporter : teleporters) {
            if (!teleporter.teleported()) {
                float toTeleporter = Math.abs((teleporter.getSprite().getX() - c.getSprite().getX()))/c.getSprite().getWidth()
                    + Math.abs((teleporter.getSprite().getY() - c.getSprite().getY()))/c.getSprite().getHeight();
                float fromTeleporter = Math.abs((end.getSprite().getX() - teleporter.getLink().getSprite().getX()))/c.getSprite().getWidth()
                    + Math.abs((end.getSprite().getY() - teleporter.getLink().getSprite().getY()))/c.getSprite().getHeight();
                float distanceWithTeleporter = toTeleporter + fromTeleporter;
                if (distanceWithTeleporter < distance)
                    distance = distanceWithTeleporter;
            }
        }
        return (int)distance;
        
    }
}
