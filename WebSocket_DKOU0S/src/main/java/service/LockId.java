package service;

public class LockId {

	private int row;
	private int column;
	private int lock_id;
	
	public LockId(int rId) {
		lock_id=rId;
	}
	
	public LockId(int r,int c,int ref_id) {
		this.row=r;
		this.column=c;
		lock_id=ref_id+1;
	}
	
	public int getId() {
		return lock_id;
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
}

