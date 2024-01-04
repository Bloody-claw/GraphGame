package org.example;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Game{
    private Player player;

    private Node graph;

    private Node current_position;
    private String target_location_label;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Node getGraph() {
        return graph;
    }

    public void setGraph(Node graph) {
        this.graph = graph;
    }

    public Node getCurrent_position() {
        return current_position;
    }

    public void setCurrent_position(Node current_position) {
        this.current_position = current_position;
    }

    public String getTarget_location_label() {
        return target_location_label;
    }

    public void setTarget_location_label(String target_location_label) {
        this.target_location_label = target_location_label;
    }

    private void createGraph() {
        List<Node> nodes = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            nodes.add(new Node(Integer.toString(i)));
        }
        nodes.get(0).setChildren(List.of(new Edge[]{new Edge(nodes.get(1), 5), new Edge(nodes.get(2), 2)}));
        setGraph(nodes.get(0));
        nodes.get(1).setChildren(List.of(new Edge[]{new Edge(nodes.get(4), 4)}));
        nodes.get(2).setChildren(List.of(new Edge[]{new Edge(nodes.get(4), 3)}));
        nodes.get(3).setChildren(List.of(new Edge[]{new Edge(nodes.get(4), 8), new Edge(nodes.get(0), 6)}));
        nodes.get(4).setChildren(List.of(new Edge[]{new Edge(nodes.get(3), 5), new Edge(nodes.get(5), 6), new Edge(nodes.get(7), 6)}));
        nodes.get(5).setChildren(List.of(new Edge[]{new Edge(nodes.get(6), 4)}));
        nodes.get(6).setChildren(List.of(new Edge[]{new Edge(nodes.get(12), 5)}));
        nodes.get(7).setChildren(List.of(new Edge[]{new Edge(nodes.get(8), 4), new Edge(nodes.get(11), 5)}));
        nodes.get(8).setChildren(List.of(new Edge[]{new Edge(nodes.get(9), 6), new Edge(nodes.get(10), 7)}));
        nodes.get(9).setChildren(List.of(new Edge[]{new Edge(nodes.get(3), 6), new Edge(nodes.get(8), 1)}));
        nodes.get(10).setChildren(List.of(new Edge[]{new Edge(nodes.get(13), 4)}));
        nodes.get(11).setChildren(List.of(new Edge[]{new Edge(nodes.get(10), 2), new Edge(nodes.get(12), 3)}));
        nodes.get(12).setChildren(List.of(new Edge[]{new Edge(nodes.get(7), 5)}));
        nodes.get(13).setChildren(List.of(new Edge[]{new Edge(nodes.get(14), 2)}));
        nodes.get(14).setChildren(List.of(new Edge[]{new Edge(nodes.get(11), 7)}));
    }

    private void createPlayer(Scanner userInput) {
        System.out.println("Please type your name:");
        String name = userInput.nextLine();
        System.out.println("Please type your nickname:");
        String nickname = userInput.nextLine();
        boolean valid = false;

        while(!valid){
        try {
            System.out.println("Please type your time budget:");
            int time_budget = Integer.parseInt(userInput.nextLine());
            this.setPlayer(new Player(name, nickname, time_budget));
            if (time_budget <= 0){
                System.out.println("Time must be positive.");
            } else{
                valid = true;
            }

        } catch (Exception e) {
            System.out.println("Not valid input");
        }
        }
    }


    private void targetLocationOfPlayer() {
        Scanner userInput = new Scanner(System.in);
        boolean valid = false;
        while(!valid){
            System.out.println("Please select your targeted classroom:");
            this.target_location_label = userInput.nextLine();
            try{
            if (1 <= Integer.parseInt(this.target_location_label) && Integer.parseInt(this.target_location_label) <= 15 ){
                valid = true;
            } else{
                System.out.println("Not valid classroom.");
            }
            } catch (NumberFormatException e){
                System.out.println("Not valid input. It has to be number.");
            }
        }
    }

    private void initGame(Scanner userInput) {
        System.out.println("Type load for loading game or press any button for starting new");
        if ("load".equals(userInput.nextLine())){
            this.loadGame(userInput);
        } else {
            this.createPlayer(userInput);
            this.createGraph();
            this.targetLocationOfPlayer();
            this.setCurrent_position(getGraph());
        }
    }

    private boolean timeTest(){
        for(var edge: current_position.getChildren()){
            if (edge.getCost() <= player.getTime_budget()){
                return true;
            }
        }
        return false;
    }

    public void gameStart() {
        Scanner userInput = new Scanner(System.in);

        this.initGame(userInput);

        while (!Objects.equals(this.current_position.getLabel(), this.target_location_label) && timeTest()) {
            this.printLocations();
            this.chooseLocation(userInput);
        }

        this.gameEnd();

    }

    public void gameEnd(){
        if (Objects.equals(this.current_position.getLabel(), this.target_location_label)) {
            System.out.println("Congratulation you get to the classroom in time!");
        } else{
            System.out.println("Sadly you ran out of time :(");
        }
    }

    private void chooseLocation(Scanner userInput) {
        String players_choice = userInput.nextLine();
        try {
            Integer choice = Integer.valueOf(players_choice);
            if (choice == this.current_position.getChildren().size() + 1){
                this.saveGame(userInput);
            } else if (this.enoughTimeCheck(this.current_position.getChildren().get(choice - 1).getCost()))
            {
            this.player.setTime_budget(this.player.getTime_budget() - this.current_position.getChildren().get(choice - 1).getCost());
            this.setCurrent_position(this.current_position.getChildren().get(choice - 1).getNode());
            } else {
                System.out.println("Not enough time to go there.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        } catch (Exception e) {
            System.out.println("Not valid number test");
        }
    }

    private boolean enoughTimeCheck(int cost){
        return cost <= player.getTime_budget();
    }

    private void printLocations() {
        System.out.println("You have " + player.getTime_budget() + " time and you are in classroom " + current_position.getLabel() );
        System.out.println("Please " + player.getNickname() + " select your next location");
        System.out.println("These are your options:");
        for (int i = 0; i < this.current_position.getChildren().size(); i++) {
            System.out.println((i + 1) + ") classroom "
                    + this.current_position.getChildren().get(i).getNode().getLabel()
                    + " and time cost: " + this.current_position.getChildren().get(i).getCost());
        }
        System.out.println( (this.current_position.getChildren().size() + 1) + ") Save game:");
    }

    private void saveGame(Scanner userInput) {
        System.out.println("Name of file to be saved:");
        String filename = userInput.nextLine();
        String filePath = "src/saves/" + filename + ".json";

        File file = new File(filePath);
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> gameData = new HashMap<>();
            gameData.put("player", this.player);
            gameData.put("graph", this.graph);
            gameData.put("current_position", this.current_position);
            gameData.put("target_location_label", this.target_location_label);

            mapper.writeValue(file, gameData);
        } catch (IOException e) {
            System.out.println("Problem saving the game");
        }
    }


    private void loadGame(Scanner userInput) {
        System.out.println("Enter the name of the file to load:");
        String filename = userInput.nextLine();
        String filePath = "src/saves/" + filename + ".json";

        File file = new File(filePath);
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();

        try {
            MapType mapType = typeFactory.constructMapType(Map.class, String.class, Object.class);
            Map<String, Object> gameData = mapper.readValue(file, mapType);

            this.player = mapper.convertValue(gameData.get("player"), Player.class);
            this.graph = mapper.convertValue(gameData.get("graph"), Node.class);
            this.current_position = mapper.convertValue(gameData.get("current_position"), Node.class);
            this.target_location_label = (String) gameData.get("target_location_label");

            System.out.println("Game loaded successfully!");
        } catch (IOException e) {
            System.out.println("Problem loading the game");
        }
    }

    }


