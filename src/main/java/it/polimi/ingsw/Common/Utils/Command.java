package it.polimi.ingsw.Common.Utils;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {

    String command;
    List<String> args;
    String description;

    public Command(String command, List<String> args, String description) {
        this.command = command;
        this.args = args;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArgs() {
        return args;
    }

    public String getDescription() {
        return description;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addArg(String arg) {
        this.args.add(arg);
    }

    public void removeArg(String arg) {
        this.args.remove(arg);
    }

    public void removeArg(int index) {
        this.args.remove(index);
    }

}
