package common;

import java.io.Serializable;

public enum Instruction implements Serializable {
    EXIT,
    SCRIPT,
    ASK_COMMAND,
    ASK_TICKET,
    ASK_COORDINATES,
    ASK_VENUE,
    ASK_ADDRESS,
    ASK_LOCATION,
    USER_NOT_VALIDATED
}