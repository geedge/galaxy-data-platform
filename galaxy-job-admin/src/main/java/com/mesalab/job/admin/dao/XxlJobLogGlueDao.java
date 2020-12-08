package com.mesalab.job.admin.dao;

import com.mesalab.job.admin.core.model.XxlJobLogGlue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * job log for glue
 */
@Mapper
public interface XxlJobLogGlueDao {

    int save(XxlJobLogGlue xxlJobLogGlue);

    List<XxlJobLogGlue> findByJobId(@Param("jobId") int jobId);

    int removeOld(@Param("jobId") int jobId, @Param("limit") int limit);

    int deleteByJobId(@Param("jobId") int jobId);

}
