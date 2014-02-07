// Copyright (C) 2012 The Android Open Source Project
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

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.errors.ConfigInvalidException;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import com.google.gerrit.common.data.GlobalCapability;
import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.reviewdb.client.Account;
import com.google.gerrit.reviewdb.client.Account.Id;
import com.google.gerrit.reviewdb.client.AccountExternalId;
import com.google.gerrit.reviewdb.client.AccountSshKey;
import com.google.gerrit.reviewdb.server.ReviewDb;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.account.AccountResolver;
import com.google.gerrit.server.account.AccountResource;
import com.google.gerrit.server.account.GetGroups;
import com.google.gerrit.server.group.GroupJson.GroupInfo;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.gwtorm.server.OrmException;
import com.google.gwtorm.server.SchemaFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;

@RequiresCapability(GlobalCapability.ADMINISTRATE_SERVER)
@CommandMetaData(name = "show-account", description = "Displays user information")
public final class ShowAccountCommand extends SshCommand {

  @Argument(usage = "User information to find: LastName,\\ Firstname,  email@address.com, account id or an user name.  Be sure to double-escape spaces, for example: \"show-account Last,\\\\ First\"")
  private String name = "";

  @Option(name = "--show-groups", usage = "show group membership by user?")
  private boolean showGroups = false;

  @Option(name = "--filter-groups", usage = "filter group string")
  private String filterGroups = null;

  @Option(name = "--show-keys", usage = "show user's public keys?")
  private boolean showKeys = false;

  final AccountResolver accountResolver;
  private final SchemaFactory<ReviewDb> schema;
  private final Provider<GetGroups> accountGetGroups;
  private final IdentifiedUser.GenericFactory userFactory;

  @Inject
  ShowAccountCommand(AccountResolver ar,
      final Provider<GetGroups> accountGetGroups,
      final IdentifiedUser.GenericFactory userFactory,
      SchemaFactory<ReviewDb> schema) throws ConfigInvalidException,
      IOException {
    accountResolver = ar;
    this.accountGetGroups = accountGetGroups;
    this.userFactory = userFactory;
    this.schema = schema;
  }

  @Override
  public void run() throws UnloggedFailure, OrmException {
    Account account;

    if (name.isEmpty()) {
      throw new UnloggedFailure(1,
          "You need to tell me who to find:  LastName,\\\\ Firstname, email@address.com, account id or an user name.  "
              + "Be sure to double-escape spaces, for example: \"show-account Last,\\\\ First\"");
    }

    Set<Id> idList = accountResolver.findAll(name);
    if (idList.isEmpty()) {
      throw new UnloggedFailure(1,
          "No accounts found for your query: \""
              + name
              + "\""
              + " Tip: Try double-escaping spaces, for example: \"show-account Last,\\\\ First\"");
    } else {
      stdout.println("Found " + idList.size() + " result"
          + (idList.size() > 1 ? "s" : "") + ": for query: \"" + name + "\"");
      stdout.println();
    }

    for (Id id : idList) {
      account = accountResolver.find(id.toString());
      stdout.println("Full name:         " + account.getFullName());
      stdout.println("Account Id:        " + id.toString());
      stdout.println("Preferred Email:   " + account.getPreferredEmail());
      stdout.println("User Name:         " + account.getUserName());
      stdout.println("Active:            " + account.isActive());
      stdout.println("Registered on:     " + account.getRegisteredOn());

      final ReviewDb db = schema.open();

      stdout.println("");
      stdout.println("External Ids:");
      stdout.println(String
          .format("%-50s %s", "Email Address:", "External Id:"));
      for (AccountExternalId accountExternalId : db.accountExternalIds()
          .byAccount(account.getId())) {
        stdout.println(String.format("%-50s %s",
            (accountExternalId.getEmailAddress() == null ? ""
                : accountExternalId.getEmailAddress()), accountExternalId
                .getExternalId()));
      }

      if (showKeys) {
        stdout.println("");
        stdout.println("Public Keys:");
        List<AccountSshKey> sshKeys =
            db.accountSshKeys().byAccount(account.getId()).toList();
        if (sshKeys == null || sshKeys.isEmpty()) {
          stdout.println("None");
        } else {
          stdout.println(String.format("%-9s %s", "Status:", "Key:"));
          for (AccountSshKey sshKey : sshKeys) {
            stdout.println(String.format("%-9s %s", (sshKey.isValid()
                ? "Active" : "Inactive"), sshKey.getSshPublicKey()));
          }
        }
      }

      db.close();

      if (showGroups) {
        stdout.println();
        stdout.println("Member of groups"
            + (filterGroups == null ? "" : " (Filtering on \"" + filterGroups
                + "\")") + ":");
        List<GroupInfo> groupInfos =
            accountGetGroups.get().apply(
                new AccountResource(userFactory.create(id)));

        Collections.sort(groupInfos, new CustomComparator());
        for (GroupInfo groupInfo : groupInfos) {
          if (null == filterGroups || groupInfo.name.toLowerCase().contains(filterGroups.toLowerCase
              ())) {
            stdout.println(groupInfo.name);
          }
        }
      }
      stdout.println("");
    }
  }

  private static class CustomComparator implements Comparator<GroupInfo> {
    @Override
    public int compare(GroupInfo o1, GroupInfo o2) {
      return o1.name.compareTo(o2.name);
    }
  }
}
