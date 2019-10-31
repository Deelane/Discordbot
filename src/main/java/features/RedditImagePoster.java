package features;

import Utils.RedditClientConfiguration;
import discord4j.core.DiscordClient;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.DefaultPaginator;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RedditImagePoster
{
    private RedditClientConfiguration config = new RedditClientConfiguration();
    private RedditClient reddit = RedditClientConfiguration.getClient();

    public RedditImagePoster(DiscordClient client)
    {
        TextChannel channel = loadChannel(client);
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource("Subreddits.properties")).getPath();
        File Subreddits = new File(path);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(new TimerTask() { //runs postImage on a timer, setting to 24 hours for now.
            public void run()
            {
                try
                {
                    BufferedReader br = new BufferedReader(new FileReader(Subreddits));
                    String str;
                    while ((str = br.readLine()) != null)
                    {
                        postImage(str, client, channel);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }, 24, TimeUnit.HOURS); //24 hours
    }

    private void postImage(String subreddit, DiscordClient client, TextChannel channel)
    {
        //posts images to channel
        //check rising, post top X images
        DefaultPaginator<Submission> paginator = reddit.subreddit(subreddit) // returns a pagination of submissions of the subreddit string passed in
                .posts()
                .limit(3) // 3 posts per page, doesn't count stickied posts
                .sorting(SubredditSort.HOT) // top posts
                .timePeriod(TimePeriod.DAY) // of all time
                .build();
        Listing<Submission> posts = paginator.next(); //creates a "listing" containing each post in the pagination
        int size = posts.size();
        List<String[]> postinfo = new ArrayList<>(); //list of array strings to hold desired info from posts
        for (int i=0; i<size; i++)
        {
            if (!(posts.get(i).isStickied())) //if posted by a mod, checking if posts is stickied should produce similar result
            {
                String[] temp = {subreddit, posts.get(i).getAuthor(), posts.get(i).getTitle(), posts.get(i).getPermalink(), posts.get(i).getUrl()}; //constructs an array containing the post's author, post title, and URL
                postinfo.add(temp);
            }
        }
        /*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        LocalDateTime now = LocalDateTime.now();
        channel.createMessage("Hello Foopers, please enjoy your daily foop for " + dtf.format(now)).block(); //creates message in channel*/
        size = postinfo.size();
        try {
            for (int i = 0; i < size; i++)
            {
                channel.createMessage("**Subreddit: **" + subreddit + "\n" + "**User: **" + postinfo.get(i)[1] + "\n" + "**Title: **" + postinfo.get(i)[2] + "\n" + "Permalink: " + postinfo.get(i)[3] + "\n" + postinfo.get(i)[4]).block(); //creates message in channel
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
    private TextChannel loadChannel(DiscordClient client)
    {
        TextChannel channel = null;
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource("TextChannel.properties")).getPath();
        try(FileInputStream fileInputStream = new FileInputStream(path))
        {
            Properties props = new Properties();
            props.load(fileInputStream);
            String channelId = props.getProperty("ChannelId");
            channel = (TextChannel) client.getChannelById(Snowflake.of(channelId)).block(); //Textchannel to post data to, constructed with channel's ID
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return channel;
    }


}
