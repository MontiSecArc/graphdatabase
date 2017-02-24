# About
[![Build Status](https://travis-ci.org/MontiSecArc/graphdatabase.svg?branch=master)](https://travis-ci.org/MontiSecArc/graphdatabase)

This IntelliJ plugin bundles a Neo4J database for every other plugin to use. It hides dependencies and allows an easy use of graph databases directly in IntelliJ plugins.

**Downloads**

[GraphDatabasePlugin-1.0.0.zip](http://138.68.65.103:8081/artifactory/intellij_plugins_snapshot_local/de/tbuning/neo4j/graphdatabase/GraphDatabasePlugin/1.0.0/GraphDatabasePlugin-1.0.0.zip) &#8226; [GraphDatabasePlugin-1.0.0.jar](http://138.68.65.103:8081/artifactory/intellij_plugins_snapshot_local/de/tbuning/neo4j/graphdatabase/GraphDatabasePlugin/1.0.0/GraphDatabasePlugin-1.0.0.jar)

# Contents
- [Quickstart](https://git.rwth-aachen.de/ma_buning/msa/edit/master/README.md#quickstart)
- [Install Plugin into IntelliJ Installation](https://git.rwth-aachen.de/ma_buning/msa/edit/master/README.md#install_plugin_into_intelliJ_installation)

# Quickstart
1. Check-Out project:

    `git clone https://git.rwth-aachen.de/ma_buning/msa.git --remote --recursive`
2. Import project into IntelliJ. Instructions can be found [here](https://www.jetbrains.com/help/idea/2016.3/importing-project-from-gradle-model.html).
3. Run an IDEA instance with the MSA language plugins pre-installed:
    1. Run/Debug `runIdea` from the gradle task list:
    ![Bildschirmfoto_2017-01-10_um_18.28.47](/uploads/3e4054765c93e1dcdcf6eada7f0eb4b1/Bildschirmfoto_2017-01-10_um_18.28.47.png)

# Install Plugin into IntelliJ Installation
The plugin requires IntelliJ Version [2016.X.X](https://www.jetbrains.com/idea/download/) to be installed.

Download the newest version of the plugin from [here](https://git.rwth-aachen.de/ma_buning/msa/builds/3094/artifacts/file/build/distributions/IntelliJ_MSA-0.7.7.SNAPSHOT.zip). Do not unzip the file, just open the IntelliJ preferences and locate "Plugins" from the left menu.
![Bildschirmfoto_2016-11-11_um_09.38.51](/uploads/4236b147e9ec65f01910140a791bc7ff/Bildschirmfoto_2016-11-11_um_09.38.51.png)
Click the "Install plugin from disk" button and select the downloaded zip file.
