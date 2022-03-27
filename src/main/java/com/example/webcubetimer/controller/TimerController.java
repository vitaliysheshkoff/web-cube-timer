package com.example.webcubetimer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.util.TimeZone;


@Controller
public class TimerController {

    @GetMapping("/")
    public String getTimer(Model model, TimeZone timeZone) throws IOException, InterruptedException {
        model.addAttribute("currentTimer", getTime().atZone(timeZone.toZoneId()));
        System.out.println(timeZone.getID());
        return "timer";
    }


    private Instant getTime() throws IOException {
        URL url = new URL("https://currentmillis.com/time/minutes-since-unix-epoch.php");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        long minutes = Long.parseLong(in.readLine());
        in.close();
        con.disconnect();
        return Instant.ofEpochSecond(minutes * 60);
    }

    @RequestMapping
    public TimeZone getTimeZone(TimeZone timezone) {
        System.out.println(timezone.getDisplayName());
        return timezone;
    }

    private static String TIME_RESOURCE = "https://time.is/";

    public void fetchCurrentTime() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TIME_RESOURCE))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse.body());
    }
}


