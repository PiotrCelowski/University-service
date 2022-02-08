#!/bin/bash

FILEPATH=`find .. -path "*/src/main/resources/*" -name "application.properties"`
echo "vaadin.productionMode=true" >> $FILEPATH