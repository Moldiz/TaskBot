package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static functions.Properties.BOT_PREFIX;
import static functions.UtilitySends.sendPM;

public class TaskCreate {

    public static void commandCreate(GuildMessageReceivedEvent e, ArrayList<String> args) {

        String commandSyntax = "Syntax: `" + BOT_PREFIX + "create [Item name], [Quality], [Quantity]`";
        String commandInfo = "Creates a new task on channel.\n\n" +
                "`[Item name]` must contain 3-50 letters,\n" +
                "`[Quality]` must contain 3-50 characters,\n" +
                "`[Quantity]` must be a valid number between 1-999999999.";

        if ((args.size()==1)) {
            sendPM(e.getAuthor(), commandSyntax + "\n\n" + commandInfo, "info");
        } else if ((args.size()>3)) {
            sendPM(e.getAuthor(), "Syntax Error. Too many arguments.\n" + commandSyntax, "error");
        } else if ((args.size()<3)) {
            sendPM(e.getAuthor(), "Syntax Error. Not enough arguments.\n" + commandSyntax, "error");
        } else if (!(args.get(0).matches(".*\\w{3,}.*"))) {
            sendPM(e.getAuthor(), "Syntax Error. Name must contain min 3 letters.\n" + commandSyntax, "error");
        } else if ((args.get(0).length() > 50)) {
            sendPM(e.getAuthor(), "Syntax Error. Name argument is too long. Max 50 characters.\n" + commandSyntax, "error");
        } else if (!(args.get(1).matches(".*\\w+.*"))) {
            sendPM(e.getAuthor(), "Syntax Error. Quality does not contain any characters.\n" + commandSyntax, "error");
        } else if ((args.get(1).length() > 50)) {
            sendPM(e.getAuthor(), "Syntax Error. Quality argument is too long. Max 50 characters.\n" + commandSyntax, "error");
        } else if (!(args.get(2).matches("\\d+"))) {
            sendPM(e.getAuthor(), "Error: Quantity is not a valid number.\n" + commandSyntax, "error");
        } else if (args.get(2).length() > 9) {
            sendPM(e.getAuthor(), "Error: Quantity is to high. Max quantity is 999 999 999.\n" + commandSyntax, "error");
        } else {

            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(0, 150, 130));

            e.getChannel().sendMessage(eb.build()).queue(message -> {

                eb.addField("Status: ","To do", true);
                eb.addField("Item: ", args.get(0), true);
                eb.addField("Quality: ", args.get(1), true);
                eb.addField("Quantity have: ", "0", true);
                eb.addField("Quantity need: ", args.get(2), true);
                eb.addField("Requested by: ", Objects.requireNonNull(e.getMember()).getAsMention(), true);
                eb.addField("Date: ", e.getMessage().getTimeCreated().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")), true);
                eb.addField("Task id: ", message.getId(), true);

                message.editMessage(eb.build()).queue();
            });

        }

    }

}