package com.example.cyslide.solver;

import com.example.cyslide.map.Map;
import com.example.cyslide.map.TilePane;
import com.example.cyslide.scene.game.mouvement.MovementDirection;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Node{

    private ArrayList<Integer> state, goalState;
    private MovementDirection movementDirection;
    private int cost, sizeX, sizeY;
    private Node up, down, left, right, previousState;
    private boolean parent, visited;

    public Node(Map map) {

        this.state = new ArrayList<>();
        this.movementDirection = MovementDirection.NONE;
        this.sizeX = map.getSizeX();
        this.sizeY = map.getSizeY();
        this.parent = true;
        this.previousState = null;

        for (int idY = 0; idY < sizeY; idY++) {
            for (int idX = 0; idX < sizeX; idX++) {
                TilePane tilePane = map.getTileByCoord(idX, idY);
                if (tilePane != null) {
                    state.add(tilePane.getTileId());
                }
            }
        }

        goalState = new ArrayList<Integer>(map.getGoalList());


        this.cost = getDistance();
    }

    public Node(Node previousState, MovementDirection movement, int sizeX, int sizeY) {

        this.parent = false;
        this.previousState = previousState;
        this.movementDirection = movement;
        this.state = new ArrayList<Integer>();
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.goalState = previousState.goalState;

        int emptyLocation;
        int swapLocation;

        emptyLocation = previousState.getState().indexOf(0);

        state.addAll(previousState.getState());

        switch (movement) {
            case UP:
                if (emptyLocation - sizeX >= 0) {
                    swapLocation = emptyLocation - sizeX;
                    state.set(emptyLocation, state.get(swapLocation));
                    state.set(swapLocation, 0);
                } else {
                    state = null;
                    this.movementDirection = MovementDirection.NONE;
                    return;
                }
                break;
            case DOWN:
                if (emptyLocation + sizeX < state.size()) {
                    swapLocation = emptyLocation + sizeX;
                    state.set(emptyLocation, state.get(swapLocation));
                    state.set(swapLocation, 0);
                } else {
                    state = null;
                    this.movementDirection = MovementDirection.NONE;
                    return;
                }
                break;
            case LEFT:
                if (emptyLocation % sizeX != 0) {
                    swapLocation = emptyLocation - 1;
                    state.set(emptyLocation, state.get(swapLocation));
                    state.set(swapLocation, 0);
                } else {
                    state = null;
                    this.movementDirection = MovementDirection.NONE;
                    return;
                }
                break;
            case RIGHT:
                if (emptyLocation % sizeX != sizeX - 1) {
                    swapLocation = emptyLocation + 1;
                    state.set(emptyLocation, state.get(swapLocation));
                    state.set(swapLocation, 0);
                } else {
                    state = null;
                    this.movementDirection = MovementDirection.NONE;
                    return;
                }
                break;
        }

        this.cost = previousState.getCost() + getDistance();

    }

    /**
     * Creates the children of the node.
     */
    public void generateChildren() {
        up = new Node(this, MovementDirection.UP, sizeX, sizeY);
        down = new Node(this, MovementDirection.DOWN, sizeX, sizeY);
        left = new Node(this, MovementDirection.LEFT, sizeX, sizeY);
        right = new Node(this, MovementDirection.RIGHT, sizeX, sizeY);
    }

    /**
     * Return the distance between the current state and the goal state, adding each the distance of each tile.
     *
     * @return The distance between the current state and the goal state.
     */
    public Integer getDistance() {
        int distance = 0;
        for (int i = 0; i < state.size(); i++) {
            if (state.get(i) != 0) {
                distance += Math.sqrt(Math.pow(i % sizeX - goalState.indexOf(state.get(i)) % sizeX, 2) + Math.pow(i / sizeX - goalState.indexOf(state.get(i)) / sizeX, 2));
            }
        }
        return distance;
    }

    /**
     * Retrieves the state of the map.
     *
     * @return The array of integers representing the state of the map.
     */
    public ArrayList<Integer> getState() {
        return state;
    }

    /**
     * Retrieves a list of the children of the node.
     *
     * @return A list of the children of the node.
     */
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        if (up.movementDirection != MovementDirection.NONE) {
            children.add(up);
        }
        if (down.movementDirection != MovementDirection.NONE) {
            children.add(down);
        }
        if (left.movementDirection != MovementDirection.NONE) {
            children.add(left);
        }
        if (right.movementDirection != MovementDirection.NONE) {
            children.add(right);
        }

        return children;
    }

    /**
     * Retrieves the cost of the node.
     *
     * @return Thr cost of the node.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Retrieves a list of the nodes that lead to the solution. Made by going through the previous states of the nodes.
     *
     * @return A list of the nodes that lead to the solution.
     */
    public List<Node> getSolution() {
        List<Node> solution = new LinkedList<>();
        Node node = this;
        while (node != null) {
            solution.add(node);
            node = node.previousState;
        }
        Collections.reverse(solution);
        return solution;
    }

    /**
     * Retrieves the movement direction of the node.
     *
     * @return The movement direction of the node.
     */
    public MovementDirection getMovementDirection() {
        return this.movementDirection;
    }

    /**
     * @return If the node is visited or not.
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Sets the node to visited or not.
     *
     * @param visited Sets the node to visited or not.
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
