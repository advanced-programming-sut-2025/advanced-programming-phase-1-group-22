package io.github.some_example_name.model;

import lombok.Getter;
import io.github.some_example_name.model.exception.InvalidInputException;

@Getter
public enum Direction {
	NORTH("north",0,1),
	SOUTH("south",0,-1),
	WEST("west",-1,0),
	EAST("east",1,0),
	NORTHWEST("northwest",-1,1),
	NORTHEAST("northeast",1,1),
	SOUTHWEST("southwest",-1,-1),
	SOUTHEAST("southeast",1,-1),
	CENTRE("centre",0,0);
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
		return null;
	}

    public static Direction getByXAndY(int dx,int dy){
        for (Direction value : Direction.values()) {
            if (value.getXTransmit() == dx && value.getYTransmit() == dy) return value;
        }
        return null;
    }

    public Direction reverse() {
        return Direction.getByXAndY(-xTransmit, -yTransmit);
    }
}
