// Copyright (C) 2022 The Android Open Source Project
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

import com.google.gerrit.entities.Account;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.ProjectUtil;
import com.google.gerrit.server.account.AccountResolver;
import com.google.gerrit.sshd.BaseCommand;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.jgit.errors.ConfigInvalidException;

public class AdminConsoleUtil {
  @Inject private AccountResolver accountResolver;

  @Inject private IdentifiedUser.GenericFactory userFactory;

  Set<Account.Id> getIdList(String userName)
      throws ConfigInvalidException, IOException, BaseCommand.UnloggedFailure {
    Set<Account.Id> idList = accountResolver.resolve(userName).asIdSet();
    if (idList.isEmpty()) {
      throw new BaseCommand.UnloggedFailure(
          1,
          "No accounts found for your query: \""
              + userName
              + "\""
              + " Tip: Try double-escaping spaces, for example: \"--user Last,\\\\ First\"");
    }
    return idList;
  }

  Set<IdentifiedUser> getUserList(String userName)
      throws ConfigInvalidException, IOException, BaseCommand.UnloggedFailure {
    return getIdList(userName).stream()
        .map(id -> userFactory.create(id))
        .collect(Collectors.toSet());
  }

  String getProjectName(String projectName) {
    while (projectName.endsWith("/")) {
      projectName = projectName.substring(0, projectName.length() - 1);
    }

    while (projectName.startsWith("/")) {
      // Be nice and drop the leading "/" if supplied by an absolute path.
      // We don't have a file system hierarchy, just a flat namespace in
      // the database's Project entities. We never encode these with a
      // leading '/' but users might accidentally include them in Git URLs.
      //
      projectName = projectName.substring(1);
    }

    return ProjectUtil.stripGitSuffix(projectName);
  }
}
