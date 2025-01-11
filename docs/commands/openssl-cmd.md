- url to download exce file for openssl in windows
        https://slproweb.com/products/Win32OpenSSL.html

- command to check the version of openssl
         openssl version 

  - commands to generate private key and public key

        openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
  
        openssl rsa -pubout -in private_key.pem -out public_key.pem


  openssl:
    This is the OpenSSL command-line tool, used for cryptographic operations like
    generating keys, certificates, and managing SSL/TLS.

  genpkey:
  This stands for "generate private key."
  It tells OpenSSL to create a new private key.

  -algorithm RSA:
  Specifies the algorithm to use for key generation.
  In this case, it generates an RSA key pair (public and private keys).

  -out private_key.pem:
  This specifies the output file where the generated private key will be saved.

  The private key will be stored in the file
  private_key.pem in the current working directory unless a different path is specified.
  `If you want to save the file to a different directory,
  provide the full path, e.g., /path/to/private_key.pem.`

  -pkeyopt rsa_keygen_bits:2048:
  This sets the RSA key size (bit length) to 2048 bits.

  2048 bits is considered secure for most applications and
  complies with modern cryptographic standards.
  `You can use higher values like 4096 for even stronger security,
  but it will increase the size of the keys and the computational requirements.`