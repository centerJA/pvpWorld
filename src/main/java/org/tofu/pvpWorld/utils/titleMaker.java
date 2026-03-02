package org.tofu.pvpWorld.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.time.Duration;

public class titleMaker {
    public static Title title(Component main, Component sub, int fadeIn, int show, int fadeOut) {
        Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeIn),
                Duration.ofSeconds(show),
                Duration.ofMillis(fadeOut)
        );

        return Title.title(main, sub, times);
    }
}
