package com.db.awmd.challenge.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AmountTransfer;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.AmountTransferException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.NotEnoughBalanceException;
import com.db.awmd.challenge.exception.TransferInSameAccountException;
import com.db.awmd.challenge.service.AccountsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

	private final AccountsService accountsService;

	@Autowired
	public AccountsController(AccountsService accountsService) {
		this.accountsService = accountsService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
		log.info("Creating account {}", account);

		try {
			this.accountsService.createAccount(account);
		} catch (DuplicateAccountIdException daie) {
			return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(path = "/{accountId}")
	public ResponseEntity<Object> getAccount(@PathVariable String accountId) {
		log.info("Retrieving account for id {}", accountId);
		final Account account ;
		try {
			account = this.accountsService.getAccount(accountId);
		}catch(AccountNotFoundException afe) {
			return new ResponseEntity<>(afe.getMessage(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(account, HttpStatus.OK);
	}

	@PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> amountTransfer(@RequestBody @Valid AmountTransfer amountTransfer) {
		try {

			this.accountsService.amountTransfer(amountTransfer.getAccountFrom(), amountTransfer.getAccountTo(),
					amountTransfer.getTransferAmount());

		} catch (AmountTransferException | TransferInSameAccountException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (AccountNotFoundException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
		} catch (NotEnoughBalanceException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>("Transfer Completed", HttpStatus.ACCEPTED);
	}

}
