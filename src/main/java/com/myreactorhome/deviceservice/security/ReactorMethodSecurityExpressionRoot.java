package com.myreactorhome.deviceservice.security;

import com.myreactorhome.deviceservice.exceptions.ModelNotFound;
import com.myreactorhome.deviceservice.models.Account;
import com.myreactorhome.deviceservice.models.Hub;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public class ReactorMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;

    HubRepository hubRepository;

    public ReactorMethodSecurityExpressionRoot(Authentication authentication, HubRepository hubRepository) {
        super(authentication);
        this.hubRepository = hubRepository;
    }

    public boolean isGroupMember(String hubId){
        Account account = (Account)this.getPrincipal();
        Optional<Hub> hubOptional = hubRepository.findById(hubId);
        Hub hub = hubOptional.orElseThrow(() -> new ModelNotFound("hub"));

        return account.getGroupsList().contains(hub.getGroupId()) || account.getOwnerGroupId().equals(hub.getGroupId());
        //return account.getGroupsList().contains(groupId) || account.getOwnerGroupId().equals(groupId);
    }

    @Override
    public void setFilterObject(Object o) {
        this.filterObject = o;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object o) {
        this.returnObject = o;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
