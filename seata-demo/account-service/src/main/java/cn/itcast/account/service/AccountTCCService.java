package cn.itcast.account.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

// TCC
@LocalTCC
public interface AccountTCCService {

  // 指定Try逻辑对应的方法
  @TwoPhaseBusinessAction(name = "deduct", commitMethod = "confirm", rollbackMethod = "cancel")
  void deduct(@BusinessActionContextParameter(paramName = "userId") String userId,
              @BusinessActionContextParameter(paramName = "money") int money);

  // confirm 确认方法
  boolean confirm(BusinessActionContext context);

  // 回滚方法
  boolean cancel(BusinessActionContext context);
}
