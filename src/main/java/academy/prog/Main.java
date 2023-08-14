package academy.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.println("Enter your login: ");
			String login = scanner.nextLine();
			User user = new User(login);
			user.send(Utils.getURL()+"/AddUser");

			Thread th = new Thread(new GetThread(login));
			th.setDaemon(true);
			th.start();

            System.out.println("Enter your message: ");
			while (true) {
				String text = scanner.nextLine();
				if (text.isEmpty()) break;

				if("/list".equals(text)){
					getAllUser();
				}

				Message m = new Message(login, text);
				int res = m.send(Utils.getURL() + "/add");

				if (res != 200) { // 200 OK
					System.out.println("HTTP error occurred: " + res);
					return;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	public static void getAllUser() {
        int n = 0;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        URL url = null;
        InputStream is;
		try {
			url = new URL(Utils.getURL() + "/getUser?from=" + n);

			HttpURLConnection http = (HttpURLConnection) url.openConnection();

			is = http.getInputStream();

			byte[] buf = responseBodyToArray(is);
			String strBuf = new String(buf, StandardCharsets.UTF_8);

			JsonUsers list = gson.fromJson(strBuf, JsonUsers.class);
			if (list != null) {
				for (User u : list.getList()) {
					System.out.println(u.getName());
				}
				n++;
			}

			is.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static byte[] responseBodyToArray(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[10240];
		int r;

		do {
			r = is.read(buf);
			if (r > 0) bos.write(buf, 0, r);
		} while (r != -1);

		return bos.toByteArray();
	}
}
