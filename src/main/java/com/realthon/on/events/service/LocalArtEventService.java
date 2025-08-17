package com.realthon.on.events.service;



import com.realthon.on.events.dto.request.LocalArtEventRequest;
import com.realthon.on.events.dto.response.LocalArtEventResponse;
import com.realthon.on.events.entity.LocalArtEvent;
import com.realthon.on.events.repository.LocalArtEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class LocalArtEventService {

    private final LocalArtEventRepository repository;

    @Transactional
    public LocalArtEventResponse create(LocalArtEventRequest req) {
        LocalArtEvent e = LocalArtEvent.builder()
                .title(z(req.getTitle()))
                .url(z(req.getUrl()))
                .startDate(parseDate(req.getStartDate()))
                .endDate(parseDate(req.getEndDate()))
                .imageUrl(z(req.getImageUrl()))
                .categories(z(req.getCategories()))
                .mainCategory(z(req.getMainCategory()))
                .siteName(z(req.getSiteName()))
                .siteType(z(req.getSiteType()))
                .scrapedDate(parseDate(req.getScrapedDate()))
                .detailTitle(z(req.getDetailTitle()))
                .detailImages(z(req.getDetailImages()))
                .imageCount(req.getImageCount())
                .build();
        return toResponse(repository.save(e));
    }

    @Transactional(readOnly = true)
    public LocalArtEventResponse get(Long id) {
        LocalArtEvent e = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
        return toResponse(e);
    }

    @Transactional(readOnly = true)
    public Page<LocalArtEventResponse> list(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(pageable).map(this::toResponse);
    }

    @Transactional
    public LocalArtEventResponse update(Long id, LocalArtEventRequest req) {
        LocalArtEvent cur = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));

        // 부분 업데이트(null/blank는 유지)
        if (nz(req.getTitle())) cur.setTitle(z(req.getTitle()));
        if (nz(req.getUrl())) cur.setUrl(z(req.getUrl()));
        if (req.getStartDate() != null) cur.setStartDate(parseDate(req.getStartDate()));
        if (req.getEndDate() != null) cur.setEndDate(parseDate(req.getEndDate()));
        if (nz(req.getImageUrl())) cur.setImageUrl(z(req.getImageUrl()));
        if (nz(req.getCategories())) cur.setCategories(z(req.getCategories()));
        if (nz(req.getMainCategory())) cur.setMainCategory(z(req.getMainCategory()));
        if (nz(req.getSiteName())) cur.setSiteName(z(req.getSiteName()));
        if (nz(req.getSiteType())) cur.setSiteType(z(req.getSiteType()));
        if (req.getScrapedDate() != null) cur.setScrapedDate(parseDate(req.getScrapedDate()));
        if (nz(req.getDetailTitle())) cur.setDetailTitle(z(req.getDetailTitle()));
        if (nz(req.getDetailImages())) cur.setDetailImages(z(req.getDetailImages()));
        if (req.getImageCount() != null) cur.setImageCount(req.getImageCount());

        return toResponse(repository.save(cur));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Event not found: " + id);
        }
        repository.deleteById(id);
    }

    // ===== helpers =====
    private boolean nz(String s){ return s != null && !s.isBlank(); }
    private String z(String s){ return s == null ? null : s.trim(); }

    private String fmt(LocalDate d){ return d == null ? null : d.toString(); }

    private LocalArtEventResponse toResponse(LocalArtEvent e) {
        return LocalArtEventResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .url(e.getUrl())
                .startDate(fmt(e.getStartDate()))
                .endDate(fmt(e.getEndDate()))
                .imageUrl(e.getImageUrl())
                .categories(e.getCategories())
                .mainCategory(e.getMainCategory())
                .siteName(e.getSiteName())
                .siteType(e.getSiteType())
                .scrapedDate(fmt(e.getScrapedDate()))
                .detailTitle(e.getDetailTitle())
                .detailImages(e.getDetailImages())
                .imageCount(e.getImageCount())
                .build();
    }

    private LocalDate parseDate(String s) {
        if (s == null || s.isBlank()) return null;
        for (var f : new DateTimeFormatter[]{
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ofPattern("yyyy.MM.dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd")
        }) {
            try { return LocalDate.parse(s.trim(), f); } catch (Exception ignore) {}
        }
        return null;
    }
}
