package de.tbuning.neo4j.graphdatabase

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.extensions.AbstractExtensionPointBean
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.util.xmlb.annotations.Attribute
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.graphdb.factory.GraphDatabaseSettings
import org.neo4j.helpers.PortBindException
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Copyright 2016 Thomas Buning
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class Neo4JGraphDatabase : AbstractExtensionPointBean() {

    companion object {

        val EP_NAME: ExtensionPointName<Neo4JGraphDatabase> = ExtensionPointName.create<Neo4JGraphDatabase>("de.tbuning.neo4j.graphdatabase.Neo4JGraphDatabase")
    }

    private var graphDb: GraphDatabaseService? = null
    private var database_dir: File? = null

    @Attribute("databaseFolder")
    var databaseFolder: String = ""

    @Attribute("withServer")
    var withServer: Boolean = false

    fun startDatabase(basePath: String) {

        /**
         * One Instance of the Database (from the plugin.xml) only results in one GraphDatabase Instance
         */

        if (graphDb != null) {

            dispose()
        }

        if (databaseFolder.isEmpty()) {

            databaseFolder = UUID.randomUUID().toString()
        }

        val startPort = 7687
        val maxDepth = 5

        database_dir = File("$basePath/gen/graph_db/$databaseFolder/")

        initDatabase(startPort, maxDepth, {

            if (withServer) {
                val notification = if (it == null) Notification("MSA", "Could not start database", "Could not start graph database on ports $startPort - ${startPort + maxDepth}", NotificationType.ERROR) else Notification("MSA", "Success", "Successfully started graph database on port $it", NotificationType.INFORMATION)
                Notifications.Bus.notify(notification)
            }
        })
    }

    private fun initDatabase(port: Int, maxRecursionDepth: Int, callback: (choosenPort: Int?) -> Unit) {

        if (maxRecursionDepth <= 0) {

            callback(null)
            return
        }

        try {

            val graphDbBuilder = GraphDatabaseFactory().newEmbeddedDatabaseBuilder(database_dir)

            if (withServer) {

                val bolt = GraphDatabaseSettings.boltConnector("0")
                graphDbBuilder.setConfig(bolt.enabled, "true").setConfig(bolt.address, "localhost:$port")
            }

            graphDb = graphDbBuilder.newGraphDatabase()

            registerShutdownHook(graphDb)

            callback(port)
        } catch(e: Exception) {

            if (e.cause?.cause is PortBindException) {

                initDatabase(port + 1, maxRecursionDepth - 1, callback)
            } else {

                callback(null)
            }
        }
    }

    fun dispose() {
        if (graphDb != null) {

            graphDb!!.shutdown()
            graphDb = null
        }
    }

    fun createDatabase(graphQuery: String): GraphDatabaseService? {

        if (graphDb == null) {

            return null
        }

        val beginTx = graphDb!!.beginTx(5, TimeUnit.MINUTES)

        try {
            graphDb!!.execute("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n, r")
            graphDb!!.execute(graphQuery)
            beginTx.success()
        } catch (e: Exception) {

            beginTx.failure()
        } finally {

            beginTx.close()
        }

        return graphDb
    }


    private fun registerShutdownHook(graphDb: GraphDatabaseService?) {

        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {

                graphDb?.shutdown()
            }
        })
    }
}
