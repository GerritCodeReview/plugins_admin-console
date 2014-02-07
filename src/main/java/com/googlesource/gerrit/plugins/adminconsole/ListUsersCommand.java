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
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.gwtorm.server.ResultSet;
import com.google.inject.Inject;

@RequiresCapability(value=GlobalCapability.ADMINISTRATE_SERVER, scope=CapabilityScope.CORE)
@CommandMetaData(name = "ls-users", description = "List users")
public final class ListUsersCommand extends SshCommand {
  private ReviewDb db;

  @Inject
  ListUsersCommand(ReviewDb db) {
    this.db = db;
  }

  @Override
  protected void run() throws UnloggedFailure, Failure, Exception {
    ResultSet<Account> accounts = db.accounts().iterateAllEntities();
    for (Account account : accounts) {
      String out = new StringBuilder()
        .append(account.getId().toString())
        .append(" |")
        .append(Strings.isNullOrEmpty(account.getFullName())
            ? ""
            : " " + account.getFullName())
        .append(" |")
        .append(Strings.isNullOrEmpty(account.getPreferredEmail())
            ? ""
            : " " + account.getPreferredEmail())
        .toString();
      stdout.println(out);
    }
  }
}
