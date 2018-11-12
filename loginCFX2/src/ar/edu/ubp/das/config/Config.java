package ar.edu.ubp.das.config;

public class Config {
	private static Config config;
	private String dbUser;
	private String dbPass;

	private Config() {
		dbUser = "SA";
		dbPass = "Changeme13";
	}

	public static Config getInstance() {
		if (config == null) {
			config = new Config();
		}
		return config;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}

}
