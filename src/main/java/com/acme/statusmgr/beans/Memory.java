package com.acme.statusmgr.beans;
import com.acme.servermgr.ServerManager;

public class Memory extends ServerStatusDecorator{

    public Memory(ServerStatusInterface status){
        super(status);
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
        return super.getStatusDesc() + ServerManager.getMemoryStatus();
    }

}
