/*
 * Copyright (c) 2020. Yuriy Stul
 */

package com.stulsoft.pvertx;

import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.kafka.client.producer.KafkaHeader;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.TimestampType;

import java.util.List;

/**
 * @author Yuriy Stul
 */
public class TestKafkaConsumerRecord<K, V> implements KafkaConsumerRecord<K, V> {
    private final K key;
    private final V value;

    public TestKafkaConsumerRecord(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String topic() {
        return null;
    }

    @Override
    public int partition() {
        return 0;
    }

    @Override
    public long offset() {
        return 0;
    }

    @Override
    public long timestamp() {
        return 0;
    }

    @Override
    public TimestampType timestampType() {
        return null;
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Override
    public long checksum() {
        return 0;
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public List<KafkaHeader> headers() {
        return null;
    }

    @Override
    public ConsumerRecord<K, V> record() {
        return new ConsumerRecord<>(topic(), partition(), offset(), key(), value());
    }
}
