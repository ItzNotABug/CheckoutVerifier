# CheckoutVerifier

<h1 align=center>
<img src="logo/horizontal.png" width=60%>
</h1>

CheckoutVerifier helps you Verify your In-App Purchase receipts & protect your Apps from hacking, patching used by Piracy Apps like Lucky Patcher.\
Since I was using these classes in every project, the Copy/Pasting of classes was annoying so thought of releasing it as a library which might be of help to others too!

The library sends the Signed Json Response & Signature that you receive after a purchase is completed on a specified server url where it checks the signature of that response data with your BASE64 Key provided to you in your Developer Console.

## Set Up
* **Get Licensing API Key**\
Navigate to Developer Console & Select your App.\
Go to **Development Tools** > **Services & API**.\
Copy the **BASE64 Licensing Key**


* **Creating a Verifying PHP File**\
Just a create a File & name it as `verify.php` or anything you want.\
Paste the following code in it & Upload it to your server.

```php
<?php
$data = $_GET['jsonResponse'];
$signature = $_GET['signature'];
$key_64 = "YOUR BASE64 KEY THAT YOU GOT FROM DEVELOPER CONSOLE, THERE SHOULD BE NO SPACES!";

$key =  "-----BEGIN PUBLIC KEY-----\n".
        chunk_split($key_64, 64,"\n").
        '-----END PUBLIC KEY-----';

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

openssl_free_key($key);
?>
```

#### Implementing Library (Gradle)
library_version: [![Download](https://api.bintray.com/packages/itznotabug/Maven/CheckoutVerifier/images/download.svg)](https://bintray.com/itznotabug/Maven/CheckoutVerifier/_latestVersion)
```gradle
dependencies {
    //This internally uses Kotlin Coroutines.
    //This will be completely moved to the `checkout-verifier` group
    implementation 'com.lazygeniouz:checkout-verifier-coroutine:$library_version'
}
```

#### CheckoutVerifier
Just pass on the required `PurchaseBundle` in the Constructor & call `authenticate();`\
The `authenticate()` returns a `Result` object.\
If the connection to the server was successful & a result was returned,\
`CompletionResult(isVerified: Boolean)` is returned, `ErrorResult(exception: Exception)` otherwise.\
Example:
```kotlin
yourScope.launch {
    val purchaseBundle = PurchaseBundle(url, jsonResponse, signature)
    when (val result = CheckoutVerifier(purchaseBundle).authenticate()) {
        is CompletionResult -> {
            val verified = result.isVerified
            // Do something
        }
      is ErrorResult -> Log.d(TAG, result.exception.message)
    }
}
```
