#sql("appDicList")
  select app_id,app_name from app_id_dict
#end

#sql("reportResults")
  select * from report_result where job_id = ? and status !=2 order by op_time desc
#end

#sql("storageLog")
 select * from sys_storage_log limit 10
#end