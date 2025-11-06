package com.example.mcp_server_tutorial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;
import org.json.JSONArray;

@Component
public class McpTools {

    private static final Logger log = LoggerFactory.getLogger(McpTools.class);

    // ✅ Reads from app.yaml → youtube.api.key → ${YOUTUBE_API_KEY}
    @Value("${YOUTUBE_API_KEY}")
    private String youtubeApiKey;

    private static final String CHANNEL_ID = "UCcabW7890RKJzL968QWEykA"; // CS50 official channel ID

    @McpTool(name = "CS50 latest videos", description = "Fetches the latest CS50 videos from YouTube")
    public String getLastVideos(@McpToolParam Integer limit) {
        try {
            // Build API URL
            String apiUrl = UriComponentsBuilder.fromUriString("https://www.googleapis.com/youtube/v3/search")
                    .queryParam("key", youtubeApiKey)
                    .queryParam("channelId", CHANNEL_ID)
                    .queryParam("part", "snippet")
                    .queryParam("order", "date")
                    .queryParam("maxResults", limit)
                    .build()
                    .toUriString();

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(apiUrl, String.class);

            JSONObject json = new JSONObject(response);
            JSONArray items = json.getJSONArray("items");

            StringBuilder result = new StringBuilder("🎓 **CS50 Latest Videos:**\n\n");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject snippet = item.getJSONObject("snippet");

                String title = snippet.getString("title");
                String videoId = item.getJSONObject("id").optString("videoId", "");
                String url = "https://www.youtube.com/watch?v=" + videoId;

                result.append(i + 1).append(". ").append(title)
                      .append("\n").append(url).append("\n\n");
            }

            return result.toString();

        } catch (Exception e) {
            log.error("Error fetching CS50 videos", e);
            return "⚠️ Failed to fetch CS50 videos: " + e.getMessage();
        }
    }
}
