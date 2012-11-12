#!/usr/local/bin/perl
# 
# run.pl
# 
# Copyright (c) 2004, 2007, Oracle. All rights reserved.  
#
#    NAME
#      run.pl - Convenient script to run the demo
#
#    DESCRIPTION
#      Usage: perl run.pl 
#      Requires the ORACLE_HOME environment variable to be set.
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

print "The following grants are needed for the database user to access the sample schema:\n";
print "  grant all on pm.online_media to scott;\n";
print "  grant select on oe.product_information to scott;\n";
print "  grant read on directory MEDIA_DIR to scott;\n";
print "Have the permissions been granted? (y/n)[n]";
chomp ($hasGranted=<STDIN>);
if ($hasGranted ne "y" && $hasGranted ne "Y")
{
  print "Please connect to the database as sysdba and grant the above permissions before try again.\n";
  exit;
}

$CLASSPATH="$ORACLE_HOME"."$PSEP"."ord"."$PSEP"."jlib"."$PSEP"."ordim.jar";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."jdbc"."$PSEP"."lib"."$PSEP"."ojdbc5.jar";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."jlib"."$PSEP"."orai18n.jar";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."lib"."$PSEP"."xmlparserv2.jar";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."rdbms"."$PSEP"."jlib"."$PSEP"."xdb.jar";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."ord"."$PSEP"."im"."$PSEP"."demo"."$PSEP"."java";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."ord"."$PSEP"."im"."$PSEP"."demo"."$PSEP"."java"."$PSEP"."icons";
$CLASSPATH="$CLASSPATH"."$S"."$ORACLE_HOME"."$PSEP"."ord"."$PSEP"."im"."$PSEP"."demo"."$PSEP"."java"."$PSEP"."conf";

print "The classpath is: ", $CLASSPATH, "\n";

$JAVA="$ORACLE_HOME"."$PSEP"."jdk"."$PSEP"."bin"."$PSEP"."java";
print "The Java is:", $JAVA, "\n";

$run="$JAVA -classpath $CLASSPATH IMExample";

system($run);
