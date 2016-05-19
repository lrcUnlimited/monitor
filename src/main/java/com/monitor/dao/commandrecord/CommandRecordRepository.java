package com.monitor.dao.commandrecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.monitor.model.CommandRecord;

@Transactional
@Repository
public interface CommandRecordRepository extends
		JpaRepository<CommandRecord, Integer> {

}
