package Utils;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;

public class ClientConfiguration
{
    final private DiscordClient client;
    public ClientConfiguration(BotConfiguration botConfig)
    {
        this.client = new DiscordClientBuilder(botConfig.getToken()).build();
    }
    public DiscordClient getClient()
    {
        return this.client;
    }
}

