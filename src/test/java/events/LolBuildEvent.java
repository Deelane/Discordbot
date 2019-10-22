package events;

import java.io.*;
import java.util.List;

public class LolBuildEvent implements MessageEvent
{
    private String link = "";
    private String champ = "";
    public void setParameters(List<String> Parsedmsg)
    {
        champ = Parsedmsg.get(1).toLowerCase().trim();
        if (isChamp())
        {
            link = "https://www.metasrc.com/5v5/champion/" + champ.replaceAll("[^A-Za-z]+", "");
        }
        else
        {
            link = "Not a valid champion.";
        }
    }

    public String doAction(List<String> Parsedmsg)
    {
        return link;
    }

    public boolean isChamp()
    {
        try
        {
            String path = getClass().getClassLoader().getResource("LolChamps.properties").getPath();
            File LolChamps = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(LolChamps));
            String str;
            while ((str = br.readLine()) != null) {
                if (str.equalsIgnoreCase(champ)) {
                    return true;
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
