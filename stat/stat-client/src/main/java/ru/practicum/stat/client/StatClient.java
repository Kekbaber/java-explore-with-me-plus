package ru.practicum.stat.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stat.dto.EndpointHit;
import ru.practicum.stat.dto.ViewStats;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatClient {
    private final RestClient restClient;

    /**
     * POST /hit
     */
    public boolean saveHit(EndpointHit hit) {
        try {
            ResponseEntity<Void> response = restClient.post()
                    .uri("/hit")
                    .body(hit)
                    .retrieve()
                    .toBodilessEntity();

            if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                return true;
            } else {
                log.warn("Просмотр не учтен в статистике: app={}, status={}",
                        hit.getApp(),
                        response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            log.error("Ошибка при сохранении данных об просмотре: app={}, uri={}, error={}",
                    hit.getApp(),
                    hit.getUri(),
                    e.getMessage());
            return false;
        }
    }

    /**
     * GET /stats
     */
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/stats")
                    .queryParam("start", start)
                    .queryParam("end", end)
                    .queryParam("unique", unique != null ? unique : false);

            if (uris != null && !uris.isEmpty()) {
                for (String uri : uris) {
                    builder.queryParam("uris", uri);
                }
            }

            String url = builder.build().toUriString();

            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ViewStats>>() {
                    });
        } catch (Exception e) {
            log.error("Ошибка при получении статистики: {}", e.getMessage());
            return List.of();
        }

    }
}