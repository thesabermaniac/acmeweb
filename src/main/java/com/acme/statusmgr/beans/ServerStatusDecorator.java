package com.acme.statusmgr.beans;

public abstract class ServerStatusDecorator implements ServerStatusInterface{
    private final ServerStatusInterface status;

    public ServerStatusDecorator(ServerStatusInterface status){
        this.status = status;
    }

    @Override
    public long getId() {
        return status.getId();
    }

    @Override
    public String getContentHeader() {
        return status.getContentHeader();
    }

    @Override
    public String getStatusDesc() {
        return status.getStatusDesc();
    }
}
