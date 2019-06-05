package it.uniroma2.dicii.sdcc.didatticamobile.error;

/*Error response represent the error message associated to a specific response code from service*/
public class ErrorResponse {

    private String error;

    public ErrorResponse() {

    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
