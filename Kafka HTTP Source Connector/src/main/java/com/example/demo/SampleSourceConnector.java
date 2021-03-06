package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Width;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.apache.kafka.connect.util.ConnectorUtils;


import com.example.demo.SampleSourceTask;

public class SampleSourceConnector extends SourceConnector{
    public static final String HTTP_URL = "http.url";
    public static final String HTTP_INTERVAL = "http.interval";
    public static final String HTTP_TOPIC = "http.topic";
    public static final String HTTP_USERS = "http.users";

    private static ConfigDef CONFIG_DEF = new ConfigDef()
        .define(HTTP_URL, Type.STRING, null, Importance.HIGH,
            "Url to publish", null, 0, Width.LONG, "URL to publish",
            Collections.EMPTY_LIST)
        .define(HTTP_INTERVAL, Type.LONG, Importance.HIGH, "Frequency in ms")
        .define(HTTP_USERS, Type.STRING, Importance.HIGH, "Comma Separated users")
        .define(HTTP_TOPIC, Type.STRING, Importance.HIGH, "Topic to publish");
    
    private Map<String, String> configProperties;

    @Override
    public String version() {
        return "kafka-submit";
    }
    
    @Override
    public void start(Map<String, String> props) {
        configProperties = props;
    }
    
    @Override
    public Class<? extends Task> taskClass() {
        return SampleSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        final List<Map<String,String>> configs = new ArrayList<>();
        List<String> users = Arrays.asList(configProperties.get(HTTP_USERS).split(","));
        int groups = Math.min(users.size(), maxTasks);

        List<List<String>> usersGroup = ConnectorUtils.groupPartitions(users, groups);
        for (int i = 0; i < maxTasks; i++) {
            Map<String, String> taskProps = new HashMap<>(configProperties);
            taskProps.put(HTTP_USERS, String.join(",", usersGroup.get(i)));
            configs.add(taskProps);
          }
        return configs;
    }

    @Override
    public Config validate(Map<String, String> connectorConfigs) {
      return super.validate(connectorConfigs);
    }
  
    @Override
    public void stop() {
  
    }
  
    @Override
    public ConfigDef config() {
      return CONFIG_DEF;
    }
}