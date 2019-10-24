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
        if (Parsedmsg.size() == 2 )
        {
            champ = Parsedmsg.get(1);
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
            champ = Parsedmsg.get(1);
            mode = Parsedmsg.get(2);
            if (isChamp() && isMode())
            {
                link += mode.replaceAll("[^A-Za-z]+", "") + "/champion/" + champ.replaceAll("[^A-Za-z]+", "") ;
            }
            else if (isChamp()) //Champ exists, mode doesn't.
            {
                link = "Not a valid mode. Proper modes are:\n" +
                        "`5v5\n" +
                        "3v3\n" +
                        "ARAM\n" +
                        "TFT\n" +
                        "Odyssey\n" +
                        "Blitz\n" +
                        "OFA\n" +
                        "ARURF`";
            }
            else if (isMode())//Mode exists, champ doesn't.
            {
                link = "Not a valid champion.";
            }
            else
            {
                link = "Not a valid champion or mode.";
            }
        }
        else //Incorrect argument syntax
        {
            link = "*Invalid syntax.*\n" + "`Proper syntax:\n" + "For default role: !lolbuild champion\n" + "For specific mode: !lolbuild champion, mode`";
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
