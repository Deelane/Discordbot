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
        List<String> sublist = getSubList();
        List<Submission> posts = makeList(sublist);
        LinkedHashSet<Submission> temp = new LinkedHashSet<>(posts); //transform into hashset
        posts = new ArrayList<>(temp); //transform back into list to remove duplicates
        Collections.shuffle(posts);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(new Helper(client, posts, channel, scheduler), 5, TimeUnit.MINUTES); //creates a new instance of helper every 10 minutes, posts the Submission stored at "index" in posts
    }



    private List<String> getSubList()
    {
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource("Subreddits.properties")).getPath();
        File subreddits = new File(path);
        List<String> sublist = new ArrayList<>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(subreddits));
            String str;
            while ((str = br.readLine()) != null) //adds every subreddit in our resource to our list
            {
                sublist.add(str);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return sublist;
    }

    private void postImage(List<String> post, DiscordClient client, TextChannel channel)
    {
        try
        {
            {
                channel.createMessage("**Subreddit: **" + "<https://reddit.com/r/" + post.get(0) + ">" + "\n" + "**Author: **" + "<https://reddit.com/u/" + post.get(1) + ">" + "\n" + "**Title: **" + post.get(2) + "\n" + "**Permalink: **" + "<https://reddit.com" + post.get(3) + ">" + "\n" + "**Image: **" + post.get(4)).block(); //creates message in channel
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    private List<Submission> makeList(List<String> sublist)
    {
        List<Submission> posts = new ArrayList<>(); //creates a "listing" containing each post in the pagination
        for (String sub : sublist)
        {
            DefaultPaginator<Submission> paginator = reddit.subreddit(sub) // returns a pagination of submissions of the subreddit string passed in
                    .posts()
                    .limit(5) // 3 posts per page, doesn't count stickied posts
                    .sorting(SubredditSort.HOT) // top posts
                    .timePeriod(TimePeriod.DAY) // of all time
                    .build();
            Listing<Submission> temp = paginator.next(); //creates a "listing" containing each post in the pagination
            posts.addAll(temp);
        }
        return posts;
    }

    private List<String> makePost(List<Submission> posts, int postListIndex) //makes a single post containing desired info from a list of posts and an index
    {
        List<String> post = new ArrayList<>(); //list to hold desired info from posts
        try
        {
        if (!(posts.get(postListIndex).isStickied())) //if posted by a mod, checking if posts is stickied should produce similar result
            {
                post.add(posts.get(postListIndex).getSubreddit());
                post.add(posts.get(postListIndex).getAuthor());
                post.add(posts.get(postListIndex).getTitle());
                post.add(posts.get(postListIndex).getPermalink());
                post.add(posts.get(postListIndex).getUrl());
                return post;
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return null;
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
    private class Helper extends TimerTask
    {
        DiscordClient client;
        List<Submission> posts;
        TextChannel channel;
        int size;
        ScheduledExecutorService scheduler;
        private Helper (DiscordClient client, List<Submission> posts, TextChannel channel, ScheduledExecutorService scheduler)
        {
            this.client = client;
            this.posts = posts;
            this.channel = channel;
            this.size = posts.size();
            this.scheduler = scheduler;
        }
        public void run()
        {
            if (size==0)
            {
                return;
            }
            else
            {
                List<String> post = makePost(posts, 0);
                if (post != null)
                {
                    postImage(post, client, channel);
                }
                posts.remove(0);
                scheduler.schedule(new Helper(client, posts, channel, scheduler), 5, TimeUnit.MINUTES);
            }
        }
    }


}
