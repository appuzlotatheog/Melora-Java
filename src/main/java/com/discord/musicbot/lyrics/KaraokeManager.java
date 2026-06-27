package com.discord.musicbot.lyrics;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KaraokeManager {
    private static final Pattern LRC_PATTERN = Pattern.compile("\\[(\\d{2}):(\\d{2})\\.(\\d{2,3})\\](.*)");

    public static class LrcLine {
        public final long timestampMs;
        public final String text;

        public LrcLine(long timestampMs, String text) {
            this.timestampMs = timestampMs;
            this.text = text;
        }
    }

    public static List<LrcLine> parseLrc(String lrc) {
        List<LrcLine> lines = new ArrayList<>();
        if (lrc == null || lrc.isBlank()) return lines;

        for (String line : lrc.split("\n")) {
            Matcher m = LRC_PATTERN.matcher(line);
            if (m.matches()) {
                long min = Long.parseLong(m.group(1));
                long sec = Long.parseLong(m.group(2));
                long ms = Long.parseLong(m.group(3));
                if (m.group(3).length() == 2) {
                    ms *= 10;
                }
                long timestamp = (min * 60 * 1000) + (sec * 1000) + ms;
                String text = m.group(4).trim();
                lines.add(new LrcLine(timestamp, text));
            }
        }
        return lines;
    }

    public static String getActiveLine(List<LrcLine> lines, long currentPositionMs) {
        if (lines == null || lines.isEmpty()) return null;
        
        String activeText = lines.get(0).text;
        for (LrcLine line : lines) {
            if (currentPositionMs >= line.timestampMs) {
                activeText = line.text;
            } else {
                break;
            }
        }
        return activeText;
    }
}
