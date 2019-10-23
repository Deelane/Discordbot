package events;

import java.io.*;
import java.util.List;

public class LolBuildEvent implements MessageEvent
{
    private String link = "https://www.metasrc.com/na/";
    private String champ = "";
    private String mode = "";

    public void setParameters(List<String> Parsedmsg)
    {
        champ = Parsedmsg.get(1).toLowerCase().trim();
        mode = Parsedmsg.get(2).toLowerCase().trim();
        if (Parsedmsg.size() == 2 )
        {
            if (isChamp())
            {
                link += "5v5/champion/" + champ.replaceAll("[^A-Za-z]+", "");
            }
            else
            {
                link = "Not a valid champion.";
            }
        }
        else if (Parsedmsg.size() == 3)
        {
            if (isMode())
            {
                link += mode.replaceAll("[^A-Za-z]+", "") + "/";
            }
            else
            {
                link = "Not a valid mode.";
            }
            if (isChamp())
            {
                link += "champion/" + champ.replaceAll("[^A-Za-z]+", "");
            }
            else
            {
                link = "Not a valid champion.";
            }

        }
        else
        {
            link = "Invalid syntax.\n" + "Proper syntax:\n" + "For default role: !lolbuild, champion\n" + "For specific mode: !lolbuild, champion, mode";
        }
    }

    public String doAction(List<String> Parsedmsg)
    {
        return link;
    }

    private boolean isChamp()
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
    private boolean isMode()
    {
        try
        {
            String path = getClass().getClassLoader().getResource("LolGameModes.properties").getPath();
            File LolGameModes = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(LolGameModes));
            String str;
            while ((str = br.readLine()) != null) {
                if (str.equalsIgnoreCase(mode)) {
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
