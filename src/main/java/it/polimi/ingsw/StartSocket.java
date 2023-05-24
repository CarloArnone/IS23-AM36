package it.polimi.ingsw;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Client.GUI.GUI;
import it.polimi.ingsw.Common.Utils.Comunication.Socket.ServerSocketHousing;
import it.polimi.ingsw.Common.Utils.Comunication.Socket.VirtualViewClientSocket;
import it.polimi.ingsw.Common.Utils.IUI;

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

            //Create RMI server
        }
        else{
            if(args[0].equals("socket")) {
                if (args[3].equals("CLI")) {
                    try {
                        VirtualViewClientSocket client = new VirtualViewClientSocket(args[1], Integer.parseInt(args[2]), new CLI());
                        client.getUI().initalizeVirtualView(client);
                        client.getUI().startIUI();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }   else{
                    try {
                        VirtualViewClientSocket client = new VirtualViewClientSocket(args[1], Integer.parseInt(args[2]));
                        client.setUI(GUI.getInstance(client));
                        client.getUI().startIUI();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

                } else {
                    System.out.println("RMI Client Started");
                }

        }








    }
}
