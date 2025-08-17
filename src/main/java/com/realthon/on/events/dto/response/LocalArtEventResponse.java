package com.realthon.on.events.dto.response;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalArtEventResponse {
    private Long id;
    private String title;
    private String url;
    private String startDate;   // yyyy-MM-dd
    private String endDate;     // yyyy-MM-dd
    private String imageUrl;
    private String categories;
    private String mainCategory;
    private String siteName;
    private String siteType;
    private String scrapedDate; // yyyy-MM-dd
    private String detailTitle;
    private String detailImages;
    private Integer imageCount;
}

