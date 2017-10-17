/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.segmentstore.server.containers;

import io.pravega.common.util.ConfigBuilder;
import io.pravega.common.util.ConfigurationException;
import io.pravega.common.util.Property;
import io.pravega.common.util.TypedProperties;
import java.time.Duration;

import lombok.Getter;

/**
 * Segment Container Configuration.
 */
public class ContainerConfig {
    //region Members

    public static final int MINIMUM_SEGMENT_METADATA_EXPIRATION_SECONDS = 60; // Minimum possible value for segmentExpiration
    public static final Property<Integer> SEGMENT_METADATA_EXPIRATION_SECONDS = Property.named("segmentMetadataExpirationSeconds",
            5 * MINIMUM_SEGMENT_METADATA_EXPIRATION_SECONDS);
    public static final Property<Integer> MAX_ACTIVE_SEGMENT_COUNT = Property.named("maxActiveSegmentCount", 10000);
    public static final Property<Integer> MAX_CONCURRENT_SEGMENT_EVICTION_COUNT = Property.named("maxConcurrentSegmentEvictionCount", 250);
    public static final long MINIMUM_MAX_WATERMARK_LAG_SECONDS = 1L; // Minimum possible value for maxWatermarkLagSeconds
    public static final Property<Long> MAX_WATERMARK_LAG_SECONDS = Property.named("maxWatermarkLagSeconds", 10L);
    public static final int MINIMUM_WATERMARK_POLLING_PERIOD_SECONDS = 1; // Minimum possible value for watermarkPollingPeriod
    public static final Property<Integer> WATERMARK_POLLING_PERIOD_SECONDS = Property.named("watermarkPollingPeriod", 1);
    private static final String COMPONENT_CODE = "containers";

    /**
     * The amount of time after which Segments are eligible for eviction from the metadata.
     */
    @Getter
    private Duration segmentMetadataExpiration;

    /**
     * The maximum number of segments that can be active at any given time in a container.
     */
    @Getter
    private int maxActiveSegmentCount;

    /**
     * The maximum number of segments to evict at once.
     */
    @Getter
    private int maxConcurrentSegmentEvictionCount;

    /**
     * The maximum lag between the most recent watermark and the current time.
     */
    @Getter
    private Duration maxWatermarkLag;

    /**
     * The period between successive attempts to advance the watermark on active segments.
     */
    @Getter
    private Duration watermarkPollingPeriod;

    //endregion

    //region Constructor

    /**
     * Creates a new instance of the ContainerConfig class.
     *
     * @param properties The java.util.Properties object to read Properties from.
     */
    ContainerConfig(TypedProperties properties) {
        int segmentMetadataExpirationSeconds = properties.getInt(SEGMENT_METADATA_EXPIRATION_SECONDS);
        if (segmentMetadataExpirationSeconds < MINIMUM_SEGMENT_METADATA_EXPIRATION_SECONDS) {
            throw new ConfigurationException(String.format("Property '%s' must be at least %s.",
                    SEGMENT_METADATA_EXPIRATION_SECONDS, MINIMUM_SEGMENT_METADATA_EXPIRATION_SECONDS));
        }
        this.segmentMetadataExpiration = Duration.ofSeconds(segmentMetadataExpirationSeconds);

        this.maxActiveSegmentCount = properties.getInt(MAX_ACTIVE_SEGMENT_COUNT);
        if (this.maxActiveSegmentCount <= 0) {
            throw new ConfigurationException(String.format("Property '%s' must be a positive integer.", MAX_ACTIVE_SEGMENT_COUNT));
        }

        this.maxConcurrentSegmentEvictionCount = properties.getInt(MAX_CONCURRENT_SEGMENT_EVICTION_COUNT);
        if (this.maxConcurrentSegmentEvictionCount <= 0) {
            throw new ConfigurationException(String.format("Property '%s' must be a positive integer.", MAX_CONCURRENT_SEGMENT_EVICTION_COUNT));
        }

        long maxWatermarkLagSeconds = properties.getLong(MAX_WATERMARK_LAG_SECONDS);
        if (maxWatermarkLagSeconds < MINIMUM_MAX_WATERMARK_LAG_SECONDS) {
            throw new ConfigurationException(String.format("Property '%s' must be at least %s.",
                    MAX_WATERMARK_LAG_SECONDS, MINIMUM_MAX_WATERMARK_LAG_SECONDS));
        }
        this.maxWatermarkLag = Duration.ofSeconds(maxWatermarkLagSeconds);

        int watermarkPollingPeriodSeconds = properties.getInt(WATERMARK_POLLING_PERIOD_SECONDS);
        if (watermarkPollingPeriodSeconds < MINIMUM_WATERMARK_POLLING_PERIOD_SECONDS) {
            throw new ConfigurationException(String.format("Property '%s' must be at least %s.",
                    WATERMARK_POLLING_PERIOD_SECONDS, MINIMUM_WATERMARK_POLLING_PERIOD_SECONDS));
        }
        this.watermarkPollingPeriod = Duration.ofSeconds(watermarkPollingPeriodSeconds);
    }

    /**
     * Creates a new ConfigBuilder that can be used to create instances of this class.
     *
     * @return A new Builder for this class.
     */
    public static ConfigBuilder<ContainerConfig> builder() {
        return new ConfigBuilder<>(COMPONENT_CODE, ContainerConfig::new);
    }

    //endregion
}
