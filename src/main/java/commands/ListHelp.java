package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

import static functions.Constants.BOT_PREFIX;
import static functions.UtilitySends.sendCustomPM;

public class ListHelp {

    public static void commandHelp(GuildMessageReceivedEvent e) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(0, 150, 130));

        eb.setAuthor("Task.it Standard Command List", null, "https://cdn.discordapp.com/app-icons/692401395613171814/6bf084825c16748ff058818115990064.png");
        eb.setDescription("To get help just type needed command name without arguments.\n:black_small_square:");
        eb.addField(":memo: Tasks Management", "`" + BOT_PREFIX + "create`\n`" + BOT_PREFIX + "update`\n`" + BOT_PREFIX + "remove`", true);
        eb.addBlankField(true);
        eb.addField(":wrench: Utility", "`" + BOT_PREFIX + "help`", true);

        sendCustomPM(e.getAuthor(), eb.build());

    }

}