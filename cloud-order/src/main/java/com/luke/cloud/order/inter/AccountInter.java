package com.luke.cloud.order.inter;

import com.luke.cloud.order.request.DecreaseAccountReq;
import com.luke.milo.core.annotation.MiloTCC;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Descrtption AccountInter
 * @Author luke
 * @Date 2019/9/25
 **/
@FeignClient(value = "cloud-account",fallback = AccountInterHystrix.class)
public interface AccountInter {

    @GetMapping("/account/findAccountByName")
    String findAccountByName(@RequestParam("username") String username);

    @MiloTCC
    @PostMapping("/account/decrease")
    String decreaseAccount(@RequestBody @Validated DecreaseAccountReq req);

}
