package mx.x10.filipebezerra.horariosrmtcgoiania.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table FAVORITE_BUS_STOP.
 */
public class FavoriteBusStop {

    private Long id;
    private int stopCode;
    /** Not-null value. */
    private String address;
    private String stopReference;

    public FavoriteBusStop() {
    }

    public FavoriteBusStop(Long id) {
        this.id = id;
    }

    public FavoriteBusStop(Long id, int stopCode, String address, String stopReference) {
        this.id = id;
        this.stopCode = stopCode;
        this.address = address;
        this.stopReference = stopReference;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStopCode() {
        return stopCode;
    }

    public void setStopCode(int stopCode) {
        this.stopCode = stopCode;
    }

    /** Not-null value. */
    public String getAddress() {
        return address;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAddress(String address) {
        this.address = address;
    }

    public String getStopReference() {
        return stopReference;
    }

    public void setStopReference(String stopReference) {
        this.stopReference = stopReference;
    }

}
