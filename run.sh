#!/bin/bash
export CLASSPATH=`find ./lib -name "*.jar" | tr '\n' ':'`
export MAINCLASS=App # <- à remplacer par le nom du programme
java -cp ${CLASSPATH}:classes $MAINCLASS 
# Le programme s'exécute depuis la racine de l'archive
