package crawler;

/**
*
* @author Brijesh Bhatt
* @since 1.0
*
*/
public final class AppConstant {

	public static final String HOME;
	public static final String FILESEPERATOR;
	public static final String MAIL_FOLDER;

	static {
		java.util.Properties properties = System.getProperties();
		HOME = properties.get("user.home").toString();
		FILESEPERATOR = properties.get("file.separator").toString();
		MAIL_FOLDER = "mail";
	}

}
