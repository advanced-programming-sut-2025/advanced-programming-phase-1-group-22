package model;

import lombok.Getter;
import model.exception.InvalidInputException;

@Getter
public enum Direction {
	NORTH("north",0,1),
	SOUTH("south",0,-1),
	WEST("west",-1,0),
	EAST("east",1,0),
	NORTHWEST("northwest",-1,1),
	NORTHEAST("northeast",1,1),
	SOUTHWEST("southwest",-1,-1),
	SOUTHEAST("southeast",1,-1);
	private final String name;
	private final Integer xTransmit;
	private final Integer yTransmit;

	Direction(String name, Integer xTransmit, Integer yTransmit) {
		this.name = name;
		this.xTransmit = xTransmit;
		this.yTransmit = yTransmit;
	}

	public static Direction getByName(String name){
		for (Direction value : Direction.values()) {
			if (value.name.equals(name)){
				return value;
			}
		}
		throw new InvalidInputException("direction is invalid");
	}
}