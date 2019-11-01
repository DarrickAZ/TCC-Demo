package com.luke.milo.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.luke.milo.common.enums.MiloPhaseEnum;
import com.luke.milo.common.enums.MiloRoleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Descrtption MiloTransactionBean
 * @Author luke
 * @Date 2019/9/19
 **/
@NoArgsConstructor
@Data
public class MiloTransactionBean implements Serializable{

    private static final long serialVersionUID = 8987639621684470468L;

    /**
     * 事务id
     */
    private String transId;

    /**
     * 阶段 {@linkplain MiloPhaseEnum}
     */
    private Integer phase;

    /**
     * 角色 {@linkplain MiloRoleEnum}
     */
    private Integer role;

    /**
     * 失败重试次数
     */
    private Integer retryTimes;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 目标类
     */
    private String targetClass;

    /**
     * 目标方法
     */
    private String targetMethod;

    /**
     * confirm方法
     */
    private String confirmMethod;

    /**
     * cancel方法
     */
    private String cancelMethod;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * updateTime
     */
    private Date updateTime;

    private List<MiloParticipantBean> participantBeanList;


    public MiloTransactionBean(String transId){
        this.transId = transId;
        participantBeanList = new ArrayList<>();
    }

    /**
     * 添加参与者
     * @param miloParticipantBean {@linkplain MiloParticipantBean}
     */
    public void addParticipant(final MiloParticipantBean miloParticipantBean){
        if(participantBeanList == null){
            participantBeanList = new ArrayList<>();
        }
        participantBeanList.add(miloParticipantBean);
    }

}
