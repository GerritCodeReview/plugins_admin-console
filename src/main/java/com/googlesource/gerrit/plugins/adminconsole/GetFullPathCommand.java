// Copyright (C) 2017 The Android Open Source Project
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

import static com.google.gerrit.sshd.CommandMetaData.Mode.MASTER_OR_SLAVE;

import com.google.gerrit.common.data.GlobalCapability;
import com.google.gerrit.extensions.annotations.CapabilityScope;
import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.reviewdb.client.Project;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.gerrit.server.git.LocalDiskRepositoryManager;
import com.google.gerrit.server.git.GitRepositoryManager;
import com.google.gerrit.server.project.ProjectCache;
import com.google.inject.Inject;

import org.kohsuke.args4j.Argument;

@RequiresCapability(value = GlobalCapability.ADMINISTRATE_SERVER, scope = CapabilityScope.CORE)
@CommandMetaData(runsAt = MASTER_OR_SLAVE, name = "get-path", description = "Gets the full path of a repository")
public final class GetFullPathCommand extends SshCommand {

  @Argument(index = 0, required = true, metaVar = "PROJECT", usage = "Name of the project")
  private String projectName = "";

  private LocalDiskRepositoryManager localDiskRepositoryManager;
  private ProjectCache projectCache;

  @Inject
  GetFullPathCommand(GitRepositoryManager grm, ProjectCache pc) {
    if (grm instanceof LocalDiskRepositoryManager) {
      localDiskRepositoryManager = (LocalDiskRepositoryManager) grm;
    }
    projectCache = pc;
  }

  @Override
  protected void run() throws UnloggedFailure {
    if (localDiskRepositoryManager == null) {
      throw new UnloggedFailure(1,
          "Command only works with disk based repository managers");
    }
    Project.NameKey nameKey = new Project.NameKey(projectName);
    if (projectCache.get(nameKey) != null) {
      stdout.print(localDiskRepositoryManager.getBasePath(nameKey) + "/"
          + nameKey.get() + ".git" + "\n");
    } else {
      throw new UnloggedFailure(1, "Repository not found");
    }
  }
}
