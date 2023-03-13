package cn.itcast.account.service.impl;

import cn.itcast.account.entity.AccountFreeze;
import cn.itcast.account.mapper.AccountFreezeMapper;
import cn.itcast.account.mapper.AccountMapper;
import cn.itcast.account.service.AccountTCCService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountTCCServiceImpl implements AccountTCCService {

  @Autowired
  private AccountMapper accountMapper;

  @Autowired
  private AccountFreezeMapper freezeMapper;

  @Override
  @Transactional
  public void deduct(String userId, int money) {
    // 0. 获取事务id
    String xid = RootContext.getXID();
    // 1. 判断freeze中是否有冻结记录，如果有，一定是CANCEL执行过，我要拒绝业务
    AccountFreeze oldFreeze = freezeMapper.selectById(xid);
    if (oldFreeze != null) {
      // CANCEL执行过了，我要拒绝业务
      return;
    }
    // 1. 扣减可用余额
    accountMapper.deduct(userId, money);
    // 2. 记录冻结金额，事务状态
    AccountFreeze freeze = new AccountFreeze();
    freeze.setUserId(userId);
    freeze.setFreezeMoney(money);
    freeze.setState(AccountFreeze.State.TRY);
    freeze.setXid(xid);
    freezeMapper.insert(freeze);
  }

  @Override
  public boolean confirm(BusinessActionContext context) {
    // 1. 获取事务id
    String xid = context.getXid();
    // 2. 根据id冻结记录
    int count = freezeMapper.deleteById(xid);
    return count == 1;
  }

  @Override
  public boolean cancel(BusinessActionContext context) {
    // 0. 查询冻结记录
    String xid = context.getXid();
    String userId = (String) context.getActionContext("userId");
    AccountFreeze freeze = freezeMapper.selectById(xid);
    // 1. 空回滚的判断，判断freeze是否为null，为null证明try没有执行，需要空回滚
    if (freeze == null) {
      // 证明try没有执行，需要空回滚
      freeze = new AccountFreeze();
      freeze.setUserId(userId);
      freeze.setFreezeMoney(0);
      freeze.setState(AccountFreeze.State.CANCEL);
      freeze.setXid(xid);
      freezeMapper.insert(freeze);
      return true;
    }
    // 2. 幂等判断
    if (freeze.getState() == AccountFreeze.State.CANCEL) {
      // 已经处理过一次CANCEL了，无须重复处理
      return true;
    }
    // 1. 恢复可用余额
    accountMapper.refund(freeze.getUserId(), freeze.getFreezeMoney());

    // 2. 将冻结金额清零，状态改为CANCEL
    freeze.setFreezeMoney(0);
    freeze.setState(AccountFreeze.State.CANCEL);
    int count = freezeMapper.updateById(freeze);
    return count == 1;
  }
}
