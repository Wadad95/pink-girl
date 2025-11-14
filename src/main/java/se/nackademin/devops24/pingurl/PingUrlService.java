package se.nackademin.devops24.pingurl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import se.nackademin.devops24.pingurl.model.PingedURL;
import se.nackademin.devops24.pingurl.repository.URLRepository;

@Service
public class PingUrlService {

    private static final Logger logger = LoggerFactory.getLogger(PingUrlService.class);

    private final URLRepository urlRepository;

    public PingUrlService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Collection<PingedURL> getPingUrls() {
        return urlRepository.getUrls();
    }

    public void addPingUrl(String url, String name) {
        urlRepository.save(name, url);
    }

    public void deleteUrlFromPing(String name) {
        urlRepository.delete(name);
    }

    public void pingOneUrl(String name) {
        var url = urlRepository.getUrls()
                .stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElseThrow();
        pingUrl(url);
    }






    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void onesPerMinute() {
        var pingedURLs = getPingUrls();

        for (var pingedURL : pingedURLs) {
            pingUrl(pingedURL);
        }
    }

    private void pingUrl(PingedURL pingedURL) {
        try (var client = HttpClient.newBuilder().build()) {
            HttpRequest request = HttpRequest.newBuilder(new URI(pingedURL.getUrl())).GET().build();
            HttpResponse<Void> result = client.send(request, HttpResponse.BodyHandlers.discarding());

            // Check that the code is ok.
            if (result.statusCode() == 200) {
                pingedURL.setResult("success");
            } else {
                pingedURL.setResult("failure");
            }
            pingedURL.setLastPinged(LocalDateTime.now());
            urlRepository.update(pingedURL);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid or unreachable URL: " + pingedURL.getUrl());
        }
    }
}
