server.ks
alias:servtest
password:servpass

server.ts
password:servtspwd

keytool -genkeypair -alias servtest -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore server.ks -storepass servpass -keypass servpass -validity 1 -dname "CN=Local-SERVER-1, OU=IT, O=Doodle, L=Moscow-1, ST=Russia, C=RU" 

keytool -alias servtest -certreq -keystore server.ks -storepass servpass -file server.req

keytool -import -alias root-test -v -keystore server.ks -storepass servpass -file root.pem

keytool -alias servtest -importcert -file signed-server.pem -keystore server.ks -storepass servpass


keytool -import -alias root-test -v -keystore server.ts -storepass servtspwd -file root.pem






