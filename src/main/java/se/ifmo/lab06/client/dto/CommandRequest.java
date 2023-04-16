package se.ifmo.lab06.client.dto;


public final class CommandRequest extends Request {
    private final String name;
    private final Object[] data;

    public CommandRequest(String name, Object[] data) {
        this.name = name;
        this.data = data;
    }

    public CommandRequest(String name) {
        this(name, new Object[]{});
    }

    public Object[] getData() {
        return data;
    }

    public String getName() {
        return name;
    }
}
