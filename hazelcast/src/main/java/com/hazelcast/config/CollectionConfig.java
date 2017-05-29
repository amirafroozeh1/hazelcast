/*
 * Copyright (c) 2008-2017, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.config;

import java.util.ArrayList;
import java.util.List;

import static com.hazelcast.util.Preconditions.checkAsyncBackupCount;
import static com.hazelcast.util.Preconditions.checkBackupCount;

/**
 * Provides configuration service for Collection.
 *
 * @param <T> Type of Collection such as List, Set
 */
public abstract class CollectionConfig<T extends CollectionConfig> {

    /**
     * Default maximum size for the Configuration.
     */
    public static final int DEFAULT_MAX_SIZE = 0;
    /**
     * The default number of synchronous backups
     */
    public static final int DEFAULT_SYNC_BACKUP_COUNT = 1;
    /**
     * The default number of asynchronous backups
     */
    public static final int DEFAULT_ASYNC_BACKUP_COUNT = 0;

    private String name;
    private List<ItemListenerConfig> listenerConfigs;
    private int backupCount = DEFAULT_SYNC_BACKUP_COUNT;
    private int asyncBackupCount = DEFAULT_ASYNC_BACKUP_COUNT;
    private int maxSize = DEFAULT_MAX_SIZE;
    private boolean statisticsEnabled = true;

    protected CollectionConfig() {
    }

    protected CollectionConfig(CollectionConfig config) {
        this.name = config.name;
        this.listenerConfigs = new ArrayList<ItemListenerConfig>(config.getItemListenerConfigs());
        this.backupCount = config.backupCount;
        this.asyncBackupCount = config.asyncBackupCount;
        this.maxSize = config.maxSize;
        this.statisticsEnabled = config.statisticsEnabled;
    }


    public abstract T getAsReadOnly();

    /**
     * Gets the name of this collection.
     *
     * @return the name of this collection
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this collection.
     *
     * @param name the name of this collection
     * @return the updated collection configuration
     */
    public T setName(String name) {
        this.name = name;
        return (T) this;
    }

    /**
     * Gets the list of ItemListenerConfigs.
     *
     * @return the list of ItemListenerConfigs
     */
    public List<ItemListenerConfig> getItemListenerConfigs() {
        if (listenerConfigs == null) {
            listenerConfigs = new ArrayList<ItemListenerConfig>();
        }
        return listenerConfigs;
    }

    /**
     * Sets the list of ItemListenerConfigs.
     *
     * @param listenerConfigs the list of ItemListenerConfigs to set
     * @return this collection configuration
     */
    public T setItemListenerConfigs(List<ItemListenerConfig> listenerConfigs) {
        this.listenerConfigs = listenerConfigs;
        return (T) this;
    }

    /**
     * Gets the total number of synchronous and asynchronous backups for this collection.
     *
     * @return the total number of synchronous and asynchronous backups for this collection
     */
    public int getTotalBackupCount() {
        return backupCount + asyncBackupCount;
    }

    /**
     * Gets the number of synchronous backups for this collection.
     *
     * @return the number of synchronous backups for this collection
     */
    public int getBackupCount() {
        return backupCount;
    }

    /**
     * Sets the number of synchronous backups for this collection.
     *
     * @param backupCount the number of synchronous backups to set for this collection
     * @return the current CollectionConfig
     * @throws IllegalArgumentException if backupCount smaller than 0,
     *                                  or larger than the maximum number of backup
     *                                  or the sum of the backups and async backups is larger than the maximum number of backups
     * @see #setAsyncBackupCount(int)
     */
    public T setBackupCount(int backupCount) {
        this.backupCount = checkBackupCount(backupCount, asyncBackupCount);
        return (T) this;
    }

    /**
     * Gets the number of asynchronous backups.
     *
     * @return the number of asynchronous backups
     */
    public int getAsyncBackupCount() {
        return asyncBackupCount;
    }

    /**
     * Sets the number of asynchronous backups.
     *
     * @param asyncBackupCount the number of asynchronous synchronous backups to set
     * @return the updated CollectionConfig
     * @throws IllegalArgumentException if asyncBackupCount is smaller than 0,
     *                                  or larger than the maximum number of backups,
     *                                  or the sum of the backups and async backups is larger than the maximum number of backups.
     * @see #setBackupCount(int)
     * @see #getAsyncBackupCount()
     */
    public T setAsyncBackupCount(int asyncBackupCount) {
        this.asyncBackupCount = checkAsyncBackupCount(asyncBackupCount, asyncBackupCount);
        return (T) this;
    }

    /**
     * Gets the maximum size for the Configuration.
     *
     * @return the maximum size for the Configuration
     */
    public int getMaxSize() {
        return maxSize == 0 ? Integer.MAX_VALUE : maxSize;
    }

    /**
     * Sets the maximum size for the collection.
     *
     * @return the current CollectionConfig
     */
    public T setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return (T) this;
    }

    /**
     * Checks if collection statistics are enabled.
     *
     * @return {@code true} if collection statistics are enabled, {@code false} otherwise
     */
    public boolean isStatisticsEnabled() {
        return statisticsEnabled;
    }

    /**
     * Sets collection statistics to enabled or disabled.
     *
     * @param statisticsEnabled {@code true} to enable collection statistics, {@code false} to disable
     * @return the current collection config instance
     */
    public T setStatisticsEnabled(boolean statisticsEnabled) {
        this.statisticsEnabled = statisticsEnabled;
        return (T) this;
    }

    /**
     * Adds an item listener to this collection (listens for when items are added or removed).
     *
     * @param itemListenerConfig the item listener to add to this collection
     */
    public void addItemListenerConfig(ItemListenerConfig itemListenerConfig) {
        getItemListenerConfigs().add(itemListenerConfig);
    }
}
