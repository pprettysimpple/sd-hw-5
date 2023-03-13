package hw.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import hw.aspect.Profile;

@Component
public class Example {

    @Value("${weatherApiKey}")
    private String API_KEY;

    @Profile
    public void getCurrentTime() {
        try {
            var url = new java.net.URL("http://worldtimeapi.org/api/timezone/Europe/Belgrade");
            var con = (java.net.HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            var in = new java.util.Scanner(con.getInputStream());
            var response = new StringBuilder();
            while (in.hasNextLine()) {
                response.append(in.nextLine());
            }
            System.out.println("Work done: " + response.hashCode());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Profile
    public void getCurrentWeather() {
        try {
                String urlStr = "https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=" + API_KEY;
            var url = new java.net.URL(urlStr);
            var con = (java.net.HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            var in = new java.util.Scanner(con.getInputStream());
            var response = new StringBuilder();
            while (in.hasNextLine()) {
                response.append(in.nextLine());
            }
            System.out.println("Work done: " + response.hashCode());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
