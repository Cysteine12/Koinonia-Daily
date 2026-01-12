package org.eni.koinoniadaily.modules.account;

import org.eni.koinoniadaily.modules.account.dto.AccountProfileRequest;
import org.eni.koinoniadaily.modules.account.dto.AccountProfileResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
  
  private final AccountService accountService;

  @GetMapping("/profile")
  public ResponseEntity<SuccessResponse<AccountProfileResponse>> getAccountProfile() {

    AccountProfileResponse response = accountService.getAccountProfile();

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PatchMapping("/profile")
  public ResponseEntity<SuccessResponse<AccountProfileResponse>> updateAccountProfile(
      @RequestBody @Valid AccountProfileRequest request
  ) {

    AccountProfileResponse response = accountService.updateAccountProfile(request);

    return ResponseEntity.ok(SuccessResponse.of(response, "Account updated successfully"));
  }

  @DeleteMapping
  public ResponseEntity<SuccessResponse<Void>> deleteAccount() {

    accountService.deleteAccount();

    return ResponseEntity.ok(SuccessResponse.message("Account deleted successfully"));
  }
}
