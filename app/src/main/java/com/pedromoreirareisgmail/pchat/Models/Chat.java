package com.pedromoreirareisgmail.pchat.Models;

public class Chat {

    private boolean lida;
    private long time;

    public Chat() {
    }

    public Chat(boolean lida, long time) {

        this.lida = lida;
        this.time = time;
    }

    public boolean isLida() {
        return lida;
    }

    public long getTime() {
        return time;
    }
}