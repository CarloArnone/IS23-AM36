package it.polimi.ingsw.Common.Utils.Comunication.Socket;

import it.polimi.ingsw.Common.Utils.JSONInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientSocket {

    private Socket comunicator;
    private PrintWriter out;
    private Scanner in;

    public ClientSocket(String ip, int port) throws IOException {
        comunicator = new Socket(ip, port);
        out = new PrintWriter(comunicator.getOutputStream(), true);
        in = new Scanner(comunicator.getInputStream());

    }

    public String sendMessage(String msg) {
        out.println(msg);
        return in.nextLine();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        comunicator.close();
    }

    public static void main(String[] args) {
        ClientSocket c = null;
        try {
            c = new ClientSocket("127.0.0.1", 12334);
        } catch (IOException e) {
            System.out.println("Client Not Started");
        }

        boolean needToClose = false;
        Scanner sc = new Scanner(System.in);
        while(!needToClose){
            String[] input = sc.nextLine().split(" ");
            List<String> arguments = new ArrayList<>();
            for (int i = 1; i < input.length; i++) {
                if(! input[i].equals("")){
                    arguments.add(i-1, input[i]);
                }
            }

            System.out.println(c.sendMessage(JSONInterface.generateCommand(input[0], arguments, "")));
        }
    }
}
