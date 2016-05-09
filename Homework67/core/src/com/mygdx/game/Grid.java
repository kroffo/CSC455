package com.mygdx.game;

import java.util.PriorityQueue;
import java.util.ArrayList;

public class Grid {
    
    Cell[][] cells;
    PriorityQueue<Path> queue = new PriorityQueue<Path>(100);
    int i = 0;
    
    public Grid(Cell[][] cells) {
        this.cells = cells;
    }


    /* Uses Dijkstra */
    public Path findPath(Cell start, Cell e) {
        Path current;
        current = new Path(start);
        queue.add(current);
        while (true) {
            if (queue.size() == 0) {
                return null;
            }
            for (Cell neighbor : current.getHead().getNeighbors()) {
                if (neighbor != null) {
                    if (!neighbor.wasVisited()) {
                        if (neighbor == e) {
                            neighbor.visit();
                            current.addCell(neighbor);
                            queue.clear();
                            return current;
                        } else {
                            if (!neighbor.occupied())
                                queue.add(new Path(current, neighbor));
                            neighbor.visit();
                        }
                    }
                }
            }
            current = queue.poll();
        }
    }
}
