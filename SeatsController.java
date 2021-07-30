package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class SeatsController {
    private static final int TOTAL_ROWS = 9;
    private static final int TOTAL_COLUMNS = 9;
    private static final String ERR_UNAUTHORIZED = "The password is wrong!";
    private static final String SUPER_SECRET_VALUE = "super_secret";
    private final Seats seats;

    public SeatsController() {
        seats = new Seats(TOTAL_ROWS, TOTAL_COLUMNS);
    }

    @GetMapping("/seats")
    public Seats getSeats() {
        return seats;
    }

    @PostMapping("/purchase")
    @ResponseBody
    public ResponseEntity<Object> purchase(@RequestBody Seat seat) {
        try {
            Ticket t = seats.purchase(seat.getRow(), seat.getColumn());
            return new ResponseEntity<>(t, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/return")
    @ResponseBody
    public ResponseEntity<Object> returnTicket(@RequestBody Ticket ticket) {
        try {
            Seat t = seats.returnTicket(ticket.getToken());
            return new ResponseEntity<>(Map.of("returned_ticket", t), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/stats")
    public ResponseEntity<Object> statistics(@RequestParam(required = false) String password) {
        if ((password != null) && password.equals(SUPER_SECRET_VALUE)) {
            return new ResponseEntity<>(seats.stat(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(Map.of("error", ERR_UNAUTHORIZED),
                    HttpStatus.UNAUTHORIZED);
        }
    }

}



