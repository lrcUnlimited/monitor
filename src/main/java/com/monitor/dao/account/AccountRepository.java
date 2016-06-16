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
 * 
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
	@Query("select account from Account account where account.userName=?1 and account.passWord=?2 and account.isDelete=0")
	public Account loginAccount(String userName, String passWord);

	@Query("select userName from Account account where account.id=?1")
	public String queryUserNameById(Integer accounId);

	/**
	 * 通过用户名找出用户，用于用户名唯一性
	 * 
	 * @param userName
	 * @return
	 */
	@Query("select account from Account account where account.userName=?1")
	public Account queryAccountbyuserName(String userName);

	/**
	 * 更新用户信息
	 * 
	 * @param userName
	 * @param userPhone
	 * @param note
	 * @param accountId
	 */
	@Modifying
	@Query("update Account account set account.userName=?1,account.userPhone=?2,account.note=?3,account.passWord=?4,account.type=?5 where account.id=?6 ")
	public void updateUserInfo(String userName, String userPhone, String note,
			String passWord, int type, int accountId);

	/**
	 * 更新用户信息,不进行密码的更新
	 * 
	 * @param userName
	 * @param userPhone
	 * @param note
	 * @param accountId
	 */
	@Modifying
	@Query("update Account account set account.userName=?1,account.userPhone=?2,account.note=?3,account.type=?4 where account.id=?5 ")
	public void updateUserInfoNoPassWord(String userName, String userPhone,
			String note, int type, int accountId);

	/**
	 * 逻辑删除用户
	 * 
	 * @param accountId
	 */
	@Modifying
	@Query("update Account account set account.isDelete=1 where account.id=?1")
	public void deleteAccount(int accountId);

}
