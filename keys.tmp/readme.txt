client.ks
alias:clientA
password:clikspass
keytool -genkeypair -alias clientA -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore client.ks -storepass clikspass -keypass clikspass -validity 3650 -dname "CN=Client A, OU=Marketins, O=abc, L=Piter, ST=Russia, C=RU" 


client.ts
password:clitspswd