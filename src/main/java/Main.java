
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class Main {

	public static ObjectMapper mapper = new ObjectMapper();
	private static final String PATH = System.getProperty("user.home")+"/Downloads/NasaPicture.jpeg";

	public static void main(String[] args) {

		String url = getUrlfromNasa();
		if (url.contains("youtube")) {
			System.out.println("Ссылка на видео: "+url);
		} else {
			try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
					FileOutputStream fileOutputStream = new FileOutputStream(PATH)) {
				byte dataBuffer[] = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
					fileOutputStream.write(dataBuffer, 0, bytesRead);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static String getUrlfromNasa() {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setDefaultRequestConfig(RequestConfig.custom()
						.setConnectTimeout(5000)
						.setSocketTimeout(30000)
						.setRedirectsEnabled(false)
						.build())
				.build();) {

			HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=PppSeD6VEbGGywDUBxbmNgVzg87iNixMPhe4IhJ0");

			CloseableHttpResponse response = httpClient.execute(request);
			Nasa nasa = mapper.readValue(
					response.getEntity().getContent(),
					new TypeReference<>() {
					}
			);

			String url = nasa.getUrl();
			response.close();
			return url;
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
