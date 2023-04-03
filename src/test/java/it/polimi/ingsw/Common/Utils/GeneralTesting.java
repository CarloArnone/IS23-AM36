package it.polimi.ingsw.Common.Utils;

import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Server.Controller.Controller;
import it.polimi.ingsw.Server.Model.LivingRoom;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GeneralTesting {

    Controller controller = new Controller();
    GameSimulator gms = new GameSimulator(controller);

    @Test
    void correctPAP(){

        LivingRoom liv = null;
        try{
            liv = controller.retrieveOldGameEvent("marioGame");
        }
        catch(NoMatchingIDException nmId){
            assert false;
        }

        Character c1 = liv.getBoard().entrySet().stream().filter(x -> x.getKey().getPosX() == 3 && x.getKey().getPosY() == 4).findFirst().get().getKey().getCard().getColor();
        Character c2 = liv.getBoard().entrySet().stream().filter(x -> x.getKey().getPosX() == 3 && x.getKey().getPosY() == 5).findFirst().get().getKey().getCard().getColor();

        gms.simulate("sium1.txt");

        try{
            liv = controller.retrieveOldGameEvent("marioGame");
        }
        catch(NoMatchingIDException nmId){
            assert false;
        }

        assert liv.getPlayers().get(1).getMyShelf().getShelf()[1][3].get().getColor().equals(c1);
        assert liv.getPlayers().get(1).getMyShelf().getShelf()[0][3].get().getColor().equals(c2);

    }

    @Test
    void emptyPAP(){

    }

    @Test
    void completeGameCarloLorenzo(){
        gms.simulate("carloPlayerStartUp.txt");
        gms.simulate("lorenzoPlayerStartUp.txt");
        gms.simulate("gameCarloLorenzo.txt");
        assert true;
    }

}
