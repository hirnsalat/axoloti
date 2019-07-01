@ECHO OFF

set PATH=%axoloti_runtime%\platform_win\gcc-arm-none-eabi-8-2018q4\bin;%axoloti_runtime%\platform_win\bin

call :setfirmware "%axoloti_firmware%"
call :sethome "%axoloti_home%"
call :setrelease "%axoloti_release%"

cd %axoloti_firmware%
make -f Makefile.module
IF %ERRORLEVEL% NEQ 0 (
	exit /b 1
)
goto :eof

rem --- path shortening

:setfirmware
set axoloti_firmware=%~s1
goto :eof

:sethome
set axoloti_home=%~s1
goto :eof

:setrelease
set axoloti_release=%~s1
goto :eof