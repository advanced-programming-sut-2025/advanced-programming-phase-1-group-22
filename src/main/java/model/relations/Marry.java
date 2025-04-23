package model.relations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.TimeAndDate;

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
