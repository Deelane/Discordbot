package events;

import com.samuelmaddock.strawpollwrapper.StrawPoll;

import java.util.ArrayList;
import java.util.List;

public class StrawPollEvent implements MessageEvent
{
    private StrawPoll strawPoll = new StrawPoll();
    private List<String> options;

    public void setParameters(List<String> Parsedmsg)
    {
        strawPoll.setTitle(Parsedmsg.get(1));
        List<String> options = new ArrayList<>();
        int size = Parsedmsg.size();//every argument after command and title will be options
        for (int i=2; i<size; i++)
        {
            strawPoll.addOptions(Parsedmsg.get(i));
        }
        strawPoll.create();
    }
    public String doAction(List<String> Parsedmsg)//Returns random number 1 through roll as string
    {
        return strawPoll.getPollURL();
    }
}

