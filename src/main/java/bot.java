import Utils.BotConfiguration;
import Utils.ClientConfiguration;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import events.MessageParser;
import events.MessageRouter;
import features.RedditImagePoster;

import java.util.NoSuchElementException;
import java.util.Optional;

public class bot
{
    public static void main(String[] args) throws Exception
    {
        final BotConfiguration botConfig = BotConfiguration.getInstance();
        final ClientConfiguration clientConfig = new ClientConfiguration(botConfig);
        final DiscordClient client = clientConfig.getClient();
        RedditImagePoster poster = new RedditImagePoster(client);

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(ready -> System.out.println("Logged in as " + ready.getSelf().getUsername()));

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(event -> {
                    Message message = event.getMessage();
                    if (firstArgument(message.getContent()).map("!"::equals).orElse(false))
                    {
                        message.getChannel().block().createMessage(getResult(message)).block();
                    }
                });

        client.login().block();

    }
    private static String getResult(Message msg)
    {
        String result = "";
        MessageParser m = new MessageParser(msg.getContent().orElse(""));
        if (m.isCommand(m.getParsedmsg()))
        {
            result = MessageRouter.RouteMessage(m.getParsedmsg());
        }
        return result;
    }
    private static Optional<String> firstArgument(Optional<String> msg)
    {
        String firstArgument = "";
        try
        {
            firstArgument = msg.get();
        }
        catch (NoSuchElementException e)
        {
            e.printStackTrace();
        }
        MessageParser m = new MessageParser(firstArgument);
        if (m.isCommand(m.getParsedmsg()))
        {
            firstArgument = "!";
        }
        return Optional.of(firstArgument);
    }
}
