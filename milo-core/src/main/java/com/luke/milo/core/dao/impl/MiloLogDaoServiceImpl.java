package com.luke.milo.core.dao.impl;

import com.google.common.collect.Maps;
import com.luke.milo.common.bean.MiloParticipantBean;
import com.luke.milo.common.bean.MiloTransactionBean;
import com.luke.milo.common.config.MiloConfig;
import com.luke.milo.common.config.MiloDbConfig;
import com.luke.milo.common.exception.MiloException;
import com.luke.milo.common.serializer.KryoSerializer;
import com.luke.milo.common.serializer.ObjectSerializer;
import com.luke.milo.core.dao.MiloLogDaoService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @Descrtption MiloDaoServiceImpl
 * @Author luke
 * @Date 2019/9/24
 **/
@Repository
@Slf4j
public class MiloLogDaoServiceImpl implements MiloLogDaoService {

    /**
     * 数据源
     */
    private DataSource dataSource;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 系列化器
     */
    private ObjectSerializer serializer;

    @Override
    public void init(MiloConfig miloConfig) {
        //创建数据库连接池
        HikariDataSource hikariDataSource = new HikariDataSource();
        MiloDbConfig miloDbConfig = miloConfig.getMiloDbConfig();
        hikariDataSource.setJdbcUrl(miloDbConfig.getUrl());
        hikariDataSource.setDriverClassName(miloDbConfig.getDriverClassName());
        hikariDataSource.setUsername(miloDbConfig.getUsername());
        hikariDataSource.setPassword(miloDbConfig.getPassword());
        hikariDataSource.setMinimumIdle(miloDbConfig.getMinimumIdle());
        hikariDataSource.setMaximumPoolSize(miloDbConfig.getMaximumPoolSize());
        hikariDataSource.setIdleTimeout(miloDbConfig.getIdleTimeout());
        hikariDataSource.setConnectionTimeout(miloDbConfig.getConnectionTimeout());
        hikariDataSource.setConnectionTestQuery(miloDbConfig.getConnectionTestQuery());
        hikariDataSource.setMaxLifetime(miloDbConfig.getMaxLifetime());
        dataSource = hikariDataSource;
        tableName = "milo_"+miloConfig.getModelName()+"_log";
        //创建表结构
        createTable(tableName);
        //系列化器
        serializer = new KryoSerializer();
    }

    @Override
    public int save(MiloTransactionBean miloLog) {
        String sql = "insert into " + tableName + "(trans_id,phase,role,retried_times,version,target_class,target_method,"
                + "confirm_method,cancel_method,create_time,update_time,invocation)"
                + " values(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            final byte[] serialize = serializer.serialize(miloLog.getParticipantBeanList());
            return executeUpdate(sql, miloLog.getTransId(), miloLog.getPhase(),miloLog.getRole(),miloLog.getRetryTimes(),miloLog.getVersion(),
                    miloLog.getTargetClass(), miloLog.getTargetMethod(),miloLog.getConfirmMethod(),miloLog.getCancelMethod(),
                    miloLog.getCreateTime(),miloLog.getUpdateTime(),serialize);
        } catch (MiloException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updatePhase(String transId, Integer phase) {
        String sql = "update " + tableName + " set phase = ? , update_time = ?  where trans_id = ?  ";
        return executeUpdate(sql, phase,new Date(),transId);
    }

    @Override
    public int updateParticipantList(MiloTransactionBean miloLog) {
        try {
            final byte[] serialize = serializer.serialize(miloLog.getParticipantBeanList());
            String sql = "update " + tableName + " set invocation = ? , update_time = ? where trans_id = ? ";
            return executeUpdate(sql, serialize, new Date(),miloLog.getTransId());
        } catch (MiloException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 删除事务日志（READY阶段的事务日志不能删除，通过定时任务去删除）
     * @param transId
     * @return
     */
    @Override
    public int remove(String transId) {
        String sql = "delete from " + tableName + " where trans_id = ? and phase != 0 ";
        return executeUpdate(sql,transId);
    }

    @Override
    public MiloTransactionBean query(String transId) {
        String selectSql = "select * from " + tableName + " where trans_id = ? ";
        List<Map<String, Object>> list = executeQuery(selectSql, transId);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .map(this::buildByResultMap)
                    .findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public List<MiloTransactionBean> queryAllByDelay(Date delayTime) {
        String selectSql = "select * from " + tableName + " where update_time < ? ";
        List<Map<String, Object>> list = executeQuery(selectSql, delayTime);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().filter(Objects::nonNull)
                    .map(this::buildByResultMap)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public int updateTransactionRetryTimes(String transId, Integer version, Integer retryTimes) {
        try {
            int currentVersion = version + 1;
            String sql = "update " + tableName + " set retried_times = ? , version = ? ,update_time = ? where trans_id = ? and version = ? ";
            return executeUpdate(sql, retryTimes,currentVersion, new Date(),transId,version);
        } catch (MiloException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void createTable(String tableName) {
        String buildTableSQL = buildMysql(tableName);
        executeUpdate(buildTableSQL);
    }

    private String buildMysql(final String tableName) {
        return "CREATE TABLE IF NOT EXISTS `" +
                tableName +
                "` (" +
                "  `trans_id` varchar(64) NOT NULL," +
                "  `phase` tinyint NOT NULL," +
                "  `role` tinyint NOT NULL," +
                "  `retried_times` tinyint NOT NULL," +
                "  `version` tinyint NOT NULL," +
                "  `target_class` varchar(256) ," +
                "  `target_method` varchar(128) ," +
                "  `confirm_method` varchar(128) ," +
                "  `cancel_method` varchar(128) ," +
                "  `invocation` longblob," +
                "  `create_time` datetime NOT NULL," +
                "  `update_time` datetime NOT NULL," +
                "  PRIMARY KEY (`trans_id`))";
    }

    private MiloTransactionBean buildByResultMap(final Map<String, Object> map) {
        MiloTransactionBean miloTransaction = new MiloTransactionBean();
        miloTransaction.setTransId((String)map.get("trans_id"));
        miloTransaction.setPhase((Integer)map.get("phase"));
        miloTransaction.setRole((Integer)map.get("role"));
        miloTransaction.setRetryTimes((Integer)map.get("retried_times"));
        miloTransaction.setVersion((Integer)map.get("version"));
        miloTransaction.setCreateTime((Date)map.get("create_time"));
        miloTransaction.setUpdateTime((Date)map.get("update_time"));
        byte[] bytes = (byte[]) map.get("invocation");
        try {
            final List<MiloParticipantBean> miloParticipantBeans = serializer.deSerialize(bytes, CopyOnWriteArrayList.class);
            miloTransaction.setParticipantBeanList(miloParticipantBeans);
        } catch (MiloException e) {
            e.printStackTrace();
        }
        return miloTransaction;
    }

    private int executeUpdate(final String sql, final Object... params) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1,params[i]);
                }
            }
            int influence = ps.executeUpdate();
            connection.commit();
            return influence;
        } catch (SQLException e) {
            log.error("create milo log table error: {}",e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
        } finally {
            close(connection, ps, null);
        }

    }

    private List<Map<String, Object>> executeQuery(final String sql, final Object... params) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = null;
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            list = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> rowData = Maps.newHashMap();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            log.error("query milo log table error: {}",e);
        } finally {
            close(connection, ps, rs);
        }
        return list;
    }


    private void close(final Connection con, final PreparedStatement ps, final ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}
