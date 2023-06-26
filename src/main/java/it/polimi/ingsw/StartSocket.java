package it.polimi.ingsw;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Common.Utils.Comunication.Socket.ServerSocketHousing;
import it.polimi.ingsw.Common.Utils.Comunication.Socket.VirtualViewClientSocket;
import it.polimi.ingsw.Client.GUI.GUI;

import java.io.IOException;

public class StartSocket {
    public static void main(String[] args) {

        int port;
        try{
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
                VirtualViewClientSocket client = VirtualViewClientSocket.getInstance(args[1], Integer.parseInt(args[2]));

                if(args[3].equals("CLI")){
                    CLI cli = new CLI();
                    client.setUI(cli);
                    client.getUI().startUI(client);
                }
                else{
                    GUI gui = new GUI();
                    client.setUI(gui);
                    client.getUI().startUI(client);
                }

            }
        }
        catch (Exception e){
            System.out.println("Missing Arguments");
            System.out.println("Starting a server on port 12345");
            try {
                new ServerSocketHousing(12345);
            } catch (IOException ee) {
                throw new RuntimeException(ee);
            }
        }


    }
}
