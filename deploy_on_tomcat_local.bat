call gradlew build
if "%ERRORLEVEL%" == "0" goto rename
echo.
echo GRADLEW BUILD HAS ERRORS! - STOPPING DEPLOYMENT PROCESS...
goto fail

:rename
del build\libs\gpsy-v01.war
ren build\libs\gpsy-0.0.1-SNAPSHOT.war gpsy-v01.war
if "%ERRORLEVEL%" == "0" goto stoptomcat
echo Cannot rename file!
goto fail

:stoptomcat
call %CATALINA_HOME%\bin\shutdown.bat

:copyfile
copy build\libs\gpsy-v01.war %CATALINA_HOME%\webapps
if "%ERRORLEVEL%" == "0" goto runtomcat
echo Cannot copy file!!!
goto fail

:runtomcat
call %CATALINA_HOME%\bin\startup.bat
goto end

:fail
echo.
echo There were errors

:end
echo.
echo Work is finished.

