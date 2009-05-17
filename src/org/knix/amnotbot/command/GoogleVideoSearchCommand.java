package org.knix.amnotbot.command;

import org.knix.amnotbot.BotCommand;
import org.knix.amnotbot.BotMessage;

/**
 *
 * @author gpoppino
 */
public class GoogleVideoSearchCommand implements BotCommand
{

    public void execute(BotMessage message)
    {
        if (message.getText().isEmpty()) return;
        
        new GoogleSearchThread(
                GoogleSearch.searchType.VIDEOS_SEARCH,
                new GoogleResultOutputVideosStrategy(),
                message);
    }

    public String help()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
