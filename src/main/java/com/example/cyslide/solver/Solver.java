package com.example.cyslide.solver;

import com.example.cyslide.map.Map;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Solver {

    private List<Node> solution;
    private Node initialState;

    /**
     * Constructs a Solver object.
     */
    public Solver() {
    }

    /**
     * Solves the map using the specified solver type.
     * @param currentMap The current map
     * @param solverType The type of solver to use
     * @return The list of nodes representing the solution
     */
    public List<Node> solve(Map currentMap, SolverType solverType) {

        initialState = new Node(currentMap);

        switch (solverType) {

            case A_STAR:

                LocalDateTime start = LocalDateTime.now();

                List<List<Integer>> existingNodes = new ArrayList<>();

                PriorityQueue<Node> nonVisitedNodes = new PriorityQueue<Node>((compared, comparedWith) -> {

                    if(compared.isVisited() && comparedWith.isVisited()){
                        return 0;
                    }else if(!compared.isVisited() && !comparedWith.isVisited()){
                        if (compared.getCost() < comparedWith.getCost()) {
                            return -1;
                        } else if (compared.getCost() > comparedWith.getCost()) {
                            return 1;
                        } else {
                            return -1;
                        }

                    }else if(compared.isVisited() && !comparedWith.isVisited()){
                        return 1;
                    }else{
                        return -1;
                    }
                }
                );

                LocalDateTime end = LocalDateTime.now();

                nonVisitedNodes.add(initialState);
                existingNodes.add(initialState.getState());

                while (!nonVisitedNodes.isEmpty() && !nonVisitedNodes.peek().isVisited() && (ChronoUnit.SECONDS.between(start,end)) < 7) {

                    Node currentNode = nonVisitedNodes.poll();

                    if (currentNode.getDistance() == 0) {

                        solution = currentNode.getSolution();
                        return solution;

                    }

                    currentNode.setVisited(true);
                    //nonVisitedNodes.add(currentNode);

                    currentNode.generateChildren();

                    for (Node node : currentNode.getChildren()) {

                        if (!existingNodes.contains(node.getState())) {
                            nonVisitedNodes.add(node);
                            existingNodes.add(node.getState());
                        }
                    }

                    end = LocalDateTime.now();

                }
                break;

            default:
                break;
        }





        return null ;

    }


}
