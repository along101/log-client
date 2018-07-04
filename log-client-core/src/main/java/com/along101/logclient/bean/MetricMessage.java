package com.along101.logclient.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinzuolong on 2017/6/1.
 */
public class MetricMessage {
    private String name;
    private Map<String, String> tags;
    private Object value;
    private long timeStamp;
    public MetricMessage() {
    }

    public static class Builder {
        private String name;
        private Map<String, String> tags;
        private Object value;
        private long timeStamp;
        private Builder() {
        }
        public static Builder newInstance() {
            return new Builder();
        }
        public MetricMessage build() {
            return new MetricMessage(name, tags, timeStamp, value);
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setTags(Map<String, String> tags) {
            this.tags = tags;
            return this;
        }
        public Builder setValue(Object value) {
            this.value = value;
            return this;
        }
        public Builder setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }
    }

    public MetricMessage(String name, Map<String, String> tags, long timeStamp, Object value) {
        this.name = name;
        this.tags = tags;
        this.timeStamp = timeStamp;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public void addTag(String key, String value) {
        if (tags == null) {
            tags = new HashMap<>();
        }
        this.tags.put(key, value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object values) {
        this.value = values;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name:").append(name).append("\n")
                .append("tag:").append(tags.toString()).append("\n")
                .append("value:").append(value).append("\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{name, timeStamp, value, tags});
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MetricMessage)) {
            return false;
        }
        final MetricMessage rhs = (MetricMessage) o;

        return equals(name, rhs.name)
                && equals(timeStamp, rhs.timeStamp)
                && equals(value, rhs.value)
                && equals(tags, rhs.tags);
    }

    private boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

}
