package com.example.demo;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java.util.stream.Collectors.toList;

import static com.example.demo.SampleSourceConnector.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

public class SampleSourceTask extends SourceTask{
    private Logger logger = getLogger(SourceTask.class);
    private String topic;
    private Long interval;
    private String url;
    private List<String> markets;
    private Long last_execution = 0L;
    private Map<String, Object> offsets = new HashMap<>();

    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> props) {
        topic = props.get(HTTP_TOPIC);
        interval = Long.valueOf(props.get(HTTP_INTERVAL));
        url = props.get(HTTP_URL);

        logger.info("Starting to fetch {} each {} ms", url, interval);
    }

    @Override
    public List<SourceRecord> poll() {
        if (System.currentTimeMillis() > (last_execution + interval)) {
        last_execution = System.currentTimeMillis();
        List<SourceRecord> records = new ArrayList<>();
        logger.info("Pooling url: {}",url);
        records.add(new SourceRecord(
            Collections.singletonMap("url",url),
            Collections.singletonMap("offset",0),
            topic, Schema.BYTES_SCHEMA,
            getUrlContents(url)
        ));
        return records;
        }
        return null;
  }

    @Override
    public void stop() {

    }

    private static byte[] getUrlContents(String sourceUrl) {
        StringBuilder content = new StringBuilder();
        try
        {
          URL httpURL = new URL(sourceUrl);
          BufferedReader in = new BufferedReader(
              new InputStreamReader(httpURL.openStream()));
    
          String inputLine;
          while ((inputLine = in.readLine()) != null)
            content.append(inputLine);
          in.close();
        }
        catch(Exception e) {
          e.printStackTrace();
        }
        return content.toString().getBytes();
    }

    private Map<String, Object> asMap(String key){
        Map<String,Object> map = new HashMap<>();
        map.put(key, null);
        return map;
    }
}