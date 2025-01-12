/**
 * Projct: Banking System - UI
 * This class contains the utility methods needed for input transaction management. 
 * Author: Arockiam Joseph
 * Created Date:12-01-2025
 * Version: 1.0
 */
package com.util.input;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.apache.bcel.classfile.Constant;

import com.common.Constants;

public class InputTransUtil {
	private static final Logger logger = LogManager.getLogger(InputTransUtil.class);

	/**
	 * This method will generate the next transaction id.
	 * @param curTxnId
	 * @return
	 */
	public static String getNextTxnId(String curTxnId) {
		String txnId = null;
		if(Objects.isNull(curTxnId))
			return txnId;
		
		String[] curSeqNo = curTxnId.split("-");
		
		int sqNo = Integer.parseInt(curSeqNo[1]);
		if (sqNo<Constants.MAX_TXN_SEQ_NO) {
			sqNo++;
			if (sqNo<10)
				txnId = curSeqNo[0]+"-0"+sqNo;
			else
				txnId = curSeqNo[0]+"-"+sqNo;
		}
		logger.info("The next generated txn id is:"+txnId);
		return txnId;
	}
}
