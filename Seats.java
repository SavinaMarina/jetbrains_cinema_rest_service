package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Seats {
    private final int totalRows;
    private final int totalColumns;
    private final List<Seat> seatList;
    private List<Ticket> tickets;
    private int currentIncome = 0;
    private static final String ERR_INVALID_SEAT_PARAMS = "The number of a row or a column is out of bounds!";
    private static final String ERR_TICKED_ALREADY_PURCHASED = "The ticket has been already purchased!";
    private static final String ERR_WRONG_TOKEN = "Wrong token!";

    public Seats(int totalRows, int totalColumns) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        seatList = new ArrayList<>(totalRows * totalColumns);
        for (int i = 1; i <= totalRows; i++)
            for (int j = 1; j <= totalColumns; j++) {
                seatList.add(new Seat(i, j, getPrice(i)));
            }
        tickets = new ArrayList<>(totalRows * totalColumns);
    }

    private int getPrice(int row) {
        return row <= 4 ? 10 : 8;
    }

    public Ticket purchase(int row, int column) throws IllegalArgumentException {
        if ((row < 1) || (row > totalRows) || (column < 1) || (column > totalColumns))
            throw new IllegalArgumentException(ERR_INVALID_SEAT_PARAMS);

        for (Seat s : seatList) {
            if ((s.getRow() == row) && (s.getColumn() == column) && (s.isAvailable())) {
                s.setAvailable(false);
                Ticket ticket = new Ticket(s);
                tickets.add(ticket);
                currentIncome += s.getPrice();
                return ticket;
            }
        }

        throw new IllegalArgumentException(ERR_TICKED_ALREADY_PURCHASED);
    }

    public Seat returnTicket(UUID token) {
        for (Ticket t : tickets) {
            if (t.getToken().equals(token)) {
                Seat s = t.getSeat();
                s.setAvailable(true);
                tickets.remove(t);
                currentIncome -= s.getPrice();
                return s;
            }
        }
        throw new IllegalArgumentException(ERR_WRONG_TOKEN);
    }

    @JsonProperty("total_rows")
    public int getTotalRows() {
        return totalRows;
    }

    @JsonProperty("total_columns")
    public int getTotalColumns() {
        return totalColumns;
    }

    @JsonProperty("available_seats")
    public List<Seat> getAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat s : seatList) {
            if (s.isAvailable()) {
                availableSeats.add(s);
            }
        }
        return availableSeats;
    }

    public Map stat() {
        return Map.of("current_income", currentIncome,
                "number_of_available_seats", getAvailableSeats().size(),
                "number_of_purchased_tickets",tickets.size());
    }
}
