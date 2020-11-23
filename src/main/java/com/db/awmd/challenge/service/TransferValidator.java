package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.AmountTransferException;
import com.db.awmd.challenge.exception.NotEnoughBalanceException;
import com.db.awmd.challenge.exception.TransferInSameAccountException;

@Service
public class TransferValidator {
	void validate(final Account accountFrom, final Account accountTo, final BigDecimal amount)
			throws AccountNotFoundException, NotEnoughBalanceException {
		
		if(amount.compareTo(BigDecimal.ZERO)<0) {
			throw new AmountTransferException("Transfer amount not valid. Please try with valid amount");
		}

		if (accountFrom == null) {
			throw new AccountNotFoundException("Account " + accountFrom + "not found");
		}

		if (accountTo == null) {
			throw new AccountNotFoundException("Account " + accountTo + "not found");
		}

		if (sameAccount(accountFrom, accountTo)) {
			throw new TransferInSameAccountException("Self Transfer not allowed");
		}

		if (!enoughBalance(accountFrom, amount)) {
			throw new NotEnoughBalanceException("Insufficient balance in account");
		}
	}

	private boolean sameAccount(final Account accountFrom, final Account accountTo) {
		return accountFrom.getAccountId().equals(accountTo.getAccountId());
	}

	private boolean enoughBalance(final Account account, final BigDecimal amount) {
		return account.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
	}

}
