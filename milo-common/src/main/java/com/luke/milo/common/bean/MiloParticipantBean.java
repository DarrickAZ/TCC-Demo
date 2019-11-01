package com.luke.milo.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Descrtption MiloParticipantBean
 * @Author luke
 * @Date 2019/9/19
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MiloParticipantBean implements Serializable {

    private static final long serialVersionUID = -7385657240991913166L;

    private String transId;

    private MiloInvocationBean confirmInvocation;

    private MiloInvocationBean cancelInvocation;

}
