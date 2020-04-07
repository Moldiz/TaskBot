package functions;

public class Constants {

    // Properties (user configurable)
    public static final String TOKEN = Configurations.getInstance().getProperty(Properties.PROP_TOKEN);
    public static final String BOT_PREFIX = Configurations.getInstance().getProperty(Properties.PROP_BOT_PREFIX);

    // Constants (not user configurable)
    public static final String PATH_CONFFILE = "config.properties";
    public static final String VERSION = "v1.2.1";
}
