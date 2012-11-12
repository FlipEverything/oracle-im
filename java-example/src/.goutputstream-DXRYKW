#!/usr/local/bin/perl
# 
# compile.pl
# 
# Copyright (c) 2004, 2008, Oracle. All rights reserved.  
#
#    NAME
#      compile.pl - Compilation script for 
#                   Oracle Multimedia Java API Sample Application
#
#    DESCRIPTION
#      Requires the ORACLE_HOME environment variable
#
#    NOTES
#

if ($ENV{OS} eq 'Windows_NT')
{
  $S=';';
  $PSEP='\\';
}
else 
{
  $S=':';
  $PSEP='/';
}

if (!$ENV{ORACLE_HOME})
{
  die ("Please set environment ORACLE_HOME, then try again.");
}

$ORACLE_HOME=$ENV{ORACLE_HOME};

$CLASSPATH="$ORACLE_HOME"."$PSEP"."ord"."$PSEP"."jlib"."$PSEP"."ordim.jar";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."jdbc"."$PSEP"."lib"."$PSEP"."ojdbc5.jar";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."jlib"."$PSEP"."orai18n.jar";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."lib"."$PSEP"."xmlparserv2.jar";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."rdbms"."$PSEP"."jlib"."$PSEP"."xdb.jar";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."ord"."$PSEP"."im"."$PSEP"."demo"."$PSEP"."java";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."ord"."$PSEP"."im"."$PSEP"."demo"."$PSEP"."java"."$PSEP"."icons";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."ord"."$PSEP"."im"."$PSEP"."demo"."$PSEP"."java"."$PSEP"."conf";

print "The classpath is: ", $CLASSPATH, "\n";

#$JAVAC="$ORACLE_HOME"."$PSEP"."jdk"."$PSEP"."bin"."$PSEP"."javac";
$JAVAC="/usr/lib/jvm/java-6-openjdk/bin/javac";
print "The Java compiler is:", $JAVAC, "\n";

$classdir="$ORACLE_HOME"."$PSEP"."ord"."$PSEP"."im"."$PSEP"."demo"."$PSEP"."java";
$compile="$JAVAC -classpath $CLASSPATH -d $classdir *.java";

system($compile);
