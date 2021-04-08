package service;

public class SeatStatusBuilder {

	
	public SeatStatus Build(String string) {
		if(string == "FREE") {
			return SeatStatus.FREE;
		}
		if(string == "LOCKED") {
			return SeatStatus.LOCKED;
		}
		if(string == "RESERVED") {
			return SeatStatus.RESERVED;
		}
		return SeatStatus.FREE;
	}
}
