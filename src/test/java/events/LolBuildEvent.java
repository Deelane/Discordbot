package events;

import java.io.*;
import java.util.List;

public class LolBuildEvent implements MessageEvent {
    private String link = "https://www.metasrc.com/na/";
    private String champ = "";
    private String mode = "";
    private String role = "";


    public void setParameters(List<String> Parsedmsg)
    {
        if (Parsedmsg.size() == 2)
        {
            champ = Parsedmsg.get(1);
            if (isValid(champ, "LolChamps.properties"))
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
            role = Parsedmsg.get(2);
            if (isValid(champ, "LolChamps.properties"))
            {
                if (isValid(mode, "LolGameModes.properties"))
                {
                    link += mode.replaceAll("[^A-Za-z]+", "") + "/champion/" + champ.replaceAll("[^A-Za-z]+", "");
                }
                else if (isValid(role, "LolChampRoles.properties"))
                {
                    link += "5v5/champion/" + champ.replaceAll("[^A-Za-z]+", "") + "/" + role.replaceAll("[^A-Za-z]+", "");
                }
                else
                    {
                    link = "Invalid mode or role. Proper modes are:\n" +
                            "`5v5\n" +
                            "3v3\n" +
                            "ARAM\n" +
                            "TFT\n" +
                            "Odyssey\n" +
                            "Blitz\n" +
                            "OFA\n" +
                            "ARURF\n" +
                            "\nProper roles are:\n" +
                            "mid\n" +
                            "top\n" +
                            "jungle\n" +
                            "adc\n" +
                            "support\n`";
                    }
            }
            else
            {
                link = "Not a valid champion.";
            }
        }
        else //Incorrect argument syntax
        {
            link = "*Invalid syntax.*\n" + "`Proper syntax:\n" + "For default role: !lolbuild champion\n" + "For specific mode: !lolbuild champion, mode`";
        }
    }

    public String doAction(List<String> Parsedmsg) {
        return link;
    }

    private boolean isValid(String input, String file) {
        try {
            String path = getClass().getClassLoader().getResource(file).getPath();
            File LolChamps = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(LolChamps));
            String str;
            while ((str = br.readLine()) != null) {
                if (str.equalsIgnoreCase(input)) {
                    return true;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
