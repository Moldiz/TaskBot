package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import static functions.Constants.BOT_PREFIX;
import static functions.UtilitySends.sendCustomPM;
import static functions.UtilitySends.sendPM;
import static net.dv8tion.jda.api.Permission.MESSAGE_MANAGE;

public class TaskRemove {



    public static void commandRemove(GuildMessageReceivedEvent e, ArrayList<String> args) {

        String commandSyntax = "Syntax: `" + BOT_PREFIX + "remove [Task id], *[Reason]`";
        String commandInfo = "Removes whole task and notify requester.\n" +
                "You need message manage privilege to use this command.\n" +
                "If you are deleting task witch is done, you can skip Reason argument.\n\n" +
                "`[Task id]` must be a valid number,\n" +
                "`[Reason]` is optional, but if used must contain 3-50 letters.";

        if (args.get(0).matches("\\s*")) {
            sendPM(e.getAuthor(), commandSyntax + "\n\n" + commandInfo, "info");
                /*
    FIXME: po co te wysztkie Objcets.requireNotNull ??? one nic nie robią i tak poleci NPE i tak
    jak chcesz się pozbyć NPE to polecam Optional (java 8+)
     */
        } else if (!Objects.requireNonNull(e.getMember()).hasPermission(e.getChannel(), MESSAGE_MANAGE)){
            sendPM(e.getAuthor(), "Error. You need message manage privilege to use this command.\n" + commandSyntax, "error");
        } else if ((args.size()>2)) {
            sendPM(e.getAuthor(), "Syntax Error. Too many arguments.\n" + commandSyntax,"error");
        } else if ((args.size()<1)) {
            sendPM(e.getAuthor(), "Syntax Error. Not enough arguments.\n" + commandSyntax,"error");
        } else if (!(args.get(0).matches("-?\\d+"))) {
            sendPM(e.getAuthor(), "Error: Task id is not a valid number.\n" + commandSyntax,"error");
        } else {

            e.getChannel().retrieveMessageById(args.get(0)).queue(message -> {

                if (args.size() > 1) {

                    if (!(args.get(1).matches(".*\\w{3,}.*"))) {
                        sendPM(e.getAuthor(), "Syntax Error. Reason must contain min 3 letters.\n" + commandSyntax, "error");
                    } else if ((args.get(1).length() > 50)) {
                        sendPM(e.getAuthor(), "Syntax Error. Reason argument is too long. Max 50 characters.\n" + commandSyntax, "error");
                    } else {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setColor(new Color(0, 150, 130));

                        eb.setAuthor("Task.it Message", null, "https://cdn.discordapp.com/app-icons/692401395613171814/6bf084825c16748ff058818115990064.png");
                        eb.setTitle("Your task has been removed by " + e.getAuthor().getName(), null);
                        eb.setDescription("Reason: " + args.get(1) + "\n:black_small_square:");
                        eb.addField("Status: ", message.getEmbeds().get(0).getFields().get(0).getValue(), true);
                        eb.addField("Item: ", message.getEmbeds().get(0).getFields().get(1).getValue(), true);
                        eb.addField("Quality: ", message.getEmbeds().get(0).getFields().get(2).getValue(), true);
                        eb.addField("Quantity have: ", message.getEmbeds().get(0).getFields().get(3).getValue(), true);
                        eb.addField("Quantity need: ", message.getEmbeds().get(0).getFields().get(4).getValue(), true);
                        eb.addField("Requested by: ", message.getEmbeds().get(0).getFields().get(5).getValue(), true);
                        eb.addField("Date: ", message.getEmbeds().get(0).getFields().get(6).getValue(), true);
                        eb.addField("Task id: ", message.getEmbeds().get(0).getFields().get(7).getValue(), true);

                        JDA jda = e.getJDA();
                        String userReq = message.getEmbeds().get(0).getFields().get(5).getValue();
                        userReq = (Objects.requireNonNull(userReq).substring(userReq.indexOf("<@") + 2, userReq.indexOf(">")));
                        if (userReq.matches("!.*")) {
                            userReq = (userReq.substring(userReq.indexOf("!") + 1));
                        }
                        jda.retrieveUserById(userReq).queue(user -> sendCustomPM(user, eb.build()));

                        message.delete().queue();
                    }
                } else {

                    if (Objects.requireNonNull(message.getEmbeds().get(0).getFields().get(0).getValue()).matches("Done")) {
                        message.delete().queue();
                    } else {
                        sendPM(e.getAuthor(), "Error. You can't remove task without mentioning reason if its not complete.\n" + commandSyntax, "error");
                    }

                }

            }, failure -> {
                if (failure instanceof ErrorResponseException) {
                    ErrorResponseException ex = (ErrorResponseException) failure;

                    if (ex.getErrorResponse() == ErrorResponse.UNKNOWN_MESSAGE) {
                        sendPM(e.getAuthor(), "Error: Message doesn't exist.", "error");
                    }
                }
            });
        }
    }
}