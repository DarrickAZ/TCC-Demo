package com.luke.cloud.account.controller;

import com.luke.cloud.account.request.DecreaseAccountReq;
import com.luke.cloud.account.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Descrtption AccountController
 * @Author luke
 * @Date 2019/9/25
 **/
@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/decrease")
    public String decreaseAccount(@RequestBody @Validated DecreaseAccountReq req){
        log.info("decreaseAccount,req:{}",req);
        Integer decreaseResult = accountService.decrease(req);
        if(decreaseResult == null || decreaseResult == 0){
            return "failure";
        }
        return "success";
    }

    @GetMapping("/findAccountByName")
    public String findAccountByName(@RequestParam("username") String username){
        return accountService.findAccountByName(username);
    }

}
