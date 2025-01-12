/**
 * Projct: Banking System - UI
 * This is the main class file which will process the user input. 
 * Author: Arockiam Joseph
 * Created Date:12-01-2025
 * Version: 1.0
 */
package com;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.common.CommonUtil;
import com.common.Constants;
import com.util.input.VaidateInputTrans;
import com.model.AccountTransaction;
import com.model.Rule;
import com.util.rule.ValidateInputRules;
import com.service.InputTransactionService;
import com.service.PrintStatementService;
import com.service.RulesService;
import com.util.stmt.ValidatePrintStmt;

import lombok.val;

import java.util.List;
import java.util.Scanner;

@Component
public class BankingSystemCommandlineApp implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger(BankingSystemCommandlineApp.class);

	private final Scanner scanner = new Scanner(System.in);
	
	@Autowired
	private InputTransactionService inputTranService;
	
	@Autowired
	private RulesService rulesService;
	
	@Autowired
	private PrintStatementService printStmtService;
	
	@Value("${it.welcome.message}")
	private String tOptionMessage;
	
	@Value("${it.invalid.param.message1}")
	private String tInvalidParameter;
		
	@Value("${it.invalid.param.message2}")
	private String tInvalidParamInput;
	
	@Value("${it.exceed.tran.message}")
	private String tExceedTran;
		
	@Value("${it.invalid.tran.type.message}")
	private String tInvalidTranType;
		
	
	@Value("${it.amount.exceed.message}")
	private String tWithdrawAmtExceed;
	
	@Value("${it.tran.success.message}")
	private String tSuccess;
		
	@Value("${console.welcome.message}")
	private String consoleWelcomeMsg;
	
	@Value("${console.option.1}")
	private String tOption;
			
	@Value("${console.option.2}")
	private String iOption;
		
	@Value("${console.option.3}")
	private String pOption;
	
	@Value("${console.option.4}")
	private String qOption;
	
	@Value("${console.exit.message}")
	private String consoleExitMsg;
	
	@Value("${console.invalid.option}")
	private String consoleInvalidOptMsg;
	
	@Value("${console.data.print.column.width}")
	private String colsoleColumnWidth;
	
	@Value("${ir.welcome.message}")
	private String consoleRuleMsg;
	
	@Value("${ir.invalid.input}")
	private String ruleInputInvalidMsg;
	
	@Value("${ir.tran.success.message}")
	private String ruleTranSuccMsg;
    
	@Value("${ptst.welcome.message}")
	private String consolePtStMsg;
	
	@Value("${ptst.invalid.input}")
	private String ptstInputInvalidMsg;

	@Value("${ptst.invalid.input.param.count}")
	private String ptstInvalidInputCountMsg;
	
	@Value("${ptst.account.number.not.exist}")
	private String ptstAccountNotExistMsg;
	
	@Value("${ptst.account.no.trans.record}")
	private String ptstNoTransFoundMsg;
	
	@Override
    public void run(String... args) {
    	
    	System.out.println();
    	System.out.println();
    	String choice=null;
    	do {
    	System.out.println(consoleWelcomeMsg);
    	System.out.println(tOption);
    	System.out.println(iOption);
    	System.out.println(pOption);
    	System.out.println(qOption);
        System.out.print(">");

        choice = scanner.nextLine();
        choice = choice.replace("\n", "").replace("\r", "");
        choice = choice.toLowerCase();
        logger.info("Selected option : "+choice);
        switch (choice) {
            case "t":
                inputTransaction();
                break;
            case "i":
                defineInterestRule();
                break;
            case "p":
                printStatement();
                break;
            case "q":
                System.out.println(consoleExitMsg);
                System.out.println();
                System.exit(0);
                break;
            default:
                System.out.println(consoleInvalidOptMsg);
                System.out.println();
                System.out.println();
                break;
        }
        
    	}while(!choice.equals("q"));
    	scanner.close();
    }

    /**
     * This method is to handle the input transactions such as deposit and withdrawal.
     */
    private void inputTransaction() {
    	logger.info("--- method inputTransaction---started---");
    	System.out.println(tOptionMessage);
        System.out.print(">");
    	String inputData = scanner.nextLine();
    	String[] tmpDataList = inputData.split(" ");
    	String[] dataList = CommonUtil.parseUserInput(tmpDataList);
    	
    	/*check the number of inputs and if it is not expected number then 
    	* not proceed. return to the main menu options. Otherwise proceed.
    	*/
    	if (dataList.length!=4) {
    		System.out.println(tInvalidParameter);
    		System.out.println();
    		logger.info(tInvalidParameter+inputData.toString());
			return;
    	}
    	String dateStr = dataList[0];
    	String accNo = dataList[1];
    	String tranType = dataList[2];
    	String amountStr = dataList[3];
    	
    	/*check the input data are correct and valid. if not valid then 
    	* not proceed. return to the main menu options. Otherwise proceed.
    	*/
    	if (!VaidateInputTrans.isValid(dataList)) {
    		System.out.println(tInvalidParamInput);
    		System.out.println();
    		logger.info(tInvalidParamInput+" "+inputData.toString());
    		return;
    	}
    
    	/*check if the maximum number of transactions per day is not exceeding  
    	* the limit 99. return to the main menu options if exceeds. Otherwise proceed.
    	*/
    	int transCount = inputTranService.getNumberofTransaction(accNo.toUpperCase(), dateStr);
    	if (transCount>=Constants.MAX_TXN_SEQ_NO){
    		System.out.println(tExceedTran);
    		System.out.println();
    		logger.info(tExceedTran+" "+inputData.toString());
    		return;
    	}
    	double amount = Double.parseDouble(amountStr); //withdrawal amount
    	/*
    	 * Check the first transaction is not 'W' or not.
    	 */
    	if (!inputTranService.isValidTransaction(accNo.toUpperCase(), tranType)) {
			System.out.println(tInvalidTranType);
    		System.out.println();
    		logger.info(tInvalidTranType+" "+inputData.toString());
			return;
    	}
    	
    	//check the withdrawal is <=balance
    	if (tranType.equalsIgnoreCase("w"))
	    	if (amount>inputTranService.getBalance(dataList[1].toUpperCase())) {
	    		System.out.println(tWithdrawAmtExceed);
	    		System.out.println();
	    		logger.info(tWithdrawAmtExceed+" "+inputData.toString());
	    		return;
	    	}
    	//if all the inputs are valid then process the transaction.	
    	boolean processStatus = inputTranService.process(dateStr, accNo.toUpperCase(), tranType.toUpperCase(), amount);
    	if (processStatus) {
    		System.out.println(tSuccess);
    		logger.info(" input transaction successful for transaction type :"+dataList[2].toUpperCase()+" for the account no:"+accNo.toUpperCase());
    		List<AccountTransaction> accTrans = inputTranService.retrieveAllTransaction(dataList[1].toUpperCase(), dataList[0]);
    		// print heading
    		int columSize = Integer.parseInt(colsoleColumnWidth);
    		if (accTrans.size()>0) {
    			System.out.println();
    			System.out.println("Account: "+dataList[1].toUpperCase());
    			System.out.print("Date");
   				System.out.print("       ");
    			System.out.print("|");
    			System.out.print(" Txn Id");
    		    System.out.print("     ");
    			System.out.print("|");
    			System.out.print(" Type");
   				System.out.print("       ");
    			System.out.print("|");
    			System.out.print(" Amount");
   				System.out.print("     ");
    			System.out.print("|");
    			System.out.println();
    			
    			for(int recInd=accTrans.size()-1;recInd>=0;recInd--) {
    				AccountTransaction accT = accTrans.get(recInd);
    				System.out.print(accT.getTxnDate());
           			for(int i=(accT.getTxnDate().length()+1);i<columSize;i++) {
        				System.out.print(" ");
        			}
        			System.out.print("|");
        			System.out.print(" "+accT.getTxnId());
           			for(int i=(accT.getTxnId().length()+1);i<columSize;i++) {
        				System.out.print(" ");
        			}
        			System.out.print("|");
        			System.out.print(" "+accT.getTxnType());
        			for(int i=(accT.getTxnType().length()+1);i<columSize;i++) {
        				System.out.print(" ");
        			}
        			System.out.print("|");
        			System.out.print(" "+accT.getAmount());
        			for(int i=(CommonUtil.convertNumberToString(accT.getAmount()).length()+1);i<columSize;i++) {
        				System.out.print(" ");
        			}
        			System.out.print("|");
        			System.out.println();
    			}
    			System.out.println();
        		System.out.println();
    		}
    	}
    	else {
    		System.out.println("The transaction is Unsuccessful");
    		logger.info("The transaction is Unsuccessful");
    	}
    }

    /**
     * This method is to handle the defining interest rule use case.
     */
    private void defineInterestRule() {
    	logger.info("--- method defineInterestRule---started---");
    	System.out.println(consoleRuleMsg);
    	System.out.print(">");
    	String inputData = scanner.nextLine();
    	String[] tmpDataList = inputData.split(" ");
    	String[] dataList = CommonUtil.parseUserInput(tmpDataList);
		int columSize = Integer.parseInt(colsoleColumnWidth);
		
    	/*check the number of inputs and if it is not expected number then 
    	* not proceed, return to the main menu options. Otherwise proceed.
    	*/
    	if (dataList.length!=3) {
    		System.out.println(ptstInvalidInputCountMsg);
			System.out.println();
			return;
    	}
    	
    	/**
    	 * check the all input data are valid.if not valid then return to the 
    	 * main menu options.
    	 */
    	if (!ValidateInputRules.isValid(dataList)) {
    		System.out.println(ruleInputInvalidMsg);
    		System.out.println();
    		return;
    	}
    	String dateStr = dataList[0];
    	String ruleId = dataList[1].toUpperCase();
    	double rate = Double.parseDouble(dataList[2]);
    	/*
    	 * Once all the validation is done then proceed to 
    	 * create/update rule to the db table.
    	 */
    	boolean ruleStatus = rulesService.createUpdateRule(dateStr, ruleId, rate);
    	if (ruleStatus) {
    		System.out.println("The transaction is Successful");
    		logger.info("Adding new rule is successful.");
    		List<Rule> rList = rulesService.retriveAllRules();
    		if (rList.size()>0) {
    			System.out.println();
    			System.out.print("Date        ");
    			System.out.print("|");
    			System.out.print(" RuleId     ");
    			System.out.print("|");
    			System.out.print(" Rate (%)   |");
    			System.out.println();
    		}
    		for(Rule r: rList) {
    			System.out.print(r.getRuleDate());
    			for(int i=(r.getRuleDate().length());i<columSize;i++) {
    				System.out.print(" ");
    			}
    			System.out.print("|");
    			System.out.print(" "+r.getRuleId());
    			for(int i=(r.getRuleId().length()+1);i<columSize;i++) {
    				System.out.print(" ");
    			}
    			System.out.print("|");
    			System.out.print("   "+r.getRate());
    			for(int i=(CommonUtil.convertNumberToString(r.getRate()).length()+3);i<columSize;i++) {
    				System.out.print(" ");
    			}
    			System.out.print("|");
    			System.out.println();
    		}
    		System.out.println();
    		System.out.println();
    	}
    	else {
    		System.out.println("The transaction is Unsuccessful");
    		logger.info(" Adding new rule is Unsuccessful");
    	}
    	logger.info("--- method defineInterestRule---ended---");
    }

    /**
     * This method is to handle the print statement use case.
     */
    private void printStatement() {
    	logger.info("-----print statement--------started-----");
    	System.out.println(consolePtStMsg);
    	System.out.print(">");
    	String inputData = scanner.nextLine();
    	String[] tmpDataList = inputData.split(" ");
    	String[] dataList = CommonUtil.parseUserInput(tmpDataList);
    	/*
    	 * Check whether the given number of inputs are correct. otherwise, return to 
    	 * the menu option.
    	 */
    	if (dataList.length!=2) {
    		System.out.println(ptstInputInvalidMsg);
			System.out.println();
			logger.info("--User input is not valid:"+inputData);
			return;
    	}
    	
    	/*
    	 *check whether all the input are valid. 
    	 */
    	if (!ValidatePrintStmt.isValid(dataList)) {
    		System.out.println(ptstInputInvalidMsg);
    		System.out.println();
    		logger.info("--User input is not valid:"+inputData);
    		return;
    	}
    	String accNo = dataList[0].toUpperCase();
    	String monthYear = dataList[1];
    	
    	if(!printStmtService.isAccountExist(accNo)) {
    		System.out.println(ptstAccountNotExistMsg);
    		System.out.println();
    		logger.info("--Account not exist...:"+accNo);
    		return;
    	}
    	//if all valid then print the transactions for the given month	
    	List<AccountTransaction> accTrans = printStmtService.retrieveAllTransaction(accNo, monthYear);
    	if (accTrans.size()>0) { // if transaction are there then print.
			System.out.println();
			System.out.println("Account: "+accNo);
			System.out.print("Date");
			System.out.print("    ");
			System.out.print("|");
			System.out.print(" Txn Id     ");
			System.out.print("|");
			System.out.print(" Type ");
			System.out.print("|");
			System.out.print(" Amount  ");
			System.out.print("|");
			System.out.print(" Balance  ");
			System.out.print("|");
			System.out.println();
			logger.info("--Transactions found for the account:"+accNo);
			for(AccountTransaction accT: accTrans) {
				System.out.print(accT.getTxnDate());
    			System.out.print("|");
    			System.out.print(" "+accT.getTxnId());
       			for(int i=(accT.getTxnId().length()+1);i<12;i++) {
    				System.out.print(" ");
    			}
    			System.out.print("|");
    			System.out.print(" "+accT.getTxnType());
    			for(int i=(accT.getTxnType().length()+1);i<6;i++) {
    				System.out.print(" ");
    			}
    			System.out.print("|");
    			System.out.print(" "+accT.getAmount());
    			for(int i=(CommonUtil.convertNumberToString(accT.getAmount()).length()+1);i<9;i++) {
    				System.out.print(" ");
    			}
    			System.out.print("|");
    			System.out.print(" "+accT.getBalance());
    			for(int i=(CommonUtil.convertNumberToString(accT.getBalance()).length()+1);i<10;i++) {
    				System.out.print(" ");
    			}
    			System.out.print("|");
    			System.out.println();
			}
			System.out.println();
    		System.out.println();
    	}else { // if no transactions for the given month.
    		logger.info("No transactions found for the given month and year "+monthYear);
    		System.out.println();
    		System.out.println(ptstNoTransFoundMsg);
    		System.out.println();
    		System.out.println();
    	}
    	logger.info("-----print statement--------ended-----");
    }
}
