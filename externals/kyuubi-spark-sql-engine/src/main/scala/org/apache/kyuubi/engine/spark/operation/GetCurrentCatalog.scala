/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kyuubi.engine.spark.operation

import org.apache.spark.sql.types.StructType

import org.apache.kyuubi.engine.spark.shim.SparkCatalogShim
import org.apache.kyuubi.operation.IterableFetchIterator
import org.apache.kyuubi.operation.log.OperationLog
import org.apache.kyuubi.operation.meta.ResultSetSchemaConstant.TABLE_CAT
import org.apache.kyuubi.session.Session

class GetCurrentCatalog(session: Session) extends SparkOperation(session) {

  private val operationLog: OperationLog = OperationLog.createOperationLog(session, getHandle)

  override def getOperationLog: Option[OperationLog] = Option(operationLog)

  override protected def resultSchema: StructType = {
    new StructType()
      .add(TABLE_CAT, "string", nullable = true, "Catalog name.")
  }

  override protected def runInternal(): Unit = {
    try {
      iter = new IterableFetchIterator(Seq(SparkCatalogShim().getCurrentCatalog(spark)))
    } catch onError()
  }
}
