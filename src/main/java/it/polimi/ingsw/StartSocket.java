package it.polimi.ingsw;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Common.Utils.BlackBoard;
import it.polimi.ingsw.Common.Utils.Comunication.Socket.ServerSocketHousing;
import it.polimi.ingsw.Common.Utils.Comunication.Socket.VirtualViewClientSocket;
import it.polimi.ingsw.Common.Utils.Comunication.Socket.VirtualViewServerSocket;
import it.polimi.ingsw.Server.Model.Player;

import java.io.IOException;

public class StartSocket {
    public static void main(String[] args) {

        int port;

        if(args[0].equals("server")){
            port = Integer.parseInt(args[1]);
            try {
                new ServerSocketHousing(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                BlackBoard blackBoard = BlackBoard.getInstance();
                VirtualViewClientSocket client = new VirtualViewClientSocket("localhost", Integer.parseInt(args[0]), new CLI());
                client.getUI().initalizeVirtualView(client);
                client.getUI().launch();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }








    }
}
