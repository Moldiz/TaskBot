package functions;

public class Constants {

    // Properties (user configurable)
    public static final String TOKEN = Configurations.getInstance().getProperty(Properties.TOKEN);
    public static final String BOT_PREFIX = Configurations.getInstance().getProperty(Properties.BOT_PREFIX);

    // Constants (not user configurable)
    public static final String PATH_CONFFILE = "config.properties";
}
