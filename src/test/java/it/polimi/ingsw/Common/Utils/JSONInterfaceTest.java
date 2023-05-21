package it.polimi.ingsw.Common.Utils;

import it.polimi.ingsw.Server.Model.LivingRoom;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class JSONInterfaceTest {

    @Test
    void removeLivingRoom() {
        JSONInterface.writeLivingRoomToJson(new LivingRoom("p2"), JSONInterface.getLivingRoomsPath());
        LivingRoom livingRoom = JSONInterface.getLivingRoomFromJson(JSONInterface.getJsonStringFrom(JSONInterface.getLivingRoomsPath()), "p2");
        // asserting that the livingRoom exist before is not necessary because getLivingRoomFromJson does not return anything otherwise
        JSONInterface.removeLivingRoom(livingRoom);
        assert ! JSONInterface.getJsonStringFrom(JSONInterface.getLivingRoomsPath()).contains(livingRoom.getLivingRoomId());
    }
}