package Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class BotConfiguration {

    // 'static' ensures one instance across the whole program
    private static BotConfiguration m_botConfiguration;

    private String m_token;

    // Encapsulation at work: don't allow direct access to m_token
    // Limit access through this method.
    public String getToken()
    {
        return m_token;
    }

    private BotConfiguration()
    {
        String path = getClass().getClassLoader().getResource("bot.properties").getPath();
        try(FileInputStream fileInputStream = new FileInputStream(path))
        {
            Properties props = new Properties();
            props.load(fileInputStream);
            m_token = props.getProperty("bot.token");
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    // Don't allow direct access to m_botConfiguration,
    // instead marshall get access through getInstance
    public static BotConfiguration getInstance()
    {
        // bread and butter of the singleton pattern:
        if(m_botConfiguration == null)
            m_botConfiguration = new BotConfiguration();

        return m_botConfiguration;
    }
}
