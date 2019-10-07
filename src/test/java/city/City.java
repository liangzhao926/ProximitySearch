package city;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"datasetid",
"recordid",
"fields",
"geometry",
"record_timestamp"
})
public class City {
	@JsonProperty("datasetid")
	private String datasetid;
	@JsonProperty("recordid")
	private String recordid;
	@JsonProperty("fields")
	private Fields fields;
	@JsonProperty("geometry")
	private Geometry geometry;
	@JsonProperty("record_timestamp")
	private String recordTimestamp;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("datasetid")
	public String getDatasetid() {
	return datasetid;
	}

	@JsonProperty("datasetid")
	public void setDatasetid(String datasetid) {
	this.datasetid = datasetid;
	}

	@JsonProperty("recordid")
	public String getRecordid() {
	return recordid;
	}

	@JsonProperty("recordid")
	public void setRecordid(String recordid) {
	this.recordid = recordid;
	}

	@JsonProperty("fields")
	public Fields getFields() {
	return fields;
	}

	@JsonProperty("fields")
	public void setFields(Fields fields) {
	this.fields = fields;
	}

	@JsonProperty("geometry")
	public Geometry getGeometry() {
	return geometry;
	}

	@JsonProperty("geometry")
	public void setGeometry(Geometry geometry) {
	this.geometry = geometry;
	}

	@JsonProperty("record_timestamp")
	public String getRecordTimestamp() {
	return recordTimestamp;
	}

	@JsonProperty("record_timestamp")
	public void setRecordTimestamp(String recordTimestamp) {
	this.recordTimestamp = recordTimestamp;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}

}
