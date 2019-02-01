# CheckoutVerifier
CheckoutVerifier helps you Verify your In-App Purchase receipts & protect your Apps from hacking, patching used by Piracy Apps like Lucky Patcher.
<br/>Since I was using these classes in every project, the Copy/Pasting of classes was annoying so thought of releasing it as a library which might be of help to others too!


## How does it work?
Well, the library sends the Signed Json Response & Signature that you receive after a purchase is completed on a specified server url where it checks the signature of that response data with your BASE64 Key provided to you in your Developer Console.


## Set Up
#### * Get Licensing API Key
Navigate to Developer Console & Select your App.
<br/>Go to <b>Development Tools</b> > <b>Services & API.</b>
<br/>Copy the <b>BASE64 Licensing Key</b>

#### * Creating a Verifying PHP File
Just a create a File & name it as `verify.php` or anything you want.
<br/>Paste the following code in it & Upload it to your server.

```php
<?php
// get jsonResponse
$data = $_GET['jsonResponse'];

// get signature
$signature = $_GET['signature'];

// get key
$key_64 = "YOUR BASE64 KEY THAT YOU GOT FROM DEVELOPER CONSOLE, THERE SHOULD BE NO SPACES!";


$key =  "-----BEGIN PUBLIC KEY-----\n".
        chunk_split($key_64, 64,"\n").
        '-----END PUBLIC KEY-----';

//using PHP to create an RSA key
$key = openssl_get_publickey($key);


// state whether signature is okay or not
$ok = openssl_verify($data, base64_decode($signature), $key, OPENSSL_ALGO_SHA1);
if ($ok == 1) {
    echo "verified";
} elseif ($ok == 0) {
    echo "unverified";
} else {
    die ("fault, error checking signature");
}

// free the key from memory
openssl_free_key($key);

?>
```

#### * Implementing Library (Gradle)
Latest_Version: [![Download](https://api.bintray.com/packages/itznotabug/Maven/CheckoutVerifier/images/download.svg) ](https://bintray.com/itznotabug/Maven/CheckoutVerifier/_latestVersion)
<br/>
```gradle
dependencies {
    implementation 'com.lazygeniouz:checkout-verifier:Latest_Version'
}
```

#### * CheckOutVerifier
Just pass on the required fields in the Constructor & call `start();`
<br/>Example:
<br/>`Kotlin`
```kotlin
CheckoutVerifier(url, jsonResponse, signature, object : VerifyingListener {
            override fun onVerificationStarted() {
            }

            override fun onVerificationCompleted(isVerified: Boolean) {
            }

            override fun onExceptionCaught(exception: Exception) {
                /** Called when there was an error connecting to Server. */
            }
        }).start()
```
<br/>`Java`
``` java
new CheckoutVerifier(url, jsonResponse, signature, new VerifyingListener() {
            @Override
            public void onVerificationStarted() {
                //Show a ProgressDialog or something
            }

            @Override
            public void onVerificationCompleted(boolean isVerified) {
                //Check if the Purchase is Valid...
                //Consume if not!
            }

            @Override
            public void onExceptionCaught(@NotNull Exception exception) {
                //Called when there was an error connecting to Server.
            }
        }).start();

```