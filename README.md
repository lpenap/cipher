# Cipher
Java RSA Cipher Example to Load/Save a single text document using a key pair.

# Features
* Load preferences from XML file.
* Load existing RSA key pairs or creates a new one.
* Encryp(Save) and Decrypt(Load) a single text document. Once encrypted, the document is saved in the specified binary file.
* Basic java-swing UI to write the text document.
* The *Encrypt & Save* button on the UI will attempt to encrypt the text written, save the results to disk, then load them again, decrypt and compare both texts to verify the contents.

# Configuration via XML file
```xml
<?xml version="1.0" encoding="UTF-8"?>	
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<entry key="privateKey">sample-keys/.lapcipher_privateKey</entry>
	<entry key="publicKey">sample-keys/.lapcipher_publicKey</entry>
	<entry key="defaultFile">saves/document.bin</entry>
</properties>
```
* In the above configuration, place an existing RSA key pair (public and private).
* If you want to create a new one, use a non-existent filename for both keys and the program will try to create them.
* defaultFile specifies the location of the encrypted text document in binary format.
