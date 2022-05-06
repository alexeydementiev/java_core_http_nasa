
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class Main {

	public static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setDefaultRequestConfig(RequestConfig.custom()
						.setConnectTimeout(5000)
						.setSocketTimeout(30000)
						.setRedirectsEnabled(false)
						.build())
				.build();

		HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=PppSeD6VEbGGywDUBxbmNgVzg87iNixMPhe4IhJ0");

		CloseableHttpResponse response = httpClient.execute(request);
		Nasa nasa = mapper.readValue(
				response.getEntity().getContent(),
				new TypeReference<>() {
				}
		);

		String url = nasa.getUrl();
		System.out.println(url);


		response.close();
		httpClient.close();
	}
}
