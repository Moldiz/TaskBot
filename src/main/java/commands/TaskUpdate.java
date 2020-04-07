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

public class TaskUpdate {

    public static void commandUpdate(GuildMessageReceivedEvent e, ArrayList<String> args) {

        String commandSyntax = "Syntax: `" + BOT_PREFIX + "update [Task id], *[Quantity], *[Message]`";
        String commandInfo = "Update status and quantity of task, also sends notification to requester.\n\n" +
                "Submitting update without `Quantity` when status is `To do` or `Partially Done` will change status to `In realisation`.\n" +
                "Submitting update with `Quantity` lower then needed will change status to `Partially done`.\n" +
                "Submitting update with `Quantity` matching need will change status to `Done`.\n\n" +
                "`[Task id]` must be a valid number,\n" +
                "`[Quantity]` is optional, but when used it must be a valid number,\n" +
                "`[Message]` is optional, but if used must contain 3-100 letters.";

        if (args.get(0).matches("\\s*")) {
            sendPM(e.getAuthor(), commandSyntax + "\n\n" + commandInfo,"info");

        } else if ((args.size()>3)) {
            sendPM(e.getAuthor(), "Syntax Error. Too many arguments.\n" + commandSyntax,"error");

        } else if (!(args.get(0).matches("-?\\d+"))) {
            sendPM(e.getAuthor(), "Error: Task id is not a valid number.\n" + commandSyntax,"error");

        } else {

            e.getChannel().retrieveMessageById(args.get(0)).queue(message -> {

                int newQuantity = Integer.parseInt(Objects.requireNonNull(message.getEmbeds().get(0).getFields().get(3).getValue()));
                String newStatus = message.getEmbeds().get(0).getFields().get(0).getValue();

                if (args.size()>1) {

                    if (!(args.get(1).matches("\\d+"))) {
                        sendPM(e.getAuthor(), "Error: Quantity is not a valid number.\n" + commandSyntax, "error");
                        return;
                    } else if (Integer.parseInt(args.get(1)) > Integer.parseInt(Objects.requireNonNull(message.getEmbeds().get(0).getFields().get(4).getValue()))) {
                        sendPM(e.getAuthor(), "Error: Quantity is to high. Max quantity for this task is: \n" + message.getEmbeds().get(0).getFields().get(4).getValue() + ".", "error");
                        return;
                    } else if (Integer.parseInt(args.get(1)) == Integer.parseInt(Objects.requireNonNull(message.getEmbeds().get(0).getFields().get(3).getValue()))) {
                        sendPM(e.getAuthor(), "Error: New quantity must be different from the already existing.\n" + commandSyntax, "error");
                        return;
                    } else if (Integer.parseInt(args.get(1)) == Integer.parseInt(Objects.requireNonNull(message.getEmbeds().get(0).getFields().get(4).getValue()))) {
                        newQuantity = Integer.parseInt(args.get(1));
                        newStatus = "Done";
                    } else {
                        newQuantity = Integer.parseInt(args.get(1));
                        newStatus = "Partially done";
                    }

                } else {

                    assert newStatus != null;
                    if ((newStatus.matches("To do")) || (newStatus.matches("Partially done"))){
                        newStatus = "In realisation";
                    } else if (newStatus.matches("In realisation")) {
                        sendPM(e.getAuthor(),"Error: This task is already in realisation\n" + commandSyntax,"error");
                        return;
                    }

                }

                // Send Notification about task update to Requester

                EmbedBuilder ebPM = new EmbedBuilder();
                ebPM.setColor(new Color(0, 150, 130));

                ebPM.setAuthor("Task.it Message", null, "https://cdn.discordapp.com/app-icons/692401395613171814/6bf084825c16748ff058818115990064.png");
                ebPM.setTitle("Your task has been updated by " + e.getAuthor().getName(), null);
                if (args.size()>2) {
                    if (!(args.get(2).matches(".*\\w{3,}.*"))) {
                        sendPM(e.getAuthor(), "Syntax Error. Message must contain min 3 letters.\n" + commandSyntax, "error");
                        return;
                    } else if ((args.get(2).length() > 100)) {
                        sendPM(e.getAuthor(), "Syntax Error. Message argument is too long. Max 100 characters.\n" + commandSyntax, "error");
                        return;
                    } else {

                        ebPM.setDescription("Message from person carrying out the order:\n" +
                                args.get(2) +
                                "\n:black_small_square:");
                    }
                } else {
                    ebPM.setDescription(":black_small_square:");
                }
                ebPM.addField("Status: ", newStatus, true);
                ebPM.addField("Item: ", message.getEmbeds().get(0).getFields().get(1).getValue(), true);
                ebPM.addField("Quality: ", message.getEmbeds().get(0).getFields().get(2).getValue(), true);
                ebPM.addField("Quantity have: ", String.valueOf(newQuantity), true);
                ebPM.addField("Quantity need: ", message.getEmbeds().get(0).getFields().get(4).getValue(), true);
                ebPM.addField("Requested by: ", message.getEmbeds().get(0).getFields().get(5).getValue(), true);
                ebPM.addField("Date: ", message.getEmbeds().get(0).getFields().get(6).getValue(), true);
                ebPM.addField("Task id: ", message.getEmbeds().get(0).getFields().get(7).getValue(), true);

                JDA jda = e.getJDA();
                String userReq = message.getEmbeds().get(0).getFields().get(5).getValue();
                userReq = (Objects.requireNonNull(userReq).substring(userReq.indexOf("<@")+2,userReq.indexOf(">")));
                if (userReq.matches("!.*")) {
                    userReq = (userReq.substring(userReq.indexOf("!")+1));
                }
                jda.retrieveUserById(userReq).queue(user -> sendCustomPM(user, ebPM.build()));

                // Build new message to swap it with existing

                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(new Color(0, 150, 130));

                eb.addField("Status: ", newStatus, true);
                eb.addField("Item: ", message.getEmbeds().get(0).getFields().get(1).getValue(), true);
                eb.addField("Quality: ", message.getEmbeds().get(0).getFields().get(2).getValue(), true);
                eb.addField("Quantity have: ", String.valueOf(newQuantity), true);
                eb.addField("Quantity need: ", message.getEmbeds().get(0).getFields().get(4).getValue(), true);
                eb.addField("Requested by: ", message.getEmbeds().get(0).getFields().get(5).getValue(), true);
                eb.addField("Date: ", message.getEmbeds().get(0).getFields().get(6).getValue(), true);
                eb.addField("Task id: ", message.getEmbeds().get(0).getFields().get(7).getValue(), true);

                // Swap task message with new one

                message.editMessage(eb.build()).queue();

            }, failure -> {
                if (failure instanceof ErrorResponseException) {
                    ErrorResponseException ex = (ErrorResponseException) failure;

                    if (ex.getErrorResponse() == ErrorResponse.UNKNOWN_MESSAGE) {
                        sendPM(e.getAuthor(), "Error: Message doesn't exist.","error");
                    }
                }
            });
        }

    }

}