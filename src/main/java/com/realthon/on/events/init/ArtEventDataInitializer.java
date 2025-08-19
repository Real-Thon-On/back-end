package com.realthon.on.events.init;

import com.realthon.on.events.entity.LocalArtEvent;
import com.realthon.on.events.repository.LocalArtEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArtEventDataInitializer implements CommandLineRunner {

    private final LocalArtEventRepository repository;
    private final ResourceLoader resourceLoader;

    @Value("${events.init.enabled:false}")
    private boolean enabled;

    @Value("${events.init.csv-path:}")
    private String csvPath;

    @Override
    public void run(String... args) {
        if (!enabled) {
            log.info("[ArtEventDataInitializer] events.init.enabled=false, skip.");
            return;
        }

        // ✅ 테이블에 데이터가 1건이라도 있으면 전체 초기화 패스
        long existing = repository.count();
        if (existing > 0) {
            log.info("[ArtEventDataInitializer] table already has {} rows, skip init.", existing);
            return;
        }

        if (csvPath == null || csvPath.isBlank()) {
            log.warn("[ArtEventDataInitializer] csv-path is empty, skip.");
            return;
        }

        Resource resource = resolveResource(csvPath);
        log.info("[ArtEventDataInitializer] resolved: desc='{}', exists={}, readable={}",
                resource.getDescription(), resource.exists(), resource.isReadable());

        // fallback: classpath:/... 로 재시도
        if (!resource.exists()) {
            String alt = csvPath.startsWith("classpath:")
                    ? csvPath.replace("classpath:", "classpath:/")
                    : "classpath:/local_art_events.csv";
            Resource fallback = resolveResource(alt);
            log.info("[ArtEventDataInitializer] fallback: {} -> {} (exists={}, readable={})",
                    csvPath, alt, fallback.exists(), fallback.isReadable());
            resource = fallback;
        }
        if (!resource.exists()) {
            log.warn("[ArtEventDataInitializer] CSV not found after fallback: {}", csvPath);
            return;
        }

        log.info("[ArtEventDataInitializer] Start loading CSV from {}", resource.getDescription());

        try (InputStream is = resource.getInputStream();
             BOMInputStream bis = new BOMInputStream(is); // UTF-8 BOM 제거
             Reader reader = new InputStreamReader(bis, StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(reader,
                     CSVFormat.DEFAULT.builder()
                             .setSkipHeaderRecord(false)
                             .setHeader()
                             .setIgnoreHeaderCase(true)
                             .setIgnoreEmptyLines(true)
                             .setTrim(true)
                             .build())) {

            log.info("[ArtEventDataInitializer] CSV headers = {}", parser.getHeaderMap().keySet());

            List<LocalArtEvent> buffer = new ArrayList<>(1000);
            long parsed = 0, saved = 0;

            for (CSVRecord rec : parser) {
                if (rec == null || rec.size() == 0) continue;

                LocalArtEvent e = mapRecord(rec);

                // title 또는 url 없으면 스킵 (NOT NULL 안전)
                if (isBlank(e.getTitle()) || isBlank(e.getUrl())) {
                    log.warn("[ArtEventDataInitializer] skip row#{}: missing title or url. raw='{}'",
                            rec.getRecordNumber(), truncate(rec.toString(), 200));
                    parsed++;
                    continue;
                }

                // ✅ 갱신/병합 로직 제거: 그대로 신규 적재만 수행
                buffer.add(e);

                if (buffer.size() >= 1000) {
                    repository.saveAll(buffer);
                    buffer.clear();
                }
                saved++;
                parsed++;

                if (parsed <= 3) { // 샘플 로그
                    log.info("[ArtEventDataInitializer] sample row#{} title='{}', url='{}'",
                            rec.getRecordNumber(), e.getTitle(), e.getUrl());
                }
            }

            if (!buffer.isEmpty()) repository.saveAll(buffer);

            log.info("[ArtEventDataInitializer] rows parsed={}, rows saved={}", parsed, saved);
            log.info("[ArtEventDataInitializer] table total count after init={}", repository.count());

        } catch (Exception e) {
            log.error("[ArtEventDataInitializer] CSV load failed", e);
        }
    }

    private Resource resolveResource(String path) {
        // classpath:, file:, http:, 절대경로 등 스프링 규약 자동 처리
        return resourceLoader.getResource(path);
    }

    /** 제공된 정확한 헤더 스키마 기준 매핑 */
    private LocalArtEvent mapRecord(CSVRecord r) {
        return LocalArtEvent.builder()
                .title(val(r,"title"))
                .url(val(r,"url"))
                .startDate(date(val(r,"start_date")))
                .endDate(date(val(r,"end_date")))
                .imageUrl(val(r,"image_url"))
                .categories(val(r,"categories"))
                .mainCategory(val(r,"main_category"))
                .siteName(val(r,"site_name"))
                .siteType(val(r,"site_type"))
                .scrapedDate(dateTimeToDate(val(r,"scraped_date")))
                .detailTitle(val(r,"detail_title"))
                .detailImages(val(r,"detail_images"))
                .imageCount(intOrNull(val(r,"image_count")))
                .build();
    }

    private boolean nz(String s) { return s != null && !s.isBlank(); }
    private boolean isBlank(String s){ return s == null || s.isBlank(); }
    private String truncate(String s, int n){ return s == null ? null : (s.length() <= n ? s : s.substring(0, n) + "..."); }

    /** 헤더 누락 시 경고 로그 + 공백을 null 처리 */
    private String val(CSVRecord r, String k){
        try {
            String v = r.get(k);
            if (v == null) return null;
            v = v.trim();
            return v.isEmpty() ? null : v;
        } catch (Exception e) {
            log.warn("[CSV] missing header '{}' in record#{}: {}", k, r.getRecordNumber(), truncate(r.toString(), 160));
            return null;
        }
    }

    private Integer intOrNull(String s){
        try { return (s == null || s.isBlank()) ? null : Integer.parseInt(s.trim()); }
        catch (Exception e) { return null; }
    }

    private LocalDate dateTimeToDate(String s){
        if (isBlank(s)) return null;
        // yyyy-MM-dd HH:mm:ss
        try {
            return java.time.LocalDateTime.parse(s.trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
        } catch (Exception ignore) {}
        return date(s);
    }

    private LocalDate date(String s){
        if (isBlank(s)) return null;
        for (var f : new DateTimeFormatter[]{
                DateTimeFormatter.ISO_LOCAL_DATE,                // 2025-08-17
                DateTimeFormatter.ofPattern("yyyy.MM.dd"),       // 2025.08.17
                DateTimeFormatter.ofPattern("yyyy/MM/dd")        // 2025/08/17
        }) {
            try { return LocalDate.parse(s.trim(), f); } catch (Exception ignore) {}
        }
        return null;
    }
}
