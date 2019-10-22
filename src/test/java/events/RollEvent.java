package events;

import java.util.*;
import java.lang.String;
import org.apache.commons.lang3.math.NumberUtils;

public class RollEvent implements MessageEvent
{
    private int min = 0;
    private int max = 100;
    public void setParameters(List<String> Parsedmsg)
    {
        if (Parsedmsg.size()==3)
        {
            if (NumberUtils.isCreatable(Parsedmsg.get(1)) && NumberUtils.isCreatable(Parsedmsg.get(2)))//if argument 2 is not a number
            {
                min = (int) Math.floor(Double.parseDouble(Parsedmsg.get(1))); //parses argument 1 for min value
                max = (int) Math.floor(Double.parseDouble(Parsedmsg.get(2))); // parses argument 2 for max value
            }
        }
    }
    public String doAction(List<String> Parsedmsg)//Returns random number 1 through roll as string
    {
        Random random = new Random();
        int randomInt = random.nextInt((max-min)+1) + min;//converts roll to int, generates random integer in range
        return "You rolled " + randomInt + ".";
    }
}
