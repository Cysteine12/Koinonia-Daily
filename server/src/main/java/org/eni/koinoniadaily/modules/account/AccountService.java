package org.eni.koinoniadaily.modules.account;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.modules.account.dto.AccountProfileRequest;
import org.eni.koinoniadaily.modules.account.dto.AccountProfileResponse;
import org.eni.koinoniadaily.modules.auth.CurrentUserProvider;
import org.eni.koinoniadaily.modules.auth.UserPrincipal;
import org.eni.koinoniadaily.modules.user.User;
import org.eni.koinoniadaily.modules.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
  
  private final UserRepository userRepository;
  private final CurrentUserProvider currentUserProvider;
  private final AccountMapper accountMapper;

  public AccountProfileResponse getAccountProfile() {

    UserPrincipal user = currentUserProvider.getCurrentUser();

    return accountMapper.toDto(user);
  }

  @Transactional
  public AccountProfileResponse updateAccountProfile(AccountProfileRequest request) {

    Long userId = currentUserProvider.getCurrentUserId();

    User user = userRepository.findById(userId)
                  .orElseThrow(() -> new NotFoundException("User not found"));

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setPhotoUrl(request.getPhotoUrl());

    return accountMapper.toDto(user);
  }
  
  @Transactional
  public void deleteAccount() {
    
    Long userId = currentUserProvider.getCurrentUserId();

    User user = userRepository.findById(userId)
                  .orElseThrow(() -> new NotFoundException("User not found"));

    userRepository.delete(user);
  }
}
