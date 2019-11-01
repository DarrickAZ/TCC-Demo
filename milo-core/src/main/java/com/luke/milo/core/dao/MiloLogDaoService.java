package com.luke.milo.core.dao;

import com.luke.milo.common.bean.MiloTransactionBean;
import com.luke.milo.common.config.MiloConfig;

import java.util.Date;
import java.util.List;

public interface MiloLogDaoService {

    void init(MiloConfig miloConfig);

    int save(MiloTransactionBean miloLog);

    int updatePhase(String transId,Integer phase);

    int updateParticipantList(MiloTransactionBean miloLog);

    int remove(String transId);

    MiloTransactionBean query(String transId);

    List<MiloTransactionBean> queryAllByDelay(Date delayTime);

    int updateTransactionRetryTimes(String transId, Integer version, Integer retryTimes);
}
