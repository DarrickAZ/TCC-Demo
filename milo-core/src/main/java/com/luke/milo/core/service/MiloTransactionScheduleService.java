package com.luke.milo.core.service;

import com.luke.milo.common.bean.MiloTransactionBean;

public interface MiloTransactionScheduleService {

    void confirmPhase(MiloTransactionBean transactionBean);

    void cancelPhase(MiloTransactionBean transactionBean);

}
