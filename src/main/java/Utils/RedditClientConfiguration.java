package Utils;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class RedditClientConfiguration
{
    private static RedditClient reddit;
    public RedditClientConfiguration()
    {
        String username = "";
        String password = "";
        String clientId = "";
        String clientSecret = "";
        String path = getClass().getClassLoader().getResource("RedditCredentials.properties").getPath();
        try(FileInputStream fileInputStream = new FileInputStream(path))
        {
            Properties props = new Properties();
            props.load(fileInputStream);
            username = props.getProperty("Reddit.username");
            password = props.getProperty("Reddit.password");
            clientId = props.getProperty("Reddit.clientId");
            clientSecret = props.getProperty("Reddit.clientSecret");
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        Credentials oauthCreds = Credentials.script(username, password, clientId, clientSecret);
        UserAgent userAgent = new UserAgent("bot", "Foopybot", "1.0.0", "broesidan");
        this.reddit = OAuthHelper.automatic(new OkHttpNetworkAdapter(userAgent), oauthCreds);
    }
    public static RedditClient getClient()
    {
        return reddit;
    }
}
