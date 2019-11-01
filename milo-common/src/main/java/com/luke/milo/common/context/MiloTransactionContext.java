package com.luke.milo.common.context;

import lombok.Data;
import java.io.Serializable;
import com.luke.milo.common.enums.MiloPhaseEnum;
import com.luke.milo.common.enums.MiloRoleEnum;

/**
 * @Descrtption TCC事务上下文
 * @Author luke
 * @Date 2019/9/18
 **/
@Data
public class MiloTransactionContext implements Serializable {

    private static final long serialVersionUID = 5712013353357711388L;

    /**
     * 事务id
     */
    private String transId;

    /**
     * 事务执行阶段 {@linkplain MiloPhaseEnum}
     */
    private int phase;

    /**
     * 事务参与的角色 {@linkplain MiloRoleEnum}
     */
    private int role;

}
