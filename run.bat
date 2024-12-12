@echo off
set CLASSPATH=

for /R lib %%f in (*.jar) do set CLASSPATH=!CLASSPATH!;%%f
set CLASSPATH=%CLASSPATH%;classes
set MAINCLASS=EcoManager

java -cp "%CLASSPATH%" %MAINCLASS%
