package com.monitor.dao.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.monitor.model.Account;

/**
 * @Description: 账户实体数据库相关操作
 * @author malin
 * @date 2014-8-14
 * @time 下午11:35:26
 */
@Transactional
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	/**
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	@Query("select account from Account account where account.userName=?1 and account.passWord=?2")
	public Account loginAccount(String userName, String passWord);

}
