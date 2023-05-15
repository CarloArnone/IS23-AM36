package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Utils.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_ClientInterface extends Remote {

    //ERRORS
    void NotEnoughSpacesInCol(Command args);
    void LoginUnsuccessful(Command args);
    void LivingRoomNotFound(Command args);
    void CreateGameNotSuccessful(Command args);
    void NotDisconnectedPlayer(Command args);
    void GameNotStarted(Command args);
    void GameNotEnded(Command args);
    void NotPossiblePick(Command args);


    //SUCCESSES
    void TurnEndedSuccessfully(Command args);
    void LoginDoneSuccessfully(Command args);
    void LivingRoomFound(Command args);
    void JoinedGame(Command args);
    void DisconnectedPlayer(Command args);
    void LivingRoomsList(Command args);
    void GameStarted(Command args);
    void GameEnded(Command args);
    void PossiblePick(Command args);
    void NotifyListener(Command args);

    //TESTING
    public String echo(String echoString) throws RemoteException;

    public String upper(String upperString) throws RemoteException;

}
