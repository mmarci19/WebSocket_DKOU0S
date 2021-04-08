package service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/cinema")


public class CinemaService {

	private static SeatList seats;
	private static ArrayList<Session> my_sessions = new ArrayList<Session>();

	public CinemaService() {
	

	}
	
@OnMessage
public String messageReceived(String message) {
	
	DataReader reader = new DataReader();
	ArrayList<String> result = reader.Read(message);
	String messageType = result.get(result.size()-1);

	
	if(messageType.equalsIgnoreCase("initRoom")) {
		return initRoom(message);
	}
	
	if(messageType.equalsIgnoreCase("getRoomSize")) {
		return getRoomSize(message);
	}
	
	if(messageType.equalsIgnoreCase("updateSeats")) {
		
		String returnString = updateSeats(message);
		
		return returnString;
	}
	
	if(messageType.equalsIgnoreCase("lockSeat")) {
		
		String returnString = lockSeat(message);
		
		return returnString;
	}
	
	if(messageType.equalsIgnoreCase("unlockSeat")) {
		
		String returnString = unlockSeat(message);
		return returnString;
	}
	
	if(messageType.equalsIgnoreCase("reserveSeat")) {
		String returnString = ReserveSeat(message);
		return returnString;

	}
	
	return "";
}
	


public String initRoom(String message) {
	seats= new SeatList();

	DataReader reader = new DataReader();
	ArrayList<String> result = reader.Read(message);

	int rows = Integer.parseInt(result.get(0));

	int columns = Integer.parseInt(result.get(1));
	
	if(rows>=1 && columns >=1) {
	this.seats.Init(rows,columns);
	}
	
	
	JsonObjectBuilder objectBuilder = null;
    String jsonString = "";
    
    objectBuilder = Json.createObjectBuilder()
			.add("-", "-");
	JsonObject jsonObject = objectBuilder.build();
	try(Writer writer = new StringWriter()){
		Json.createWriter(writer).write(jsonObject);
		jsonString = writer.toString();
	}
	catch(IOException ie) {
		
	}
	if(rows==0) {
		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "Row is zero.");
		 jsonObject = objectBuilder.build();
		
		try(Writer writer = new StringWriter()){
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
		}
		catch(IOException ie) {
			
		}
	}
	if(columns==0) {
		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "Column is zero.");
		 jsonObject = objectBuilder.build();
		
		try(Writer writer = new StringWriter()){
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
		}
		catch(IOException ie) {
			
		}
	}
	if(rows<0) {
		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "Row is negative.");
		 jsonObject = objectBuilder.build();
		
		try(Writer writer = new StringWriter()){
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
		}
		catch(IOException ie) {
			
		}
	}
	if(columns<0) {
		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "Column is negative.");
		jsonObject = objectBuilder.build();
		
		try(Writer writer = new StringWriter()){
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
		}
		catch(IOException ie) {
			
		}
	}


	return jsonString;
}



public String getRoomSize(String message) {
	
	DataReader reader = new DataReader();
	ArrayList<String> result = reader.Read(message);
	String messageType = result.get(0);
	JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
			.add("type", "roomSize")
			.add("rows", this.seats.rows)
			.add("columns", this.seats.columns);
	
	String jsonString="";
	JsonObject jsonObject = objectBuilder.build();
	
	try(Writer writer = new StringWriter()){
		Json.createWriter(writer).write(jsonObject);
		jsonString = writer.toString();
	}
	catch(IOException ie) {
		
	}
	
	return jsonString;
	
}

public String to_array_string(ArrayList<SeatStatus> array) {
	return "";
}



public String updateSeats(String message) {
	DataReader reader = new DataReader();
	ArrayList<String> result = reader.Read(message);
	String messageType = result.get(0);
	String jsonString = "";
	
	for(int i=0;i<this.seats.rows;i++) {
		
		
		for(int j=0;j<this.seats.columns;j++) {
			
			SeatStatus status = null;
			status = this.seats.returnSeatByRowAndColumn(i+1, j+1).getStatus();

			
			JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
					.add("type", "seatStatus")
					.add("row", i+1)
					.add("column", j+1)
					.add("status", to_string(status).toLowerCase());
			
			jsonString="";
			JsonObject jsonObject = objectBuilder.build();
			
			try(Writer writer = new StringWriter()){
				Json.createWriter(writer).write(jsonObject);
				jsonString = writer.toString();
			}
			catch(IOException ie) {
				
			}
			try {
				for (int a=0; a<my_sessions.size();a++) {
				my_sessions.get(a).getBasicRemote().sendText(jsonString);
				}
			}
			catch(IOException ie) {
				
			}
		}
		
	}
	
	return "";
}


	
public String getSeatStatus(String message) {
	DataReader reader = new DataReader();
	ArrayList<String> result = reader.Read(message);
	int row = Integer.parseInt(result.get(0));
	int column = Integer.parseInt(result.get(1));
	SeatStatus status = this.seats.returnSeatByRowAndColumn(row, column).getStatus();
	JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
			.add("type", "seatStatus")
			.add("row", row)
			.add("column", column)
			.add("status", to_string(status).toLowerCase());
	
	String jsonString="";
	JsonObject jsonObject = objectBuilder.build();
	
	try(Writer writer = new StringWriter()){
		Json.createWriter(writer).write(jsonObject);
		jsonString = writer.toString();
	}
	catch(IOException ie) {
		
	}
	
	return jsonString;
}


