package com.discord.musicbot.audio;

import com.sedmelluq.discord.lavaplayer.filter.AudioFilter;
import com.sedmelluq.discord.lavaplayer.filter.equalizer.Equalizer;
import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.github.natanbc.lavadsp.tremolo.TremoloPcmAudioFilter;
import com.github.natanbc.lavadsp.vibrato.VibratoPcmAudioFilter;
import com.github.natanbc.lavadsp.rotation.RotationPcmAudioFilter;
import com.github.natanbc.lavadsp.distortion.DistortionPcmAudioFilter;
import com.github.natanbc.lavadsp.lowpass.LowPassPcmAudioFilter;
import com.github.natanbc.lavadsp.karaoke.KaraokePcmAudioFilter;
import com.github.natanbc.lavadsp.channelmix.ChannelMixPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterManager {

    private static final Logger logger = LoggerFactory.getLogger(FilterManager.class);
    
    private final MusicManager musicManager;
    private String activeFilter = "none";

    private static final float[] BASS_BOOST = {
            0.2f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, -0.1f, -0.1f, -0.1f, -0.1f,
            -0.1f, -0.1f, -0.1f, -0.1f, -0.1f
    };

    private static final float[] EARRAPE = {
            1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f, 1.0f
    };

    private static final float[] POP = {
            -0.05f, -0.05f, 0.0f, 0.05f, 0.1f, 0.1f, 0.15f, 0.1f, 0.1f, 0.05f,
            0.05f, 0.0f, -0.05f, -0.05f, -0.05f
    };

    private static final float[] ROCK = {
            0.1f, 0.1f, 0.05f, 0.0f, -0.05f, -0.05f, -0.05f, 0.0f, 0.05f, 0.1f,
            0.1f, 0.15f, 0.1f, 0.1f, 0.1f
    };

    private static final float[] ELECTRONIC = {
            0.15f, 0.15f, 0.1f, 0.05f, 0.0f, -0.05f, 0.0f, 0.05f, 0.1f, 0.15f,
            0.15f, 0.1f, 0.1f, 0.1f, 0.15f
    };

    public FilterManager(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    public String getActiveFilter() {
        return activeFilter;
    }

    public void applyFilter(String filterName) throws Exception {
        AudioPlayer player = musicManager.getPlayer();
        this.activeFilter = filterName.toLowerCase();
        
        player.setFilterFactory((track, format, output) -> {
            List<AudioFilter> filters = new ArrayList<>();
            try {
                switch (activeFilter) {
                    case "bassboost":
                        Equalizer bb = new Equalizer(format.channelCount, output);
                        for (int i = 0; i < BASS_BOOST.length; i++) bb.setGain(i, BASS_BOOST[i]);
                        filters.add(bb);
                        break;
                    case "earrape":
                        Equalizer er = new Equalizer(format.channelCount, output);
                        for (int i = 0; i < EARRAPE.length; i++) er.setGain(i, EARRAPE[i]);
                        filters.add(er);
                        break;
                    case "pop":
                        Equalizer pop = new Equalizer(format.channelCount, output);
                        for (int i = 0; i < POP.length; i++) pop.setGain(i, POP[i]);
                        filters.add(pop);
                        break;
                    case "rock":
                        Equalizer rock = new Equalizer(format.channelCount, output);
                        for (int i = 0; i < ROCK.length; i++) rock.setGain(i, ROCK[i]);
                        filters.add(rock);
                        break;
                    case "electronic":
                        Equalizer elec = new Equalizer(format.channelCount, output);
                        for (int i = 0; i < ELECTRONIC.length; i++) elec.setGain(i, ELECTRONIC[i]);
                        filters.add(elec);
                        break;
                    case "nightcore":
                        TimescalePcmAudioFilter nightcore = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                        nightcore.setRate(1.25);
                        filters.add(nightcore);
                        break;
                    case "vaporwave":
                        TimescalePcmAudioFilter vaporwave = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                        vaporwave.setRate(0.8);
                        filters.add(vaporwave);
                        break;
                    case "8d":
                        RotationPcmAudioFilter rot = new RotationPcmAudioFilter(output, format.sampleRate);
                        rot.setRotationSpeed(0.2);
                        filters.add(rot);
                        break;
                    case "tremolo":
                        TremoloPcmAudioFilter trem = new TremoloPcmAudioFilter(output, format.channelCount, format.sampleRate);
                        trem.setDepth(0.5f);
                        trem.setFrequency(2.0f);
                        filters.add(trem);
                        break;
                    case "vibrato":
                        VibratoPcmAudioFilter vib = new VibratoPcmAudioFilter(output, format.channelCount, format.sampleRate);
                        vib.setDepth(0.5f);
                        vib.setFrequency(2.0f);
                        filters.add(vib);
                        break;
                    case "distortion":
                        DistortionPcmAudioFilter dist = new DistortionPcmAudioFilter(output, format.channelCount);
                        dist.setSinOffset(0.0f);
                        dist.setSinScale(1.0f);
                        dist.setCosOffset(0.0f);
                        dist.setCosScale(1.0f);
                        dist.setTanOffset(0.0f);
                        dist.setTanScale(1.0f);
                        dist.setOffset(0.0f);
                        dist.setScale(1.0f);
                        filters.add(dist);
                        break;
                    case "muffled":
                        LowPassPcmAudioFilter lowpass = new LowPassPcmAudioFilter(output, format.channelCount, format.sampleRate);
                        lowpass.setSmoothing(20.0f);
                        filters.add(lowpass);
                        break;
                    case "vocal_remove":
                        KaraokePcmAudioFilter karaoke = new KaraokePcmAudioFilter(output, format.channelCount, format.sampleRate);
                        karaoke.setLevel(1.0f);
                        karaoke.setMonoLevel(1.0f);
                        karaoke.setFilterBand(220.0f);
                        karaoke.setFilterWidth(100.0f);
                        filters.add(karaoke);
                        break;
                    case "mono":
                        ChannelMixPcmAudioFilter mono = new ChannelMixPcmAudioFilter(output);
                        mono.setLeftToLeft(0.5f);
                        mono.setLeftToRight(0.5f);
                        mono.setRightToLeft(0.5f);
                        mono.setRightToRight(0.5f);
                        filters.add(mono);
                        break;
                    case "clear":
                    case "none":
                        break;
                    default:
                        logger.warn("Unknown filter requested: {}", activeFilter);
                        break;
                }
            } catch (Exception e) {
                logger.error("Failed to build filter factory for {}", activeFilter, e);
                // On error, return no filters to prevent crash
                return Collections.emptyList();
            }
            return filters;
        });

        // Lavaplayer buffers 5 seconds of audio in advance.
        // By seeking to the exact current position, we force the player to flush 
        // the buffer and immediately re-decode with the new filters applied!
        if (player.getPlayingTrack() != null) {
            player.getPlayingTrack().setPosition(player.getPlayingTrack().getPosition());
        }
    }

    public void clearFilters() {
        try {
            applyFilter("none");
        } catch (Exception e) {
            logger.error("Failed to clear filters", e);
        }
    }
}
