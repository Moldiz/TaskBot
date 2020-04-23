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

    /*
    FIXME: Ogólnie bym podzielił meotdy na parę mniejszych prywatnych, bo wychodzą strasznie długie i mało czytelne
     np walidacja inputu od użytkownika to mogła by być metoda prywatna

     if( ! isInputValid(args)){
        show help ..
     }

     podobnie z parsowaniem. To możesz zroić dla wysztkich innych metod (jak są długie)
     np wszystkie taski zaczynają się od walidacji inputu i parsowania.


     A jak będziesz bardzo ambitny to możesz pomyśleć o uniwerslnym rozwiązaniu na validację
     i/lub parsowanie argumentów (framework)
         */
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        // If message is posted by bot ignore it
        if (e.getAuthor().isBot()) return;

        // Setup - prefix for bot commands and commands list

        String[] commandList = {"create","update","remove","help"}; // <-- Fixme: nigdy się nie zmienia, więc ustaw jako stałe pole. Możę być w constants
        String commandName = "";

        // Capture user input
        String inputT = e.getMessage().getContentRaw();

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

        //Fixme: Umiesz streamy (z Javy 8+) ? Bo moim znadnie wyglądają lepiej i czytelniej
        // można tym zastąpić każdą pętlę. Np tą powyżej
        /*String commandName =Arrays.stream(commandList)
                .filter( command -> inputT.startsWith(BOT_PREFIX + command))
                .findFirst();

         */



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
        /* Fixme:
        Jak chcesz mieć testy to musisz się pozbyć wywołania statycznyc metod
        Zamiast tego to wszstko to powinny być wywołania na obiektach
        Te obiekty (serwisy) powinny być w polach które populujesz przez konstruktor.
        Powinno to wyglądać mniej więcej tak:

        public class CommandParser extends ListenerAdapter {

            private final TaskCreate taskCreateService;
            .......

            public CommandParser(TaskCreate taskCreate, .....){
                this.taskCreateService = taskCreate;
            }

        public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

            i populować je przy tworzeniu (pewnie w klasie Bot)
         */
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