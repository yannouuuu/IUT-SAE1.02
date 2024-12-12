@echo off
set SOURCES=src
set CLASSES=classes
set CLASSPATH=

for /R lib %%f in (*.jar) do set CLASSPATH=!CLASSPATH!;%%f

javac -cp "%CLASSPATH%" -sourcepath "%SOURCES%" -d "%CLASSES%" %* 
for /R src %%f in (*.java) do javac -cp "%CLASSPATH%" -sourcepath "%SOURCES%" -d "%CLASSES%" "%%f"
