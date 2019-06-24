/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.pvertx.clustered.sender;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.JoinConfig;

/**
 * Hazelcast cluster configuration
 *
 * @author Yuriy Stul
 */
public class ClusterConfiguratorHelper {
    private ClusterConfiguratorHelper() {
    }

    public static Config getHazelcastConfiguration() {
        final Config config = new Config();
        final GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName("dev").setPassword("dev-pass");

        final JoinConfig joinConfig = config.getNetworkConfig().getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getTcpIpConfig().setEnabled(true).addMember("127.0.0.1");

        final InterfacesConfig interfaceConfig = config.getNetworkConfig().getInterfaces();
        interfaceConfig.setEnabled(true).addInterface("127.0.0.1");

        return config;
    }
}
