package com.ab.model;

public class AbPoint {
	
	public double x;
	public double y;
	
	public AbPoint() {
		super();
	}

	public AbPoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public boolean equals(Object o) {
		AbPoint point = (AbPoint)o;
		if(this.x == point.x && this.y == point.y){
			return true;   
		}
        return false;   
	}

	@Override
	public int hashCode() {
		return (int)(x*y)^8;
	}
	
	
}
