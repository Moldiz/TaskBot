import commands.ListHelp;
import commands.TaskCreate;
import commands.TaskRemove;
import commands.TaskUpdate;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import static functions.Constants.BOT_PREFIX;
import static functions.UtilitySends.sendPM;


public class CommandParser extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        // If message is posted by bot ignore it
        if (e.getAuthor().isBot()) return;

        // Setup - prefix for bot commands and commands list

        String[] commandList = {"create","update","remove","help"};
        String commandName = "";

        // Capture user input
        String inputT = e.getMessage().getContentRaw();

        System.out.println(BOT_PREFIX.length() + "|" + BOT_PREFIX +"|");

        // Remove all user messages from tasklist channels and all user commands from any channels
        if (e.getChannel().getName().endsWith("tasklist") || e.getMessage().getContentRaw().startsWith(BOT_PREFIX)) {
            e.getMessage().delete().queue();
        }

        // Check if user is using a valid command
        for (String s : commandList) {
            if (inputT.startsWith(BOT_PREFIX + s)) {
                commandName = s;
                break;
            }
        }

        // If not send him help and quit
        if (inputT.startsWith(BOT_PREFIX) && (commandName.equals(""))) {
            sendPM(e.getAuthor(), "There is no such command, for help type `" + BOT_PREFIX + "help`.", "error");// ListHelp or sendError
            return;
        }

        // If user is typed valid command parse it
        if (inputT.length() > BOT_PREFIX.length() + commandName.length()) {
            inputT = inputT.substring( inputT.indexOf(commandName) + commandName.length()+1 );
        } else {
            inputT = inputT.substring(inputT.indexOf(commandName) + commandName.length());
        }

        ArrayList<String> args = new ArrayList<>(Arrays.asList(inputT.split(", ")));

        // Call command function
        switch (commandName) {
            case "create":
                TaskCreate.commandCreate(e, args);
                break;
            case "update":
                TaskUpdate.commandUpdate(e, args);
                break;
            case "remove":
                TaskRemove.commandRemove(e, args);
                break;
            case "help":
                ListHelp.commandHelp(e);
                break;
        }

    }

}