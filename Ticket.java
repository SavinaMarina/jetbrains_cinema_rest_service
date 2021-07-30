package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Ticket {
    private UUID token;
    private Seat seat;

    public Ticket() {
    }

    public Ticket(Seat seat) {
        this.token = randomUUID();
        this.seat = seat;
    }

    @JsonProperty("ticket")
    public Seat getSeat() {
        return seat;
    }

    @JsonProperty("token")
    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
