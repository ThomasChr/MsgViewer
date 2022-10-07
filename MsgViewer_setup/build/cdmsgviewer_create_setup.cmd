REM @ECHO OFF
REM --- FUNKTION: ERSTELLT DEFAULTMÄßIG BASIC+HOME-EDITION!
REM ---
SET PROGRAM_X86=C:\PROGRAM FILES (X86)
SET SRC_DIR=..\SOURCE

:INSTALL_NSIS
start /WAIT "" "..\NSIS_installer\NSIS-3.08-SETUP.EXE" /S
SET PATH=%NSISDIR%;%PATH%

:INSTALL_7ZIP
start /WAIT "" "..\7zip\7z2201.exe" /S
SET PATH=%PROGRAM_X86%\7-Zip;%PATH%

:COPY_NSIS_PLUGIN
xcopy /Y "..\NSIS_installer\Plugins\x86-unicode\*.dll" "%PROGRAM_X86%\NSIS\Plugins\x86-unicode\"

REM --- w4glapps-Verzechnis hinzufuegen, wegen 7z.exe weiter unten
REM --- NSISDIR + NSISCONFDIR werden von makensis.exe abgefragt und bestimmen das Installationsverzeichnis von NSIS

IF "%NSISDIR%"=="" SET NSISDIR=%PROGRAM_X86%\NSIS

IF "%NSISCONFDIR%"=="" SET NSISCONFDIR=%NSISDIR%
IF "%NSIS_EXE%"=="" SET NSIS_EXE=%NSISDIR%\makensis.exe
SET CURDIR=%~dp0
REM --- Standard-Pfad nach Installation (momentan nicht mehr benötigt):
REM IF NOT EXIST "%NSIS_EXE%" SET NSIS_EXE=C:\Program Files (x86)\NSIS\makensis.exe

IF NOT EXIST "%NSIS_EXE%" (
TITLE Installations-Fehler!
echo NSIS ist nicht installiert!
echo [nsis.sourceforge.net^/Download]
echo.
timeout 15
exit)

cls

call :SETUP_START

REM --- Verzeichnis öffnen
IF "%NOOPEN_EXPLORER%" NEQ "1" start explorer /e, ..\bin\release
GOTO :ENDE

:: --- Hauptprogramm ENDE

:SETUP_START
COLOR 0D
SET EDITION_NAME=cdMsgViewer_setup
SET EDITION_SCRIPT=cdMsgViewer.nsi
call :ZIP_JRE
call :CREATE_EDITION
goto :EOF

:ZIP_JRE
7z a -t7z -m0=LZMA -mmt=2 -mqs "..\files\jre.7z" D:\git\MsgViewer\cimdata\jre\*
goto :EOF

:CREATE_EDITION
TITLE Create %EDITION_NAME%.exe ...
echo.
echo CREATING %EDITION_NAME% ...
"%NSIS_EXE%" %SRC_DIR%\%EDITION_SCRIPT%
IF ERRORLEVEL 1 (
TITLE NSIS-Fehler Level %ERRORLEVEL% bei %EDITION_NAME%
pause
goto :EOF

call :DEL_FILES
TITLE Weitere NSIS-Projekte werden erstellt...
) ELSE (
echo Ende
TITLE ---
goto :EOF)

:ENDE
exit
