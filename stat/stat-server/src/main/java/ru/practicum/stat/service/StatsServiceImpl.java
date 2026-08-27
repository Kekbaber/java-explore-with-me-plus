package ru.practicum.stat.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.EndpointHit;
import ru.practicum.stat.dto.ViewStats;
import ru.practicum.stat.model.Hit;
import ru.practicum.stat.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public void saveHit(EndpointHit hitDto) {
        Hit hit = Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(LocalDateTime.parse(hitDto.getTimestamp(), formatter))
                .build();
        statsRepository.save(hit);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start,
                                    LocalDateTime end,
                                    List<String> uris,
                                    Boolean unique) {

        if (unique) {
            if (uris != null && !uris.isEmpty()) {
                return statsRepository.getStatsWithUniqueIpAndUris(start, end, uris);
            } else {
                return statsRepository.getStatsWithUniqueIpWithoutUris(start, end);
            }
        } else {
            if (uris != null && !uris.isEmpty()) {
                return statsRepository.getStatsAllAndUris(start, end, uris);
            } else {
                return statsRepository.getStatsAllWithoutUris(start, end);
            }
        }
    }
}
