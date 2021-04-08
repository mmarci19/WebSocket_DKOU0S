package service;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

public class Seat {
	
	private SeatStatus status;
	
	private int row;

	private int column;
	
	public Seat(int r, int c) {
		row=r;column=c;
		this.status = SeatStatus.FREE;
	}
	
	public Seat() {
		this.status=SeatStatus.FREE;
	}
	
	public SeatStatus getStatus() {
		return status;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	
	public void setRow(int r) {
		this.row=r;
	}
	
	public void setColumn(int c) {
		this.column=c;
	}
	
	public void setStatus(SeatStatus s) {
		this.status=s;
	}
	
	public void changeStatus(SeatStatus newStatus) {
		this.status = newStatus;
	}
}
