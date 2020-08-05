root.ks
alias:rootest
password:rootpass

root.ts
password:roottspwd

keytool -genkeypair -alias rootest -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore root.ks -storepass rootpass -keypass rootpass -validity 3650 -ext bc:c -dname "CN=root-test, OU=IT, O=Doodle, L=Moscow-0, ST=Russia, C=RU" 

keytool -export -rfc -alias rootest -keystore root.ks -storepass rootpass -file root.pem

--------------------------
SERVER:
keytool -alias rootest -gencert -infile server.req -keystore root.ks -storepass rootpass -rfc -outfile signed-server.pem

--------------------------
CLIENT:
keytool -alias rootest -gencert -infile client.req -keystore root.ks -storepass rootpass -rfc -outfile signed-client.pem
