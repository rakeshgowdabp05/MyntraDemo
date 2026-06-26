$tomcat = "C:\Users\rakes\OneDrive\Desktop\apache-tomcat-10.1.55-windows-x64\apache-tomcat-10.1.55"

$env:MYNTRADEMO_DB_URL = "jdbc:mysql://localhost:3306/myntrademo_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata"
$env:MYNTRADEMO_DB_USERNAME = "root"
$env:MYNTRADEMO_DB_PASSWORD = "Rakesh@2005"

cd "$tomcat\bin"
.\catalina.bat run
