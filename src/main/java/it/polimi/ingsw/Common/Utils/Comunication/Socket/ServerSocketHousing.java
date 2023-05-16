package it.polimi.ingsw.Common.Utils.Comunication.Socket;

import it.polimi.ingsw.Server.Controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ServerSocketHousing {

    private ServerSocket serverSocket;
    private Controller c = Controller.getInstance();

    private List<List<VirtualViewServerSocket>> rooms = new ArrayList<>();

    public ServerSocketHousing(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        rooms.add(new ArrayList<>());
        rooms.add(new ArrayList<>());
        parseCommands();
    }

    public void addClient(int roomId, VirtualViewServerSocket socket) {
        rooms.get(roomId).add(socket);
    }

    public List<VirtualViewServerSocket> getRoom(int roomId){
        return rooms.get(roomId);
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public void parseCommands(){

        while(true){
            try {
                new VirtualViewServerSocket(serverSocket.accept(), c).start();
            } catch (IOException e) {
                System.out.println("ClientNotAccepted");
            }
        }


    }

    public static void main(String[] args) {
        try {
            ServerSocketHousing server = new ServerSocketHousing(12334);
        } catch (IOException e) {
            System.out.println("server not started");
        }
    }

}


