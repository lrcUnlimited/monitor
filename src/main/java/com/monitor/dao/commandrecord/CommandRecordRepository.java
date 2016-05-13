package com.monitor.dao.commandrecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.monitor.model.Account;
import com.monitor.model.CommandRecord;

public interface CommandRecordRepository extends JpaRepository<CommandRecord,Integer>{
	
}