public String lockSeat(String message) {
	DataReader reader = new DataReader();
	ArrayList<String> result = reader.Read(message);
	int row = Integer.parseInt(result.get(0));
	int column = Integer.parseInt(result.get(1));
	String messageType = result.get(result.size()-1);
	boolean err = false;
	JsonObjectBuilder objectBuilder = null;
	if(this.seats == null) {
		
		
		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "Not initialized.");
		
		String jsonString="";
		JsonObject jsonObject = objectBuilder.build();
		
		try(Writer writer = new StringWriter()){
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
		}
		catch(IOException ie) {
			
		}

		
		return jsonString;
		
		
	}
	
	if(row>this.seats.getSize().get(0)) {
		err = true;
	}
	
	if(column>this.seats.getSize().get(1)) {
		err = true;
	}
	LockId id = null;
	
	
	
	if(this.seats.returnSeatByRowAndColumn(row, column).getStatus() == SeatStatus.FREE) {
		id = this.seats.lockSeat(row, column);
		objectBuilder = Json.createObjectBuilder()
				.add("type", "lockResult")
				.add("id", id.getId())
				.add("status", to_string(this.seats.getSeatByLockId(id.getId())));
	}
	else {
		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "Seat is not free.");
		
	}
	
	if(err==true) {
		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "Invalid seat.");
	}
	
	
	String jsonString="";
	JsonObject jsonObject = objectBuilder.build();
	
	try(Writer writer = new StringWriter()){
		Json.createWriter(writer).write(jsonObject);
		jsonString = writer.toString();
	}
	catch(IOException ie) {
		
	}

	
	return jsonString;
	
}



public String unlockSeat(String message) {
	
	DataReader reader = new DataReader();
	ArrayList<String> result = reader.Read(message);
	int lockId = Integer.parseInt(result.get(0));
	int unlock_result = this.seats.UnlockSeat(lockId);
	
	JsonObjectBuilder objectBuilder = null;
	if(this.seats == null) {
		
		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "Not initialized.");
		
		String jsonString="";
		JsonObject jsonObject = objectBuilder.build();
		
		try(Writer writer = new StringWriter()){
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
		}
		catch(IOException ie) {
			
		}

		
		return jsonString;
	}
	
	objectBuilder = Json.createObjectBuilder()
			.add("type", "unlockSeat")
			.add("lockId", lockId)
			.add("status", to_string(this.seats.getSeatByLockId(lockId)));
	
	if(unlock_result==-1) {
		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "No lock with this id.");
	}
	String jsonString="";
	JsonObject jsonObject = objectBuilder.build();
	
	try(Writer writer = new StringWriter()){
		Json.createWriter(writer).write(jsonObject);
		jsonString = writer.toString();
	}
	catch(IOException ie) {
		
	}
	
	return jsonString;
	//return this.seats.getSeatByLockId(lockId);
	
	
	
}



public String ReserveSeat(String message) {
	DataReader reader = new DataReader();
	ArrayList<String> result = reader.Read(message);
	int lockId = Integer.parseInt(result.get(0));
	JsonObjectBuilder objectBuilder = null;
	
	

	if(this.seats == null) {
		
		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "Not initialized.");
		
		String jsonString="";
		JsonObject jsonObject = objectBuilder.build();
		
		try(Writer writer = new StringWriter()){
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
		}
		catch(IOException ie) {
			
		}

		
		return jsonString;
	}
	
	
	
	if(this.seats.getSeatByLockId(lockId) == SeatStatus.RESERVED) {

		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "Cannot unlock a reserved seat.");
	}
	if(this.seats.getSeatByLockId(lockId)== SeatStatus.LOCKED) {
		int reserve_result = this.seats.ReserveSeat(lockId);
		
		objectBuilder = Json.createObjectBuilder()
				.add("type", "reserveSeat")
				.add("lockId", lockId)
				.add("status", to_string(this.seats.getSeatByLockId(lockId)));
	}
	if(this.seats.getSeatByLockId(lockId)== SeatStatus.FREE) {

		objectBuilder = Json.createObjectBuilder()
				.add("type", "error")
				.add("message", "No lock with this id.");
	}
	
	
	String jsonString="";
	JsonObject jsonObject = objectBuilder.build();
	
	try(Writer writer = new StringWriter()){
		Json.createWriter(writer).write(jsonObject);
		jsonString = writer.toString();
	}
	catch(IOException ie) {
		
	}
	return jsonString;
}

public String to_string(SeatStatus s) {
	String return_string="";
	if(s==SeatStatus.FREE) {
		return_string="FREE";
	}
	if(s==SeatStatus.LOCKED) {
		return_string="LOCKED";
	}
	if(s==SeatStatus.RESERVED) {
		return_string="RESERVED";
	}
	return return_string;
}


@OnOpen
public void open(Session session) {
	my_sessions.add(session);
}
@OnClose
public void close(Session session) {
	my_sessions.remove(session);
}


}
