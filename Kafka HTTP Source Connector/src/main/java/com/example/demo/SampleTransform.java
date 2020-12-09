package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.transforms.InsertField;
import org.apache.kafka.connect.transforms.Transformation;
import org.apache.kafka.connect.transforms.util.SimpleConfig;

import static org.apache.kafka.connect.transforms.util.Requirements.requireMap;
import java.sql.Timestamp;

import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

public abstract class SampleTransform <R extends ConnectRecord<R>> implements Transformation<R>{

    private Logger logger = getLogger(SampleTransform.class);
	public static final String OVERVIEW_DOC = "Insert Timestamp into a connect record";
	public static String FIELD_NAME = "timeStamp.field";
    

	public static ConfigDef CONFIG_DEF = new ConfigDef()
			.define(FIELD_NAME, ConfigDef.Type.STRING, "timeStamp", ConfigDef.Importance.HIGH,
				      "Field name for timeStamp");
	private static final String PURPOSE = "adding TimeStamp to record";
	
	private String fieldName;
	
	@Override
	public void configure(Map<String, ?> configs) {
		
		final SimpleConfig config = new SimpleConfig(CONFIG_DEF, configs);
		fieldName = config.getString(FIELD_NAME);
		
	}

	
	
	@Override
	public R apply(R record) {
		final Map<String, Object> value = requireMap(operatingValue(record), PURPOSE);
		return newRecord(record, null,  value);
	}
	
	private Object getTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp;
	}

	protected abstract Object operatingValue(R record);
	
	protected abstract R newRecord(R record, Schema updatedSchema, Object updatedValue);
	
	public static class Key<R extends ConnectRecord<R>> extends InsertField<R> {

		@Override
		protected Schema operatingSchema(R record) {
			return record.keySchema();
		}

		@Override
		protected Object operatingValue(R record) {
			return record.key();
		}

		@Override
		protected R newRecord(R record, Schema updatedSchema, Object updatedValue) {
			return record.newRecord(record.topic(), record.kafkaPartition(), updatedSchema, updatedValue, record.valueSchema(), record.value(), record.timestamp());
		}
		
	}
	
	 public static class Value<R extends ConnectRecord<R>> extends InsertField<R> {

		    @Override
		    protected Schema operatingSchema(R record) {
		      return record.valueSchema();
		    }

		    @Override
		    protected Object operatingValue(R record) {
		      return record.value();
		    }

		    @Override
		    protected R newRecord(R record, Schema updatedSchema, Object updatedValue) {
		      return record.newRecord(record.topic(), record.kafkaPartition(), record.keySchema(), record.key(), updatedSchema, updatedValue, record.timestamp());
		    }
	 }
	
	@Override
	public ConfigDef config() {
		return CONFIG_DEF;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
}
