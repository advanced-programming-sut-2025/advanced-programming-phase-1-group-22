package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Marry {
    private Integer id;
    private Player husband;
    private Player wife;
    private TimeAndDate request;
    private Boolean accepted;
}
