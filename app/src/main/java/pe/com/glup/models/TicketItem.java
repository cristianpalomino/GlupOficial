package pe.com.glup.models;

import java.util.ArrayList;

/**
 * Created by Glup on 28/09/15.
 */
public class TicketItem {
    private String numTicket;
    private String total;
    private ArrayList<ReservaItem> reserva;

    public TicketItem(String numTicket, String total, ArrayList<ReservaItem> reserva) {
        this.numTicket = numTicket;
        this.total = total;
        this.reserva = reserva;
    }

    public String getNumTicket() {
        return numTicket;
    }

    public String getTotal() {
        return total;
    }

    public ArrayList<ReservaItem> getReserva() {
        return reserva;
    }
}
