/*

 */

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

import static functions.Constants.BOT_PREFIX;
import static functions.Constants.TOKEN;

public class Bot {

    public static void main(String[] args) {

        System.out.println("\nLoaded properties:\nTOKEN: " + TOKEN + "\nBOT_PREFIX: " + BOT_PREFIX + "\n");

        try
        {
            JDA jda = JDABuilder.createDefault(TOKEN)
                    .addEventListeners(new CommandParser())
                    .build()
                    .awaitReady();

            jda.getPresence().setActivity(Activity.playing("Type " + BOT_PREFIX + "help for help :)"));

            System.out.println("Finished Building JDA!");
        }

        catch (LoginException | InterruptedException e)
        {
            e.printStackTrace();
        }

    }

}