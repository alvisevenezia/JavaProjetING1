package com.example.cyslide.map;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapLoader {

    /**
     Saves a map to the specified file path.
     @param map The map to be saved.
     @param path The file path where the map will be saved.
     */
    public static void saveMap(Map map, String path) throws IOException {

        File mapSaveFile = new File(path);

        BufferedWriter writer = new BufferedWriter(new FileWriter(mapSaveFile));

        writer.write(map.getSizeX() + ";" + map.getSizeY() + ";"+ map.getMapName() +";"+map.getImageURL()+";"+map.getRecord()+";"+(map.isUnlocked()?"1":"0")+"\n");

        for(int idY = 0,idX = 0; idY < map.getSizeY(); idY++){
            for(idX = 0; idX < map.getSizeX(); idX++){
                TilePane tilePane = map.getTileByCoord(idX,idY);
                writer.write(tilePane.getTileType() + ";");
            }
            writer.write("\n");
        }

        writer.close();

    }

    /**
     * Unlocks the next map in the map list.
     * @param mapID The id of the map to be unlocked.
     */
    public static void unloackNextMap(int mapID) throws IOException {

        Map map = MapLoader.loadMap("./map/map"+(mapID+1)+".txt");

        if(map == null){
            return;
        }

        map.setUnlocked(true);
        MapLoader.saveMap(map,"./map/map"+(mapID+1)+".txt");

    }


    /**
     Breaks an image into sub-images based on the specified number of rows and columns, width, and height.
     @param source The source image file path.
     @param rows The number of rows for the sub-images.
     @param column The number of columns for the sub-images.
     @param width The width of each sub-image.
     @param height The height of each sub-image.
     @return A list of sub-images generated from the source image.
     */
    public List<Image> breakImage(String source,int rows,int column, int width, int height){

        List<Image> subImageList = new ArrayList<>();
        Image image = new Image(source);

        for(int y = 0; y < rows; y++){
            for(int x = 0; x < column; x++){
                PixelReader reader = image.getPixelReader();

                if(reader == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Image not found!");
                    alert.showAndWait();
                    return null;
                }

                WritableImage subImage = new WritableImage(reader, x * width, y * height, width, height);
                subImageList.add(subImage);
            }
        }

        return subImageList;

    }

    /**
     Loads a map from the specified file path.
     @param path The file path of the map data.
     @return The loaded Map object.
     @throws FileNotFoundException if the file is not found.
     */
    public static Map loadMap(String path) throws FileNotFoundException {

        List<TilePane> tilePaneList = new ArrayList<>();

        File mapData = new File(path);

        if (!mapData.exists()) {
            return null;
        }

        Scanner scanner = new Scanner(mapData);

        String header = scanner.nextLine();
        String[] headerData = header.split(";");

        int sizeX = Integer.parseInt(headerData[0]);
        int sizeY = Integer.parseInt(headerData[1]);
        String mapName = headerData[2];
        String imgSource = headerData[3];
        int record = Integer.parseInt(headerData[4]);
        boolean unlocked = headerData[5].equalsIgnoreCase("1");

        if(record == 0){
            record = Integer.MAX_VALUE;
        }

        Image image = new Image(imgSource);

        List<Image> subImageList = new MapLoader().breakImage(imgSource, sizeY, sizeX,600/sizeX,600/sizeY);

        Map map = new Map(null, 0, mapName, sizeX, sizeY);

        for(int idY = 0; idY < sizeY; idY++){
            String tileData = scanner.nextLine();
            for(int idX = 0; idX < sizeX; idX++){
                String[] tileDataArray = tileData.split(";");
                TilePane tilePane;

                if(tileDataArray[idX].equalsIgnoreCase("w")){
                    tilePane = new TilePane(idX, idY, false,true,map);
                }
                else if(tileDataArray[idX].equalsIgnoreCase("1")){
                    tilePane = new TilePane(idX, idY, false,false,map);
                }
                else{
                    tilePane = new TilePane(idX, idY, true,false,map);

                }

                tilePane.setTileId(tilePane.isEmpty()?0:idY*sizeX+idX+1);
                map.getGoalList().add(tilePane.isEmpty()?0:idY*sizeX+idX+1);
                tilePane.setImage(subImageList.get(sizeX * idY + idX));
                tilePaneList.add(tilePane);

            }
        }

        map.setRecord(record);
        map.setTilePaneList(tilePaneList);
        map.setImage(image);
        map.setImageURL(imgSource);
        map.setMvtCounter(0);
        map.setUnlocked(unlocked);

        return map;
    }

}
