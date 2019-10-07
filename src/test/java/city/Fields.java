package city;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"city",
"coordinates",
"state",
"rank",
"growth_from_2000_to_2013",
"population"
})
public class Fields {
	@JsonProperty("city")
	private String city;
	@JsonProperty("coordinates")
	private List<Double> coordinates = null;
	@JsonProperty("state")
	private String state;
	@JsonProperty("rank")
	private Integer rank;
	@JsonProperty("growth_from_2000_to_2013")
	private Double growthFrom2000To2013;
	@JsonProperty("population")
	private Integer population;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	@JsonProperty("city")
	public String getCity() {
	return city;
	}

	@JsonProperty("city")
	public void setCity(String city) {
	this.city = city;
	}

	@JsonProperty("coordinates")
	public List<Double> getCoordinates() {
	return coordinates;
	}

	@JsonProperty("coordinates")
	public void setCoordinates(List<Double> coordinates) {
	this.coordinates = coordinates;
	}

	@JsonProperty("state")
	public String getState() {
	return state;
	}

	@JsonProperty("state")
	public void setState(String state) {
	this.state = state;
	}

	@JsonProperty("rank")
	public Integer getRank() {
	return rank;
	}

	@JsonProperty("rank")
	public void setRank(Integer rank) {
	this.rank = rank;
	}

	@JsonProperty("growth_from_2000_to_2013")
	public Double getGrowthFrom2000To2013() {
	return growthFrom2000To2013;
	}

	@JsonProperty("growth_from_2000_to_2013")
	public void setGrowthFrom2000To2013(Double growthFrom2000To2013) {
	this.growthFrom2000To2013 = growthFrom2000To2013;
	}

	@JsonProperty("population")
	public Integer getPopulation() {
	return population;
	}

	@JsonProperty("population")
	public void setPopulation(Integer population) {
	this.population = population;
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
