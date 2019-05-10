// Copyright (C) 2014 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlesource.gerrit.plugins.adminconsole;

import com.google.common.base.Strings;
import com.google.gerrit.common.data.GlobalCapability;
import com.google.gerrit.extensions.annotations.CapabilityScope;
import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.index.query.Predicate;
import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.server.account.AccountState;
import com.google.gerrit.server.query.account.AccountPredicates;
import com.google.gerrit.server.query.account.InternalAccountQuery;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.kohsuke.args4j.Option;

@RequiresCapability(value = GlobalCapability.ADMINISTRATE_SERVER, scope = CapabilityScope.CORE)
@CommandMetaData(name = "ls-users", description = "List users")
public final class ListUsersCommand extends SshCommand {
  private final Provider<InternalAccountQuery> accountQueryProvider;

  @Option(name = "--active-only", usage = "show only active users")
  private boolean activeOnly = false;

  @Option(name = "--inactive-only", usage = "show only inactive users")
  private boolean inactiveOnly = false;

  @Inject
  ListUsersCommand(Provider<InternalAccountQuery> accountQueryProvider) {
    this.accountQueryProvider = accountQueryProvider;
  }

  @Override
  protected void run() throws UnloggedFailure, Failure, Exception {
    if (activeOnly && inactiveOnly) {
      throw die("--active-only and --inactive-only are mutually exclusive");
    }

    Predicate<AccountState> queryPredicate;
    if (activeOnly) {
      queryPredicate = AccountPredicates.isActive();
    } else if (inactiveOnly) {
      queryPredicate = AccountPredicates.isNotActive();
    } else {
      // This is a work-around to get all the account from the index, querying
      // active and inactive users returns all the users. Another option is to
      // use the Accounts class which will list all the account ids from notedb
      // and then query the secondary index for each user but this way is less
      // efficient.
      queryPredicate = Predicate.or(AccountPredicates.isActive(), AccountPredicates.isNotActive());
    }
    for (AccountState accountState : accountQueryProvider.get().query(queryPredicate)) {
      Account account = accountState.getAccount();
      String out =
          new StringBuilder()
              .append(account.getId().toString())
              .append(" |")
              .append(
                  accountState.getUserName().isPresent()
                      ? ""
                      : " " + accountState.getUserName().get())
              .append(" |")
              .append(
                  Strings.isNullOrEmpty(account.getFullName()) ? "" : " " + account.getFullName())
              .append(" |")
              .append(
                  Strings.isNullOrEmpty(account.getPreferredEmail())
                      ? ""
                      : " " + account.getPreferredEmail())
              .append(" |")
              .append(account.isActive() ? " active" : " inactive")
              .toString();
      stdout.println(out);
    }
  }
}
