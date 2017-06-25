package com.kloudless;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kloudless.StaticImporter.Props;

/**
 * Created by gchiou on 08/06/2017.
 * <p>
 * TestInfoHelper collects required information for unit test cases
 */
public final class TestInfoHelper {

	private final static String API_KEY_ARG = "apiKey";
	private final static String BEARER_TOKEN_ARG = "bearerToken";
	private final static String API_KEY_ENV = "API_KEY";
	private final static String BEARER_TOKEN_ENV = "BEARER_TOKEN";

	private final static String TEST_ACCOUNTS_KEY = "test_accounts";
	private final static String API_SERVER_ADDR_KEY = "api_server_addr";
	private final static String API_SERVER_PROTO_KEY = "api_server_proto";
	private final static String API_SERVER_PORT_KEY = "api_server_port";

	private final static String ONE_TEST_ACCOUNT_JSON_KEY = "one_test_account_json";
	private final static String PATH_UPLOADING_FILE_KEY = "path_uploading_file";
	private final static Object lck = new Object();
	private static TestInfoHelper instance = null;
	private String apiKey = "";
	private String beaerToken = "";

	private TestInfoHelper() {
	}

	public static TestInfoHelper getInst() {
		if (instance == null) {
			synchronized (lck) {
				if (instance == null) {
					instance = new TestInfoHelper();
					instance.bindKey();
				}
			}
		}
		return instance;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getBearerToken() {
		return beaerToken;
	}

	public String getApiServerAddr() {
		return Props.getProperty(API_SERVER_ADDR_KEY, "api.kloudless.com").trim();
	}

	public String getApiServerProto() {
		return Props.getProperty(API_SERVER_PROTO_KEY, "https").trim();
	}

	public String getApiServerPort() {
		return Props.getProperty(API_SERVER_PORT_KEY, "443").trim();
	}

	public String getOneTestAccountJson() {
		return Props.getProperty(ONE_TEST_ACCOUNT_JSON_KEY).trim();
	}

	public String getPathUploadingFile() {
		return Props.getProperty(PATH_UPLOADING_FILE_KEY);
	}

	public String getApiBasedUrl() {
		String proto = getApiServerProto();
		String addr = getApiServerAddr();
		String port = getApiServerPort();

		StringBuilder sb = new StringBuilder();
		sb.append(proto);
		sb.append("://");
		sb.append(addr);
		if (proto.equalsIgnoreCase("https") && !port.equals("443")) {
			sb.append(":");
			sb.append(port);
		}
		if (proto.equalsIgnoreCase("http") && !port.equals("80")) {
			sb.append(":");
			sb.append(port);
		}
		return sb.toString();
	}

	public List<String> getTestAccounts() {
		String[] accounts = Props.getProperty(TEST_ACCOUNTS_KEY).split(";");
		if (accounts.length == 0) return new ArrayList<>(0);
		List<String> accounts2 = Arrays.stream(accounts).filter(x -> x.length() > 0)
				.map(String::trim).collect(Collectors.toList());
		return accounts2;
	}

	/**
	 * key binding order is environment variable, command argument and property file
	 */
	private void bindKey() {
		//get api key from env
		Optional<String> opt = Optional.ofNullable(System.getenv(API_KEY_ENV));

		if (opt.isPresent() && opt.get().length() > 0) {
			apiKey = opt.get().trim();
		} else {
			opt = Optional.ofNullable(System.getProperty(API_KEY_ARG));
			if (opt.isPresent() && opt.get().length() > 0) {
				apiKey = opt.get().trim();
			}
		}

		opt = Optional.ofNullable(System.getenv(BEARER_TOKEN_ENV));
		if (opt.isPresent() && opt.get().length() > 0) {
			beaerToken = opt.get().trim();
		} else {
			opt = Optional.ofNullable(System.getProperty(BEARER_TOKEN_ARG));
			if (opt.isPresent() && opt.get().length() > 0) {
				beaerToken = opt.get().trim();
			}
		}

		if (apiKey.length() == 0 && beaerToken.length() == 0) {
			throw new IllegalArgumentException("api key or bearer token is not found!");
		}
	}
}
