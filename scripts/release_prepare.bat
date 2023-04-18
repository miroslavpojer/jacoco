@ECHO OFF
CD %USERPROFILE%\Programming\Local
IF EXIST enceladus_release (
	REN enceladus_release enceladus_release.old
	RMDIR /S /Q enceladus_release.old
)
git clone https://github.com/AbsaOSS/enceladus.git enceladus_release
CD enceladus_release
REM mvn gitflow:release -B
mvn gitflow:release