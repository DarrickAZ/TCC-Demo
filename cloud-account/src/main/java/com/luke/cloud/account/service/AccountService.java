package com.luke.cloud.account.service;

import com.luke.cloud.account.request.DecreaseAccountReq;

public interface AccountService {

    Integer decrease(DecreaseAccountReq req);

    String findAccountByName(String username);

}
