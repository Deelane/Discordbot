package events;

import java.util.Arrays;
import java.util.List;

public class MessageParser
{
    private String msg;
    private List<String> Parsedmsg;
    public MessageParser(String msg)
    {
        this.msg = msg;
        this.Parsedmsg = Arrays.asList(msg.split(","));
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
