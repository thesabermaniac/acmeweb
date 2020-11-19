package com.acme.statusmgr.beans;
import com.acme.servermgr.ServerManager;

public class Operations extends ServerStatusDecorator{

    public Operations(ServerStatusInterface s){
        super(s);
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public String getContentHeader() {
        return super.getContentHeader();
    }

    @Override
    public String getStatusDesc(){
        return super.getStatusDesc() + ServerManager.getOperationsStatus();
    }

}
