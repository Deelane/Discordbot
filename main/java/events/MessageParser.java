package events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageParser
{
    private String msg;
    private List<String> Parsedmsg = new ArrayList<>();
    public MessageParser(String msg)
    {
        this.msg = msg;
        List<String> command = Arrays.asList(msg.split(" ",2 )); //splits command from rest of message
        if (command.size() > 1)
        {
            String arguments = command.get(1); //stores rest of message into a new string
            List<String> argumentsList = Arrays.asList(arguments.split(","));//splits arguments by commas and stores them into parsedmsg
            int argumentsListSize = argumentsList.size();//so that we don't go out of bounds
            Parsedmsg.add(0, command.get(0).toLowerCase());//inserts command into 0 index of parsedmsg
            for (int i = 0; i < argumentsListSize; i++)
            {
                Parsedmsg.add(argumentsList.get(i).toLowerCase().trim());
            }
        }
        else
        {
            Parsedmsg.add(msg);
        }
    }
    public String getMessage()
    {
        return msg;
    }
    public void setMessage(String msg)
    {
        this.msg = msg;
    }
    public List<String> getParsedmsg()
    {
        return Parsedmsg;
    }
    public void setParsedmsg(String msg)
    {
        this.Parsedmsg = Arrays.asList(msg.split(","));
    }
    public String getParsedmsgAt(String[] Parsedmsg, int index)
    {
        return Parsedmsg[index];
    }
    public boolean isCommand(List<String> Parsedmsg) //checks if message is a command or not, denoted by the first char being "!".
    {
        String[] firstargument = new String[2];
        firstargument = Parsedmsg.get(0).split("", 2); //splits string at first character, and only there.
        return firstargument[0].equals("!");
    }
}
