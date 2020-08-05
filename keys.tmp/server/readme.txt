server.ks
alias:localtest
password:srvkspass
keytool -genkeypair -alias localtest -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore server.ks -storepass srvkspass -keypass srvkspass -validity 3650 -ext bc:c -dname "CN=SERG-test, OU=IT, O=Booble, L=Moscow-2, ST=Russia, C=RU" 


server.ts
password:sertspwd
keytool -import -v -keystore server.ts -storepass sertspwd -file client.cer -storetype PKCS12