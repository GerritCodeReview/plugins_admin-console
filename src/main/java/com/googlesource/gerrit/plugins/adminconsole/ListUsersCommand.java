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
import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.account.AccountResolver;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.ResultSet;
import com.google.inject.Inject;

import org.kohsuke.args4j.Option;

@RequiresCapability(value=GlobalCapability.ADMINISTRATE_SERVER, scope=CapabilityScope.CORE)
@CommandMetaData(name = "ls-users", description = "List users")
public final class ListUsersCommand extends SshCommand {
  private ReviewDb db;
  private final AccountResolver accountResolver;

  @Option(name = "--active-only", usage = "show only active users")
  private boolean activeOnly = false;

  @Option(name = "--inactive-only", usage = "show only inactive users")
  private boolean inactiveOnly = false;

  @Inject
  ListUsersCommand(ReviewDb db,
      AccountResolver accountResolver) {
    this.db = db;
    this.accountResolver = accountResolver;
  }

  @Override
  protected void run() throws UnloggedFailure, Failure, Exception {
    ResultSet<Account> accounts = db.accounts().all();
    for (Account account : accounts) {
      if (activeOnly && !account.isActive()) {
        continue;
      }
      if (inactiveOnly && account.isActive()) {
        continue;
      }
      String username = getUsername(account);
      String out = new StringBuilder()
        .append(account.getId().toString())
        .append(" |")
        .append(Strings.isNullOrEmpty(username)
            ? ""
            : " " + username)
        .append(" |")
        .append(Strings.isNullOrEmpty(account.getFullName())
            ? ""
            : " " + account.getFullName())
        .append(" |")
        .append(Strings.isNullOrEmpty(account.getPreferredEmail())
            ? ""
            : " " + account.getPreferredEmail())
        .append(" |")
        .append(account.isActive()
            ? " active"
            : " inactive")
        .toString();
      stdout.println(out);
    }
  }

  private String getUsername(Account account) throws OrmException {
    String id = account.getId().toString();
    Account accountFromResolver = accountResolver.find(id);
    return accountFromResolver == null ? null
        : accountFromResolver.getUserName();
  }
}
