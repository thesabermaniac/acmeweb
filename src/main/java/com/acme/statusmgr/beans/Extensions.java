package com.acme.statusmgr.beans;
import com.acme.servermgr.ServerManager;

public class Extensions extends ServerStatusDecorator{

    public Extensions(ServerStatusInterface status){
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
        return super.getStatusDesc() +  ServerManager.getExtensionsStatus();
    }

}
