package service;

import java.io.StringReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class DataReader {

	public ArrayList<String> Read(String jsonString) {
		
		SeatStatusBuilder stb = new SeatStatusBuilder();
		
		JsonReader reader = Json.createReader(new StringReader(jsonString));
		JsonObject jsonObject = reader.readObject();
		Seat seat = new Seat();
		String messageType = jsonObject.getString("type");

		ArrayList<String> temp_list = new ArrayList<String>();
		
		if(messageType.equalsIgnoreCase("seatStatus")) {
			temp_list.add(jsonObject.getString("row"));
			temp_list.add(jsonObject.getString("column"));
		}
		
		if(messageType.equalsIgnoreCase("initRoom")) {
			temp_list.add((jsonObject.getString("row")));
			temp_list.add((jsonObject.getString("column")));
		}
		
		if(messageType.equalsIgnoreCase("getRoomSize")) {
			//temp_list = null;
		}
		
		if(messageType.equalsIgnoreCase("updateSeats")) {
			//temp_list = null;
		}
		
		if(messageType.equalsIgnoreCase("lockSeat")) {
			temp_list.add(String.valueOf(jsonObject.getInt("row")));
			temp_list.add(String.valueOf(jsonObject.getInt("column")));
		}
		
		if(messageType.equalsIgnoreCase("unlockSeat")) {
			temp_list.add(String.valueOf(jsonObject.getInt("lockId")));
		}
		
		if(messageType.equalsIgnoreCase("reserveSeat")) {
			temp_list.add(String.valueOf(jsonObject.getInt("lockId")));
		}
		
		temp_list.add(messageType);
		return temp_list;
	}
	
	
}
