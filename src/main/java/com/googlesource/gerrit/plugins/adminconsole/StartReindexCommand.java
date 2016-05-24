// Copyright (C) 2016 The Android Open Source Project
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

import org.kohsuke.args4j.Argument;

import com.google.gerrit.common.data.GlobalCapability;
import com.google.gerrit.extensions.annotations.CapabilityScope;
import com.google.gerrit.extensions.annotations.RequiresCapability;
import com.google.gerrit.lucene.OnlineReindexer;
import com.google.gerrit.sshd.CommandMetaData;
import com.google.gerrit.sshd.SshCommand;
import com.google.inject.Inject;

@RequiresCapability(value=GlobalCapability.ADMINISTRATE_SERVER, scope=CapabilityScope.CORE)
@CommandMetaData(name = "start-reindex", description = "Start a new on-line re-indexing for a target Lucene index version")
public final class StartReindexCommand extends SshCommand {

  @Argument(index = 0, usage = "Index version", metaVar = "VERSION")
  private int indexVersion;

  @Inject OnlineReindexer.Factory reindexerFactory;

  @Override
  public void run() {
    OnlineReindexer indexer = reindexerFactory.create(indexVersion);
    indexer.start();
    System.out.println("On-line reindexing scheduled for version " +
        indexVersion);
  }
}
