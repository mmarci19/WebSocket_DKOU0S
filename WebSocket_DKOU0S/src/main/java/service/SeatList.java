package service;

import java.util.ArrayList;


public class SeatList {
	
	public static int rows;
	public static int columns;
	private static ArrayList<LockId> locks;
	private static ArrayList<Seat> seats;
	
	public void Init(int row,int column) {
		
		
		this.rows=row;
		this.columns=column;
		
		seats=new ArrayList<Seat>();
		locks = new ArrayList<LockId>();
		
		for (int rw = 1; rw <= row; rw ++) {
			for (int cl = 1; cl <= column; cl ++) {
				
				this.seats.add(new Seat(rw,cl));
			}
		}
		
	}
	
	public SeatList() {
		
		
	}
	
	public SeatStatus getSeatByLockId(int lockId) {
		
		int t_row=0;
		int t_col=0;
		
		for (int i=0;i<locks.size();i++) {
			if(locks.get(i).getId() == lockId) {
				t_row = locks.get(i).getRow();
				t_col = locks.get(i).getColumn();
			}
		}
		SeatStatus t_status = SeatStatus.FREE;
		
		for (int i=0;i<seats.size();i++) {
			if(this.seats.get(i).getRow() == t_row && this.seats.get(i).getColumn() == t_col) {
				t_status = this.seats.get(i).getStatus();
			}
		}
		return t_status;
		
	}
	
	
	
	public ArrayList<Integer> getSize() {
		ArrayList<Integer> templist = new ArrayList<Integer>();
		templist.add(rows);
		templist.add(columns);
		return templist;
	}
	
	public ArrayList<SeatStatus> getStatuses(){
		
		ArrayList<SeatStatus> tempList = new ArrayList<SeatStatus>();
		
		for (int i=0; i<this.seats.size();i++) {
			
			tempList.add(seats.get(i).getStatus());
		}
		
		return tempList;
	}
	
	
	public Seat returnSeatByRowAndColumn(int r,int c) {
		
		for (int i=0; i<this.seats.size();i++) {
			
			if(this.seats.get(i).getRow() == r && this.seats.get(i).getColumn() == c) {
				
				return this.seats.get(i);
			}
			else {
				//TODO
			}
		}
		return null;
		
	}
	
	public LockId lockSeat(int row, int column) {
		
		if(this.returnSeatByRowAndColumn(row, column).getStatus()!=SeatStatus.FREE) {
			LockId err_id = new LockId(-2);
			
			return err_id;
		}
		
		else {
			
		
		this.returnSeatByRowAndColumn(row,column).changeStatus(SeatStatus.LOCKED);
		int ref_id = 0;
		
		if (this.locks.size()!=0) {
			ref_id = this.locks.get(this.locks.size()-1).getId();
		}
		
		LockId lock_id_counter = new LockId(row,column,ref_id);
		this.locks.add(lock_id_counter);
		return lock_id_counter;
		}

		
	}
	
	public int UnlockSeat(int id) {
		int row_temp=-1;
		int col_temp=-1;
		boolean exists_lock = false;
		
		for(int i=0; i<this.locks.size();i++) {
			if(this.locks.get(i).getId() == id) {
				row_temp = this.locks.get(i).getRow();
				col_temp = this.locks.get(i).getColumn();
				exists_lock=true;
			}
		}
		
		if(exists_lock==false) {
			return -1;
		}
		else {
			for(int i=0;i<this.seats.size();i++) {
				if(this.seats.get(i).getRow() == row_temp &&this.seats.get(i).getColumn() == col_temp) {
					this.seats.get(i).changeStatus(SeatStatus.FREE);
				}
			}
			return 1;
		}
		
	
	}
	
	
	public int ReserveSeat(int id) {
		
		int row_temp=-1;
		boolean exists_lock=false;
		
		int col_temp=-1;
		for(int i=0; i<this.locks.size();i++) {
			if(this.locks.get(i).getId() == id) {
				row_temp = this.locks.get(i).getRow();
				col_temp = this.locks.get(i).getColumn();
				exists_lock = true;
			}
		}
		if(exists_lock==false) {
			return -1;
		}
		else
		{
			for(int i=0;i<this.seats.size();i++) {
				if(this.seats.get(i).getRow() == row_temp &&this.seats.get(i).getColumn() == col_temp) {
					this.seats.get(i).changeStatus(SeatStatus.RESERVED);
				}
			}
		return 1;
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}

