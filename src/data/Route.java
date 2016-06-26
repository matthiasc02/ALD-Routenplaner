package data;

public class Route {

	private int fromTownId;
	private int toTownId;
	private String streetName;
	private int distance;
	
	public Route(int fromTownId, int toTownId, String streetName, int distance) {
		this.fromTownId = fromTownId;
		this.toTownId = toTownId;
		this.streetName = streetName;
		this.distance = distance;
	}

	public int getFromTownId() {
		return fromTownId;
	}

	public void setFromTownId(int fromTownId) {
		this.fromTownId = fromTownId;
	}

	public int getToTownId() {
		return toTownId;
	}

	public void setToTownId(int toTownId) {
		this.toTownId = toTownId;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
}