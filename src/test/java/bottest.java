import Utils.BotConfiguration;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import events.MessageRouter;

import events.MessageParser;

import java.util.NoSuchElementException;
import java.util.Optional;

public class bottest
{
    public static void main(String[] args) throws Exception
    {
        final BotConfiguration botConfig = BotConfiguration.getInstance();
        final DiscordClient client = new DiscordClientBuilder(botConfig.getToken()).build();

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
