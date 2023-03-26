package it.polimi.ingsw.Common.Utils;

import it.polimi.ingsw.Server.Model.LivingRoom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestGeneratorTest {

    @Test
    public void testGeneratingNewLivingRoom(){
        LivingRoom l = TestGenerator.generateLivingRoom(3);
        System.out.println(l.toString());
        assert true;
    }

}