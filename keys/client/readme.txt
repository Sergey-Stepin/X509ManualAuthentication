client.ks
alias:clitest
password:clientpass

client.ts
password:clientpwd

keytool -genkeypair -alias clitest -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore client.ks -storepass clientpass -keypass clientpass -validity 1 -dname "CN=Client-A1, OU=Marketing, O=Doodle, L=Moscow-2, ST=Russia, C=RU"

keytool -alias clitest -certreq -keystore client.ks -storepass clientpass -file client.req

keytool -import -alias root-test -v -keystore client.ks -storepass clientpass -file root.pem

keytool -alias clitest -importcert -file signed-client.pem -keystore client.ks -storepass clientpass

