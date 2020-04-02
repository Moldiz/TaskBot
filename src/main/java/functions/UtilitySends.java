package functions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class UtilitySends {

    static public void sendCustomPM(User user, MessageEmbed content) {
        user.openPrivateChannel().queue((channel) ->
            channel.sendMessage(content).queue());
    }

    static public void sendPM(User user, String content, String type) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(0, 150, 130));

        eb.setAuthor("Task.it Message", null, "https://cdn.discordapp.com/app-icons/692401395613171814/6bf084825c16748ff058818115990064.png");

        switch (type) {
            case "error":
                eb.setTitle("We have encountered a Error.", null);
                break;
            case "info":
                eb.setTitle("Here is the info about command:", null);
                break;
            default:
                eb.setTitle(type);
                break;
        }

        eb.setDescription(content);

        user.openPrivateChannel().queue((channel) ->
                channel.sendMessage(eb.build()).queue());
    }
}