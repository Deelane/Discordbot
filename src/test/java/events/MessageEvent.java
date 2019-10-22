package events;
import java.util.*;

interface MessageEvent
{
    String doAction(List<String> Parsedmsg);
    void setParameters(List<String> Parsedmsg);
}
