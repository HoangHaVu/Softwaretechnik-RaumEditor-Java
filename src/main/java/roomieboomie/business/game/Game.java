package roomieboomie.business.game;

import java.util.ArrayList;

import roomieboomie.business.item.placable.PlacableItem;
import roomieboomie.business.room.Room;
import roomieboomie.persistence.Config;

public class Game {

    Room room;
    byte[][] gameLayout;
    ArrayList<PlacableItem> items;

    public Game(){
        




    }


    public void loadRoom(Room room){

        gameLayout = room.getLayout();
        this.room = room;

        for (int i = 0; i < gameLayout.length; i++){
            for (int j = 0; j < gameLayout[0].length; j++){
                if (gameLayout[i][j] < Config.get().EDITORMAXWINDOWVALUE()){
                    gameLayout[i][j] = Config.get().GAMEWINDOWVALUE();
                }
            }
        }
        for (int i = 0; i < gameLayout.length; i++){
            for (int j = 0; j < gameLayout[0].length; j++){
                if (gameLayout[i][j] >= Config.get().EDITORMINWALLVALUE()){
                    gameLayout[i][j] = Config.get().GAMEWALLVALUE();
                }
            }
        }

    }

    

    public void refreshLayout(){

        



        



        







    }


}
