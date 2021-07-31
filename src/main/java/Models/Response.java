package Models;

public class Response {
    ResponseType responseType;
    String data;
    public Response(ResponseType responseType, String data) {
        this.responseType = responseType;
        this.data = data;
    }
}
